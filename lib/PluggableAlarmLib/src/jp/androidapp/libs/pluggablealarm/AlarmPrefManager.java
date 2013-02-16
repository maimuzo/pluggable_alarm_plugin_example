package jp.androidapp.libs.pluggablealarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AlarmPrefManager {
	private SharedPreferences mPref;
	private static final String PREF_MAX_ALARM_ID = "pref_max_alarm_id";
	public static final String ALARM_NAME_BASE = "pref_alarm_";
	public static final String PREF_ALARM_ID = "alarmId";
	private Context mContext;
	
	public AlarmPrefManager(Context context, String prefName){
		mPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		mContext = context;
	}
	
	private int getMaxAlarmId(){
		return mPref.getInt(PREF_MAX_ALARM_ID, 0);
	}
	
	private void setMaxAlarmId(int alarmId){
		Editor editor = mPref.edit();
		editor.putInt(PREF_MAX_ALARM_ID, alarmId);
		editor.commit();
	}

	public void useAlarmId(String prefName){
		Editor editor = mPref.edit();
		editor.putString(prefName, "");
		editor.commit();
	}
	
	public void removeAlarmId(int alarmId){
		synchronized (mPref) {
			Editor editor = mPref.edit();
			editor.remove(ALARM_NAME_BASE + alarmId);
			editor.commit();
			if(getMaxAlarmId() == alarmId){
				// 最大のalarmIdが消されたときは、maxAlarmIdも更新する
				for(int i = alarmId - 1; 0 != i; i--){
					if(mPref.contains(ALARM_NAME_BASE + i)){
						setMaxAlarmId(i);
					}
				}
				setMaxAlarmId(0);
			}
		}
	}
	
	public String nextPrefName(){
		int alarmId;
		String prefName;
		synchronized (mPref) {
			alarmId = getMaxAlarmId() + 1;
			setMaxAlarmId(alarmId);
			prefName = ALARM_NAME_BASE + alarmId;
			useAlarmId(prefName);
		}
		SharedPreferences p = mContext.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		Editor editor = p.edit();
		editor.putInt(PREF_ALARM_ID, alarmId);
		editor.commit();

		return prefName;
	}
}
