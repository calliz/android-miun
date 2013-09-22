package com.example.yrparser;

public class Translater {

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
			s = "'Wind name not found'";
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
			s = "'Wind name not found'";
		}
		return s;
	}
}
