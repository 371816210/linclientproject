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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.inhuasoft.smart.client.setup.SetupActivity;

import android.R.integer;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class LoginActivity extends Activity implements OnClickListener {
	private static String TAG = LoginActivity.class.getCanonicalName();


	
	Button btnSubmit;
	EditText editUserName, editPassword, editRePassword;
	TextView txtMsginfo;
	CheckBox chkMsginfo;

	

	private static final int Admin_Login_Action = 0X06;
	private static final int Admin_Login_Fail = 0X07;
	private static final int Admin_Login_Success = 0X08;
	private static final int Sip_Add_User_Action = 0X09;
	private static final int Sip_Add_User_Fail = 0X10;
	private static final int Sip_Add_User_Success = 0X11;
	private static final int Sip_Add_Device_Action = 0X12;
	private static final int Sip_Add_Device_Fail = 0X13;
	private static final int Sip_Add_Device_Success = 0X14;
	private static final int Device_Reg_Action = 0X15;
	private static final int Device_Reg_Success = 0X16;
	private static final int Device_Reg_Fail = 0X17;
	private static final int User_Reg_Action = 0X18;
	private static final int User_Reg_Success = 0X019;
	private static final int User_Reg_Fail = 0X20;
	private static final int Bind_User_Device_Action = 0X21;
	private static final int Bind_User_Device_Success = 0X22;
	private static final int Bind_User_Device_Fail = 0X23;
	private static final int GetUserByDevice_Action = 0x24;
	private static final int GetUserByDevice_Success = 0x25;
	private static final int GetUserByDevice_Fail = 0x26;
	private static final int Show_Reg_UI = 0x27;
	private static final int Show_Login_UI = 0x28;
	private static final int StartMain = 0x29;
	private static final int User_Login_Action = 0x30;
	private static final int User_Login_Success = 0x31;
	private static final int User_Login_Fail = 0x32;


	 private void  SetSystemInfo() {
		/*
		 mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_IMPU,"sip:"+getDeviceNo()+"@115.28.9.71");
		 mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_IMPI, getDeviceNo());
		 mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_DISPLAY_NAME, getDeviceNo());
		 mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_PASSWORD, getDeviceNo());
		 mConfigurationService.putBoolean(NgnConfigurationEntry.DEVICE_REG, true);
		 mConfigurationService.putBoolean(NgnConfigurationEntry.DEVICE_LOGIN, true);
		 mConfigurationService.putString(NgnConfigurationEntry.USERNAME,editUserName.getText().toString());
		 mConfigurationService.putString(NgnConfigurationEntry.USER_PASSWORD,editPassword.getText().toString());
		 mConfigurationService.putString(NgnConfigurationEntry.NETWORK_PCSCF_DISCOVERY,NgnConfigurationEntry.DEFAULT_NETWORK_PCSCF_DISCOVERY);
		 mConfigurationService.putString(NgnConfigurationEntry.NETWORK_PCSCF_HOST,NgnConfigurationEntry.DEFAULT_NETWORK_PCSCF_HOST);
		 mConfigurationService.putInt(NgnConfigurationEntry.NETWORK_PCSCF_PORT,NgnConfigurationEntry.DEFAULT_NETWORK_PCSCF_PORT);
		 mConfigurationService.putString(NgnConfigurationEntry.NETWORK_TRANSPORT,NgnConfigurationEntry.DEFAULT_NETWORK_TRANSPORT);
		 mConfigurationService.putBoolean(NgnConfigurationEntry.NETWORK_USE_WIFI,true);
		 mConfigurationService.putBoolean(NgnConfigurationEntry.NETWORK_USE_3G,true);
		 mConfigurationService.putString(NgnConfigurationEntry.NETWORK_IP_VERSION, 
					"ipv4");
		 mConfigurationService.putString(NgnConfigurationEntry.Devices_SIP_NUMBER,"sip:"+editUserName.getText().toString()+"@115.28.9.71" );
		 if(!mConfigurationService.commit()){
				Log.e(TAG, "Failed to Commit() configuration");
			}
		 */
		 if(LinphonePreferences.instance().getAccountCount() < 1)
		 {
		   LinphonePreferences.instance().CreatedAccount(getDeviceNo(),getDeviceNo(),"115.28.9.71");
		 }
		 
	}
	 
	 private void  StartMain() {
		 SetSystemInfo();
		 startActivity(new Intent().setClass(LoginActivity.this, MainActivity.class));
		 finish();
		/* if(mScreenService != null )
		 {
			 try {
				    SetSystemInfo();
					//mSipService.register(LoginActivity.this);

					//Thread.sleep(1000);
				   //mScreenService.show(ScreenNetwork.class);
				    mScreenService.show(ScreenHome.class);
				   finish();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		 }*/
	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {

			case Admin_Login_Action:
				SipAdminLoginThread thread_admin_login = new SipAdminLoginThread();
				thread_admin_login.start();
				break;
			case Admin_Login_Fail:
				int admin_login_errorcode = msg.arg1;
				final AlertDialog admin_login_dialog = CustomDialog.create(
						LoginActivity.this, R.drawable.exit_48, null,
						" A error has occurred, the error code is  "
								+ admin_login_errorcode, "exit",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								//LoginActivity.this.finish();
								ResetInput();

							}
						}, null, null);
				admin_login_dialog.show();
				break;
			case Admin_Login_Success:
				Message sip_add_user_message = mHandler
						.obtainMessage(Sip_Add_User_Action);
				sip_add_user_message.sendToTarget();
				break;
			case Sip_Add_User_Action:
				SipAddUserThread thread_add_user = new SipAddUserThread();
				thread_add_user.start();
				break;
			case Sip_Add_User_Fail:
				int sip_add_user_errorcode = msg.arg1;
				if (sip_add_user_errorcode == 106) {
					final AlertDialog sip_add_user_dialog = CustomDialog
							.create(LoginActivity.this, R.drawable.exit_48, null,
									" The user name has been registered! the return code is  "
											+ sip_add_user_errorcode, "OK",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											ResetInput();

										}
									}, null, null);
					sip_add_user_dialog.show();
				} else {
					final AlertDialog sip_add_user_dialog = CustomDialog
							.create(LoginActivity.this, R.drawable.exit_48, null,
									" A error has occurred, the error code is  "
											+ sip_add_user_errorcode, "exit",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											//LoginActivity.this.finish();
											ResetInput();

										}
									}, null, null);
					sip_add_user_dialog.show();
				}
				break;
			case Sip_Add_User_Success:
				Message sip_add_device_message = mHandler
						.obtainMessage(Sip_Add_Device_Action);
				sip_add_device_message.sendToTarget();
				break;
			case Sip_Add_Device_Action:
				SipAddDeviceThread thread_add_device = new SipAddDeviceThread();
				thread_add_device.start();
				break;
			case Sip_Add_Device_Fail:
				int sip_add_device_errorcode = msg.arg1;
				if (sip_add_device_errorcode == 206) {
					final AlertDialog sip_add_device_dialog = CustomDialog
							.create(LoginActivity.this, R.drawable.exit_48, null,
									" The device has been registered! the return code is   "
											+ sip_add_device_errorcode, "OK",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											    //LoginActivity.this.finish();
											ResetInput();

										}
									}, null, null);
					sip_add_device_dialog.show();
				} else {
					final AlertDialog sip_add_device_dialog = CustomDialog
							.create(LoginActivity.this, R.drawable.exit_48, null,
									" A error has occurred, the error code is  "
											+ sip_add_device_errorcode, "exit",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											//LoginActivity.this.finish();
											ResetInput();

										}
									}, null, null);
					sip_add_device_dialog.show();
				}

				break;
			case Sip_Add_Device_Success:
				Message user_reg_message = mHandler
						.obtainMessage(User_Reg_Action);
				user_reg_message.sendToTarget();
				break;
			case Device_Reg_Action:
				DeviceRegThread thread_device_reg = new DeviceRegThread();
				thread_device_reg.start();
				break;
			case Device_Reg_Success:
				Message bind_user_device_message = mHandler
						.obtainMessage(Bind_User_Device_Action);
				bind_user_device_message.sendToTarget();
				break;
			case Device_Reg_Fail:
				int device_reg_errorcode = msg.arg1;
				if (device_reg_errorcode == 505) {
					final AlertDialog device_reg_dialog = CustomDialog.create(
							LoginActivity.this, R.drawable.exit_48, null,
							" The device has been registered! the return code is   "
									+ device_reg_errorcode, "OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// LoginActivity.this.finish();
									// enter login window
									ResetInput();

								}
							}, null, null);
					device_reg_dialog.show();
				} else {
					final AlertDialog device_reg_dialog = CustomDialog.create(
							LoginActivity.this, R.drawable.exit_48, null,
							" A error has occurred, the error code is  "
									+ device_reg_errorcode, "exit",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									//LoginActivity.this.finish();
									ResetInput();

								}
							}, null, null);
					device_reg_dialog.show();
				}
				break;
			case User_Reg_Action:
				UserRegThread thread_user_reg = new UserRegThread();
				thread_user_reg.start();
				break;
			case User_Reg_Success:
				Message device_reg_message = mHandler
						.obtainMessage(Device_Reg_Action);
				device_reg_message.sendToTarget();
				break;
			case User_Reg_Fail:
				int user_reg_errorcode = msg.arg1;
				if (user_reg_errorcode == 605) {
					final AlertDialog user_reg_dialog = CustomDialog.create(
							LoginActivity.this, R.drawable.exit_48, null,
							"The user name has been registered! the return code is "
									+ user_reg_errorcode, "OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									ResetInput();

								}
							}, null, null);
					user_reg_dialog.show();
				} else {
					final AlertDialog user_reg_dialog = CustomDialog.create(
							LoginActivity.this, R.drawable.exit_48, null,
							" A error has occurred,the error code is  "
									+ user_reg_errorcode, "exit",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									//LoginActivity.this.finish();
									ResetInput();

								}
							}, null, null);
					user_reg_dialog.show();
				}
				break;
			case Bind_User_Device_Action:
				BindUserDeviceThread thread_bind_user_device = new BindUserDeviceThread();
				thread_bind_user_device.start();
				break;
			case Bind_User_Device_Success:
				//SetSystemInfo();
				StartMain();
				break;
			case Bind_User_Device_Fail:
				int bind_user_device_errorcode = msg.arg1;
				final AlertDialog bind_user_device_dialog = CustomDialog
						.create(LoginActivity.this, R.drawable.exit_48, null,
								" A error has occurred,the error code is  "
										+ bind_user_device_errorcode, "exit",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										//LoginActivity.this.finish();
										ResetInput();

									}
								}, null, null);
				bind_user_device_dialog.show();
				break;
			case GetUserByDevice_Action:
				GetUserByDeviceThread thread_get_user_by_device = new GetUserByDeviceThread();
				thread_get_user_by_device.start();
				break;
			case GetUserByDevice_Fail:
				int get_user_by_device_errorcode = msg.arg1;
				final AlertDialog get_user_by_device_dialog = CustomDialog
						.create(LoginActivity.this, R.drawable.exit_48, null,
								" A error has occurred,the error code is  "
										+ get_user_by_device_errorcode, "exit",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										//LoginActivity.this.finish();
										ResetInput();

									}
								}, null, null);
				get_user_by_device_dialog.show(); 
				break;
			case GetUserByDevice_Success:
				
				break;
			case Show_Reg_UI:
				SetRegUI();
				break;
			case Show_Login_UI:
				SetLoginUI();
				break;
			case StartMain:
				StartMain();
				break;
			case User_Login_Action:
				UserLoginThread thread_user_login = new UserLoginThread();
				thread_user_login.start();
				break;
			case User_Login_Success:
				LinphonePreferences.instance().setLogin(true);
				/*mConfigurationService.putBoolean(NgnConfigurationEntry.DEVICE_LOGIN, true);
				 if(!mConfigurationService.commit()){
						Log.e(TAG, "Failed to Commit() configuration");
					} */
				 
				StartMain();
				break;
			case User_Login_Fail:
				int user_login_fail_errorcode = msg.arg1;
				if(user_login_fail_errorcode == 802)
				{
				final AlertDialog user_login_fail_dialog = CustomDialog
						.create(LoginActivity.this, R.drawable.exit_48, null,
								" The user name or password is not correct. ", "exit",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										//LoginActivity.this.finish();
										ResetInput();

									}
								}, null, null);
				user_login_fail_dialog.show(); 
				}
				else 
				{
					final AlertDialog user_login_fail_dialog = CustomDialog
							.create(LoginActivity.this, R.drawable.exit_48, null,
									" A error has occurred,the error code is  "
											+ user_login_fail_errorcode, "exit",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											//LoginActivity.this.finish();
											ResetInput();

										}
									}, null, null);
					user_login_fail_dialog.show(); 
				}
				break;
			default:
				super.handleMessage(msg);
				break;

			}
		}
	};

	public static String getDeviceNo() {
		return Build.SERIAL;
	}

	public static String getValByTagName(Document doc, String tagName) {
		NodeList list = doc.getElementsByTagName(tagName);
		if (list.getLength() > 0) {
			Node node = list.item(0);
			Node valNode = node.getFirstChild();
			if (valNode != null) {
				String val = valNode.getNodeValue();
				return val;
			}
		}
		return null;
	}

	public static String ParserXml(String xml, String nodeName) {
		String returnStr = null;
		ByteArrayInputStream tInputStringStream = null;
		try {
			if (xml != null && !xml.trim().equals("")) {
				tInputStringStream = new ByteArrayInputStream(xml.getBytes());
			}
		} catch (Exception e) {
			return null;
		}
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(tInputStringStream, "UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					String name = parser.getName();
					if (name.equalsIgnoreCase(nodeName)) {
						returnStr = parser.nextText();
					}
					break;
				}
				eventType = parser.next();
			}
			tInputStringStream.close();
			// return persons;
		} catch (XmlPullParserException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return returnStr;
	}

	class SipAdminLoginThread extends Thread {

		public void run() {
			String login_ok = "8010210925-35110-444-10984-4122-322463124";
			String httpUrl = "http://sip.inhuasoft.cn/login.php";
			HttpPost request = new HttpPost(httpUrl);
			HttpClient httpClient = new DefaultHttpClient();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", "admin"));
			params.add(new BasicNameValuePair("password", "admin"));
			HttpResponse response;

			try {
				HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
				request.setEntity(entity);
				response = httpClient.execute(request);

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String str = EntityUtils.toString(response.getEntity());
					String strmd5 = MD5.getMD5(str);
					System.out.println("response:" + strmd5);
					if (strmd5.equals(login_ok)) {
						System.out.println("sip admin web login success");
						Message message = mHandler
								.obtainMessage(Admin_Login_Success);
						message.arg1 = 300;
						message.sendToTarget();
						CookieStore cookies = ((AbstractHttpClient) httpClient)
								.getCookieStore();
						SmartApplication appCookie = ((SmartApplication) LoginActivity.this
								.getApplication());
						// ((AbstractHttpClient)
						// httpClient).setCookieStore(cookies);
						appCookie.setCookie(cookies);
						// Toast.makeText(getApplicationContext(),
						// " login success ", Toast.LENGTH_SHORT).show();
					} else {
						System.out.println("sip admin web login fail ");
						Message message = mHandler
								.obtainMessage(Admin_Login_Fail);
						message.arg1 = 301;
						message.sendToTarget();
						// sip_admin_login_flag = false;
						// Toast.makeText(getApplicationContext(),
						// " login fail ", Toast.LENGTH_SHORT).show();
					}
				} else {
					System.out.println("sip admin web reponse fail");
					Message message = mHandler.obtainMessage(Admin_Login_Fail);
					message.arg1 = 302;
					message.sendToTarget();
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Message message = mHandler.obtainMessage(Admin_Login_Fail);
				message.arg1 = 303;
				message.sendToTarget();
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Message message = mHandler.obtainMessage(Admin_Login_Fail);
				message.arg1 = 304;
				message.sendToTarget();
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				Message message = mHandler.obtainMessage(Admin_Login_Fail);
				message.arg1 = 305;
				message.sendToTarget();
				e.printStackTrace();
			}
		}
	}

	class SipAddUserThread extends Thread {

		public void run() {
			String httpUrl = "http://sip.inhuasoft.cn/tools/users/user_management/user_management.php?action=add_verify&id=";
			HttpPost request = new HttpPost(httpUrl);
			HttpClient httpClient = new DefaultHttpClient();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			System.out.println("=================username:"
					+ editUserName.getText());
			params.add(new BasicNameValuePair("uname", editUserName.getText()
					.toString()));
			params.add(new BasicNameValuePair("email", editUserName.getText()
					.toString() + "@inhuasoft.cn"));
			params.add(new BasicNameValuePair("alias", editUserName.getText()
					.toString()));
			params.add(new BasicNameValuePair("domain", "115.28.9.71"));
			params.add(new BasicNameValuePair("alias_type", "dbaliases"));
			params.add(new BasicNameValuePair("passwd", editPassword.getText()
					.toString()));
			params.add(new BasicNameValuePair("confirm_passwd", editPassword
					.getText().toString()));
			HttpResponse response;
			SmartApplication appCookie = ((SmartApplication) LoginActivity.this.getApplication());
			((AbstractHttpClient) httpClient).setCookieStore(appCookie
					.getCookie());
			try {
				HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
				request.setEntity(entity);
				response = httpClient.execute(request);

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String str = EntityUtils.toString(response.getEntity());
					String strmd5 = MD5.getMD5(str);
					if (str.contains("is already a valid user")) {
						// sip_add_user = true;
						Message message = mHandler
								.obtainMessage(Sip_Add_User_Fail);
						message.arg1 = 106;
						message.sendToTarget();
						System.out
								.println("=================is already a valid user");
					} else if (str.contains("New User added!")) {
						Message message = mHandler
								.obtainMessage(Sip_Add_User_Success);
						message.arg1 = 101;
						message.sendToTarget();
						System.out.println("New User added!");
						// sip_add_user = true;
					} else {
						System.out.println("add user fail ");
						// sip_add_user = false;
						Message message = mHandler
								.obtainMessage(Sip_Add_User_Fail);
						message.arg1 = 105;
						message.sendToTarget();
					}
					// System.out.println(" login in add user");
				} else {
					System.out.println("add user fail ");
					// sip_add_user = false;
					Message message = mHandler.obtainMessage(Sip_Add_User_Fail);
					message.arg1 = 102;
					message.sendToTarget();
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Message message = mHandler.obtainMessage(Sip_Add_User_Fail);
				message.arg1 = 102;
				message.sendToTarget();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Message message = mHandler.obtainMessage(Sip_Add_User_Fail);
				message.arg1 = 103;
				message.sendToTarget();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Message message = mHandler.obtainMessage(Sip_Add_User_Fail);
				message.arg1 = 104;
				message.sendToTarget();
			}
		}
	}

	class SipAddDeviceThread extends Thread {

		public void run() {
			String httpUrl = "http://sip.inhuasoft.cn/tools/users/user_management/user_management.php?action=add_verify&id=";
			HttpPost request = new HttpPost(httpUrl);
			HttpClient httpClient = new DefaultHttpClient();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			System.out.println("==========device=======username:"
					+ getDeviceNo());
			params.add(new BasicNameValuePair("uname", getDeviceNo()));
			params.add(new BasicNameValuePair("email", getDeviceNo()
					+ "@inhuasoft.cn"));
			params.add(new BasicNameValuePair("alias", getDeviceNo()));
			params.add(new BasicNameValuePair("domain", "115.28.9.71"));
			params.add(new BasicNameValuePair("alias_type", "dbaliases"));
			params.add(new BasicNameValuePair("passwd", getDeviceNo()));
			params.add(new BasicNameValuePair("confirm_passwd", getDeviceNo()));
			HttpResponse response;
			SmartApplication appCookie = ((SmartApplication) LoginActivity.this.getApplication());
			((AbstractHttpClient) httpClient).setCookieStore(appCookie
					.getCookie());
			try {
				HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
				request.setEntity(entity);
				response = httpClient.execute(request);

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String str = EntityUtils.toString(response.getEntity());
					String strmd5 = MD5.getMD5(str);
					if (str.contains("is already a valid user")) {
						// sip_add_user = true;
						Message message = mHandler
								.obtainMessage(Sip_Add_Device_Fail);
						message.arg1 = 206;
						message.sendToTarget();
						System.out
								.println("=================is already a valid user");
						return;
					} else if (str.contains("New User added!")) {
						Message message = mHandler
								.obtainMessage(Sip_Add_Device_Success);
						message.arg1 = 201;
						message.sendToTarget();
						System.out.println("New device added!");
						return;
						// sip_add_user = true;
					} else {
						System.out.println("add user fail ");
						// sip_add_user = false;
						Message message = mHandler
								.obtainMessage(Sip_Add_Device_Fail);
						message.arg1 = 207;
						message.sendToTarget();
						return;
					}
					// System.out.println(" login in add user");
				} else {
					System.out.println("add user fail ");
					// sip_add_user = false;
					Message message = mHandler
							.obtainMessage(Sip_Add_Device_Fail);
					message.arg1 = 202;
					message.sendToTarget();
					return;
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Message message = mHandler.obtainMessage(Sip_Add_Device_Fail);
				message.arg1 = 203;
				message.sendToTarget();
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Message message = mHandler.obtainMessage(Sip_Add_Device_Fail);
				message.arg1 = 204;
				message.sendToTarget();
				return;
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Message message = mHandler.obtainMessage(Sip_Add_Device_Fail);
				message.arg1 = 205;
				message.sendToTarget();
				return;
			}
		}
	}

	class UserRegThread extends Thread {

		public void run() {
			String RequestUrl = "http://ota.inhuasoft.cn/SHS_WS/ShsService.asmx?op=UserRegist";
			URL url = null;
			try {
				url = new URL(RequestUrl);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				Message message = mHandler.obtainMessage(User_Reg_Fail);
				message.arg1 = 601;
				message.sendToTarget();
				e1.printStackTrace();
			}
			String envelope = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
					+ "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"
					+ "<soap12:Header>"
					+ "<MySoapHeader xmlns=\"http://tempuri.org/\">"
					+ "<UserName>SysAdmin</UserName>"
					+ "<PassWord>SysAdminSysAdmin</PassWord>"
					+ "</MySoapHeader>" + "</soap12:Header>" + "<soap12:Body>"
					+ "<UserRegist xmlns=\"http://tempuri.org/\">"
					+ "<userName>" + editUserName.getText() + "</userName>"
					+ "<passWord>" + editPassword.getText() + "</passWord>"
					+ "<Mobile></Mobile>" + "<Email></Email>"
					+ "<SipAccount>sip:" + editUserName.getText()
					+ "@115.28.9.71</SipAccount>" + "<SipPwd>"
					+ editPassword.getText() + "</SipPwd>" + "</UserRegist>"
					+ "</soap12:Body>" + "</soap12:Envelope>";
			HttpURLConnection httpConnection = null;
			OutputStream output = null;
			InputStream input = null;
			try {
				httpConnection = (HttpURLConnection) url.openConnection();
				httpConnection.setRequestMethod("POST");
				httpConnection.setRequestProperty("Content-Length",
						String.valueOf(envelope.length()));
				httpConnection.setRequestProperty("Content-Type",
						"text/xml; charset=utf-8");
				httpConnection.setDoOutput(true);
				httpConnection.setDoInput(true);
				output = httpConnection.getOutputStream();
				output.write(envelope.getBytes());
				output.flush();
				input = httpConnection.getInputStream();
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document dom = builder.parse(input);
				String returncode = getValByTagName(dom, "UserRegistResult");// ������
				if ("-2".equals(returncode)) {
					Message message = mHandler.obtainMessage(User_Reg_Fail);
					message.arg1 = 602;
					message.sendToTarget();
				} else if ("0".equals(returncode)) {
					Message message = mHandler.obtainMessage(User_Reg_Fail);
					message.arg1 = 603;
					message.sendToTarget();
				} else if ("1".equals(returncode)) {
					Message message = mHandler.obtainMessage(User_Reg_Success);
					message.arg1 = 600;
					message.sendToTarget();
				} else if ("2".equals(returncode)) {
					Message message = mHandler.obtainMessage(User_Reg_Fail);
					message.arg1 = 605;
					message.sendToTarget();
				} else {
					Message message = mHandler.obtainMessage(User_Reg_Fail);
					message.arg1 = 604;
					message.sendToTarget();
				}
				System.out
						.println("=======UserRegThread======= return code is  "
								+ returncode);

			} catch (Exception ex) {
				Log.d(TAG, "-->getResponseString:catch" + ex.getMessage());
				Message message = mHandler.obtainMessage(User_Reg_Fail);
				message.arg1 = 606;
				message.sendToTarget();
			} finally {
				try {
					output.close();
					input.close();
					httpConnection.disconnect();
				} catch (Exception e) {
					Log.d(TAG, "-->getResponseString:finally" + e.getMessage());
					Message message = mHandler.obtainMessage(User_Reg_Fail);
					message.arg1 = 607;
					message.sendToTarget();
				}
			}

		}
	}

	class DeviceRegThread extends Thread {

		public void run() {
			String RequestUrl = "http://ota.inhuasoft.cn/SHS_WS/ShsService.asmx?op=DeviceRegist";
			URL url = null;
			try {
				url = new URL(RequestUrl);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				Message message = mHandler.obtainMessage(Device_Reg_Fail);
				message.arg1 = 501;
				message.sendToTarget();
				e1.printStackTrace();
			}
			String envelope = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
					+ "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"
					+ "<soap12:Header>"
					+ "<MySoapHeader xmlns=\"http://tempuri.org/\">"
					+ "<UserName>SysAdmin</UserName>"
					+ "<PassWord>SysAdminSysAdmin</PassWord>"
					+ "</MySoapHeader>" + "</soap12:Header>" + "<soap12:Body>"
					+ "<DeviceRegist xmlns=\"http://tempuri.org/\">"
					+ "<DeviceNo>" + getDeviceNo() + "</DeviceNo>"
					+ "<SipAccount>sip:" + getDeviceNo()
					+ "@115.28.9.71</SipAccount>" + "<SipPwd>" + getDeviceNo()
					+ "</SipPwd>" + "</DeviceRegist>" + "</soap12:Body>"
					+ "</soap12:Envelope>";
			HttpURLConnection httpConnection = null;
			OutputStream output = null;
			InputStream input = null;
			try {
				httpConnection = (HttpURLConnection) url.openConnection();
				httpConnection.setRequestMethod("POST");
				httpConnection.setRequestProperty("Content-Length",
						String.valueOf(envelope.length()));
				httpConnection.setRequestProperty("Content-Type",
						"text/xml; charset=utf-8");
				httpConnection.setDoOutput(true);
				httpConnection.setDoInput(true);
				output = httpConnection.getOutputStream();
				output.write(envelope.getBytes());
				output.flush();
				input = httpConnection.getInputStream();
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document dom = builder.parse(input);
				String returncode = getValByTagName(dom, "DeviceRegistResult");// ������
				System.out
						.println("=======DeviceRegThread======= return code is  "
								+ returncode);

				if ("-2".equals(returncode)) {
					Message message = mHandler.obtainMessage(Device_Reg_Fail);
					message.arg1 = 502;
					message.sendToTarget();
				} else if ("0".equals(returncode)) {
					Message message = mHandler.obtainMessage(Device_Reg_Fail);
					message.arg1 = 503;
					message.sendToTarget();
				} else if ("1".equals(returncode)) {
					Message message = mHandler
							.obtainMessage(Device_Reg_Success);
					message.arg1 = 500;
					message.sendToTarget();
				} else if ("2".equals(returncode)) {
					Message message = mHandler.obtainMessage(Device_Reg_Fail);
					message.arg1 = 505;
					message.sendToTarget();
				} else {
					Message message = mHandler.obtainMessage(Device_Reg_Fail);
					message.arg1 = 504;
					message.sendToTarget();
				}

			} catch (Exception ex) {
				Log.d(TAG, "-->getResponseString:catch" + ex.getMessage());
				Message message = mHandler.obtainMessage(Device_Reg_Fail);
				message.arg1 = 506;
				message.sendToTarget();
			} finally {
				try {
					output.close();
					input.close();
					httpConnection.disconnect();
				} catch (Exception e) {
					Log.d(TAG, "-->getResponseString:finally" + e.getMessage());
					Message message = mHandler.obtainMessage(Device_Reg_Fail);
					message.arg1 = 507;
					message.sendToTarget();
				}
			}

		}
	}

	class BindUserDeviceThread extends Thread {

		public void run() {
			String RequestUrl = "http://ota.inhuasoft.cn/SHS_WS/ShsService.asmx?op=BindUserDevice";
			URL url = null;
			try {
				url = new URL(RequestUrl);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				Message message = mHandler.obtainMessage(Bind_User_Device_Fail);
				message.arg1 = 401;
				message.sendToTarget();
				e1.printStackTrace();
			}
			String envelope = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
					+ "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"
					+ "<soap12:Header>"
					+ "<MySoapHeader xmlns=\"http://tempuri.org/\">"
					+ "<UserName>SysAdmin</UserName>"
					+ "<PassWord>SysAdminSysAdmin</PassWord>"
					+ "</MySoapHeader>" + "</soap12:Header>" + "<soap12:Body>"
					+ "<BindUserDevice xmlns=\"http://tempuri.org/\">"
					+ "<UserName>" + editUserName.getText() + "</UserName>"
					+ "<DeviceNo>" + getDeviceNo() + "</DeviceNo>"
					+ "</BindUserDevice>" + "</soap12:Body>"
					+ "</soap12:Envelope>";
			HttpURLConnection httpConnection = null;
			OutputStream output = null;
			InputStream input = null;
			try {
				httpConnection = (HttpURLConnection) url.openConnection();
				httpConnection.setRequestMethod("POST");
				httpConnection.setRequestProperty("Content-Length",
						String.valueOf(envelope.length()));
				httpConnection.setRequestProperty("Content-Type",
						"text/xml; charset=utf-8");
				httpConnection.setDoOutput(true);
				httpConnection.setDoInput(true);
				output = httpConnection.getOutputStream();
				output.write(envelope.getBytes());
				output.flush();
				input = httpConnection.getInputStream();
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document dom = builder.parse(input);
				String returncode = getValByTagName(dom, "BindUserDeviceResult");// ������
				System.out
						.println("======BindUserDeviceThread======== return code is  "
								+ returncode);
				if ("-2".equals(returncode)) {
					Message message = mHandler
							.obtainMessage(Bind_User_Device_Fail);
					message.arg1 = 402;
					message.sendToTarget();
				} else if ("0".equals(returncode)) {
					Message message = mHandler
							.obtainMessage(Bind_User_Device_Fail);
					message.arg1 = 403;
					message.sendToTarget();
				} else if ("1".equals(returncode)) {
					Message message = mHandler
							.obtainMessage(Bind_User_Device_Success);
					message.arg1 = 400;
					message.sendToTarget();
				} else if ("2".equals(returncode)) {
					Message message = mHandler
							.obtainMessage(Bind_User_Device_Fail);
					message.arg1 = 405;
					message.sendToTarget();
				} else {
					Message message = mHandler
							.obtainMessage(Bind_User_Device_Fail);
					message.arg1 = 404;
					message.sendToTarget();
				}

			} catch (Exception ex) {
				Log.d(TAG, "-->getResponseString:catch" + ex.getMessage());
				Message message = mHandler.obtainMessage(Bind_User_Device_Fail);
				message.arg1 = 406;
				message.sendToTarget();
			} finally {
				try {
					output.close();
					input.close();
					httpConnection.disconnect();
				} catch (Exception e) {
					Log.d(TAG, "-->getResponseString:finally" + e.getMessage());
					Message message = mHandler
							.obtainMessage(Bind_User_Device_Fail);
					message.arg1 = 407;
					message.sendToTarget();
				}
			}

		}
	}

	class GetUserByDeviceThread extends Thread {

		public void run() {
			String RequestUrl = "http://ota.inhuasoft.cn/SHS_WS/ShsService.asmx?op=GetUserByDevice";
			URL url = null;
			try {
				url = new URL(RequestUrl);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				Message message = mHandler.obtainMessage(GetUserByDevice_Fail);
				message.arg1 = 701;
				message.sendToTarget();
				e1.printStackTrace();
			}
			String envelope = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
					+ "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"
					+ "<soap12:Header>"
					+ "<MySoapHeader xmlns=\"http://tempuri.org/\">"
					+ "<UserName>SysAdmin</UserName>"
					+ "<PassWord>SysAdminSysAdmin</PassWord>"
					+ "</MySoapHeader>" + "</soap12:Header>" + "<soap12:Body>"
					+ "<GetUserByDevice xmlns=\"http://tempuri.org/\">"
					+ "<DeviceNo>" + getDeviceNo() + "</DeviceNo>"
					+ "</GetUserByDevice>" + "</soap12:Body>"
					+ "</soap12:Envelope>";
			HttpURLConnection httpConnection = null;
			OutputStream output = null;
			InputStream input = null;
			try {
				httpConnection = (HttpURLConnection) url.openConnection();
				httpConnection.setRequestMethod("POST");
				httpConnection.setRequestProperty("Content-Length",
						String.valueOf(envelope.length()));
				httpConnection.setRequestProperty("Content-Type",
						"text/xml; charset=utf-8");
				httpConnection.setDoOutput(true);
				httpConnection.setDoInput(true);
				output = httpConnection.getOutputStream();
				output.write(envelope.getBytes());
				output.flush();
				input = httpConnection.getInputStream();
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document dom = builder.parse(input);
				String returncode = getValByTagName(dom,
						"GetUserByDeviceResult");
				System.out
						.println("======GetUserByDeviceResult======== return code is  "
								+ returncode);
				if ("-2".equals(returncode)) {
					Message message = mHandler
							.obtainMessage(GetUserByDevice_Fail);
					message.arg1 = 702;
					message.sendToTarget();
				} else {
					String username = ParserXml(returncode, "UserName");
					if (username != null) {
						LinphonePreferences.instance().setReg(true);
						LinphonePreferences.instance().setUserSip("sip:"+username+"@115.28.9.71");
						if(!LinphonePreferences.instance().isLogin())
						{
							//SetLoginUI();
							Message message = mHandler.obtainMessage(Show_Login_UI);
							message.sendToTarget();
						}
						else {
							Message message = mHandler.obtainMessage(StartMain);
							message.sendToTarget();
						}
						/*mConfigurationService.putBoolean(NgnConfigurationEntry.DEVICE_REG, true);
						mConfigurationService.putString(NgnConfigurationEntry.USERNAME, editUserName.getText().toString());
						mConfigurationService.putString(NgnConfigurationEntry.USER_PASSWORD, editPassword.getText().toString());
						mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_IMPU,"sip:"+getDeviceNo()+"@115.28.9.71");
						mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_IMPI, getDeviceNo());
						mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_DISPLAY_NAME, getDeviceNo());
						mConfigurationService.putString(NgnConfigurationEntry.IDENTITY_PASSWORD, getDeviceNo());
						if(!mConfigurationService.commit()){
							Log.e(TAG, "Failed to Commit() configuration");
						}
						if(!mConfigurationService.getBoolean(NgnConfigurationEntry.DEVICE_LOGIN, NgnConfigurationEntry.DEFAULT_DEVICE_LOGIN))
						{
							//SetLoginUI();
							Message message = mHandler.obtainMessage(Show_Login_UI);
							message.sendToTarget();
						}
						else { 
							Message message = mHandler.obtainMessage(StartMain);
							message.sendToTarget();
						}*/
					} 
					else {
						//SetRegUI();
						Message message = mHandler.obtainMessage(Show_Reg_UI);
						message.sendToTarget();
					}

				}

			} catch (Exception ex) {
				Log.d(TAG, "-->getResponseString:catch" + ex.getMessage());
				Message message = mHandler.obtainMessage(GetUserByDevice_Fail);
				message.arg1 = 706;
				message.sendToTarget();
			} finally {
				try {
					output.close();
					input.close();
					httpConnection.disconnect();
				} catch (Exception e) {
					Log.d(TAG, "-->getResponseString:finally" + e.getMessage());
					Message message = mHandler
							.obtainMessage(GetUserByDevice_Fail);
					message.arg1 = 707;
					message.sendToTarget();
				}
			}

		}
	}

	
	
	class UserLoginThread extends Thread {

		public void run() {
			String RequestUrl = "http://ota.inhuasoft.cn/SHS_WS/ShsService.asmx?op=GetUserByDevice";
			URL url = null;
			try {
				url = new URL(RequestUrl);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				Message message = mHandler.obtainMessage(User_Login_Fail);
				message.arg1 = 701;
				message.sendToTarget();
				e1.printStackTrace();
			}
			String envelope = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
					+ "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"
					+ "<soap12:Header>"
					+ "<MySoapHeader xmlns=\"http://tempuri.org/\">"
					+ "<UserName>"+editUserName.getText().toString()+"</UserName>"
					+ "<PassWord>"+editPassword.getText().toString()+"</PassWord>"
					+ "</MySoapHeader>" + "</soap12:Header>" + "<soap12:Body>"
					+ "<GetUserByDevice xmlns=\"http://tempuri.org/\">"
					+ "<DeviceNo>" + getDeviceNo() + "</DeviceNo>"
					+ "</GetUserByDevice>" + "</soap12:Body>"
					+ "</soap12:Envelope>";
			HttpURLConnection httpConnection = null;
			OutputStream output = null;
			InputStream input = null;
			try {
				httpConnection = (HttpURLConnection) url.openConnection();
				httpConnection.setRequestMethod("POST");
				httpConnection.setRequestProperty("Content-Length",
						String.valueOf(envelope.length()));
				httpConnection.setRequestProperty("Content-Type",
						"text/xml; charset=utf-8");
				httpConnection.setDoOutput(true);
				httpConnection.setDoInput(true);
				output = httpConnection.getOutputStream();
				output.write(envelope.getBytes());
				output.flush();
				input = httpConnection.getInputStream();
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document dom = builder.parse(input);
				String returncode = getValByTagName(dom,
						"GetUserByDeviceResult");// ������
				System.out
						.println("======GetUserByDeviceResult======== return code is  "
								+ returncode);
				if ("-2".equals(returncode)) {
					Message message = mHandler.obtainMessage(User_Login_Fail);
					message.arg1 = 802;
					message.sendToTarget();
				} 
				else 
				{
					String username = ParserXml(returncode, "UserName");
					if (editUserName.getText().toString().equals(username)) {
						Message message = mHandler.obtainMessage(User_Login_Success);
						message.sendToTarget();
					}
					else {
						Message message = mHandler.obtainMessage(User_Login_Fail);
						message.arg1 = 808;
						message.sendToTarget();
					}
					
				}

			} catch (Exception ex) {
				Log.d(TAG, "-->getResponseString:catch" + ex.getMessage());
				Message message = mHandler.obtainMessage(User_Login_Fail);
				message.arg1 = 806;
				message.sendToTarget();
			} finally {
				try {
					output.close();
					input.close();
					httpConnection.disconnect();
				} catch (Exception e) {
					Log.d(TAG, "-->getResponseString:finally" + e.getMessage());
					Message message = mHandler
							.obtainMessage(User_Login_Fail);
					message.arg1 = 807;
					message.sendToTarget();
				}
			}

		}
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_box_login);

		btnSubmit = (Button) findViewById(R.id.btnsubmit);
		btnSubmit.setOnClickListener(this);
		editUserName = (EditText) findViewById(R.id.edit_username);
		editPassword = (EditText) findViewById(R.id.edit_password);
		editRePassword = (EditText) findViewById(R.id.edit_checkpassword);
		txtMsginfo = (TextView)findViewById(R.id.txt_msginfo);
        chkMsginfo =(CheckBox)findViewById(R.id.chk_msginfo);
        if (!LinphonePreferences.instance().isReg()) {
        	Message message = mHandler.obtainMessage(GetUserByDevice_Action);
			message.sendToTarget();
		}
        else if ( LinphonePreferences.instance().isReg()&& ! LinphonePreferences.instance().isLogin()) {
        	Message message = mHandler.obtainMessage(Show_Login_UI);
			message.sendToTarget();
		}
        
    	/*if (!mConfigurationService.getBoolean(NgnConfigurationEntry.DEVICE_REG,
				NgnConfigurationEntry.DEFAULT_DEVICE_REG)) {
			Message message = mHandler.obtainMessage(GetUserByDevice_Action);
			message.sendToTarget();
		}else if(mConfigurationService.getBoolean(NgnConfigurationEntry.DEVICE_REG,
				NgnConfigurationEntry.DEFAULT_DEVICE_REG) && !mConfigurationService.getBoolean(NgnConfigurationEntry.DEVICE_LOGIN,
				NgnConfigurationEntry.DEFAULT_DEVICE_LOGIN))
    	{
    		Message message = mHandler.obtainMessage(Show_Login_UI);
			message.sendToTarget();
    	}
    	*/

	}

	@Override
	protected void onDestroy() {
		/*
		 * if(mSipBroadCastRecv != null){ unregisterReceiver(mSipBroadCastRecv);
		 * mSipBroadCastRecv = null; }
		 */
		super.onDestroy();
	}

	private void ResetInput() {
		editUserName.setText("");
		editPassword.setText("");
		editRePassword.setText("");
	}
	
	
	public void SetRegUI()
	{
		editRePassword.setVisibility(View.VISIBLE);
		txtMsginfo.setVisibility(View.VISIBLE);
		chkMsginfo.setVisibility(View.VISIBLE);
		btnSubmit.setText("Sign Up");
	}
	public void SetLoginUI()
	{
		editRePassword.setVisibility(View.GONE);
		txtMsginfo.setVisibility(View.GONE);
		chkMsginfo.setVisibility(View.GONE);
		btnSubmit.setText("Sign In");
	}

	private boolean ValidateRegInput() {
		if (!RegexUtils.checkUserName(editUserName.getText().toString())) {
			final AlertDialog username_error_dialog = CustomDialog.create(
					LoginActivity.this, R.drawable.exit_48, null,
					" The username is 4-16 any combination of characters ",
					"OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					}, null, null);
			username_error_dialog.show();
			ResetInput();
			return false;
		}
		if (!RegexUtils.checkPassWord(editPassword.getText().toString())) {
			final AlertDialog password_error_dialog = CustomDialog.create(
					LoginActivity.this, R.drawable.exit_48, null,
					" The password is 6-16 any combination of characters ",
					"OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					}, null, null);
			password_error_dialog.show();
			editPassword.setText("");
			editRePassword.setText("");
			return false;
		}
		if (!editPassword.getText().toString()
				.equals(editRePassword.getText().toString())) {
			final AlertDialog repassword_error_dialog = CustomDialog.create(
					LoginActivity.this, R.drawable.exit_48, null,
					" The password and confirm password must be the same",
					"OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					}, null, null);
			repassword_error_dialog.show();
			editPassword.setText("");
			editRePassword.setText("");
			return false;
		}

		return true;
	}
	
	
	
	private boolean ValidateLoginInput() {
		if (!RegexUtils.checkUserName(editUserName.getText().toString())) {
			final AlertDialog username_error_dialog = CustomDialog.create(
					LoginActivity.this, R.drawable.exit_48, null,
					" The username is 4-16 any combination of characters ",
					"OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					}, null, null);
			username_error_dialog.show();
			ResetInput();
			return false;
		}
		if (!RegexUtils.checkPassWord(editPassword.getText().toString())) {
			final AlertDialog password_error_dialog = CustomDialog.create(
					LoginActivity.this, R.drawable.exit_48, null,
					" The password is 6-16 any combination of characters ",
					"OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					}, null, null);
			password_error_dialog.show();
			editPassword.setText("");
			return false;
		}

		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.btnsubmit) {
			
			if(!LinphonePreferences.instance().isReg())
			{
				if (ValidateRegInput()) {
					Message message = mHandler.obtainMessage(Admin_Login_Action);
					message.sendToTarget();
				}
			}
			else if (LinphonePreferences.instance().isReg() && !LinphonePreferences.instance().isLogin())
			{
				if (ValidateLoginInput()) {
					Message message = mHandler.obtainMessage(User_Login_Action);
					message.sendToTarget();
				}
			}
			
			
		/*	if (!mConfigurationService.getBoolean(NgnConfigurationEntry.DEVICE_REG,
					NgnConfigurationEntry.DEFAULT_DEVICE_REG)) {
				if (ValidateRegInput()) {
					Message message = mHandler.obtainMessage(Admin_Login_Action);
					message.sendToTarget();
				}
			} else if(mConfigurationService.getBoolean(NgnConfigurationEntry.DEVICE_REG,
					NgnConfigurationEntry.DEFAULT_DEVICE_REG) && !mConfigurationService.getBoolean(NgnConfigurationEntry.DEVICE_LOGIN,
					NgnConfigurationEntry.DEFAULT_DEVICE_LOGIN))
	    	{
				if (ValidateLoginInput()) {
					Message message = mHandler.obtainMessage(User_Login_Action);
					message.sendToTarget();
				}
	    	} */
			
			
		}
	}
	


}
