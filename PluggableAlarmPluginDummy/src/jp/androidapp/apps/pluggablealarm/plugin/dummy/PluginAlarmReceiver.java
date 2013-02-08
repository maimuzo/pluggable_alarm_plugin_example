package jp.androidapp.apps.pluggablealarm.plugin.dummy;

import jp.androidapp.libs.pluggablealarm.AlarmData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * アラーム登録されたPendingIntentに反応するレシーバ
 * ここからServiceを呼び出すなりAlarmActivityを起動するなり
 * @author maimuzo
 *
 */
public class PluginAlarmReceiver extends BroadcastReceiver  {
	private static final String TAG = "PluginAlarmReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, PluginAlarmActivity.class);
		// パラメータ詰め替え
		AlarmData a = AlarmData.from(intent);
		Log.d(TAG, "alarm data: " + a);
		a.setForAlarmTo(i);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}

}
