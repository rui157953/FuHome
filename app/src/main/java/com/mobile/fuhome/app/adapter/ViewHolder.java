package com.mobile.fuhome.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Ryan on 2016/4/13.
 */
public class ViewHolder {

	private final SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;

	public ViewHolder(Context context, ViewGroup parent, int layoutId,
			int position) {
		mPosition = position;
		mViews = new SparseArray<>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, null);
		mConvertView.setTag(this);
	}

	public static ViewHolder get(Context context, ViewGroup parent,
			View convertView, int layoutId, int position) {

		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		}
		return (ViewHolder) convertView.getTag();
	}

	public <T extends View> T getView(int viewId) {

		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView() {
		return mConvertView;
	}

	public ViewHolder setText(int viewId, String text) {
		TextView textView = getView(viewId);
		textView.setText(text);
		return this;
	}

	public TextView getText(int viewId) {
		TextView textView = getView(viewId);
		return textView;
	}

	public ViewHolder setImage(int viewId, int drawableId) {
		ImageView imageView = getView(viewId);
		imageView.setImageResource(drawableId);
		return this;
	}

	public ImageView getImage(int viewId) {
		ImageView imageView = getView(viewId);
		return imageView;
	}

	public ViewHolder setImage(int viewId, Bitmap bitmap) {
		ImageView imageView = getView(viewId);
		imageView.setImageBitmap(bitmap);
		return this;
	}

	public ViewHolder setImage(int viewId, String picPath) {
		ImageView imageView = getView(viewId);
		// TODO: 2016/4/13 get pic from networld

		return this;
	}

	public ViewHolder setImageByUrl(int viewId, String url)
	{
//		Uri Uri = android.net.Uri.parse(url);
		SimpleDraweeView simpleDraweeView = (SimpleDraweeView) getView(viewId);
		simpleDraweeView.setImageURI(url);
		return this;
	}

	public int getPosition() {
		return mPosition;
	}
}
