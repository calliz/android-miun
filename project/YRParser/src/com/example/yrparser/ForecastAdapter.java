package com.example.yrparser;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ForecastAdapter extends ArrayAdapter<ForecastData> {

	private Context context;
	private int layoutResourceId;
	private ForecastData data[] = null;

	public ForecastAdapter(Context context, int layoutResourceId,
			ForecastData[] data) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		WeatherHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new WeatherHolder();
			holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
			holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);

			row.setTag(holder);
		} else {
			holder = (WeatherHolder) row.getTag();
		}

		ForecastData weather = data[position];
		holder.txtTitle.setText(weather.title);
		holder.imgIcon.setImageResource(weather.icon);

		return row;
	}

	static class WeatherHolder {
		ImageView imgIcon;
		TextView txtTitle;
	}

}