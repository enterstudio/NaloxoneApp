/*
 * Copyright (c) 2016 Humworld INC,
 */

package com.humworld.lifesaverapi.util;

/**
 * Utility class, which contains constant variables and static methods.
 * 
 * @author Humworld
 * @version 1.0
 */
public class WebServiceUtils {

	public static double distanceTo(double latitudeGiven, double longitudeGiven, 
									double latitudeTaken, double longitudeTaken) {
		double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
		double lat1 = Math.toRadians(latitudeGiven);
		double lon1 = Math.toRadians(longitudeGiven);
		double lat2 = Math.toRadians(latitudeTaken);
		double lon2 = Math.toRadians(longitudeTaken);

		// great circle distance in radians, using law of cosines formula
		double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2) 
						+ Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

		// each degree on a great circle of Earth is 60 nautical miles
		double nauticalMiles = 60 * Math.toDegrees(angle);
		double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
		return statuteMiles;
	}

}
