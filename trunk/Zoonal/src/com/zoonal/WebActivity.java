package com.zoonal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.webkit.ConsoleMessage;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.zoonal.menu.MenuAdapter;
import com.zoonal.settings.SettingMenuActivity;

public class WebActivity extends Activity implements AnimationListener {
	
	public static final String HOST = "zoonal.com";
	public static final String URL = "http://zoonal.com/";
	public static final float SLIDING_MENU_RELATIVE_WIDTH = 0.8f;
	public static final int SLIDING_MENU_DELAY = 500;
	
	private WebView webview;
	private ProgressDialog pd;
	private EditText txtSearch;
	private View web, menu;
	private boolean menuOut = false;
	private int menuWidth;
	private int currMotionEvent = -1;
	private PointF lastMovePoint, startDownPoint;
	private ListView listview;
	private MenuAdapter adapter;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        
        this.initFakeMenu();

        menu = findViewById(R.id.menu);
        web = findViewById(R.id.web);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        web.findViewById(R.id.bttSlide).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				WebActivity.this.setMenuWidth();
				int to = (!menuOut) ? menuWidth : -menuWidth;
				menuOut = !menuOut;
				slidingMenu(0, to, SLIDING_MENU_DELAY);
			}
		});
        
        pd = ProgressDialog.show(this, "Login", "Loading...", true, false);
        webview = (WebView) findViewById(R.id.webview);
        webview.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				WebActivity.this.setMenuWidth();
				final float x = event.getX() + WebActivity.this.getLeftMargin(web);
				event.setLocation(x, event.getY());
				WebActivity.this.onWebViewTouchEvent(event);
				return false;
			}
		});
        
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setSaveFormData(false);
        settings.setSavePassword(false);
        
        webview.setWebViewClient(new MyWebViewClient());
        webview.setWebChromeClient(new MyWebChromeClient());
        webview.loadUrl(URL);
    }
    
    @Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
    		webview.goBack();
    		return true;
    	}
		return super.onKeyDown(keyCode, event);		
	}
	
	private void setMenuWidth() {
		if (menuWidth == 0) {
			menuWidth = (int)(web.getMeasuredWidth() * SLIDING_MENU_RELATIVE_WIDTH);
			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) menu.getLayoutParams();
			params.width = (int)(menuWidth);
			menu.setLayoutParams(params);
			log("new menu width " + menuWidth);
		}
	}
    
    private void initFakeMenu() {
    	adapter = new MenuAdapter(this, R.layout.menu_list);
    	listview = (ListView) findViewById(R.id.menu_listview);
    	listview.setAdapter(adapter);
    	listview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long time) {
				if (position == 0) {
					Intent settingInt = new Intent(WebActivity.this, SettingMenuActivity.class);
					WebActivity.this.startActivity(settingInt);
				}
			}
    		
		});
    }
    
    private void slidingMenu(int from, int to, int duration) {
    	Animation anim = null;
        anim = new TranslateAnimation(from, to, 0, 0);
		anim.setDuration(duration);
		anim.setAnimationListener(WebActivity.this);
		anim.setFillEnabled(false);
		anim.setFillAfter(false);
		web.startAnimation(anim);
    }
    
    private void translateX(View view, int dx, int minX, int maxX) {
    	FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
    	params.leftMargin += dx;
    	if (params.leftMargin < minX) {
    		params.leftMargin = minX;    				
    	} else if (params.leftMargin > maxX) {
    		params.leftMargin = maxX;
    	}
    	view.setLayoutParams(params);
    }
    
    private int getLeftMargin(View v) {
    	FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) v.getLayoutParams();
    	return params.leftMargin;
    }
    
	private void onWebViewTouchEvent(MotionEvent event) {
    	log("touch event " + event.getAction());
    	int action = event.getAction();
    	if (action == MotionEvent.ACTION_DOWN) {
    		currMotionEvent = action;
    		startDownPoint = new PointF(event.getX(), event.getY());
    	} else if (action == MotionEvent.ACTION_MOVE && menuOut 
    			&& currMotionEvent == MotionEvent.ACTION_DOWN) {
    		PointF p = new PointF(event.getX(), event.getY());
    		if (lastMovePoint != null) {
    			log("touch move from " + lastMovePoint.x + " to " + event.getX());
				translateX(web, (int) (p.x - lastMovePoint.x), 0, menuWidth);
    		}
    		lastMovePoint = p;
    	} else if (action == MotionEvent.ACTION_UP) {
    		currMotionEvent = action;
    		lastMovePoint = null;
			if (event.getX() > (menuWidth / (2 * SLIDING_MENU_RELATIVE_WIDTH))) {
				this.slidingMenu(0, menuWidth - this.getLeftMargin(web), SLIDING_MENU_DELAY / 2);
				menuOut = true;
			} else {
				this.slidingMenu(0, -this.getLeftMargin(web), SLIDING_MENU_DELAY / 2);
				menuOut = false;
				if (startDownPoint != null) {
					PointF endUpPoint = new PointF(event.getX(), event.getY());
					if (endUpPoint.x - startDownPoint.x > 200
							&& Math.abs(endUpPoint.y - startDownPoint.y) < 20) {
						log("distance x: " + (endUpPoint.x - startDownPoint.x));
						log("distance y: " + (endUpPoint.y - startDownPoint.y));
						slidingMenu(0, menuWidth, SLIDING_MENU_DELAY);
					}
				}
			}
    	}
	}
    
    private void log(Object msg) {
    	Log.e("ZOONAL DEBUG", String.valueOf(msg));
    }
    
	private Handler handler = new Handler() {
    	
		@Override
		public void handleMessage(Message msg) {
			webview.setVisibility(View.VISIBLE);
			pd.dismiss();			
		}
	};
    
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

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			log("load finish url " + url);
			
			if (url.equals("http")) {
				log("load javascript login");
				String username = getIntent().getStringExtra("username");
				String password = getIntent().getStringExtra("password");
				webview.loadUrl("javascript:(function login() { " +
					"console.log('function ok');" +
					"document.getElementById('samelogin_username').value ='" + username + "';" +
					"document.getElementById('samelogin_password').value='" + password + "';" +
					"document.getElementById('samelogin_loginbutton').click();" +
				"})()");
			} 
			else {
				handler.sendEmptyMessage(0);
			}
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			log("load started url " + url);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onReceivedHttpAuthRequest(WebView view,
				HttpAuthHandler handler, String host, String realm) {
			// TODO Auto-generated method stub
			super.onReceivedHttpAuthRequest(view, handler, host, realm);
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			// TODO Auto-generated method stub
			super.onReceivedSslError(view, handler, error);
		}
    }
    
    private class MyWebChromeClient extends WebChromeClient {
    	
    	@Override
    	public boolean onConsoleMessage(ConsoleMessage cm) {
        	Log.e("JAVASCRIPT_DEBUG", cm.message() + " -- From line "
        			+ cm.lineNumber() + " of "
        			+ cm.sourceId() );
        	return true;
        }
    	
    	@Override
    	public void onProgressChanged(WebView view, int progress) {
    		WebActivity.this.setProgress(progress * 1000);
	    }
    }

	public void onAnimationEnd(Animation animation) {
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) web.getLayoutParams();
        if (!menuOut) {
            params.leftMargin = 0;
        } else {
        	params.leftMargin = menuWidth;
        }
        params.width = (int) (menuWidth / SLIDING_MENU_RELATIVE_WIDTH);
        web.setLayoutParams(params);
        web.clearAnimation();
	}

	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
}
