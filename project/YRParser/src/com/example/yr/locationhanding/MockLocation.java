package com.example.yr.locationhanding;
//package com.example.yrparser;
//
//import android.content.Context;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.util.Log;
//
//public class MockLocation implements LocationListener {
//	public static final String GPS_MOCK_PROVIDER = "GpsMockProvider";
//	private static final double LATITUDE = 51.532669;
//	private static final double LONGITUDE = -0.119691;
//	private static final String TAG = "MockLocation";
//
//	public MockLocation() {
//		setupGps();
//		setupLocation();
//	}
//
//	private void setupGps() {
//		/** Setup GPS. */
//		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//			// use real GPS provider if enabled on the device
//			locationManager.requestLocationUpdates(
//					LocationManager.GPS_PROVIDER, 0, 0, this);
//		} else if (!locationManager.isProviderEnabled(GPS_MOCK_PROVIDER)) {
//			// otherwise enable the mock GPS provider
//			locationManager.addTestProvider(GPS_MOCK_PROVIDER, false, false,
//					false, false, true, false, false, 0, 5);
//			locationManager.setTestProviderEnabled(GPS_MOCK_PROVIDER, true);
//		}
//
//		if (locationManager.isProviderEnabled(GPS_MOCK_PROVIDER)) {
//			locationManager.requestLocationUpdates(GPS_MOCK_PROVIDER, 0, 0,
//					this);
//		}
//	}
//
//	private void setupLocation() {
//		Location location = new Location(GPS_MOCK_PROVIDER);
//		location.setLatitude(LATITUDE);
//		location.setLongitude(LONGITUDE);
//		location.setAltitude(0);
//		location.setTime(System.currentTimeMillis());
//
//		// show debug message in log
//		Log.d(TAG, location.toString());
//
//		// provide the new location
//		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//		locationManager.setTestProviderLocation(GPS_MOCK_PROVIDER, location);
//	}
//
//	private LocationManager getSystemService(String locationService) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public void onLocationChanged(Location arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onProviderDisabled(String arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onProviderEnabled(String arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
//		// TODO Auto-generated method stub
//
//	}
//
// }
