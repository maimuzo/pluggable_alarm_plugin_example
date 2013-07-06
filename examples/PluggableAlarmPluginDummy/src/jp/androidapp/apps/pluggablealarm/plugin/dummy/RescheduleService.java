package jp.androidapp.apps.pluggablealarm.plugin.dummy;

import jp.androidapp.libs.pluggablealarm.AlarmData;
import jp.androidapp.libs.pluggablealarm.AlarmPrefManager;
import jp.androidapp.libs.pluggablealarm.AlarmUtil;
import jp.androidapp.libs.pluggablealarm.IntentParam;
import android.app.AlarmManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class RescheduleService
    extends IntentService {
    private static final String TAG = "RescheduleService";
    private AlarmManager mAlarmManager;

    public RescheduleService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String actionName = intent.getAction();
        Log.d(TAG, "enter RescheduleService.onHandleIntent().");

        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        String prefName;
        SharedPreferences pref;
        int[] ids = intent.getIntArrayExtra(IntentParam.EXTRAS_RESCHEDULE_ALARM_ID_LIST);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int id : ids) {
            sb.append(id).append(",");
        }
        sb.append("]");
        Log.d(TAG, RescheduleService.class.getName() + ", reschedule alarm_ids: " + sb.toString());
        for (int alarmId : ids) {
            prefName = AlarmPrefManager.ALARM_NAME_BASE + alarmId;
            pref = getSharedPreferences(prefName, Context.MODE_PRIVATE);

            // データ不整合対策。新規に開いた場合は無視する
            if (0 < pref.getAll().size()) {
                // デバッグ用
                AlarmUtil.dumpCurrentSharedPrerence(pref);

                AlarmData alarmData = from(pref);
                Log.d(TAG, RescheduleService.class.getName() + ", reschedule alarm_id: " + alarmData.alarmId);
                AlarmUtil.setNextAlarm(this, mAlarmManager, alarmData, alarmData.nextDelayInMillis, false);
            }
        }
    }

    public AlarmData from(SharedPreferences pref) {
        int hour = pref.getInt("time_Hour", 0);
        int minute = pref.getInt("time_Minute", 0);
        long delay = AlarmUtil.getNextDelayInMillisForNextAlarm(this, hour, minute, AlarmUtil.getWeeks(pref));

        AlarmData alarmData = new AlarmData();
        alarmData.nextDelayInMillis = delay;
        alarmData.pickedAlarmResource = pref.getString("ringtone", "");
        alarmData.alarmSpecialAction = IntentParam.ACTION_DEFUALT_ALARM;
        alarmData.editSpecialAction = IntentParam.ACTION_DEFUALT_EDIT;
        alarmData.nextAlarmSpecialAction = IntentParam.ACTION_DEFUALT_NEXT_ALARM;
        alarmData.nextSnoozeSpecialAction = IntentParam.ACTION_DEFUALT_NEXT_SNOOZE;
        alarmData.rescheduleSpecialAction = IntentParam.ACTION_DEFUALT_RESCHEDULE;
        alarmData.alarmId = pref.getInt(AlarmPrefManager.PREF_ALARM_ID, -1);
        return alarmData;
    }
}
