package jp.androidapp.apps.pluggablealarm.plugin.dummy;

import jp.androidapp.libs.pluggablealarm.BaseAlarmActivity;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class PluginAlarmActivity
    extends BaseAlarmActivity {
    private static final String TAG = "PluginAlarmActivity";
    private Ringtone mRingtoneManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_alarm);

        /**
         * 画面全体のリスナー
         */
        findViewById(R.id.alarm_plugin_viewgroup_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mRingtoneManager.stop();
                setNextSnooze(mOnSetAlarm);
            }
        });

        /**
         * 止めるボタン
         */
        findViewById(R.id.alarm_plugin_button_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // mRingtoneManager.stop();
                setNextAlarm(mOnSetAlarm);
            }
        });

        Uri uri = Uri.parse("content://settings/system/alarm_alert");
        mRingtoneManager = RingtoneManager.getRingtone(this, uri);
        mRingtoneManager.play();
    }

    private final BaseAlarmActivity.OnSetAlarm mOnSetAlarm = new BaseAlarmActivity.OnSetAlarm() {
        @Override
        public void onSetAlarmDone() {
            mRingtoneManager.stop();
            finish();
        }

        @Override
        public void onSetSnoozeDone() {
            mRingtoneManager.stop();
            finish();
        }
    };
}
