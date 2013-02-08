package jp.androidapp.libs.pluggablealarm;

import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class BaseAlarmActivity extends Activity {
	private static final String TAG = "BaseAlarmActivity";
	private static final int REQUEST_CODE_ACTION_NEXT_ALARM = 2;
	private static final int REQUEST_CODE_ACTION_NEXT_SNOOZE = 3;

	private KeyguardLock keylock = null;
	protected AlarmManager mAlarmManager;
	protected AlarmData mAlarmData;
	private OnSetAlarm mOnSetAlarm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent data = getIntent();
		mAlarmData = AlarmData.from(data);

		mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		// スクリーンオン(ディスプレイの電源Off状態からでも強制的にデイスプレイ表示する。タイムアウト付き)
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);  
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "My Tag");  
        wl.acquire(20000); // タイムアウト20秒
        
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
    		// API 13(3.2)以降用
    		Window window = getWindow();  
            
            // lock pattern を設定していても、  
            // このアプリ起動中は画面オン時にロックはでない  
            // 別のアプリやホームに移動するときにロックがでる  
            window.setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,   
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);  
      
//            // lock pattern を設定していない場合、  
//            // このアプリ起動中は画面オン時にロックはでない  
//            window.setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,   
//                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD); 
        	
        } else {
        	// API 13以下の場合はkeyguard.newKeyguardLock()を使う
            // スクリーンロックを解除する(これが無いとロック画面は表示されるが、このActivityは表示されない)
            KeyguardManager keyguard = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            keylock = keyguard.newKeyguardLock("disableLock");
        }
	}

	@Override
	public void onResume(){
		super.onResume();
		if(null != keylock){
            keylock.disableKeyguard();
		}		
	}
	
	@Override
	public void onPause(){
		super.onPause();
		if(null != keylock){
			keylock.reenableKeyguard();
		}
	}
	
	public void setNextAlarm(OnSetAlarm listener){
		mOnSetAlarm = listener;
		Intent intent = new Intent();
		// パラメータ詰め替え
		mAlarmData.setForAlarmTo(intent);
		intent.setAction(mAlarmData.nextAlarmSpecialAction);
		startActivityForResult(intent, REQUEST_CODE_ACTION_NEXT_ALARM);
	}
	
	public void setNextSnooze(OnSetAlarm listener){
		mOnSetAlarm = listener;
		Intent intent = new Intent();
		// パラメータ詰め替え
		mAlarmData.setForAlarmTo(intent);
		intent.setAction(mAlarmData.nextSnoozeSpecialAction);
		startActivityForResult(intent, REQUEST_CODE_ACTION_NEXT_SNOOZE);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		AlarmData alarmData;
		if(resultCode == RESULT_OK){
			switch (requestCode) {
			case REQUEST_CODE_ACTION_NEXT_ALARM:
				alarmData = AlarmData.from(data);
				Log.d(TAG, "result: " + alarmData.toString());

				AlarmUtil.setNextAlarm(this, mAlarmManager, alarmData, alarmData.nextDelayInMillis);
				if(null != mOnSetAlarm){
					mOnSetAlarm.onSetAlarmDone();
				}
				break;
			case REQUEST_CODE_ACTION_NEXT_SNOOZE:
				alarmData = AlarmData.from(data);
				Log.d(TAG, "result: " + alarmData.toString());

				AlarmUtil.setNextAlarm(this, mAlarmManager, alarmData, alarmData.nextDelayInMillis);
				if(null != mOnSetAlarm){
					mOnSetAlarm.onSetSnoozeDone();
				}
				break;
			default:
				break;
			}
		}
    }

	public interface OnSetAlarm{
		void onSetAlarmDone();
		void onSetSnoozeDone();
	}
}
