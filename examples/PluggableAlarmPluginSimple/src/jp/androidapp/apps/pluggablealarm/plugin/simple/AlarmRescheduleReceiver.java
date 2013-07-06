package jp.androidapp.apps.pluggablealarm.plugin.simple;

import jp.androidapp.libs.pluggablealarm.IntentParam;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 再起動時や時刻を調整した場合に、予約していたアラームが消えるため、このプラグインによるアラームを再予約する必要がある。
 * 
 * @author maimuzo
 */
public class AlarmRescheduleReceiver
    extends BroadcastReceiver {
    private static final String TAG = "AlarmRescheduleReceiver";

    @Override
    public void onReceive(Context context,
                          Intent intent) {
        Log.d(TAG, "enter AlarmRescheduleReceiver.onReceive(). ACTION:" + intent.getAction());
        Intent i = new Intent(context, RescheduleService.class);
        int[] ids = intent.getIntArrayExtra(IntentParam.EXTRAS_RESCHEDULE_ALARM_ID_LIST);
        if (null != ids && 0 < ids.length) {
            i.putExtra(IntentParam.EXTRAS_RESCHEDULE_ALARM_ID_LIST, ids);
            context.startService(i);
        }
    }
}
