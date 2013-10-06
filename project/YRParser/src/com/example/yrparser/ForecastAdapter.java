package com.example.yrparser;

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
		ForecastHolder holder = null;

		if (convertView == null) {
			view = layoutInflater.inflate(layoutResourceId, parent, false);

			holder = new ForecastHolder();
			holder.imgSymbol = (ImageView) view.findViewById(R.id.symbol);
			holder.txtInfo = (TextView) view.findViewById(R.id.info);

			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ForecastHolder) view.getTag();
		}

		Forecast forecast = getItem(position);
		holder.imgSymbol.setImageResource(forecast.getIcon());
		holder.txtInfo.setText(forecast.getLabel());

		return view;
	}

	static class ForecastHolder {
		ImageView imgSymbol;
		TextView txtInfo;
	}

	public void setData(WeatherData weatherData) {
		clear();
		if (weatherData != null) {
			for (Forecast forecast : weatherData) {
				add(forecast);
			}
		}
	}

}