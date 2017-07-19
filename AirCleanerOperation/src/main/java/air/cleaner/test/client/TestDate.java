package air.cleaner.test.client;

import java.util.Calendar;

import org.apache.mina.util.ExpiringMap;

public class TestDate {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Calendar calendar = Calendar.getInstance();
		System.out.println(calendar.get(Calendar.YEAR) +" " + 
				calendar.get(Calendar.MONTH) +" " +
				calendar.get(Calendar.DATE) +" " +
				calendar.get(Calendar.HOUR_OF_DAY) +" " +
				calendar.get(Calendar.MINUTE) +" " +
				calendar.get(Calendar.SECOND));
		
		double rate = Math.pow(1 + 4.1 / 100 /365, 365) -1;
		System.out.println(rate);
		ExpiringMap<String, String> map = new ExpiringMap<String, String>(10 * 60);
	}

}
