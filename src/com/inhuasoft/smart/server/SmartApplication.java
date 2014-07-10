package com.inhuasoft.smart.server;

import org.apache.http.client.CookieStore;

import android.app.Application;

public class SmartApplication extends Application {

    	private CookieStore cookies;  
	    public CookieStore getCookie(){   
	        return cookies;
	    }
	    public void setCookie(CookieStore cks){
	        cookies = cks;
	    }
}
