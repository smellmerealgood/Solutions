package solutions.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.TimeZone;

public class Date {
	public static String convertDate(Instant utcTime) {
		return convertDate(utcTime.toString());
	}
	
	public static String convertDate(String utcTime) {
		SimpleDateFormat utcFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		utcFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		java.util.Date gpsUTCDate = null;

		try {
			gpsUTCDate = utcFormatter.parse(utcTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		SimpleDateFormat localFormatter = new SimpleDateFormat("MM-dd-yyyy' at 'hh:mm:ss a z");
		localFormatter.setTimeZone(TimeZone.getTimeZone("EST"));
		assert gpsUTCDate != null;

		return localFormatter.format(gpsUTCDate.getTime());
	}
}