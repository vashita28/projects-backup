package com.snapshare.utility;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import com.facebook.Session;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.view.Surface;


public class Common {
	
	public static boolean flag = false;
	
	/**
	 * Logout From Facebook 
	 */
	public static void callFacebookLogout(Context context) {
	    Session session = Session.getActiveSession();
	    if (session != null) {

	        if (!session.isClosed()) {
	            session.closeAndClearTokenInformation();
	            //clear your preferences if saved
	        }
	    } else {

	        session = new Session(context);
	        Session.setActiveSession(session);

	        session.closeAndClearTokenInformation();

	    }

	}
	
	/**
	 * Read bytes
	 */
	public static byte[] readBytes(String uri) throws IOException {


        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(uri));
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1){  
        	
        	byteBuffer.write(buffer, 0, len); 
        }
        inputStream.close(); // this will release some more memory

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
      }
 
 	/**
 	 * Set camera orientation
 	 */
	 public static int setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera){
		 
	     android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
	     android.hardware.Camera.getCameraInfo(cameraId, info);
	     int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
	     int degrees = 0;
	     switch (rotation)
	     {
	     case Surface.ROTATION_0:
	         degrees = 0;
	         break;
	     case Surface.ROTATION_90:
	         degrees = 90;
	         break;
	     case Surface.ROTATION_180:
	         degrees = 180;
	         break;
	     case Surface.ROTATION_270:
	         degrees = 270;
	         break;
	     }
	
	     int result;
	     if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
	     {
	    	 result = (info.orientation + degrees) % 360;
	    	 result = (360 - result) % 360; // compensate the mirror
	         
	         //Log.e("1 "+degrees, info.orientation+" :result: "+result);
	     }
	     else
	     { // back-facing
	    	 result = (info.orientation - degrees + 360) % 360;
	         
	         //Log.e("2 "+degrees, info.orientation+" :result: "+result);
	     }
	     
	     camera.setDisplayOrientation(result);
	     
	     return result;
	 }
 
}
