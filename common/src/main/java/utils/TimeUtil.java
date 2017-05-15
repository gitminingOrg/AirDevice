package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtil {
	public static String getCurrentTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Calendar calendar = Calendar.getInstance();
		String time = sdf.format(calendar.getTime());
		return time;
	}
	
	public static String getCurrentDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String time = sdf.format(calendar.getTime());
		return time;
	}
}
