package org.nuntius35.wrongpinshutdown;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class ShutdownManager {
    private static final String TRIES_KEY = "tries";
    private static final String MAX_TRIES_KEY = "max_tries";

    private int tries;
    private int max_tries;
    private SharedPreferences sharedPref;

    ShutdownManager(Context context) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        tries = sharedPref.getInt(TRIES_KEY, 0);
        max_tries = Integer.parseInt(sharedPref.getString(MAX_TRIES_KEY, "2"));
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
			new String[] { "su", "-c", "reboot", "-p" });
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
