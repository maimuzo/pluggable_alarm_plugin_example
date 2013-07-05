package jp.androidapp.libs.pluggablealarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class AlarmUtil {
    private static final String TAG = "AlarmUtil";

    /**
     * delayInMillisミリ秒後に次回アラームをセットする
     * 
     * @param context
     * @param manager
     * @param data
     * @param delayInMillis
     */
    public static void setNextAlarm(Context context,
                                    AlarmManager manager,
                                    AlarmData data,
                                    long delayInMillis,
                                    boolean needToShowMessage) {
        // アラームをセット
        PendingIntent pi = createPendingIntentForAlarmService(context, data.alarmSpecialAction, data);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + delayInMillis, pi);
        String message = convertMillisToTimeFromNow(delayInMillis) + "後にアラームをセットしました";
        if (needToShowMessage) {
            // Toastメッセージを表示
            showMessage(context, message);
        }
        Log.d(TAG, "set next alarm to AlarmManager after " + message + " (delay " + delayInMillis + " msec)");
    }

    public static void setNextAlarm(Context context,
                                    AlarmManager manager,
                                    AlarmData data,
                                    long delayInMillis) {
        setNextAlarm(context, manager, data, delayInMillis, true);
    }

    public static void unsetNextAlarm(Context context,
                                      AlarmManager manager,
                                      AlarmData data) {
        PendingIntent pi = createPendingIntentForAlarmService(context, data.alarmSpecialAction, data);
        manager.cancel(pi);
        showMessage(context, "アラームを解除しました");
    }

    /**
     * 今からmillisミリ秒後の時間を「n日n時n分」で返す 0日となる時は日を省略する。 0時となる時は時を省略する。
     * 
     * @param millis
     *            　アラーム設定時刻
     * @return
     */
    private static String convertMillisToTimeFromNow(long delayMillis) {
        // 日付の差分
        long one_date_time = 1000 * 60 * 60 * 24;
        long diffDays = (delayMillis) / one_date_time;

        // 時間の差分
        long one_hour_time = 1000 * 60 * 60;
        long diffHours = (delayMillis - (diffDays * one_date_time)) / one_hour_time;

        // 分の差分
        long one_minute_time = 1000 * 60;
        long diffMinute = (delayMillis - (diffDays * one_date_time) - (diffHours * one_hour_time)) / one_minute_time;

        // 文字列を作成する
        StringBuilder sb = new StringBuilder();
        if (0 != diffDays) {
            sb.append(diffDays);
            sb.append("日");
        }
        if (0 != diffHours) {
            sb.append(diffHours);
            sb.append("時");
        }
        // 1分未満の場合は1分と返す
        if (0 == diffMinute) {
            diffMinute += 1;
        }
        sb.append(diffMinute);
        sb.append("分");

        return sb.toString();
    }

    private static PendingIntent createPendingIntentForAlarmService(Context context,
                                                                    String action,
                                                                    AlarmData alarmData) {
        Intent i = new Intent();
        alarmData.setForAlarmTo(i);
        i.setAction(action);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
        return pi;
    }

    private static void showMessage(Context context,
                                    String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static Calendar calculateNextTiming(int alarmHour,
                                               int alarmMinute,
                                               ArrayList<Integer> daysOfWeek) {

        // 現在時刻
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());

        // 現在時刻をベースに、時と分だけ合わせたカレンダーを生成
        Calendar base = Calendar.getInstance();
        base.setTimeInMillis(System.currentTimeMillis());
        base.set(Calendar.HOUR_OF_DAY, alarmHour);
        base.set(Calendar.MINUTE, alarmMinute);
        base.set(Calendar.SECOND, 0);
        base.set(Calendar.MILLISECOND, 0);

        // 曜日指定されていない時は、毎日鳴らすものとして扱う
        if (null == daysOfWeek || 0 == daysOfWeek.size()) {
            if (base.after(now)) {
                // 今日のこれから鳴らす
                return base;
            } else {
                // 明日鳴らす
                base.add(Calendar.DAY_OF_YEAR, 1);
                return base;
            }
        }

        Calendar c;
        for (Integer i : daysOfWeek) {
            // 曜日リストを最初から試して、将来時刻を指すものを探す
            c = Calendar.getInstance();
            c.setTime(base.getTime());
            c.set(Calendar.DAY_OF_WEEK, i.intValue());
            if (c.after(now)) {
                return c;
            }
        }
        // 今週には該当がない場合、来週の1件目、すなわちdaysOfWeek.get(0)をセットする
        c = Calendar.getInstance();
        c.setTime(base.getTime());
        c.add(Calendar.WEEK_OF_YEAR, 1);
        c.set(Calendar.DAY_OF_WEEK, daysOfWeek.get(0).intValue());
        return c;
    }

    /**
     * 曜日文字列に対応した曜日情報を返します.
     * 
     * @param weeks
     *            曜日リスト(区切り文字はカンマのみ)
     * @return 曜日配列(Calendar.SUNDAY～に対応した数値型配列)
     */
    public static ArrayList<Integer> getDayOfWeekList(Context context,
                                                      String weeks) {

        // 文字列をカンマで区切る
        String[] weeklist = weeks.split(",");

        // 曜日データのリソースを取得
        String[] displayList = context.getResources().getStringArray(R.array.array_day_of_weeks);

        // 曜日リストのハッシュマップを生成
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        int value = Calendar.SUNDAY;
        for (int i = 0; i < displayList.length; i++) {
            map.put(displayList[i], value);
            value++;
        }

        // ハッシュマップから曜日文字列に対応した曜日情報を生成
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < weeklist.length; i++) {
            if (map.containsKey(weeklist[i])) {
                list.add(map.get(weeklist[i]));
            }
        }

        return list;
    }

    /**
     * アラーム起動までのミリ秒(差分時間)を返す
     * 
     * @param hour
     * @param minute
     * @param alarmDaysOfWeek
     * @return
     */
    public static long getNextDelayInMillisForNextAlarm(Context context,
                                                        int hour,
                                                        int minute,
                                                        String alarmDaysOfWeek) {
        // dayOfWeekListに示された曜日(もしくは指定がなければ現在時刻より最短のタイミング)で、アラームを鳴らすべき時刻を求める
        ArrayList<Integer> dayOfWeekList = getDayOfWeekList(context, alarmDaysOfWeek);
        Calendar nextAlarm = calculateNextTiming(hour, minute, dayOfWeekList);

        final int year = nextAlarm.get(Calendar.YEAR);
        final int month = nextAlarm.get(Calendar.MONTH);
        final int day = nextAlarm.get(Calendar.DAY_OF_MONTH);
        final int hour_r = nextAlarm.get(Calendar.HOUR_OF_DAY);
        final int minute_r = nextAlarm.get(Calendar.MINUTE);
        final int dayOfWeek_r = nextAlarm.get(Calendar.DAY_OF_WEEK);
        Log.d(TAG, "year/month/day(day of week) hour:minute"
                   + year
                   + "/"
                   + (month + 1)
                   + "/"
                   + day
                   + "("
                   + dayOfWeek_r
                   + ")"
                   + " "
                   + hour_r
                   + ":"
                   + minute_r);

        // 返すのは現在時刻からの差(ミリ秒)
        long nextDelayInMillis = nextAlarm.getTimeInMillis() - System.currentTimeMillis();
        return nextDelayInMillis;
    }

    /**
     * フラームが有効になる曜日を指す文字列をprefから生成する
     * 
     * @param pref
     * @return
     */
    public static String getWeeks(SharedPreferences pref) {
        StringBuilder sb = new StringBuilder();
        if (pref.getBoolean("sun", false)) {
            sb.append("日");
        }
        if (pref.getBoolean("mon", false)) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append("月");
        }
        if (pref.getBoolean("tue", false)) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append("火");
        }
        if (pref.getBoolean("wed", false)) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append("水");
        }
        if (pref.getBoolean("thu", false)) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append("木");
        }
        if (pref.getBoolean("fri", false)) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append("金");
        }
        if (pref.getBoolean("sat", false)) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append("土");
        }
        String weekString = sb.toString();
        if (0 == weekString.length()) {
            return "毎日";
        }
        return weekString;
    }

    /**
     * prefの中身を全てログに書き出す
     * 
     * @param pref
     */
    public static void dumpCurrentSharedPrerence(SharedPreferences pref) {
        Map<String, ?> map = pref.getAll();
        Iterator<String> ite = map.keySet().iterator();
        while (ite.hasNext()) {
            String key = ite.next();
            Log.d(TAG, "dump " + key + " : " + String.valueOf(map.get(key)));
        }
    }
}
