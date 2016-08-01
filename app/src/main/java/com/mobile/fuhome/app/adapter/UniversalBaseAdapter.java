package com.mobile.fuhome.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan on 2016/4/13.
 */
public abstract class UniversalBaseAdapter<T> extends BaseAdapter {

	protected Context mContext;
	protected List<T> mData;
	protected LayoutInflater mInflater;
	protected int mLayoutId;
	protected boolean[] isChoice;
	protected int count;
	private boolean isSelectable;

	public UniversalBaseAdapter(Context context, List<T> data, int layoutId) {
		this.mContext = context;
		this.mData = data;
		this.mLayoutId = layoutId;
		this.mInflater = LayoutInflater.from(context);
		this.isChoice = new boolean[data.size()];
		this.isSelectable = false;
	}

	public boolean isSelectable() {
		return isSelectable;
	}

	public void setSelectable(boolean isSelectable) {
		this.isSelectable = isSelectable;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public T getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder = ViewHolder.get(mContext, parent,
				convertView, mLayoutId, position);

		convert(viewHolder, getItem(position));
		if (isSelectable) {
			choosed(viewHolder, position);

		}
		return viewHolder.getConvertView();
	}

	protected abstract void convert(ViewHolder viewHolder, T item);

	protected void choosed(ViewHolder viewHolder, int position) {
		if (isChoice[position]) {
			selectedState(viewHolder);
		} else {
			normalState(viewHolder);
		}
	}

	public void normalState(ViewHolder viewHolder) {

	}

	public void selectedState(ViewHolder viewHolder) {

	}

	public void choiceState(int position) {
		// isChoice[position] = isChoice[position] == true ? false : true;

		if (isChoice[position]) {
			isChoice[position] = false;
			count--;
		} else {
			isChoice[position] = true;
			count++;
		}

		notifyDataSetChanged();
	}

	public ArrayList<Integer> getSeleted() {
		ArrayList<Integer> positions = new ArrayList<>();

		for (int i = 0; i < isChoice.length; i++) {
			if (isChoice[i]) {
				positions.add(i);
			}
		}
		return positions;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		if (!isSelectable) {
			isChoice = new boolean[mData.size()];
		}
	}

	public int getSelectedCount() {

		return count;
	}

}
