package com.example.tripnotify;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
//extra comments

public class TripNotify extends Activity {

	private SensorManager sensorManager; // monitors accelerometer
	private float acceleration; // acceleration
	private float currentAcceleration; // current acceleration
	private float lastAcceleration; // last acceleration
	private TextView incidentStatus;
	private ToggleButton toggleButton;
	private TextView viewTime;
	private Button contactButton;

	// value used to determine whether user fell
//	private static final int ACCELERATION_THRESHOLD = 15000;
	
	private static final int VERTICAL_FALL_THRESHOLD = 15;
	private static int PICK_CONTACT;
	private static final int CONTACT_PICKER_RESULT = 1001; 

	// ON/OFF status switch
	private static boolean toggle_status = false;
	
	//Phone number selected as emergency contact
	private static String phoneNumber;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flow_one);
		toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);
		toggleButton.setOnClickListener(toggleSwitchButtonListener);
		incidentStatus = (TextView) findViewById(R.id.textView3);
		viewTime = (TextView) findViewById(R.id.textView4);
		contactButton = (Button) findViewById(R.id.button1);
		contactButton.setOnClickListener(contactButtonListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_flow_one, menu);
		return true;
	}
	 
	  
	public void doLaunchContactPicker(View view) {  
	    Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,  
	            Contacts.CONTENT_URI);  
	    startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);  
	    
	    new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		};
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
	            phoneNumber = "";
	            try {
	                Uri result = data.getData();
	                Log.v("TAG2", "Got a contact result: "
	                        + result.toString());

	                // get the contact id from the Uri
	                String id = result.getLastPathSegment();

	                // query for everything email
	                cursor = getContentResolver().query(Phone.CONTENT_URI,
	                        null, Phone.CONTACT_ID + "=?", new String[] { id },
	                        null);
	                
	                int phoneIdx = cursor.getColumnIndex(Phone.DATA);

	                // let's just get the first email
	                if (cursor.moveToFirst()) {
	                    phoneNumber = cursor.getString(phoneIdx);
	                    Log.v("TAG2", "Got phone number: " + phoneNumber);
	                } else {
	                    Log.w("TAG2", "No results");
	                }
	            } catch (Exception e) {
	                Log.e("TAG2", "Failed to get phone number data", e);
	            } finally {
	                if (cursor != null) {
	                    cursor.close();
	                }
//	                EditText emailEntry = (EditText) findViewById(R.id.invite_email);
//	                emailEntry.setText(email);
	                if (phoneNumber.length() == 0) {
	                    Toast.makeText(this, "No phone number found for contact.",
	                            Toast.LENGTH_LONG).show();
	                }
	            }
	            break;
	        }
	    } else {
	        Log.w("TAG2", "Warning: activity result not ok");
	    }
	}
	

	// called when the ON/OFF Button is touched
	private OnClickListener toggleSwitchButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (toggle_status == false) {
				contactButton.setVisibility(View.GONE);
				toggle_status = true;
				// initialize acceleration values
				acceleration = 0.00f;
				currentAcceleration = SensorManager.GRAVITY_EARTH;
				lastAcceleration = SensorManager.GRAVITY_EARTH;
				enableAccelerometerListening(); // initialize accelerometer
			} else {
				contactButton.setVisibility(View.VISIBLE);
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
											+ phoneNumber));
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
											+ "9787788305"));
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
