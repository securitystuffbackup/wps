package org.nuntius35.wrongpinshutdown;

import android.content.Context;
import android.content.SharedPreferences;

class ShutdownManager {
    private static final String MAX_TRIES_KEY = "max_tries";
    private static final String TRIES_KEY = "tries";
    private static final String PREFERENCES_KEY = "org.nuntius35.wrongpinshutdown.prefs";
    private int tries;
    private int max_tries;
    private SharedPreferences sharedPref;

    ShutdownManager(Context context) {
        sharedPref = context.getSharedPreferences(
		        PREFERENCES_KEY, Context.MODE_PRIVATE);
        tries = sharedPref.getInt(TRIES_KEY, 0);
        max_tries = sharedPref.getInt(MAX_TRIES_KEY, 2);
    }

    int getMaxTries() {
        return max_tries;
    }

    void setMaxTries(int m) {
        max_tries = m;
        sharedPref.edit().putInt(MAX_TRIES_KEY, m).apply();
    }

    void resetTries() {
        tries = 0;
        sharedPref.edit().putInt(TRIES_KEY, tries).apply();
    }

    void incTries() {
        tries++;
        sharedPref.edit().putInt(TRIES_KEY, tries).apply();
        if (tries > max_tries) {
			resetTries();
			shutdownDevice();
		}
    }

    void shutdownDevice() {
        try {
            Process process = Runtime.getRuntime().exec(
			new String[] { "su", "-c", "reboot", "-p" });
			process.waitFor();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
