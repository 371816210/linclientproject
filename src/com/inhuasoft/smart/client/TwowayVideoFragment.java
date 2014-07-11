package com.inhuasoft.smart.client;

import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneCallParams;
import org.linphone.core.LinphoneCore;
import org.linphone.mediastream.Log;
import org.linphone.mediastream.video.AndroidVideoWindowImpl;
import org.linphone.mediastream.video.capture.hwconf.AndroidCameraConfiguration;

import com.inhuasoft.smart.client.compatibility.Compatibility;
import com.inhuasoft.smart.client.compatibility.CompatibilityScaleGestureDetector;
import com.inhuasoft.smart.client.compatibility.CompatibilityScaleGestureListener;

import android.app.Activity;
import android.app.Fragment;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;

public class TwowayVideoFragment extends Fragment implements OnGestureListener,
		OnDoubleTapListener, CompatibilityScaleGestureListener, OnClickListener {

	private static final String TAG = TwowayVideoFragment.class
			.getCanonicalName();

	private SurfaceView mVideoView;
	private SurfaceView mCaptureView;
	private AndroidVideoWindowImpl androidVideoWindowImpl;
	private GestureDetector mGestureDetector;
	private float mZoomFactor = 1.f;
	private float mZoomCenterX, mZoomCenterY;
	private CompatibilityScaleGestureDetector mScaleDetector;
	private ScreenHome inCallActivity;
	ImageButton imgbtn_capture_img, imgbtn_record_video;
	Button imgbtn_hangup;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_twoway_video, container,
				false);
		
		
		    imgbtn_hangup = (Button) view.findViewById(R.id.imgbtn_hang_up);
	        imgbtn_hangup.setOnClickListener(this);
	        imgbtn_capture_img = (ImageButton)view.findViewById(R.id.imgbtn_capture_img);
	        imgbtn_capture_img.setOnClickListener(this);
	        //imgbtn_record_video =(ImageButton) view.findViewById(R.id.imgbtn_record_video);
	        //imgbtn_record_video.setOnClickListener(this);
		

		mVideoView = (SurfaceView) view.findViewById(R.id.videoSurface);
		mCaptureView = (SurfaceView) view
				.findViewById(R.id.videoCaptureSurface);
		mCaptureView.getHolder().setType(
				SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // Warning useless
															// because value is
															// ignored and
															// automatically set
															// by new APIs.

		fixZOrder(mVideoView, mCaptureView);

		androidVideoWindowImpl = new AndroidVideoWindowImpl(mVideoView,
				mCaptureView);
		androidVideoWindowImpl
				.setListener(new AndroidVideoWindowImpl.VideoWindowListener() {
					public void onVideoRenderingSurfaceReady(
							AndroidVideoWindowImpl vw, SurfaceView surface) {
						LinphoneManager.getLc().setVideoWindow(vw);
						mVideoView = surface;
					}

					public void onVideoRenderingSurfaceDestroyed(
							AndroidVideoWindowImpl vw) {
						LinphoneCore lc = LinphoneManager.getLc();
						if (lc != null) {
							lc.setVideoWindow(null);
						}
					}

					public void onVideoPreviewSurfaceReady(
							AndroidVideoWindowImpl vw, SurfaceView surface) {
						mCaptureView = surface;
						LinphoneManager.getLc().setPreviewWindow(mCaptureView);
					}

					public void onVideoPreviewSurfaceDestroyed(
							AndroidVideoWindowImpl vw) {
						// Remove references kept in jni code and restart camera
						LinphoneManager.getLc().setPreviewWindow(null);
					}
				});
		androidVideoWindowImpl.init();

		mVideoView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (mScaleDetector != null) {
					mScaleDetector.onTouchEvent(event);
				}

				mGestureDetector.onTouchEvent(event);
				// if (inCallActivity != null) {
				// inCallActivity.displayVideoCallControlsIfHidden();
				// }
				return true;
			}
		});

		return view;
	}

	private void fixZOrder(SurfaceView video, SurfaceView preview) {
		video.setZOrderOnTop(false);
		preview.setZOrderOnTop(true);
		preview.setZOrderMediaOverlay(true); // Needed to be able to display
												// control layout over
	}

	public void switchCamera() {
		try {
			int videoDeviceId = LinphoneManager.getLc().getVideoDevice();
			videoDeviceId = (videoDeviceId + 1)
					% AndroidCameraConfiguration.retrieveCameras().length;
			LinphoneManager.getLc().setVideoDevice(videoDeviceId);
			CallManager.getInstance().updateCall();

			// previous call will cause graph reconstruction -> regive preview
			// window
			if (mCaptureView != null) {
				LinphoneManager.getLc().setPreviewWindow(mCaptureView);
			}
		} catch (ArithmeticException ae) {
			Log.e("Cannot swtich camera : no camera");
		}
	}

	@Override
	public void onDestroy() {
		inCallActivity = null;

		mCaptureView = null;
		if (mVideoView != null) {
			mVideoView.setOnTouchListener(null);
			mVideoView = null;
		}
		if (androidVideoWindowImpl != null) {
			// Prevent linphone from crashing if correspondent hang up while you
			// are rotating
			androidVideoWindowImpl.release();
			androidVideoWindowImpl = null;
		}
		if (mGestureDetector != null) {
			mGestureDetector.setOnDoubleTapListener(null);
			mGestureDetector = null;
		}
		if (mScaleDetector != null) {
			mScaleDetector.destroy();
			mScaleDetector = null;
		}

		super.onDestroy();
	}

	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		inCallActivity = (ScreenHome) activity;
	}
	
	@Override
	public void onPause() {
		if (androidVideoWindowImpl != null) {
			synchronized (androidVideoWindowImpl) {
				/*
				 * this call will destroy native opengl renderer which is used
				 * by androidVideoWindowImpl
				 */
				LinphoneManager.getLc().setVideoWindow(null);
			}
		}

		if (mVideoView != null) {
			((GLSurfaceView) mVideoView).onPause();
		}

		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (mVideoView != null) {
			((GLSurfaceView) mVideoView).onResume();
		}

		if (androidVideoWindowImpl != null) {
			synchronized (androidVideoWindowImpl) {
				LinphoneManager.getLc().setVideoWindow(androidVideoWindowImpl);
			}
		}

		mGestureDetector = new GestureDetector(inCallActivity, this);
		mScaleDetector = Compatibility.getScaleGestureDetector(inCallActivity,
				this);

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	public boolean onScale(CompatibilityScaleGestureDetector detector) {
		mZoomFactor *= detector.getScaleFactor();
		// Don't let the object get too small or too large.
		// Zoom to make the video fill the screen vertically
		float portraitZoomFactor = ((float) mVideoView.getHeight())
				/ (float) ((3 * mVideoView.getWidth()) / 4);
		// Zoom to make the video fill the screen horizontally
		float landscapeZoomFactor = ((float) mVideoView.getWidth())
				/ (float) ((3 * mVideoView.getHeight()) / 4);
		mZoomFactor = Math.max(
				0.1f,
				Math.min(mZoomFactor,
						Math.max(portraitZoomFactor, landscapeZoomFactor)));

		LinphoneCall currentCall = LinphoneManager.getLc().getCurrentCall();
		if (currentCall != null) {
			currentCall.zoomVideo(mZoomFactor, mZoomCenterX, mZoomCenterY);
			return true;
		}
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		if (LinphoneUtils.isCallEstablished(LinphoneManager.getLc()
				.getCurrentCall())) {
			if (mZoomFactor == 1.f) {
				// Zoom to make the video fill the screen vertically
				float portraitZoomFactor = ((float) mVideoView.getHeight())
						/ (float) ((3 * mVideoView.getWidth()) / 4);
				// Zoom to make the video fill the screen horizontally
				float landscapeZoomFactor = ((float) mVideoView.getWidth())
						/ (float) ((3 * mVideoView.getHeight()) / 4);

				mZoomFactor = Math.max(portraitZoomFactor, landscapeZoomFactor);
			} else {
				resetZoom();
			}

			LinphoneManager.getLc().getCurrentCall()
					.zoomVideo(mZoomFactor, mZoomCenterX, mZoomCenterY);
			return true;
		}

		return false;
	}

	private void resetZoom() {
		mZoomFactor = 1.f;
		mZoomCenterX = mZoomCenterY = 0.5f;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if (LinphoneUtils.isCallEstablished(LinphoneManager.getLc()
				.getCurrentCall())) {
			if (mZoomFactor > 1) {
				// Video is zoomed, slide is used to change center of zoom
				if (distanceX > 0 && mZoomCenterX < 1) {
					mZoomCenterX += 0.01;
				} else if (distanceX < 0 && mZoomCenterX > 0) {
					mZoomCenterX -= 0.01;
				}
				if (distanceY < 0 && mZoomCenterY < 1) {
					mZoomCenterY += 0.01;
				} else if (distanceY > 0 && mZoomCenterY > 0) {
					mZoomCenterY -= 0.01;
				}

				if (mZoomCenterX > 1)
					mZoomCenterX = 1;
				if (mZoomCenterX < 0)
					mZoomCenterX = 0;
				if (mZoomCenterY > 1)
					mZoomCenterY = 1;
				if (mZoomCenterY < 0)
					mZoomCenterY = 0;

				LinphoneManager.getLc().getCurrentCall()
						.zoomVideo(mZoomFactor, mZoomCenterX, mZoomCenterY);
				return true;
			}
		}

		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	private void hangUp() {
		LinphoneCore lc = LinphoneManager.getLc();
		LinphoneCall currentCall = lc.getCurrentCall();
		
		if (currentCall != null) {
			lc.terminateCall(currentCall);
		} else if (lc.isInConference()) {
			lc.terminateConference();
		} else {
			lc.terminateAllCalls();
		}
	}
	
	
	private void takeSnapshot() {
		LinphoneCore lc = LinphoneManager.getLc();
		LinphoneCall currentCall = lc.getCurrentCall();
		if (currentCall != null) {
			currentCall.takeSnapshot("/mnt/sdcard/1.jpg");
		}
	}
	
	private void startVideoRecord()
	{
		LinphoneCore lc = LinphoneManager.getLc();
		LinphoneCall currentCall = lc.getCurrentCall();
		if (currentCall != null) {
			LinphoneCallParams params = currentCall.getCurrentParamsCopy();
			params.setRecordFile("/mnt/sdcard/1.mp4");
			//lc.updateCall(currentCall, params);
			currentCall.startRecording();
		}
		
	}
	
	private void stopVideoRecord() {
		LinphoneCore lc = LinphoneManager.getLc();
		LinphoneCall currentCall = lc.getCurrentCall();
		if (currentCall != null) {
			currentCall.stopRecording();
		}
	}
	
	private boolean IsRecord = false ; 
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(arg0.getId() == R.id.imgbtn_hang_up)
		{
			hangUp();
		}
		else if(arg0.getId() == R.id.imgbtn_capture_img) {
			takeSnapshot();
		}
		else if(arg0.getId() == R.id.imgbtn_record_video)
		{
			if(!IsRecord)
			{
				imgbtn_record_video.setImageResource(R.drawable.phone_hold_48);
				startVideoRecord();
				IsRecord = true ;
			}
		    else {
		    	imgbtn_record_video.setImageResource(R.drawable.phone_resume_48);
				stopVideoRecord();
				IsRecord = false ;
			}
		}
	}
}
