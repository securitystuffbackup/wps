package org.nuntius35.wrongpinshutdown;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class LogInReceiver extends DeviceAdminReceiver {

	@Override
	public void onPasswordFailed(Context context, Intent intent) {
		SharedPreferences sharedPref = context.getSharedPreferences(
		        "org.nuntius35.wrongpinshutdown.prefs", Context.MODE_PRIVATE);
		int tries = sharedPref.getInt("tries", 0);
        int max_tries = sharedPref.getInt("max_tries", 2);

		tries++;
		sharedPref.edit().putInt("tries", tries).apply();

		if (tries > max_tries) {
			tries = 0;
			sharedPref.edit().putInt("tries", tries).apply();
			try {
				Process process = Runtime.getRuntime().exec(
						new String[] { "su", "-c", "reboot", "-p" });
				process.waitFor();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void onPasswordSucceeded(Context context, Intent intent) {
        SharedPreferences sharedPref = context.getSharedPreferences(
		        "org.nuntius35.wrongpinshutdown.prefs", Context.MODE_PRIVATE);
		sharedPref.edit().putInt("tries", 0).apply();
	}
}