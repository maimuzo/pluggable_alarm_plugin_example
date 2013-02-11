package jp.androidapp.libs.pluggablealarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class AlarmUtil {
	private static final String TAG = "AlarmUtil";
	
	public static void setNextAlarm(Context context, AlarmManager manager, AlarmData data, long delayInMillis){
        PendingIntent pi = createPendingIntentForAlarmService(context, data.alarmSpecialAction, data);
        Log.d(TAG, "set next alarm to AlarmManager after " + delayInMillis + "msec");
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + delayInMillis, pi);
        showMessage(context, convertMillisToTimeFromNow(delayInMillis) + "後にアラームをセットしました");
	}
	
	public static void unsetNextAlarm(Context context, AlarmManager manager, AlarmData data){
        PendingIntent pi = createPendingIntentForAlarmService(context, data.alarmSpecialAction, data);
        manager.cancel(pi);
        showMessage(context, "アラームを解除しました");
	}
	
	private static String convertMillisToTimeFromNow(long millis){
		// TODO: 今からmillisミリ秒後の時間を「n日n時n分」で返す
		return "[実装必要]" + millis + "msec";
	}
	
    private static PendingIntent createPendingIntentForAlarmService(Context context, String action, AlarmData alarmData){
        Intent i = new Intent();
        alarmData.setForAlarmTo(i);
        i.setAction(action);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
        return pi;
    }
    
    private static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 時刻、分から次にアラームを鳴らす日付のカレンダーオブジェクトを生成します.
     * @param c must be set to today
     */
    public static Calendar calculateAlarm(int hour, int minute,
            int[] daysOfWeek) {

        // 現在日時でカレンダーオブジェクト作成
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        // 時分を取り出す
        int nowHour = c.get(Calendar.HOUR_OF_DAY);  // 24H指定
        int nowMinute = c.get(Calendar.MINUTE);

        // 日をまたぐかどうか？
        // 時間が過去をさしている
        // 時間が同じで、分が同じか過去をさしている場合
        // if alarm is behind current time, advance one day
        if (hour < nowHour  ||
            hour == nowHour && minute <= nowMinute) {
            // 翌日の時刻を返す
            c.add(Calendar.DAY_OF_YEAR, 1);
        }
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);


        // 次のアラーム鳴動曜日までに、加算する日数を計算する
        int nextRingDaysOfWeek = getNextDaysOfWeek(c,daysOfWeek);
        int addDays = getAddDay(c,nextRingDaysOfWeek);
        if (addDays > 0) {
            c.add(Calendar.DAY_OF_WEEK, addDays);
        }

        return c;
    }

    /**
     * 次のアラーム鳴動は何曜日かを返す.
     * 指定した日付の曜日が曜日配列の中に存在する場合は、その曜日を返す.
     * 曜日配列　月水金 = {Calendar.MONDAY,Calendar.WEDNESDAY,Calendar.WEDNESDAY,Calendar.FRIDAY}
     * @return 次のアラーム鳴動曜日 Calendar.MONDAY~
     */
    public static int getNextDaysOfWeek(Calendar c, int[] dowList) {
        
        if (null == dowList) {
            // 曜日リストに何も入っていない場合は現在の曜日を返す
            return c.get(Calendar.DAY_OF_WEEK);
        }        
        
        // 現在の曜日を求める
        int nowDOW = c.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i < dowList.length; i++) {
            // 配列をチェックして、現在の曜日より大きい値が出てくるまで探す
            // ex 今：月曜日　配列：月水金　→　次に鳴動させるのは月曜日
            if ((dowList[i] == nowDOW) || (dowList[i] >= nowDOW)) {
                return dowList[i];
            }
        }
        
        // 見つからない場合は配列の先頭が次のアラーム鳴動曜日となる
        // ex 今：土曜日　配列：月水金　→　次に鳴動させるのは月曜日
        return dowList[0];    
    }
    
    /**
     * 次のアラーム鳴動曜日までに、加算する日数を返す
     */
    public static int getAddDay(Calendar c, int nextDOW) {
        
        int addDays = 0;
        
        // 現在の曜日を求める
        int nowDOW = c.get(Calendar.DAY_OF_WEEK);
        
        if(nowDOW == nextDOW){
            // 曜日が同一の場合は日付は加算しない
            addDays = 0;
        }else if(nextDOW > nowDOW){
            // 次の曜日のほうが大きい曜日の場合
            for (int i = 0; i < 7; i++) {

                // 曜日が同じになるまで加算する
                addDays++;
                if (nextDOW == (nowDOW + addDays)) {
                    break;
                }
            }
        }else{
            // 次の曜日のほうが小さい曜日の場合
            // 曜日が同じになるまで減算する
            for (int i = 0; i < 7; i++) {
                addDays++;
                if (nextDOW == (nowDOW - addDays)) {
                    break;
                }
            }
        }
        
        // 計算した日付を返す
        return addDays;
    }
    
    /**
     * 曜日文字列に対応した曜日情報を返します.
     * @param weeks 曜日リスト(区切り文字はカンマのみ)
     * @return 曜日配列(Calendar.SUNDAY～に対応した数値型配列)
     */
    public static ArrayList<Integer> getDayOfWeekList(Context context, String weeks) {

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
    
}
