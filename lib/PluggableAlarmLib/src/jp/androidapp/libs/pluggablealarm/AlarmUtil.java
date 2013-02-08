package jp.androidapp.libs.pluggablealarm;

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

}
