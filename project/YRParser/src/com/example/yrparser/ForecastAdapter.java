package com.example.yrparser;

import java.util.List;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ForecastAdapter extends ArrayAdapter<Forecast> {

	private int layoutResourceId;
	private final LayoutInflater layoutInflater;

	public ForecastAdapter(Context context, int layoutResourceId) {
		super(context, layoutResourceId);
		this.layoutResourceId = layoutResourceId;
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = layoutInflater.inflate(layoutResourceId, parent, false);

		} else {
			view = convertView;
		}

		Forecast forecast = getItem(position);
		((ImageView) view.findViewById(R.id.symbol)).setImageResource(forecast
				.getIcon());
		((TextView) view.findViewById(R.id.info)).setText(forecast.getLabel());

		return view;
	}

	public void setData(List<Forecast> forecastList) {
		clear();
		if (forecastList != null) {
			for (Forecast forecast : forecastList) {
				add(forecast);
			}
		}
	}

}