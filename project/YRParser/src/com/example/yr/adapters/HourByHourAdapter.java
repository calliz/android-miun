package com.example.yr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yr.R;
import com.example.yr.forecastcomponents.Forecast;
import com.example.yr.forecastcomponents.ForecastHolder;
import com.example.yr.utils.Utils;

public class HourByHourAdapter extends ArrayAdapter<Forecast> {
	private int layoutResourceId;
	private final LayoutInflater layoutInflater;

	public HourByHourAdapter(Context context, int layoutResourceId) {
		super(context, layoutResourceId);
		this.layoutResourceId = layoutResourceId;
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder = null;

		if (convertView == null) {
			view = layoutInflater.inflate(layoutResourceId, parent, false);

			holder = new ViewHolder();
			holder.imgSymbol = (ImageView) view.findViewById(R.id.symbol);
			holder.txtInfo = (TextView) view.findViewById(R.id.info);

			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		Forecast forecast = getItem(position);
		holder.imgSymbol.setImageResource(Utils.getIcon(
				forecast.getSymbolNumber(), forecast.getTimePeriod()));
		holder.txtInfo.setText(forecast.getLabel());

		return view;
	}

	static class ViewHolder {
		ImageView imgSymbol;
		TextView txtInfo;
	}

	public void setData(ForecastHolder weatherData) {
		clear();
		if (weatherData != null) {
			for (Forecast forecast : weatherData) {
				add(forecast);
			}
		}
	}

}