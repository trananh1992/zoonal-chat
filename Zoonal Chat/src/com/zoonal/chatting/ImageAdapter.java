/**
 * 
 */
package com.zoonal.chatting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Hiepnn
 * 
 */
public class ImageAdapter extends BaseAdapter {

	private Context mContext;

	public ImageAdapter(Context c) {
		mContext = c;
	}

	public Integer[] mThumbIds = { R.drawable.pic_1, R.drawable.pic_2,
			R.drawable.pic_3, R.drawable.pic_4, R.drawable.pic_5,
			R.drawable.pic_6, R.drawable.pic_7, R.drawable.pic_8,
			R.drawable.pic_9, R.drawable.pic_10, R.drawable.pic_11,
			R.drawable.pic_12, R.drawable.pic_13, R.drawable.pic_14,
			R.drawable.pic_15, };

	public int getCount() {
		// TODO Auto-generated method stub
		return mThumbIds.length;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mThumbIds[position];
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridView;
		
		if(convertView == null){
			gridView = new View(mContext);
			
			gridView = inflater.inflate(R.layout.contact_details, null);
			
			TextView textView = (TextView) gridView.findViewById(R.id.name_contact);
			ImageView imageView = (ImageView) gridView.findViewById(R.id.image_contact);
			
			textView.setText(mThumbIds[position]);
			imageView.setImageResource(mThumbIds[position]);
		}else {
			gridView = convertView;
		}
		
		return gridView;
		
		/*ImageView imageView = new ImageView(mContext);
		imageView.setImageResource(mThumbIds[position]);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setLayoutParams(new GridView.LayoutParams(80,80));
		return imageView;*/
		
	}

}
