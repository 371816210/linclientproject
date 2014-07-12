package com.inhuasoft.smart.client;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class VideoFragment extends Fragment {
	
	private ImageView mTwoWayVideo,  mAudioButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_video, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mTwoWayVideo = (ImageView) getActivity().findViewById(R.id.twoway_video_butt);
		mTwoWayVideo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LinphonePreferences.instance().setInitiateVideoCall(true);
				LinphonePreferences.instance().setAutomaticallyAcceptVideoRequests(true);
				LinphoneManager.getInstance().newOutgoingCall("myitm1@opensips.org","myitm1");
			}
		});
		
		mAudioButton = (ImageView) getActivity().findViewById(R.id.twoway_audio_butt);
		mAudioButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LinphonePreferences.instance().setInitiateVideoCall(false);
				LinphonePreferences.instance().setAutomaticallyAcceptVideoRequests(false);
				LinphoneManager.getInstance().newOutgoingCall("myitm1@opensips.org","myitm1");
			}
		});
	
	}

}
