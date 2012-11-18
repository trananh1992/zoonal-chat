package com.zoonal.chatting.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class AvatarLoader extends AsyncTaskLoader<Bitmap> {

	private String urlStr;
	private Bitmap bitmap;

	public AvatarLoader(Context context, String url) {
		super(context);
		this.urlStr = url;
	}

	@Override
	public Bitmap loadInBackground() {
		int response = -1;
		InputStream in = null;
		try {
			URL url = new URL(urlStr);
			URLConnection con = url.openConnection();
			if (!(con instanceof HttpURLConnection)) throw new IOException("Not an HTTP connection");
			
			HttpURLConnection httpConn = (HttpURLConnection) con;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			response = httpConn.getResponseCode();
			try {
				if (response == HttpURLConnection.HTTP_OK) {
					in = httpConn.getInputStream();
					bitmap = BitmapFactory.decodeStream(in);
				}
				httpConn.disconnect();
			}
			catch (Exception ex) {
				if (in != null) {
					in.close();
				}
			}
		} catch (Exception ex) {
			log(ex.getMessage());
		}
		return bitmap;
	}

	@Override
	public void deliverResult(Bitmap data) {
		if (isReset()) {
			// The Loader has been reset; ignore the result and invalidate the data.
			return;
		}

		// Hold a reference to the old data so it doesn't get garbage collected.
		// The old data may still be in use (i.e. bound to an adapter, etc.), so
		// we must protect it until the new data has been delivered.
		Bitmap oldData = bitmap;
		bitmap = data;

		if (isStarted()) {
			// If the Loader is in a started state, deliver the results to the
			// client. The superclass method does this for us.
			super.deliverResult(data);
		}

		// Invalidate the old data as we don't need it any more.
		if (oldData != null && oldData != data) {
			oldData = null;
		}
	}

	@Override
	protected void onReset() {
		// TODO Auto-generated method stub
		super.onReset();
	}

	@Override
	protected void onStartLoading() {
		if (bitmap != null) {
			// Deliver any previously loaded data immediately.
			deliverResult(bitmap);
		}

		if (takeContentChanged() || bitmap == null) {
			// When the observer detects a change, it should call onContentChanged()
			// on the Loader, which will cause the next call to takeContentChanged()
			// to return true. If this is ever the case (or if the current data is
			// null), we force a new load.
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	private void log(Object msg) {
		Log.e("LOADER", String.valueOf(msg));
	}
	
	public void setURL(String url) {
		this.urlStr = url;
	}

}
