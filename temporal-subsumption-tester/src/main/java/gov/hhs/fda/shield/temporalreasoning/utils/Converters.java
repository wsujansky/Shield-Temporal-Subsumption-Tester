package gov.hhs.fda.shield.temporalreasoning.utils;

import java.util.HashMap;
import java.util.Map;

import gov.hhs.fda.shield.temporalreasoning.TemporalUom;

public class Converters {

	public Converters() {
	}
	
	/**  Note:  This uses the following conversion factors from the paper:
	 *          1 year = 12 months = 365.24 days
	 *          1 week = 7 days
	 *          Therefore, 1 month = 4.348095238 weeks = 30.43666667 days  
	 *          Also, 1 Day = 24 Hours; 1 Hour = 60 Minutes ; 1 Minute = 60 Seconds    */
	public static Float convertToSeconds(Float rawNumericValue, TemporalUom uom) {
		final Map<TemporalUom, Float> conversionFactorToSeconds = new HashMap<TemporalUom, Float>();
		conversionFactorToSeconds.put(TemporalUom.SEC, Float.parseFloat("1"));  // secs in a sec
		conversionFactorToSeconds.put(TemporalUom.MIN, Float.parseFloat("60")); // secs in a minute
		conversionFactorToSeconds.put(TemporalUom.HOUR, Float.parseFloat("3600")); // secs in a hour
		conversionFactorToSeconds.put(TemporalUom.DAY, Float.parseFloat("86400")); // secs in a day
		conversionFactorToSeconds.put(TemporalUom.WEEK, Float.parseFloat("604800")); // secs in a week
		conversionFactorToSeconds.put(TemporalUom.MONTH, Float.parseFloat("2629728")); // secs in a month
		conversionFactorToSeconds.put(TemporalUom.YEAR, Float.parseFloat("31556736")); // secs in a year
		
		return rawNumericValue * conversionFactorToSeconds.get(uom);       
	}


}
