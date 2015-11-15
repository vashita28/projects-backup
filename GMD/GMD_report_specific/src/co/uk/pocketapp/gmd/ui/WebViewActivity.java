package co.uk.pocketapp.gmd.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.WebView;
import co.uk.pocketapp.gmd.R;

public class WebViewActivity extends Activity {
private WebView mWebView;

/** Called when the activity is first created. */
     @Override
     public void onCreate (Bundle savedInstanceState) {
         super. onCreate (savedInstanceState);
         setContentView(R.layout.webview);



         // html file with sample swf video in sdcard

         //swf2.html points to swf in sdcard

         mWebView = (WebView)findViewById(R.id.mywebview);
         mWebView.getSettings().setJavaScriptEnabled(true);
         mWebView.getSettings().setPluginsEnabled(true);
         mWebView.getSettings().setAllowFileAccess(true);


         if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
             System.exit(4);
         } else {
             mWebView.loadUrl("file://" + Environment.getExternalStorageDirectory() + "/GMD/swf2.html");
         }

     }
}
