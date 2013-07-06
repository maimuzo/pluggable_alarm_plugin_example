package jp.androidapp.apps.pluggablealarm.plugin.simple;

import java.util.Iterator;
import java.util.Map;

import jp.androidapp.libs.pluggablealarm.AlarmPrefManager;
import jp.androidapp.libs.pluggablealarm.AlarmUtil;
import jp.androidapp.libs.pluggablealarm.IntentParam;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class PluginSettingActivity
    extends PreferenceActivity {
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
    private static final String PREF_MANAGE_NAME = "pref_manage";

    private Map<String, ?> mPrefMapBeforeModify = null;
    private SharedPreferences mPrefSetting;
    private String mEditMode;
    private Button mButtonCansel;
    private Button mButtonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent data = getIntent();

        if (ACTION_PLUGIN_NEXT_ALARM.equals(data.getAction())) {
            forNextAlarm(data);
        } else if (ACTION_PLUGIN_NEXT_SNOOZE.equals(data.getAction())) {
            forNextSnooze(data);
        } else {
            // see: http://d.hatena.ne.jp/kaw0909/20110403/1301827783
            setContentView(R.layout.activity_plugin_setting);
            addPreferencesFromResource(R.xml.default_setting);
            forEdit(data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getPreferenceScreen().getSharedPreferences();
        // 現在値を表示
        Map<String, ?> map = pref.getAll();
        Iterator<String> ite = map.keySet().iterator();
        while (ite.hasNext()) {
            String key = ite.next();
            // Log.d(TAG, key + " : " + String.valueOf(map.get(key)));
            mListener.onSharedPreferenceChanged(pref, key);
        }
        // 　更新時リスナーのセット
        pref.registerOnSharedPreferenceChangeListener(mListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mListener);
    }

    /**
     * ここで summary を動的に変更
     */
    private final SharedPreferences.OnSharedPreferenceChangeListener mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                              String key) {
            Log.d(TAG, "onSharedPreferenceChanged, key: " + key);
            // AlarmUtil.dumpCurrentSharedPrerence(mPrefSetting);
            if ("max_volume".equals(key)) {
                String volume = sharedPreferences.getString(key, "");
                findPreference(key).setSummary("現在値: " + volume + "％");
            } else if ("min_volume".equals(key)) {
                String volume = sharedPreferences.getString(key, "");
                findPreference(key).setSummary("現在値: " + volume + "％");
            } else if ("time_summary".equals(key)) {
                findPreference("time").setSummary(sharedPreferences.getString(key, ""));
            } else if ("snooze_interval".equals(key)) {
                String interval = sharedPreferences.getString(key, "0");
                long intervalLong = Long.valueOf(interval) / (1000L * 60L);
                String intervalStr = intervalLong + "分";
                findPreference(key).setSummary(intervalStr);
            } else if ("sun".equals(key)
                       || "mon".equals(key)
                       || "tue".equals(key)
                       || "wed".equals(key)
                       || "thu".equals(key)
                       || "fri".equals(key)
                       || "sat".equals(key)
                       || "use_snooze".equals(key)
                       || "is_viblate_on".equals(key)
                       || "need_to_incliment_volume".equals(key)) {
                boolean enabled = sharedPreferences.getBoolean(key, true);
                Log.d(TAG, "CheckBoxPreference key: " + key + ", enabled: " + enabled);
                CheckBoxPreference p = (CheckBoxPreference) findPreference(key);
                if (enabled) {
                    p.setChecked(true);
                } else {
                    p.setChecked(false);
                }
            } else if ("ringtone".equals(key)) {
                // // see: Android開発-着信音の選択と再生- - 明日の鍵
                // http://d.hatena.ne.jp/tomorrowkey/20090826/1251294895
                String uriString = sharedPreferences.getString(key, "");
                if ("".equals(uriString)) {
                    return;
                }
                Ringtone ringtone = RingtoneManager.getRingtone(PluginSettingActivity.this, Uri.parse(uriString));
                String ringtoneName = ringtone.getTitle(PluginSettingActivity.this);
                findPreference(key).setSummary("選択中のアラーム: " + ringtoneName);
                Log.d(TAG, "ringtone: " + uriString + ", ringtone name: " + ringtoneName);
            }
        }
    };

    private void forEdit(Intent data) {
        final int alarmId;
        if (data.hasExtra(IntentParam.EXTRAS_ALARM_ID)) {
            alarmId = data.getIntExtra(IntentParam.EXTRAS_ALARM_ID, -1);
        } else {
            alarmId = -1;
        }

        findViewById(R.id.plugin_setting_button_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // アラームの内容を入力させて、計算して、保存して、最終的な内容を返す
                AlarmUtil.dumpCurrentSharedPrerence(mPrefSetting);

                // 入力データを返す
                Intent data = new Intent();
                data.putExtra(IntentParam.EXTRAS_RESULT, mEditMode);
                data.putExtra(IntentParam.EXTRAS_TIME, mPrefSetting.getString("time", ""));
                data.putExtra(IntentParam.EXTRAS_WEEKS, AlarmUtil.getWeeks(mPrefSetting));
                data.putExtra(IntentParam.EXTRAS_PLUGIN_NAME, sPackageName);
                data.putExtra(IntentParam.EXTRAS_NEXT_DELAY_IN_MILLIS, getNextDelayInMillisForNextAlarm());
                data.putExtra(IntentParam.EXTRAS_ALARM_TITLE, getResources().getString(R.string.setting_name));
                data.putExtra(IntentParam.EXTRAS_PICKED_ALARM_RESOURCE, getSelectedAlarmResource());
                data.putExtra(IntentParam.EXTRAS_ALARM_SPECIAL_ACTION, ACTION_PLUGIN_ALARM);
                data.putExtra(IntentParam.EXTRAS_EDIT_SPECIAL_ACTION, ACTION_PLUGIN_EDIT);
                data.putExtra(IntentParam.EXTRAS_NEXT_ALARM_SPECIAL_ACTION, ACTION_PLUGIN_NEXT_ALARM);
                data.putExtra(IntentParam.EXTRAS_NEXT_SNOOZE_SPECIAL_ACTION, ACTION_PLUGIN_NEXT_SNOOZE);
                data.putExtra(IntentParam.EXTRAS_RESCHEDULE_SPECIAL_ACTION, ACTION_PLUGIN_RESCHEDULE);
                data.putExtra(IntentParam.EXTRAS_ALARM_ID, mPrefSetting.getInt(AlarmPrefManager.PREF_ALARM_ID, -1)); // このプラグイン毎にユニークなID
                setResult(RESULT_OK, data);
                finish();
            }
        });
        mButtonCansel = (Button) findViewById(R.id.plugin_setting_button_cansel);
        mButtonCansel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 開いた時の情報をSharedPreferenceに書き戻す
                if (IntentParam.EXTRAS_RESULT_UPDATED.equals(mEditMode)) {
                    restoreSavedState(mPrefSetting);
                }

                Intent data = new Intent();
                setResult(RESULT_CANCELED, data);
                finish();
            }
        });
        mButtonDelete = (Button) findViewById(R.id.plugin_setting_button_delete);
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                int a = mPrefSetting.getInt(AlarmPrefManager.PREF_ALARM_ID, -1);
                Intent data = new Intent();
                data.putExtra(IntentParam.EXTRAS_RESULT, IntentParam.EXTRAS_RESULT_DELETED);
                data.putExtra(IntentParam.EXTRAS_PLUGIN_NAME, sPackageName);
                data.putExtra(IntentParam.EXTRAS_ALARM_ID, a); // このプラグイン毎にユニークなID
                setResult(RESULT_OK, data);

                // このSharedPreferenceを消す
                Editor editor = mPrefSetting.edit();
                editor.clear();
                editor.commit();

                AlarmPrefManager man = new AlarmPrefManager(PluginSettingActivity.this, PREF_MANAGE_NAME);
                man.removeAlarmId(a);

                finish();
            }
        });

        if (-1 != alarmId) {
            // 編集用のコード
            getPreferenceManager().setSharedPreferencesName(AlarmPrefManager.ALARM_NAME_BASE + alarmId);
            mPrefSetting = getPreferenceManager().getSharedPreferences();
            mEditMode = IntentParam.EXTRAS_RESULT_UPDATED;

            // 最初の状態を保存しておく
            saveCurrentState(mPrefSetting);
        } else {
            // 新規用のコード
            AlarmPrefManager man = new AlarmPrefManager(this, PREF_MANAGE_NAME);
            getPreferenceManager().setSharedPreferencesName(man.nextPrefName());
            mPrefSetting = getPreferenceManager().getSharedPreferences();
            mEditMode = IntentParam.EXTRAS_RESULT_CREATED;
            mButtonDelete.setVisibility(View.INVISIBLE);

            // デフォルト値を保存
            Editor editor = mPrefSetting.edit();
            editor.putString("time_summary", "現在の設定 : 07:00");
            editor.putString("time", "07:00");
            editor.putInt("time_Hour", 7);
            editor.putInt("time_Minute", 0);
            editor.putBoolean("use_snooze", true);
            editor.putString("snooze_interval", "300000");
            editor.putBoolean("sun", true);
            editor.putBoolean("mon", true);
            editor.putBoolean("tue", true);
            editor.putBoolean("wed", true);
            editor.putBoolean("thu", true);
            editor.putBoolean("fri", true);
            editor.putBoolean("sat", true);
            editor.putString("ringtone", "content://settings/system/alarm_alert");
            editor.putBoolean("is_viblate_on", true);
            editor.putString("max_volume", "100");
            editor.putBoolean("need_to_incliment_volume", true);
            editor.putString("min_volume", "10");
            editor.commit();
        }

    }

    private void forNextAlarm(Intent intent) {
        // alarmidを元に呼び出して計算して、最終的な内容を返す(次回アラーム時間までのdelayを計算)
        int alarmId = 0;
        if (intent.hasExtra(IntentParam.EXTRAS_ALARM_ID)) {
            alarmId = intent.getIntExtra(IntentParam.EXTRAS_ALARM_ID, 0);
        } else {
            throw new RuntimeException("need EXTRAS_ALARM_ID");
        }
        getPreferenceManager().setSharedPreferencesName(AlarmPrefManager.ALARM_NAME_BASE + alarmId);
        mPrefSetting = getPreferenceManager().getSharedPreferences();
        AlarmUtil.dumpCurrentSharedPrerence(mPrefSetting);

        Intent data = new Intent();
        data.putExtra(IntentParam.EXTRAS_RESULT, IntentParam.EXTRAS_RESULT_CALCULATED);
        data.putExtra(IntentParam.EXTRAS_NEXT_DELAY_IN_MILLIS, getNextDelayInMillisForNextAlarm());
        data.putExtra(IntentParam.EXTRAS_PICKED_ALARM_RESOURCE, getSelectedAlarmResource());
        data.putExtra(IntentParam.EXTRAS_ALARM_SPECIAL_ACTION, ACTION_PLUGIN_ALARM);
        data.putExtra(IntentParam.EXTRAS_EDIT_SPECIAL_ACTION, ACTION_PLUGIN_EDIT);
        data.putExtra(IntentParam.EXTRAS_NEXT_ALARM_SPECIAL_ACTION, ACTION_PLUGIN_NEXT_ALARM);
        data.putExtra(IntentParam.EXTRAS_NEXT_SNOOZE_SPECIAL_ACTION, ACTION_PLUGIN_NEXT_SNOOZE);
        data.putExtra(IntentParam.EXTRAS_RESCHEDULE_SPECIAL_ACTION, ACTION_PLUGIN_RESCHEDULE);
        data.putExtra(IntentParam.EXTRAS_ALARM_ID, alarmId); // このプラグイン毎にユニークなID
        setResult(RESULT_OK, data);
        finish();
    }

    private void forNextSnooze(Intent intent) {
        // alarmidを元に呼び出して計算して、最終的な内容を返す(次回スヌーズ時間までのdelayを計算)
        int alarmId = 0;
        if (intent.hasExtra(IntentParam.EXTRAS_ALARM_ID)) {
            alarmId = intent.getIntExtra(IntentParam.EXTRAS_ALARM_ID, 0);
        } else {
            throw new RuntimeException("need EXTRAS_ALARM_ID");
        }
        getPreferenceManager().setSharedPreferencesName(AlarmPrefManager.ALARM_NAME_BASE + alarmId);
        mPrefSetting = getPreferenceManager().getSharedPreferences();
        AlarmUtil.dumpCurrentSharedPrerence(mPrefSetting);

        Intent data = new Intent();
        data.putExtra(IntentParam.EXTRAS_RESULT, IntentParam.EXTRAS_RESULT_CALCULATED);
        data.putExtra(IntentParam.EXTRAS_NEXT_DELAY_IN_MILLIS, getNextDelayInMillisForNextSnooze());
        data.putExtra(IntentParam.EXTRAS_PICKED_ALARM_RESOURCE, getSelectedAlarmResource());
        data.putExtra(IntentParam.EXTRAS_ALARM_SPECIAL_ACTION, ACTION_PLUGIN_ALARM);
        data.putExtra(IntentParam.EXTRAS_EDIT_SPECIAL_ACTION, ACTION_PLUGIN_EDIT);
        data.putExtra(IntentParam.EXTRAS_NEXT_ALARM_SPECIAL_ACTION, ACTION_PLUGIN_NEXT_ALARM);
        data.putExtra(IntentParam.EXTRAS_NEXT_SNOOZE_SPECIAL_ACTION, ACTION_PLUGIN_NEXT_SNOOZE);
        data.putExtra(IntentParam.EXTRAS_RESCHEDULE_SPECIAL_ACTION, ACTION_PLUGIN_RESCHEDULE);
        data.putExtra(IntentParam.EXTRAS_ALARM_ID, alarmId); // このプラグイン毎にユニークなID
        setResult(RESULT_OK, data);
        finish();
    }

    private long getNextDelayInMillisForNextSnooze() {
        return Long.valueOf(mPrefSetting.getString("snooze_interval", "0"));
    }

    private long getNextDelayInMillisForNextAlarm() {
        int hour = mPrefSetting.getInt("time_Hour", 0);
        int minute = mPrefSetting.getInt("time_Minute", 0);
        return AlarmUtil.getNextDelayInMillisForNextAlarm(this, hour, minute, AlarmUtil.getWeeks(mPrefSetting));
    }

    private String getSelectedAlarmResource() {
        return mPrefSetting.getString("ringtone", "");
    }

    private void saveCurrentState(SharedPreferences pref) {
        mPrefMapBeforeModify = pref.getAll();
    }

    private void restoreSavedState(SharedPreferences pref) {
        Editor editor = pref.edit();
        editor.clear();
        Iterator<String> ite = mPrefMapBeforeModify.keySet().iterator();
        while (ite.hasNext()) {
            String key = ite.next();
            Object value = mPrefMapBeforeModify.get(key);
            if (value instanceof String) {
                editor.putString(key, (String) value);
            } else if (value instanceof Integer) {
                editor.putInt(key, (Integer) value);
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean) value);
            } else if (value instanceof Float) {
                editor.putFloat(key, (Float) value);
            } else if (value instanceof Long) {
                editor.putLong(key, (Long) value);
            } else {
                Log.d(TAG, key + " : " + String.valueOf(mPrefMapBeforeModify.get(key)));
            }
        }
        editor.commit();
    }

}
