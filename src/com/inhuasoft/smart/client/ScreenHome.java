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
package com.inhuasoft.smart.client;



import static android.content.Intent.ACTION_MAIN;

import com.inhuasoft.smart.client.LinphoneSimpleListener.LinphoneOnMessageReceivedListener;
import com.inhuasoft.smart.client.LinphoneSimpleListener.LinphoneOnRegistrationStateChangedListener;

import org.linphone.core.LinphoneAddress;
import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCallParams;
import org.linphone.core.LinphoneChatMessage;
import org.linphone.core.LinphoneCore;
import org.linphone.core.LinphoneCall.State;
import org.linphone.core.LinphoneCore.RegistrationState;

import com.inhuasoft.smart.client.LinphoneSimpleListener.LinphoneOnCallStateChangedListener;
import org.linphone.mediastream.Log;
import org.linphone.mediastream.video.capture.hwconf.AndroidCameraConfiguration;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
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
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ScreenHome extends Activity  implements OnClickListener ,LinphoneOnCallStateChangedListener 
,LinphoneOnMessageReceivedListener ,LinphoneOnRegistrationStateChangedListener{
	private static String TAG = ScreenHome.class.getCanonicalName();
	
	public static final int HOME_INTENT_FLAG = 0;
	public static final int VIDEO_INTENT_FLAG = HOME_INTENT_FLAG + 1 ;
	public static final int SWITCH_INTENT_FLAG = VIDEO_INTENT_FLAG + 1;
	public static final int CONTROL_INTENT_FLAG = SWITCH_INTENT_FLAG + 1;
	public static final int MORE_INTENT_FLAG = CONTROL_INTENT_FLAG + 1;
	public static final int DIAL_INTENT_FLAG = MORE_INTENT_FLAG + 1;
	public static final int VDIAL_INTENT_FLAG = DIAL_INTENT_FLAG + 1;
	public static final int TWOWAY_INTENT_FLAG = VDIAL_INTENT_FLAG + 1;
	public static final int PHOTO_INTENT_FLAG = TWOWAY_INTENT_FLAG + 1;
	public static final int RECORD_INTENT_FLAG = PHOTO_INTENT_FLAG + 1;
	public static final int AUDIO_INTENT_FLAG = PHOTO_INTENT_FLAG + 1;
	
	private MenubarView mHomeLayout;
	private MenubarView mVideoLayout;
	private MenubarView mSwitchLayout;
	private MenubarView mControlLayout;
	private MenubarView mMoreLayout;


	private FragmentManager mFragmentManager;

	private HomeFragment mHomeFragment;
	private VideoFragment mVideoFragment;
	private SwitchFragment mSwitchFragment;
	private ControlFragment mControlFragment;
	private MoreFragment mMoreFragment;
	private TwowayVideoFragment mTwowayVideoFragment;
	private AudioCall_New_Fragment mAudioCallFragment;
	private int mAlwaysChangingPhoneAngle = -1;
	
	
	
	private static final int MENU_EXIT = 0;
	private static final int MENU_SETTINGS = 1;
	private static final int MENU_ORGIN = 2;
	
	
	
	private static ScreenHome instance;
	
	private Handler mHandler = new Handler();
	private OrientationEventListener mOrientationHelper;
	private static final int CALL_ACTIVITY = 19;
	
	private boolean isSpeakerEnabled = false, isMicMuted = false, isVideoEnabled, isTransferAllowed, isAnimationDisabled;
	private int cameraNumber;
	
	static final boolean isInstanciated() {
		return instance != null;
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




	public static final ScreenHome instance() {
		if (instance != null)
			return instance;
		throw new RuntimeException("MainActivity not instantiated yet");
	}
	


	private void initViews() {
		// TODO Auto-generated method stub
		mHomeLayout = (MenubarView) findViewById(R.id.home_layout);
		mHomeLayout.setOnClickListener(this);
		mVideoLayout = (MenubarView) findViewById(R.id.video_layout);
		mVideoLayout.setOnClickListener(this);
		mSwitchLayout = (MenubarView) findViewById(R.id.switch_layout);
		mSwitchLayout.setOnClickListener(this);
		mControlLayout = (MenubarView) findViewById(R.id.control_layout);
		mControlLayout.setOnClickListener(this);
		mMoreLayout = (MenubarView) findViewById(R.id.more_layout);
		mMoreLayout.setOnClickListener(this);

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		initViews();
        mFragmentManager = getFragmentManager();
		setTabSelection(HOME_INTENT_FLAG);
		
		
		
		cameraNumber = AndroidCameraConfiguration.retrieveCameras().length;
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.home_layout:
			setTabSelection(HOME_INTENT_FLAG);
			break;
		case R.id.video_layout:
			setTabSelection(VIDEO_INTENT_FLAG);
			break;
		case R.id.switch_layout:
			setTabSelection(SWITCH_INTENT_FLAG);
			break;
		case R.id.control_layout:
			setTabSelection(CONTROL_INTENT_FLAG);
			break;
		case R.id.more_layout:
			setTabSelection(MORE_INTENT_FLAG);
			break;

		default:
			break;
		}
	}
	
	
	
	private void switchVideo(final boolean displayVideo, final boolean isInitiator) {
		final LinphoneCall call = LinphoneManager.getLc().getCalls()[0];
		if (call == null) {
			return;
		}
		
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (!displayVideo) {
					if (isInitiator) {
						LinphoneCallParams params = call.getCurrentParamsCopy();
						params.setVideoEnabled(false);
						LinphoneManager.getLc().updateCall(call, params);
					}
					showAudioView();
				} else {
					if (!call.getRemoteParams().isLowBandwidthEnabled()) {
						LinphoneManager.getInstance().addVideo();
						showVideoView();
					} else {
						displayCustomToast(getString(R.string.error_low_bandwidth), Toast.LENGTH_LONG);
					}
				}
			}
		});
	}
	
	
	private void showAudioView() {
		//video.setBackgroundResource(R.drawable.video_on);

		LinphoneManager.startProximitySensorForActivity(ScreenHome.this);
		setTabSelection(ScreenHome.AUDIO_INTENT_FLAG);
	}
	
	private void showVideoView() {
		LinphoneManager.stopProximitySensorForActivity(ScreenHome.this);
		setTabSelection(ScreenHome.TWOWAY_INTENT_FLAG);
	}
	
	public void setTabSelection(int index) {
		// TODO Auto-generated method stub
		// 每次选中之前先清楚掉上次的选中状态
		// 开启一个Fragment事务
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		
		switch (index) {
		case HOME_INTENT_FLAG:
			clearSelection();
			mHomeLayout.setSelected(true);
			if(mHomeFragment == null) {
				mHomeFragment = new HomeFragment();
				transaction.add(R.id.main_content, mHomeFragment);
			} else {
				transaction.show(mHomeFragment);
			}
			break;
		case VIDEO_INTENT_FLAG:
			clearSelection();
			mVideoLayout.setSelected(true);
			if(mVideoFragment == null) {
				mVideoFragment = new VideoFragment();
				transaction.add(R.id.main_content, mVideoFragment);
			} else {
				transaction.show(mVideoFragment);
			}
			break;
		case SWITCH_INTENT_FLAG:
			clearSelection();
			mSwitchLayout.setSelected(true);
			if(mSwitchFragment == null) {
				mSwitchFragment = new SwitchFragment();
				transaction.add(R.id.main_content, mSwitchFragment);
			} else {
				transaction.show(mSwitchFragment);
			}
			break;
			
		case CONTROL_INTENT_FLAG:
			clearSelection();
			mControlLayout.setSelected(true);
			if(mControlFragment == null) {
				mControlFragment = new ControlFragment();
				transaction.add(R.id.main_content, mControlFragment);
			} else {
				transaction.show(mControlFragment);
			}
			break;
			
		case MORE_INTENT_FLAG:
			clearSelection();
			mMoreLayout.setSelected(true);
			if(mMoreFragment == null) {
				mMoreFragment = new MoreFragment();
				transaction.add(R.id.main_content, mMoreFragment);
			} else {
				transaction.show(mMoreFragment);
			}
			break;
			
		case TWOWAY_INTENT_FLAG:
			if(mTwowayVideoFragment == null) {
				mTwowayVideoFragment = new TwowayVideoFragment();
				transaction.add(R.id.main_content, mTwowayVideoFragment);
			} else {
				transaction.show(mTwowayVideoFragment);
			}
			break;
		case AUDIO_INTENT_FLAG:
			if(mAudioCallFragment == null) {
				mAudioCallFragment = new AudioCall_New_Fragment();
				transaction.add(R.id.main_content, mAudioCallFragment);
			} else {
				transaction.show(mAudioCallFragment);
			}
			break;

		default:
			break;
		}
		transaction.commit();
	}

	
	
	@Override
	protected void onDestroy() {
       super.onDestroy();
       LinphoneManager.removeListener(this);
		if (mOrientationHelper != null) {
			mOrientationHelper.disable();
			mOrientationHelper = null;
		}

		instance = null;
		super.onDestroy();
		System.gc();
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
				startVideo(call);
			} else {
				startIncall(call);
			}
		} 
		else if (state == State.StreamsRunning)
		{

			boolean isVideoEnabledInCall = call.getCurrentParamsCopy().getVideoEnabled();
			if (isVideoEnabledInCall != isVideoEnabled) {
				isVideoEnabled = isVideoEnabledInCall;
				switchVideo(isVideoEnabled, false);
			}

			//LinphoneManager.getLc().enableSpeaker(isSpeakerEnabled);

			//isMicMuted = LinphoneManager.getLc().isMicMuted();
			//enableAndRefreshInCallActions();
		
		}
		else if (state == State.CallEnd || state == State.Error || state == State.CallReleased) {
			// Convert LinphoneCore message for internalization
			if (message != null && message.equals("Call declined.")) { 
				displayCustomToast(getString(R.string.error_call_declined), Toast.LENGTH_LONG);
			} else if (message != null && message.equals("Not Found")) {
				displayCustomToast(getString(R.string.error_user_not_found), Toast.LENGTH_LONG);
			} else if (message != null && message.equals("Unsupported media type")) {
				displayCustomToast(getString(R.string.error_incompatible_media), Toast.LENGTH_LONG);
			}
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					setTabSelection(ScreenHome.HOME_INTENT_FLAG);
				}
			});
			//setTabSelection(ScreenHome.HOME_INTENT_FLAG);
			//resetClassicMenuLayoutAndGoBackToCallIfStillRunning();
		}

		//int missedCalls = LinphoneManager.getLc().getMissedCallsCount();
		//displayMissedCalls(missedCalls);
	}
	
	public void startVideo(LinphoneCall currentCall) {
		//Intent intent = new Intent(this, InCallActivity.class);
		//intent.putExtra("VideoEnabled", true);
		//startOrientationSensor();
		//startActivityForResult(intent, CALL_ACTIVITY);
		isVideoEnabled = true ;
		setTabSelection(ScreenHome.TWOWAY_INTENT_FLAG);
	}

	public void startIncall(LinphoneCall currentCall) {
		//Intent intent = new Intent(this, InCallActivity.class);
		//intent.putExtra("VideoEnabled", false);
		//startOrientationSensor();
		//startActivityForResult(intent, CALL_ACTIVITY);
		isVideoEnabled = false ;
		setTabSelection(ScreenHome.AUDIO_INTENT_FLAG);
	}
	
	
	
	private int getTextColor(int id) {
		// TODO Auto-generated method stub
		return getResources().getColor(id);
	}

	private void clearSelection() {
		mHomeLayout.setSelected(false);
		mVideoLayout.setSelected(false);
		mSwitchLayout.setSelected(false);
		mControlLayout.setSelected(false);
		mMoreLayout.setSelected(false);
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (mVideoFragment != null) {
			transaction.hide(mVideoFragment);
		}
		if (mHomeFragment != null) {
			transaction.hide(mHomeFragment);
		}
		if (mSwitchFragment != null) {
			transaction.hide(mSwitchFragment);
		}
		if (mControlFragment != null) {
			transaction.hide(mControlFragment);
		}
		if (mMoreFragment != null) {
			transaction.hide(mMoreFragment);
		}
		if (mTwowayVideoFragment != null) {
			transaction.hide(mTwowayVideoFragment);
		}
		if (mAudioCallFragment != null) {
			transaction.hide(mAudioCallFragment);
		}
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
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(Menu.NONE, ScreenHome.MENU_SETTINGS, 0, "Settings");
		menu.add(Menu.NONE, ScreenHome.MENU_ORGIN, 0, "Orgin");
	    menu.add(Menu.NONE, ScreenHome.MENU_EXIT, 0, "Exit");
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case ScreenHome.MENU_EXIT:
				 exit();
				break;
			case ScreenHome.MENU_SETTINGS:
				
				break;
            case ScreenHome.MENU_ORGIN:
            	startActivity(new Intent().setClass(ScreenHome.this,LinphoneActivity.class));
				break;
		}
		return true;
	}




	@Override
	public void onRegistrationStateChanged(RegistrationState state) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void onMessageReceived(LinphoneAddress from,
			LinphoneChatMessage message, int id) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
