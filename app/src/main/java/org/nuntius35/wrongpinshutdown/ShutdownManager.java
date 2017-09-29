package org.nuntius35.wrongpinshutdown;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class ShutdownManager {
    private static final String TRIES_KEY = "tries";
    private static final String MAX_TRIES_KEY = "max_tries";
    private static final String SHUTDOWN_KEY = "shutdown_cmd";

    private int tries;
    private int max_tries;
    private SharedPreferences sharedPref;
    private String shutdown_cmd;

    ShutdownManager(Context context) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        tries = sharedPref.getInt(TRIES_KEY, 0);
        max_tries = Integer.parseInt(sharedPref.getString(MAX_TRIES_KEY, "2"));
        shutdown_cmd = sharedPref.getString(SHUTDOWN_KEY,null);
    }

    void resetTries() {
        tries = 0;
        sharedPref.edit().putInt(TRIES_KEY, tries).apply();
    }

    void incTries() {
        tries++;
        sharedPref.edit().putInt(TRIES_KEY, tries).apply();
        max_tries = Integer.parseInt(sharedPref.getString(MAX_TRIES_KEY, "2"));
        if (tries > max_tries) {
			resetTries();
			shutdownDevice();
		}
    }

    private void shutdownDevice() {
        try {
            Process process = Runtime.getRuntime().exec(
			new String[] { "su", "-c", shutdown_cmd});
			process.waitFor();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    void testRoot() {
        try {
            Process process = Runtime.getRuntime().exec(
			new String[] {"su", "-c", "ls"});
			process.waitFor();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
