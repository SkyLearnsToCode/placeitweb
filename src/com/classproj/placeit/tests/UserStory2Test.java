package com.classproj.placeit.tests;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.test.ActivityUnitTestCase;

import com.classproj.placeit.MainActivity;
import com.classproj.placeit.mockView;
import com.google.android.gms.maps.model.LatLng;

import Models.PlaceIt;
import PlaceItControllers.PlaceItController;
import PlaceItDB.mockPlaceItHandler;


/*
 * This test is mainly supported as a BDD test, so we will test this using that
 * Okray. 
 */
public class UserStory2Test extends ActivityUnitTestCase<MainActivity> {
	
	public UserStory2Test(Class<MainActivity> activityClass) {
		super(MainActivity.class);
		// TODO Auto-generated constructor stub
	}

	private Location mockUserLocation = new Location("user story 2");
	private PlaceItController controller;
	private LatLng mockUserPosition = new LatLng(mockUserLocation.getLatitude(), mockUserLocation.getLongitude());
	
	
	public void testOnMapClick(){
		/*
		 * how to test the geocoder and how to test 
		 * geocoder when u click find it will find it. 
		 * assume that google is rihgt
		 * okray
		 * !yar
		 */
		
	}
	
	public void testGeocoderTask(){
		/*
		 * We can't test this because we assume google de god, but we can do errr..
		 * a system test maybe. yarr...
		 */
	}
}
