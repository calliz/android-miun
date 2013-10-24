package com.example.yr.locationhanding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.yrparser.R;

public class Utils {
	private static final String TAG = "FilterUtils";

	public static String translateWindSpeedName(String attributeValue) {
		String s = null;
		if (attributeValue.equals("Stille")) {
			s = "Calm";
		} else if (attributeValue.equals("Flau vind")) {
			s = "Light air";
		} else if (attributeValue.equals("Svak vind")) {
			s = "Light breeze";
		} else if (attributeValue.equals("Lett bris")) {
			s = "Gentle breeze";
		} else if (attributeValue.equals("Laber bris")) {
			s = "Moderate breeze";
		} else if (attributeValue.equals("Frisk bris")) {
			s = "Fresh breeze";
		} else if (attributeValue.equals("Liten kuling")) {
			s = "Strong breeze";
		} else if (attributeValue.equals("Stiv kuling")) {
			s = "Moderate gale";
		} else if (attributeValue.equals("Sterk kuling")) {
			s = "Fresh gale";
		} else if (attributeValue.equals("Liten storm")) {
			s = "Strong gale";
		} else if (attributeValue.equals("Full storm")) {
			s = "Whole gale";
		} else if (attributeValue.equals("Sterk storm")) {
			s = "Storm";
		} else if (attributeValue.equals("Orkan")) {
			s = "Hurricane";
		} else {
			s = attributeValue;
		}
		return s;
	}

	public static String translateWindDirection(String attributeValue) {
		String s = null;
		if (attributeValue.equals("Stille")) {
			s = "Calm";
		} else if (attributeValue.equals("Flau vind")) {
			s = "Light air";
		} else if (attributeValue.equals("Svak vind")) {
			s = "Light breeze";
		} else if (attributeValue.equals("Lett bris")) {
			s = "Gentle breeze";
		} else if (attributeValue.equals("Laber bris")) {
			s = "Moderate breeze";
		} else if (attributeValue.equals("Frisk bris")) {
			s = "Fresh breeze";
		} else if (attributeValue.equals("Liten kuling")) {
			s = "Strong breeze";
		} else if (attributeValue.equals("Stiv kuling")) {
			s = "Moderate gale";
		} else if (attributeValue.equals("Sterk kuling")) {
			s = "Fresh gale";
		} else if (attributeValue.equals("Liten storm")) {
			s = "Strong gale";
		} else if (attributeValue.equals("Full storm")) {
			s = "Whole gale";
		} else if (attributeValue.equals("Sterk storm")) {
			s = "Storm";
		} else if (attributeValue.equals("Orkan")) {
			s = "Hurricane";
		} else {
			s = attributeValue;
		}
		return s;
	}

	public static int getIcon(String sym, String per) {
		int symbol;
		int period;
		if (sym == null) {
			return R.drawable.sym_01d;
		} else {
			symbol = Integer.parseInt(sym);
		}
		// period = 2 as default for hourbyhour forecasts
		if (per == null) {
			period = 2;
		} else {
			period = Integer.parseInt(per);
		}

		switch (symbol) {
		case 1:
			if (period > 0 && period < 3) {
				return R.drawable.sym_01d;
			} else {
				return R.drawable.sym_01n;
			}
		case 2:
			if (period > 0 && period < 3) {
				return R.drawable.sym_02d;
			} else {
				return R.drawable.sym_02n;
			}
		case 3:
			if (period > 0 && period < 3) {
				return R.drawable.sym_03d;
			} else {
				return R.drawable.sym_03n;
			}
		case 4:
			return R.drawable.sym_04;
		case 5:
			if (period > 0 && period < 3) {
				return R.drawable.sym_05d;
			} else {
				return R.drawable.sym_05n;
			}
		case 6:
			if (period > 0 && period < 3) {
				return R.drawable.sym_06d;
			} else {
				return R.drawable.sym_06n;
			}
		case 7:
			if (period > 0 && period < 3) {
				return R.drawable.sym_07d;
			} else {
				return R.drawable.sym_07n;
			}
		case 8:
			if (period > 0 && period < 3) {
				return R.drawable.sym_08d;
			} else {
				return R.drawable.sym_08n;
			}
		case 9:
			return R.drawable.sym_09;
		case 10:
			return R.drawable.sym_10;
		case 11:
			return R.drawable.sym_11;
		case 12:
			return R.drawable.sym_12;
		case 13:
			return R.drawable.sym_13;
		case 14:
			return R.drawable.sym_14;
		case 15:
			return R.drawable.sym_15;
		case 16:
			return R.drawable.sym_16;
		case 17:
			return R.drawable.sym_17;
		case 18:
			return R.drawable.sym_18;
		case 19:
			return R.drawable.sym_19;
		default:
			return R.drawable.sym_01d;
		}
	}

	public static String convertToDate(String dateTime) {
		Date input = null;
		try {
			input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
					.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String output = new SimpleDateFormat("yyyy-MM-dd").format(input);
		return output;
	}

	public static String convertToTime(String dateTime) {
		Date input = null;
		try {
			input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
					.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String output = new SimpleDateFormat("HH:mm").format(input);
		return output;
	}
}
