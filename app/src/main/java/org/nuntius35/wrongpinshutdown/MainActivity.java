package org.nuntius35.wrongpinshutdown;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.preference.PreferenceFragment;

public class MainActivity extends AppCompatActivity {
    static final int ACTIVATION_REQUEST = 1;
    final Context context = this;

    DevicePolicyManager devicePolicyManager;
    ComponentName demoDeviceAdmin;

    ShutdownManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        sm = new ShutdownManager(context);

        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        demoDeviceAdmin = new ComponentName(this, LogInReceiver.class);

        boolean isActive = devicePolicyManager.isAdminActive(demoDeviceAdmin);
        if (!isActive) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, demoDeviceAdmin);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    getString(R.string.adminExplanation));
            startActivityForResult(intent, ACTIVATION_REQUEST);
        }

        sm.testRoot();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
