/* Copyright (C) 2010-2011, Mamadou Diop.
*  Copyright (C) 2011, Doubango Telecom.
*
* Contact: Mamadou Diop <diopmamadou(at)doubango(dot)org>
*	
* This file is part of imsdroid Project (http://code.google.com/p/imsdroid)
*
* imsdroid is free software: you can redistribute it and/or modify it under the terms of 
* the GNU General Public License as published by the Free Software Foundation, either version 3 
* of the License, or (at your option) any later version.
*	
* imsdroid is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
* See the GNU General Public License for more details.
*	
* You should have received a copy of the GNU General Public License along 
* with this program; if not, write to the Free Software Foundation, Inc., 
* 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package com.inhuasoft.smart.server;


import static android.content.Intent.ACTION_MAIN;

import java.util.ArrayList;
import java.util.Collection;

import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCall.State;
import org.linphone.mediastream.Log;

import com.inhuasoft.smart.server.LinphoneSimpleListener.LinphoneOnCallStateChangedListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends Activity implements LinphoneOnCallStateChangedListener {
	
	private static final int MENU_EXIT = 0;
	private static final int MENU_SETTINGS = 1;
	private static final int MENU_ORGIN = 2;
	private Handler mHandler = new Handler();
	private OrientationEventListener mOrientationHelper;
	private static final int CALL_ACTIVITY = 19;
	private int mAlwaysChangingPhoneAngle = -1;
	
	private static MainActivity instance;
	

	
	static final boolean isInstanciated() {
		return instance != null;
	}

	public static final MainActivity instance() {
		if (instance != null)
			return instance;
		throw new RuntimeException("MainActivity not instantiated yet");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_new);
		
		int rotation = getWindowManager().getDefaultDisplay().getRotation();
		switch (rotation) {
		case Surface.ROTATION_0:
			rotation = 0;
			break;
		case Surface.ROTATION_90:
			rotation = 90;
			break;
		case Surface.ROTATION_180:
			rotation = 180;
			break;
		case Surface.ROTATION_270:
			rotation = 270;
			break;
		}

		LinphoneManager.getLc().setDeviceRotation(rotation);
		mAlwaysChangingPhoneAngle = rotation;
		
		instance = this;

	
	}

	
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!LinphoneService.isReady())  {
			startService(new Intent(ACTION_MAIN).setClass(this, LinphoneService.class));
		}
		
		// Remove to avoid duplication of the listeners
		LinphoneManager.removeListener(this);
		LinphoneManager.addListener(this);
		
		LinphoneManager.getInstance().changeStatusToOnline();
		
		if (LinphoneManager.getLc().getCalls().length > 0) {
			LinphoneCall call = LinphoneManager.getLc().getCalls()[0];
			LinphoneCall.State callState = call.getState();
			if (callState == State.IncomingReceived) {
				startActivity(new Intent(this, IncomingCallActivity.class));
			}
		}
	}

	
	
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		Bundle extras = intent.getExtras();
		if (extras != null && extras.getBoolean("GoToChat", false)) {
			LinphoneService.instance().removeMessageNotification();
			String sipUri = extras.getString("ChatContactSipUri");
			//displayChat(sipUri);
		} else if (extras != null && extras.getBoolean("Notification", false)) {
			if (LinphoneManager.getLc().getCallsNb() > 0) {
				LinphoneCall call = LinphoneManager.getLc().getCalls()[0];
				if (call.getCurrentParamsCopy().getVideoEnabled()) {
					startVideoActivity(call);
				} else {
					startIncallActivity(call);
				}
			}
		} else {
			/*if (dialerFragment != null) {
				if (extras != null && extras.containsKey("SipUriOrNumber")) {
					if (getResources().getBoolean(R.bool.automatically_start_intercepted_outgoing_gsm_call)) {
						((DialerFragment) dialerFragment).newOutgoingCall(extras.getString("SipUriOrNumber"));
					} else {
						((DialerFragment) dialerFragment).displayTextInAddressBar(extras.getString("SipUriOrNumber"));
					}
				} else {
					((DialerFragment) dialerFragment).newOutgoingCall(intent);
				}
			}*/
			if (LinphoneManager.getLc().getCalls().length > 0) {
				LinphoneCall calls[] = LinphoneManager.getLc().getCalls();
				if (calls.length > 0) {
					LinphoneCall call = calls[0];
					
					if (call != null && call.getState() != LinphoneCall.State.IncomingReceived) {
						if (call.getCurrentParamsCopy().getVideoEnabled()) {
							startVideoActivity(call);
						} else {
							startIncallActivity(call);
						}
					}
				}
				
				// If a call is ringing, start incomingcallactivity
				Collection<LinphoneCall.State> incoming = new ArrayList<LinphoneCall.State>();
				incoming.add(LinphoneCall.State.IncomingReceived);
				if (LinphoneUtils.getCallsInState(LinphoneManager.getLc(), incoming).size() > 0) {
					if (InCallActivity.isInstanciated()) {
						InCallActivity.instance().startIncomingCallActivity();
					} else {
						startActivity(new Intent(this, IncomingCallActivity.class));
					}
				}
			}
		}
	}
	
	
	@Override
	protected void onDestroy() {
		LinphoneManager.removeListener(this);
		if (mOrientationHelper != null) {
			mOrientationHelper.disable();
			mOrientationHelper = null;
		}

		instance = null;
		super.onDestroy();
		System.gc();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(Menu.NONE, MainActivity.MENU_SETTINGS, 0, "Settings");
		menu.add(Menu.NONE, MainActivity.MENU_ORGIN, 0, "Orgin");
	    menu.add(Menu.NONE, MainActivity.MENU_EXIT, 0, "Exit");
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case MainActivity.MENU_EXIT:
				 exit();
				break;
			case MainActivity.MENU_SETTINGS:
				
				break;
            case MainActivity.MENU_ORGIN:
            	startActivity(new Intent().setClass(MainActivity.this,LinphoneActivity.class));
				break;
		}
		return true;
	}
	
	
	public void exit() {
		finish();
		stopService(new Intent(ACTION_MAIN).setClass(this, LinphoneService.class));
	}

	@Override
	public void onCallStateChanged(LinphoneCall call, State state,
			String message) {
		// TODO Auto-generated method stub
		if (state == State.IncomingReceived) {
			startActivity(new Intent(this, IncomingCallActivity.class));
		} else if (state == State.OutgoingInit) {
			if (call.getCurrentParamsCopy().getVideoEnabled()) {
				startVideoActivity(call);
			} else {
				startIncallActivity(call);
			}
		} else if (state == State.CallEnd || state == State.Error || state == State.CallReleased) {
			// Convert LinphoneCore message for internalization
			if (message != null && message.equals("Call declined.")) { 
				displayCustomToast(getString(R.string.error_call_declined), Toast.LENGTH_LONG);
			} else if (message != null && message.equals("Not Found")) {
				displayCustomToast(getString(R.string.error_user_not_found), Toast.LENGTH_LONG);
			} else if (message != null && message.equals("Unsupported media type")) {
				displayCustomToast(getString(R.string.error_incompatible_media), Toast.LENGTH_LONG);
			}
			//resetClassicMenuLayoutAndGoBackToCallIfStillRunning();
		}

		//int missedCalls = LinphoneManager.getLc().getMissedCallsCount();
		//displayMissedCalls(missedCalls);
	}
	
	public void startVideoActivity(LinphoneCall currentCall) {
		Intent intent = new Intent(this, InCallActivity.class);
		intent.putExtra("VideoEnabled", true);
		startOrientationSensor();
		startActivityForResult(intent, CALL_ACTIVITY);
	}

	public void startIncallActivity(LinphoneCall currentCall) {
		Intent intent = new Intent(this, InCallActivity.class);
		intent.putExtra("VideoEnabled", false);
		startOrientationSensor();
		startActivityForResult(intent, CALL_ACTIVITY);
	}
	
	
	private class LocalOrientationEventListener extends OrientationEventListener {
		public LocalOrientationEventListener(Context context) {
			super(context);
		}

		@Override
		public void onOrientationChanged(final int o) {
			if (o == OrientationEventListener.ORIENTATION_UNKNOWN) {
				return;
			}

			int degrees = 270;
			if (o < 45 || o > 315)
				degrees = 0;
			else if (o < 135)
				degrees = 90;
			else if (o < 225)
				degrees = 180;

			if (mAlwaysChangingPhoneAngle == degrees) {
				return;
			}
			mAlwaysChangingPhoneAngle = degrees;

			Log.d("Phone orientation changed to ", degrees);
			int rotation = (360 - degrees) % 360;
			LinphoneCore lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
			if (lc != null) {
				lc.setDeviceRotation(rotation);
				LinphoneCall currentCall = lc.getCurrentCall();
				if (currentCall != null && currentCall.cameraEnabled() && currentCall.getCurrentParamsCopy().getVideoEnabled()) {
					lc.updateCall(currentCall, null);
				}
			}
		}
	}
	
	
	/**
	 * Register a sensor to track phoneOrientation changes
	 */
	private synchronized void startOrientationSensor() {
		if (mOrientationHelper == null) {
			mOrientationHelper = new LocalOrientationEventListener(this);
		}
		mOrientationHelper.enable();
	}
	
	public void displayCustomToast(final String message, final int duration) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toastRoot));

				TextView toastText = (TextView) layout.findViewById(R.id.toastMessage);
				toastText.setText(message);

				final Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setDuration(duration);
				toast.setView(layout);
				toast.show();
			}
		});
	}
	

	
	
}
