package utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
	
	public static List<String> getLastWeekdays(){
		String[] weekdays = {"周日","周一","周二","周三","周四","周五","周六"};
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		int current_index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		List<String> result = new ArrayList<String>();
		for(int i=0; i<7; i++){
			int index = current_index % 7;
			current_index++;
			result.add(weekdays[index]);
			if(i == 6){
				result.set(6, "昨日");
			}
		}
		return result;
	}
}
