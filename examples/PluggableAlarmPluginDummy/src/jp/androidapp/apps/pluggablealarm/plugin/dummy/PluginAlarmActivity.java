package jp.androidapp.apps.pluggablealarm.plugin.dummy;

import jp.androidapp.libs.pluggablealarm.BaseAlarmActivity;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class PluginAlarmActivity extends BaseAlarmActivity {
	private Ringtone ringtone = null;
	private Handler mHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plugin_alarm);
        
		findViewById(R.id.alarm_plugin_viewgroup_root).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setNextSnooze(mOnSetAlarm);
				if (ringtone != null) {
					ringtone.stop();
				}
			}
		});
		
		findViewById(R.id.alarm_plugin_button_stop).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setNextAlarm(mOnSetAlarm);
//				// 強制終了
//				moveTaskToBack(true);
				if (ringtone != null) {
					ringtone.stop();
				}
			}
		});
	}

	@Override
	public void onResume(){
		super.onResume();
		if (ringtone == null){
			Uri uri = Uri.parse(mAlarmData.pickedAlarmResource);
			ringtone = RingtoneManager.getRingtone(this, uri);
			ringtone.play();
		}
	}
	
	private BaseAlarmActivity.OnSetAlarm mOnSetAlarm = new BaseAlarmActivity.OnSetAlarm(){
		@Override
		public void onSetAlarmDone() {
			mHandler.postDelayed(mFinishTask, 2000); // 2sec
		}

		@Override
		public void onSetSnoozeDone() {
			mHandler.postDelayed(mFinishTask, 2000); // 2sec
		}
	};
	
	private Runnable mFinishTask = new Runnable() {
		@Override
		public void run() {
			finish();
		}
	};
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.activity_plugin_main, menu);
//		return true;
//	}

}
