package org.nuntius35.wrongpinshutdown;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	static final int ACTIVATION_REQUEST = 47; // identifies our request id
    final Context context = this;

	DevicePolicyManager devicePolicyManager;
	ComponentName demoDeviceAdmin;

    protected Button adminButton;
	protected Button testButton;
    protected Button settingButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        adminButton = (Button) findViewById(R.id.adminButton);
		testButton = (Button) findViewById(R.id.testButton);
        settingButton = (Button) findViewById(R.id.settingButton);

        SharedPreferences sharedPref = this.getSharedPreferences(
            "org.nuntius35.wrongpinshutdown.prefs", Context.MODE_PRIVATE);
        int m = sharedPref.getInt("max_tries", 2);
        sharedPref.edit().putInt("max_tries", m).apply();

		devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		demoDeviceAdmin = new ComponentName(this, LogInReceiver.class);

		adminButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
                boolean isActive = devicePolicyManager.isAdminActive(demoDeviceAdmin);
				if (!isActive) {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, demoDeviceAdmin);
			        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            getString(R.string.adminExplanation));
			        startActivityForResult(intent, ACTIVATION_REQUEST);
	        	}
	        	else {
                    Toast.makeText(MainActivity.this,
                    R.string.isAdmin, Toast.LENGTH_LONG).show();
		        }
			}
		});

        testButton.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               AlertDialog.Builder alert = new AlertDialog.Builder(context);
               TextView t_view = new TextView(context);
               t_view.setTextSize(14);
               t_view.setText(R.string.confirmReboot);
               alert.setCustomTitle(t_view);
               //alert.setTitle(R.string.ConfirmReboot);
               alert.setMessage(R.string.helpReboot);
               alert.setIcon(android.R.drawable.ic_dialog_alert);
               alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int whichButton) {
                       try {
                           Process process = Runtime.getRuntime().exec(
                           		new String[] { "su", "-c", "reboot", "-p" });
                           process.waitFor();
                       } catch (Exception ex) {
                           ex.printStackTrace();
                       }
                   }});
               alert.setNegativeButton(android.R.string.no, null);
               alert.show();
           }
        });

        settingButton.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               SharedPreferences sharedPref = getSharedPreferences(
                       "org.nuntius35.wrongpinshutdown.prefs", Context.MODE_PRIVATE);

               AlertDialog.Builder alert = new AlertDialog.Builder(context);
               TextView t_view = new TextView(context);
               t_view.setText(R.string.settingDialog);
               alert.setCustomTitle(t_view);
               //alert.setTitle(R.string.settingDialog);
               String[] numbers = {"1", "2", "3", "4", "5"};
               int checkedItem = sharedPref.getInt("max_tries",2);
               alert.setSingleChoiceItems(numbers, checkedItem,
                   new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                       SharedPreferences sharedPref = getSharedPreferences(
                               "org.nuntius35.wrongpinshutdown.prefs", Context.MODE_PRIVATE);
                       sharedPref.edit().putInt("max_tries", which).apply();
                     }
               });
               alert.setPositiveButton(android.R.string.ok, null);
               alert.show();
           }
        });
	}
}