package com.example.tripnotify;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
<<<<<<< HEAD
=======
import android.database.Cursor;
>>>>>>> Commit 2 - Hackanooga 2012
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
<<<<<<< HEAD
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
=======
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
>>>>>>> Commit 2 - Hackanooga 2012
import android.widget.ToggleButton;

public class TripNotify extends Activity {

	private SensorManager sensorManager; // monitors accelerometer
	private float acceleration; // acceleration
	private float currentAcceleration; // current acceleration
	private float lastAcceleration; // last acceleration
	private TextView incidentStatus;
	private ToggleButton toggleButton;
	private TextView viewTime;
<<<<<<< HEAD
=======
	private Button contactButton;
>>>>>>> Commit 2 - Hackanooga 2012

	// value used to determine whether user fell
//	private static final int ACCELERATION_THRESHOLD = 15000;
	
	private static final int VERTICAL_FALL_THRESHOLD = 15;
<<<<<<< HEAD
=======
	private static int PICK_CONTACT;
	private static final int CONTACT_PICKER_RESULT = 1001; 
>>>>>>> Commit 2 - Hackanooga 2012

	// ON/OFF status switch
	private static boolean toggle_status = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flow_one);
		toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);
		toggleButton.setOnClickListener(toggleSwitchButtonListener);
		incidentStatus = (TextView) findViewById(R.id.textView3);
		viewTime = (TextView) findViewById(R.id.textView4);
<<<<<<< HEAD
=======
		contactButton = (Button) findViewById(R.id.button1);
>>>>>>> Commit 2 - Hackanooga 2012

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_flow_one, menu);
		return true;
	}
<<<<<<< HEAD
=======
	 
	  
	public void doLaunchContactPicker(View view) {  
	    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,  
	            Contacts.CONTENT_URI);  
	    startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);  
	}
	
	// called when the contact Button is touched
	private OnClickListener contactButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			doLaunchContactPicker(v);		
		} // end method onClick
	}; //

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == RESULT_OK) {
	        switch (requestCode) {
	        case CONTACT_PICKER_RESULT:
	            Cursor cursor = null;
	            String email = "";
	            try {
	                Uri result = data.getData();
	                Log.v("TAG2", "Got a contact result: "
	                        + result.toString());

	                // get the contact id from the Uri
	                String id = result.getLastPathSegment();

	                // query for everything email
	                cursor = getContentResolver().query(Email.CONTENT_URI,
	                        null, Email.CONTACT_ID + "=?", new String[] { id },
	                        null);

	                int emailIdx = cursor.getColumnIndex(Email.DATA);

	                // let's just get the first email
	                if (cursor.moveToFirst()) {
	                    email = cursor.getString(emailIdx);
	                    Log.v("TAG2", "Got email: " + email);
	                } else {
	                    Log.w("TAG2", "No results");
	                }
	            } catch (Exception e) {
	                Log.e("TAG2", "Failed to get email data", e);
	            } finally {
	                if (cursor != null) {
	                    cursor.close();
	                }
//	                EditText emailEntry = (EditText) findViewById(R.id.invite_email);
//	                emailEntry.setText(email);
	                if (email.length() == 0) {
	                    Toast.makeText(this, "No email found for contact.",
	                            Toast.LENGTH_LONG).show();
	                }

	            }

	            break;
	        }

	    } else {
	        Log.w("TAG2", "Warning: activity result not ok");
	    }
	}


 
>>>>>>> Commit 2 - Hackanooga 2012

	// called when the ON/OFF Button is touched
	private OnClickListener toggleSwitchButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (toggle_status == false) {
<<<<<<< HEAD
=======
				contactButton.setVisibility(View.GONE);
>>>>>>> Commit 2 - Hackanooga 2012
				toggle_status = true;
				// initialize acceleration values
				acceleration = 0.00f;
				currentAcceleration = SensorManager.GRAVITY_EARTH;
				lastAcceleration = SensorManager.GRAVITY_EARTH;
				enableAccelerometerListening(); // initialize accelerometer
			} else {
<<<<<<< HEAD
=======
				contactButton.setVisibility(View.VISIBLE);
>>>>>>> Commit 2 - Hackanooga 2012
				disableAccelerometerListening(); // initialize accelerometer
				toggle_status = false;
			}
			incidentStatus.setText("");
			viewTime.setText("");

		} // end method onClick
	}; //

	// enable listening for accelerometer events
	private void enableAccelerometerListening() {
		// initialize the SensorManager
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorManager.registerListener(sensorEventListener,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	} // end method enableAccelerometerListening

	// disable listening for accelerometer events
	private void disableAccelerometerListening() {
		// stop listening for sensor events
		if (sensorManager != null) {
			sensorManager.unregisterListener(sensorEventListener);
			sensorManager = null;
		} // end if
	} // end method disableAccelerometerListening

	// event handler for accelerometer events
	private SensorEventListener sensorEventListener = new SensorEventListener() {

		// use accelerometer to determine whether user shook device
		@Override
		public void onSensorChanged(SensorEvent event) {
			// get x, y, and z values for the SensorEvent
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];

			// save previous acceleration value
			lastAcceleration = currentAcceleration;

			// calculate the current acceleration
			currentAcceleration = x * x + y * y + z * z;

			// calculate the change in acceleration
			acceleration = currentAcceleration
					* (currentAcceleration - lastAcceleration);

			float maxX = 0;
			float maxY = 0;
			float maxZ = 0;
			if (x > maxX) {
				maxX = x;
			}
			if (y > maxY) {
				maxY = y;
			}
			if (z > maxZ) {
				maxZ = z;
			}

			// if the acceleration is above a certain threshold
			if(z > VERTICAL_FALL_THRESHOLD) {
//			if (acceleration > ACCELERATION_THRESHOLD) {
				
				disableAccelerometerListening();
				
				// create a list of the possible user actions
				final String[] possibleChoices = getResources().getStringArray(
						R.array.user_options);

				// create a new AlertDialog Builder and set its title
				AlertDialog.Builder choicesBuilder = new AlertDialog.Builder(
						TripNotify.this);
				choicesBuilder.setTitle(R.string.options_title);

				// add possibleChoices's items to the Dialog and set the
				// behavior when one of the items is clicked
				choicesBuilder.setItems(R.array.user_options,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								// switch(item)
								// {
								// case 0:
								if (0 == item) {
									// DO NOTHING
									incidentStatus.setText(R.string.case1disp);
									// incidentStatus.setTextColor(Color.GREEN);
									toggleButton.setChecked(false);
									toggle_status = false;
									// case 1:
								} else if (1 == item) {
									// CALL EMERGENCY CONTACT / SEND DATA
									Time today = new Time(Time.getCurrentTimezone());
									today.setToNow();
									viewTime.setText(today.format("%k:%M:%S"));
									
									incidentStatus.setText(R.string.case2disp);
									// incidentStatus.setText(Color.BLUE);
									toggleButton.setChecked(false);
									toggle_status = false;

									Intent dial = new Intent();
									dial.setAction("android.intent.action.DIAL");
									dial.setData(Uri.parse("tel:"
<<<<<<< HEAD
											+ "6179995522"));
=======
											+ "6178408658"));
>>>>>>> Commit 2 - Hackanooga 2012
									startActivity(dial);
									// case 2:
								} else if (2 == item) {
									// CALL 911 / SEND DATA
									Time today = new Time(Time.getCurrentTimezone());
									today.setToNow();
									viewTime.setText(today.format("%k:%M:%S"));
									
									incidentStatus.setText(R.string.case3disp);
									// incidentStatus.setTextColor(Color.RED);
									toggleButton.setChecked(false);
									toggle_status = false;

									Intent dial = new Intent();
									dial.setAction("android.intent.action.DIAL");
									dial.setData(Uri.parse("tel:"
<<<<<<< HEAD
											+ "6178885522"));
=======
											+ "9787788305"));
>>>>>>> Commit 2 - Hackanooga 2012
									startActivity(dial);
								}
							} // end method onClick
						} // end anonymous inner class
				); // end call to setItems

				// create an AlertDialog from the Builder
				AlertDialog choicesDialog = choicesBuilder.create();
				choicesDialog.show(); // show the Dialog
			}
		} // end method onSensorChanged

		// required method of interface SensorEventListener
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		} // end method onAccuracyChanged

	}; // end anonymous inner class

}
