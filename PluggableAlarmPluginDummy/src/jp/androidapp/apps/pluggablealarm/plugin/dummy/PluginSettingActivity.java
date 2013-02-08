package jp.androidapp.apps.pluggablealarm.plugin.dummy;

import jp.androidapp.libs.pluggablealarm.IntentParam;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class PluginSettingActivity extends Activity {
	private static final String TAG = "PluginSettingActivity";
	private static final String sPackageName;
	static{
		sPackageName = PluginSettingActivity.class.getPackage().getName();		
	}
	private static final String ACTION_PLUGIN_ALARM = sPackageName + ".ACTION_ALARM";
	private static final String ACTION_PLUGIN_EDIT = sPackageName + ".ACTION_OPEN_ALARM_SETTING";
	private static final String ACTION_PLUGIN_NEXT_ALARM = sPackageName + ".ACTION_NEXT_ALARM";
	private static final String ACTION_PLUGIN_NEXT_SNOOZE = sPackageName + ".ACTION_NEXT_SNOOZE";
	
	private String mSampleAlarmResource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plugin_setting);
		
		
		// see: Android開発-着信音の選択と再生- - 明日の鍵 http://d.hatena.ne.jp/tomorrowkey/20090826/1251294895
		Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		mSampleAlarmResource = uri.toString();
		Log.d(TAG, "filepath: " + mSampleAlarmResource);
		
		Intent data = getIntent();
		
		if(ACTION_PLUGIN_NEXT_ALARM.equals(data.getAction())){
			forNextAlarm(data);
		} else if(ACTION_PLUGIN_NEXT_SNOOZE.equals(data.getAction())){
			forNextSnooze(data);
		} else {
			forEdit(data);
		}
	}
	
	private void forEdit(Intent data){
		int alarmId = 0;
		if(data.hasExtra(IntentParam.EXTRAS_ALARM_ID)){
			alarmId = data.getIntExtra(IntentParam.EXTRAS_ALARM_ID, 0);
		}
		if(0 != alarmId){
			Toast.makeText(this, "既に設定してあるアラームの編集 ID: " + alarmId, Toast.LENGTH_LONG).show();
			// TODO: 編集用のコード
		} else {
			Toast.makeText(this, "新規にアラームの編集", Toast.LENGTH_LONG).show();
			// TODO: 編集用のコード
		}
		
		findViewById(R.id.plugin_setting_button_submit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {				
				// TODO: アラームの内容を入力させて、計算して、保存して、最終的な内容を返す
				
				Intent data = new Intent();
				data.putExtra(IntentParam.EXTRAS_TIME, "8:55");
				data.putExtra(IntentParam.EXTRAS_WEEKS, "月金");
				data.putExtra(IntentParam.EXTRAS_PLUGIN_NAME, "ダミープラグインアラーム");
				data.putExtra(IntentParam.EXTRAS_NEXT_DELAY_IN_MILLIS, (long)(1000 * 10)); // テスト用10sec
				data.putExtra(IntentParam.EXTRAS_PICKED_ALARM_RESOURCE, mSampleAlarmResource);
				data.putExtra(IntentParam.EXTRAS_ALARM_SPECIAL_ACTION, ACTION_PLUGIN_ALARM);
				data.putExtra(IntentParam.EXTRAS_EDIT_SPECIAL_ACTION, ACTION_PLUGIN_EDIT);
				data.putExtra(IntentParam.EXTRAS_NEXT_ALARM_SPECIAL_ACTION, ACTION_PLUGIN_NEXT_ALARM);
				data.putExtra(IntentParam.EXTRAS_NEXT_SNOOZE_SPECIAL_ACTION, ACTION_PLUGIN_NEXT_SNOOZE);
				data.putExtra(IntentParam.EXTRAS_ALARM_ID, 10); // このプラグイン毎にユニークなID
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}
	
	private void forNextAlarm(Intent intent){
		// TODO: alarmidを元に呼び出して計算して、最終的な内容を返す(次回アラーム時間までのdelayを計算)
		
		Intent data = new Intent();
		data.putExtra(IntentParam.EXTRAS_NEXT_DELAY_IN_MILLIS, (long)(1000 * 10)); // テスト用10sec
		data.putExtra(IntentParam.EXTRAS_PICKED_ALARM_RESOURCE, mSampleAlarmResource);
		data.putExtra(IntentParam.EXTRAS_ALARM_SPECIAL_ACTION, ACTION_PLUGIN_ALARM);
		data.putExtra(IntentParam.EXTRAS_NEXT_ALARM_SPECIAL_ACTION, ACTION_PLUGIN_NEXT_ALARM);
		data.putExtra(IntentParam.EXTRAS_NEXT_SNOOZE_SPECIAL_ACTION, ACTION_PLUGIN_NEXT_SNOOZE);
		data.putExtra(IntentParam.EXTRAS_ALARM_ID, 10); // このプラグイン毎にユニークなID
		setResult(RESULT_OK, data);
		finish();		
	}

	private void forNextSnooze(Intent intent){
		// TODO: alarmidを元に呼び出して計算して、最終的な内容を返す(次回スヌーズ時間までのdelayを計算)
		
		Intent data = new Intent();
		data.putExtra(IntentParam.EXTRAS_NEXT_DELAY_IN_MILLIS, (long)(1000 * 10)); // テスト用10sec
		data.putExtra(IntentParam.EXTRAS_PICKED_ALARM_RESOURCE, mSampleAlarmResource);
		data.putExtra(IntentParam.EXTRAS_ALARM_SPECIAL_ACTION, ACTION_PLUGIN_ALARM);
		data.putExtra(IntentParam.EXTRAS_NEXT_ALARM_SPECIAL_ACTION, ACTION_PLUGIN_NEXT_ALARM);
		data.putExtra(IntentParam.EXTRAS_NEXT_SNOOZE_SPECIAL_ACTION, ACTION_PLUGIN_NEXT_SNOOZE);
		data.putExtra(IntentParam.EXTRAS_ALARM_ID, 10); // このプラグイン毎にユニークなID
		setResult(RESULT_OK, data);
		finish();
	}

	//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.activity_plugin_main, menu);
//		return true;
//	}

}
