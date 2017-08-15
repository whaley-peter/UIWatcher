// https://android.googlesource.com/platform/frameworks/testing/+/master/uiautomator_test_libraries/src/com/android/uiautomator/common/UiWatchers.java
/*
 * Copyright (C) 2013 The Android Open Source Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.whaleytest;

import android.util.Log;
import com.android.uiautomator.core.*;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UiWatchers extends UiAutomatorTestCase{
  private static final String LOG_TAG = UiWatchers.class.getSimpleName();
  private final List<String>  mErrors = new ArrayList<String>();

  /**
   * We can use the UiDevice registerWatcher to register a small script to be
   * executed when the framework is waiting for a control to appear. Waiting may
   * be the cause of an unexpected dialog on the screen and it is the time when
   * the framework runs the registered watchers. This is a sample watcher
   * looking for ANR and crashes. it closes it and moves on. You should create
   * your own watchers and handle error logging properly for your type of tests.
   */
  public void testinstallAppWatchers(){	 
	  while(true){
		UiDevice.getInstance().registerWatcher("oppoInstall1", new UiWatcher(){
			@Override
			public boolean checkForCondition() {
				//输入密码弹框
				UiObject installbutton1 = new UiObject(new UiSelector().resourceId("com.coloros.safecenter:id/verify_input"));
				if(installbutton1.exists()){
					try{
						//输出账号
						installbutton1.setText("long2000");
						sleep(1000);
						//收起键盘
						UiDevice.getInstance().pressBack();
						
					}catch(UiObjectNotFoundException e) {
			            Log.e(LOG_TAG, "Exception", e);
			          }
				return true;
				}
				return false;
			}
		});
		UiDevice.getInstance().registerWatcher("oppoInstall2", new UiWatcher(){
			@Override
			public boolean checkForCondition() {
				//1,输入密码界面安装按钮
				//2,重新安装时的弹框按钮
				UiObject installbutton2 = new UiObject(new UiSelector().resourceId("android:id/button1"));
				if(installbutton2.exists()){
					try{
						installbutton2.clickAndWaitForNewWindow();
						
					}catch(UiObjectNotFoundException e){
						Log.e(LOG_TAG, "Exception",e);
					}
				return true;
				}
				return false;
			}});
		
		UiDevice.getInstance().registerWatcher("oppoInstall3", new UiWatcher(){

			@Override
			public boolean checkForCondition() {
				//安装界面的安装按钮
				UiObject installbutton3 = new UiObject(new UiSelector().resourceId("com.android.packageinstaller:id/ok_button"));
				if(installbutton3.exists()){
					try{
						installbutton3.clickAndWaitForNewWindow();
						sleep(10000);
					}catch(UiObjectNotFoundException e){
						Log.e(LOG_TAG, "Exception",e);
					}
				return true;
				}
				return false;
			}
		});
		UiDevice.getInstance().registerWatcher("oppoInstall4", new UiWatcher(){

			@Override
			public boolean checkForCondition() {
				//安装完成界面，完成按钮
				UiObject installbutton4 = new UiObject(new UiSelector().resourceId("com.android.packageinstaller:id/bottom_button_one"));
				if(installbutton4.exists()){
					try{
						installbutton4.clickAndWaitForNewWindow();
					}catch(UiObjectNotFoundException e){
						Log.e(LOG_TAG, "Exception",e);
					}
				return true;
				}
				return false;
			}
		});
		
		UiDevice.getInstance().registerWatcher("oppoInstall5", new UiWatcher(){

			@Override
			public boolean checkForCondition() {
				//app安装界面，是否继续安装旧版本按钮
				UiObject installbutton5 = new UiObject(new UiSelector().resourceId("com.android.packageinstaller:id/install_old_version_tip"));
				if(installbutton5.exists()){
					try{
						installbutton5.clickAndWaitForNewWindow();
						sleep(3000);
					}catch(UiObjectNotFoundException e){
						Log.e(LOG_TAG, "Exception",e);
					}
				return true;
				}
				return false;
			}
		});
	
		UiDevice.getInstance().registerWatcher("oppoInstall6", new UiWatcher(){

			@Override
			public boolean checkForCondition() {
				//app安装界面，继续安装按钮
				UiObject installbutton6 = new UiObject(new UiSelector().resourceId("com.android.packageinstaller:id/btn_allow_once"));
				if(installbutton6.exists()){
					try{
						installbutton6.clickAndWaitForNewWindow();
						sleep(3000);
					}catch(UiObjectNotFoundException e){
						Log.e(LOG_TAG, "Exception",e);
					}
				return true;
				}
				return false;
			}
		});
		
		UiDevice.getInstance().registerWatcher("vivoinstall", new UiWatcher(){
			@Override
			public boolean checkForCondition() {
				//输入密码弹框
				UiObject installbutton1 = new UiObject(new UiSelector().resourceId("vivo:id/vivo_adb_install_ok_button"));
				if(installbutton1.exists()){
					try{
						//点击安装
						installbutton1.clickAndWaitForNewWindow();
						sleep(1000);						
					}catch(UiObjectNotFoundException e) {
			            Log.e(LOG_TAG, "Exception", e);
			          }
				return true;
				}
				return false;
			}
		});
		
		UiDevice.getInstance().runWatchers();
		
	  }
  }

  public void registerAnrAndCrashWatchers() {

    UiDevice.getInstance().registerWatcher("ANR", new UiWatcher() {
      @Override
      public boolean checkForCondition() {
        UiObject window = new UiObject(new UiSelector()
            .className("com.android.server.am.AppNotRespondingDialog"));
        String errorText = null;
        if (window.exists()) {
          try {
            errorText = window.getText();
          } catch (UiObjectNotFoundException e) {
            Log.e(LOG_TAG, "dialog gone?", e);
          }
          onAnrDetected(errorText);
          postHandler();
          return true; // triggered
        }
        return false; // no trigger
      }
    });

    // class names may have changed
    UiDevice.getInstance().registerWatcher("ANR2", new UiWatcher() {
      @Override
      public boolean checkForCondition() {
        UiObject window = new UiObject(new UiSelector().packageName("android")
            .textContains("isn't responding."));
        if (window.exists()) {
          String errorText = null;
          try {
            errorText = window.getText();
          } catch (UiObjectNotFoundException e) {
            Log.e(LOG_TAG, "dialog gone?", e);
          }
          onAnrDetected(errorText);
          postHandler();
          return true; // triggered
        }
        return false; // no trigger
      }
    });

    UiDevice.getInstance().registerWatcher("CRASH", new UiWatcher() {
      @Override
      public boolean checkForCondition() {
        UiObject window = new UiObject(new UiSelector()
            .className("com.android.server.am.AppErrorDialog"));
        if (window.exists()) {
          String errorText = null;
          try {
            errorText = window.getText();
          } catch (UiObjectNotFoundException e) {
            Log.e(LOG_TAG, "dialog gone?", e);
          }
          onCrashDetected(errorText);
          postHandler();
          return true; // triggered
        }
        return false; // no trigger
      }
    });

    UiDevice.getInstance().registerWatcher("CRASH2", new UiWatcher() {
      @Override
      public boolean checkForCondition() {
        UiObject window = new UiObject(new UiSelector().packageName("android")
            .textContains("has stopped"));
        if (window.exists()) {
          String errorText = null;
          try {
            errorText = window.getText();
          } catch (UiObjectNotFoundException e) {
            Log.e(LOG_TAG, "dialog gone?", e);
          }
          onCrashDetected(errorText);
          postHandler();
          return true; // triggered
        }
        return false; // no trigger
      }
    });

    Log.i(LOG_TAG, "Registed GUI Exception watchers");
  }

  public void registerAcceptSSLCertWatcher() {
    UiDevice.getInstance().registerWatcher("SSLCERTERROR", new UiWatcher() {
      @Override
      public boolean checkForCondition() {
        UiObject continueButton = new UiObject(new UiSelector()
          .className("android.widget.Button").packageName("com.android.browser").text("Continue"));
        if (continueButton.exists()) {
          try {
            continueButton.click();
            return true; // triggered
          } catch (UiObjectNotFoundException e) {
            Log.e(LOG_TAG, "Exception", e);
          }
        }
        return false; // no trigger
      }
    });

    Log.i(LOG_TAG, "Registered SSL Certificate Error Watchers");
  }

  public void onAnrDetected(String errorText) {
    mErrors.add(errorText);
  }

  public void onCrashDetected(String errorText) {
    mErrors.add(errorText);
  }

  public void reset() {
    mErrors.clear();
  }

  public List<String> getErrors() {
    return Collections.unmodifiableList(mErrors);
  }

  /**
   * Current implementation ignores the exception and continues.
   */
  public void postHandler() {
    // TODO: Add custom error logging here

    String formatedOutput = String.format("UI Exception Message: %-20s\n",
        UiDevice.getInstance().getCurrentPackageName());
    Log.e(LOG_TAG, formatedOutput);

    UiObject buttonOK = new UiObject(new UiSelector().text("OK").enabled(true));
    // sometimes it takes a while for the OK button to become enabled
    buttonOK.waitForExists(5000);
    try {
      buttonOK.click();
    } catch (UiObjectNotFoundException e) {
      Log.e(LOG_TAG, "Exception", e);
    }
  }
}