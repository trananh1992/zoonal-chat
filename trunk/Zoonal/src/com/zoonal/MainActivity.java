package com.zoonal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
	
	public static final String HOST = "zoonal.com";
	
	private WebView web;

    @SuppressLint("SetJavaScriptEnabled")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        web = (WebView) findViewById(R.id.webview);
        web.setWebViewClient(new MyWebViewClient());
        web.setWebChromeClient(new MyWebChromeClient());
        web.loadUrl("http://zoonal.com");
        
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
    		web.goBack();
    		return true;
    	}
		return super.onKeyDown(keyCode, event);		
	}    
    
    private class MyWebViewClient extends WebViewClient {
    	
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals(HOST)) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }
    
    private class MyWebChromeClient extends WebChromeClient {
    	
    	@Override
    	public boolean onConsoleMessage(ConsoleMessage cm) {
        	Log.d("MyApplication", cm.message() + " -- From line "
        			+ cm.lineNumber() + " of "
        			+ cm.sourceId() );
        	return true;
        }
    }
}
