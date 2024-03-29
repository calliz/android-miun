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
			holder.symbol = (ImageView) row.findViewById(R.id.symbol);
			holder.info = (TextView) row.findViewById(R.id.info);

			row.setTag(holder);
		} else {
			holder = (WeatherHolder) row.getTag();
		}

		ForecastData forecastData = data[position];
		holder.symbol.setImageResource(forecastData.symbol);
		holder.info.setText(forecastData.info);

		return row;
	}

	static class WeatherHolder {
		ImageView symbol;
		TextView info;
	}

}