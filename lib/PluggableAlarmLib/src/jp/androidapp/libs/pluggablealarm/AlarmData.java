package jp.androidapp.libs.pluggablealarm;

import android.content.Intent;

public class AlarmData {
    public String time;
    public String weeks;
    public String title;
    public String pluginName;
    public boolean isEnable;
    public long nextDelayInMillis;
    public String pickedAlarmResource;
    public String alarmSpecialAction;
    public String editSpecialAction;
    public String nextAlarmSpecialAction;
    public String nextSnoozeSpecialAction;
    public String rescheduleSpecialAction;
    public int alarmId;

    public static AlarmData from(Intent data) {
        AlarmData alarmData = new AlarmData();
        alarmData.time = data.getStringExtra(IntentParam.EXTRAS_TIME);
        alarmData.weeks = data.getStringExtra(IntentParam.EXTRAS_WEEKS);
        alarmData.title = data.getStringExtra(IntentParam.EXTRAS_ALARM_TITLE);
        alarmData.pluginName = data.getStringExtra(IntentParam.EXTRAS_PLUGIN_NAME);
        alarmData.isEnable = true;
        alarmData.nextDelayInMillis = data.getLongExtra(IntentParam.EXTRAS_NEXT_DELAY_IN_MILLIS, 0);
        alarmData.pickedAlarmResource = data.getStringExtra(IntentParam.EXTRAS_PICKED_ALARM_RESOURCE);
        alarmData.alarmSpecialAction = data.getStringExtra(IntentParam.EXTRAS_ALARM_SPECIAL_ACTION);
        alarmData.editSpecialAction = data.getStringExtra(IntentParam.EXTRAS_EDIT_SPECIAL_ACTION);
        alarmData.nextAlarmSpecialAction = data.getStringExtra(IntentParam.EXTRAS_NEXT_ALARM_SPECIAL_ACTION);
        alarmData.nextSnoozeSpecialAction = data.getStringExtra(IntentParam.EXTRAS_NEXT_SNOOZE_SPECIAL_ACTION);
        alarmData.rescheduleSpecialAction = data.getStringExtra(IntentParam.EXTRAS_RESCHEDULE_SPECIAL_ACTION);
        alarmData.alarmId = data.getIntExtra(IntentParam.EXTRAS_ALARM_ID, 0);
        return alarmData;
    }

    public void setForAlarmTo(Intent intent) {
        intent.putExtra(IntentParam.EXTRAS_PICKED_ALARM_RESOURCE, pickedAlarmResource);
        intent.putExtra(IntentParam.EXTRAS_ALARM_SPECIAL_ACTION, alarmSpecialAction);
        intent.putExtra(IntentParam.EXTRAS_NEXT_ALARM_SPECIAL_ACTION, nextAlarmSpecialAction);
        intent.putExtra(IntentParam.EXTRAS_NEXT_SNOOZE_SPECIAL_ACTION, nextSnoozeSpecialAction);
        intent.putExtra(IntentParam.EXTRAS_RESCHEDULE_SPECIAL_ACTION, rescheduleSpecialAction);
        intent.putExtra(IntentParam.EXTRAS_ALARM_ID, alarmId);
    }

    @Override
    public String toString() {
        return "AlarmData [time="
               + time
               + ", weeks="
               + weeks
               + ", title="
               + title
               + ", pluginName="
               + pluginName
               + ", isEnable="
               + isEnable
               + ", nextDelayInMillis="
               + nextDelayInMillis
               + ", alarmFilePath="
               + pickedAlarmResource
               + ", alarmSpecialAction="
               + alarmSpecialAction
               + ", editSpecialAction="
               + editSpecialAction
               + ", nextAlarmSpecialAction="
               + nextAlarmSpecialAction
               + ", nextSnoozeSpecialAction="
               + nextSnoozeSpecialAction
               + ", rescheduleSpecialAction="
               + rescheduleSpecialAction
               + ", alarmId="
               + alarmId
               + "]";
    }
}
