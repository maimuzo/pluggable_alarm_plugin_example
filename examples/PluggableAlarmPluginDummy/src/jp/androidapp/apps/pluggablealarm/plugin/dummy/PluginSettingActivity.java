package jp.androidapp.apps.pluggablealarm.plugin.dummy;

import jp.androidapp.libs.pluggablealarm.IntentParam;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PluginSettingActivity
    extends Activity {
    private static final String TAG = "PluginSettingActivity";
    private static final String sPackageName;
    static {
        sPackageName = PluginSettingActivity.class.getPackage().getName();
    }
    private static final String ACTION_PLUGIN_ALARM = sPackageName + ".ACTION_ALARM";
    private static final String ACTION_PLUGIN_EDIT = sPackageName + ".ACTION_OPEN_ALARM_SETTING";
    private static final String ACTION_PLUGIN_NEXT_ALARM = sPackageName + ".ACTION_NEXT_ALARM";
    private static final String ACTION_PLUGIN_NEXT_SNOOZE = sPackageName + ".ACTION_NEXT_SNOOZE";
    private static final String ACTION_PLUGIN_RESCHEDULE = sPackageName + ".ACTION_RESCHEDULE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent data = getIntent();

        if (ACTION_PLUGIN_NEXT_ALARM.equals(data.getAction())) {
            forNextAlarm(data);
        } else if (ACTION_PLUGIN_NEXT_SNOOZE.equals(data.getAction())) {
            forNextSnooze(data);
        } else {
            setContentView(R.layout.activity_plugin_setting);
            forEdit(data);
        }
    }

    private void forEdit(Intent data) {
        findViewById(R.id.plugin_setting_button_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 入力データを返す
                Intent data = new Intent();
                data.putExtra(IntentParam.EXTRAS_RESULT, IntentParam.EXTRAS_RESULT_CREATED); // 新規追加時
                // data.putExtra(IntentParam.EXTRAS_RESULT,
                // IntentParam.EXTRAS_RESULT_UPDATED); // 更新時
                data.putExtra(IntentParam.EXTRAS_TIME, "7:00");
                data.putExtra(IntentParam.EXTRAS_WEEKS, "月火水木金");
                data.putExtra(IntentParam.EXTRAS_PLUGIN_NAME, sPackageName); // プラグインのID
                data.putExtra(IntentParam.EXTRAS_NEXT_DELAY_IN_MILLIS, 300000l); // 今から何ミリ秒後にアラームを鳴らすか
                data.putExtra(IntentParam.EXTRAS_ALARM_TITLE, "メイン画面に表示するタイトル(ダミー)");
                data.putExtra(IntentParam.EXTRAS_PICKED_ALARM_RESOURCE, "content://settings/system/alarm_alert"); // ringリソースのURI
                data.putExtra(IntentParam.EXTRAS_ALARM_SPECIAL_ACTION, ACTION_PLUGIN_ALARM);
                data.putExtra(IntentParam.EXTRAS_EDIT_SPECIAL_ACTION, ACTION_PLUGIN_EDIT);
                data.putExtra(IntentParam.EXTRAS_NEXT_ALARM_SPECIAL_ACTION, ACTION_PLUGIN_NEXT_ALARM);
                data.putExtra(IntentParam.EXTRAS_NEXT_SNOOZE_SPECIAL_ACTION, ACTION_PLUGIN_NEXT_SNOOZE);
                data.putExtra(IntentParam.EXTRAS_RESCHEDULE_SPECIAL_ACTION, ACTION_PLUGIN_RESCHEDULE);
                data.putExtra(IntentParam.EXTRAS_ALARM_ID, 10); // このプラグイン毎にユニークなID
                setResult(RESULT_OK, data);
                finish();
            }
        });
        findViewById(R.id.plugin_setting_button_cansel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 入力データを返す
                Intent data = new Intent();
                setResult(RESULT_CANCELED, data);
                finish();
            }
        });
        findViewById(R.id.plugin_setting_button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 入力データを返す
                Intent data = new Intent();
                data.putExtra(IntentParam.EXTRAS_RESULT, IntentParam.EXTRAS_RESULT_DELETED);
                data.putExtra(IntentParam.EXTRAS_PLUGIN_NAME, sPackageName); // プラグインのID
                data.putExtra(IntentParam.EXTRAS_ALARM_ID, 10); // このプラグイン毎にユニークなID
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    private void forNextAlarm(Intent intent) {
        // alarmidを元に呼び出して計算して、最終的な内容を返す(次回アラーム時間までのdelayを計算)
        Intent data = new Intent();
        data.putExtra(IntentParam.EXTRAS_RESULT, IntentParam.EXTRAS_RESULT_CALCULATED);
        data.putExtra(IntentParam.EXTRAS_NEXT_DELAY_IN_MILLIS, 300000l); // 今から何ミリ秒後にアラームを鳴らすか
        data.putExtra(IntentParam.EXTRAS_PICKED_ALARM_RESOURCE, "content://settings/system/alarm_alert"); // ringリソースのURI
        data.putExtra(IntentParam.EXTRAS_ALARM_SPECIAL_ACTION, ACTION_PLUGIN_ALARM);
        data.putExtra(IntentParam.EXTRAS_EDIT_SPECIAL_ACTION, ACTION_PLUGIN_EDIT);
        data.putExtra(IntentParam.EXTRAS_NEXT_ALARM_SPECIAL_ACTION, ACTION_PLUGIN_NEXT_ALARM);
        data.putExtra(IntentParam.EXTRAS_NEXT_SNOOZE_SPECIAL_ACTION, ACTION_PLUGIN_NEXT_SNOOZE);
        data.putExtra(IntentParam.EXTRAS_RESCHEDULE_SPECIAL_ACTION, ACTION_PLUGIN_RESCHEDULE);
        data.putExtra(IntentParam.EXTRAS_ALARM_ID, 10); // このプラグイン毎にユニークなID
        setResult(RESULT_OK, data);
        finish();
    }

    private void forNextSnooze(Intent intent) {
        // alarmidを元に呼び出して計算して、最終的な内容を返す(次回スヌーズ時間までのdelayを計算)
        Intent data = new Intent();
        data.putExtra(IntentParam.EXTRAS_RESULT, IntentParam.EXTRAS_RESULT_CALCULATED);
        data.putExtra(IntentParam.EXTRAS_NEXT_DELAY_IN_MILLIS, 300000l); // 今から何ミリ秒後にアラームを鳴らすか
        data.putExtra(IntentParam.EXTRAS_PICKED_ALARM_RESOURCE, "content://settings/system/alarm_alert"); // ringリソースのURI
        data.putExtra(IntentParam.EXTRAS_ALARM_SPECIAL_ACTION, ACTION_PLUGIN_ALARM);
        data.putExtra(IntentParam.EXTRAS_EDIT_SPECIAL_ACTION, ACTION_PLUGIN_EDIT);
        data.putExtra(IntentParam.EXTRAS_NEXT_ALARM_SPECIAL_ACTION, ACTION_PLUGIN_NEXT_ALARM);
        data.putExtra(IntentParam.EXTRAS_NEXT_SNOOZE_SPECIAL_ACTION, ACTION_PLUGIN_NEXT_SNOOZE);
        data.putExtra(IntentParam.EXTRAS_RESCHEDULE_SPECIAL_ACTION, ACTION_PLUGIN_RESCHEDULE);
        data.putExtra(IntentParam.EXTRAS_ALARM_ID, 10); // このプラグイン毎にユニークなID
        setResult(RESULT_OK, data);
        finish();
    }

}
