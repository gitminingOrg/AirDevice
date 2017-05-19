package util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AqiDao extends BasicDao{
	
	public void insertCityAqi(String cityName, String time, int aqi_value, String aqi_grade){
		try {
			PreparedStatement ps = conn.prepareStatement("insert into city_aqi(city_name,time,aqi_data,aqi_grade) values(?,?,?,?)");
			ps.setString(1, cityName);
			ps.setString(2, time);
			ps.setInt(3, aqi_value);
			ps.setString(4, aqi_grade);
			ps.execute();
			
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean findCityAqi(String cityName, String time){
		boolean result = false;
		
		try {
			PreparedStatement ps = conn.prepareStatement("select * from city_aqi where city_name=? and time=?");
			ps.setString(1, cityName);
			ps.setString(2, time);
			ResultSet rs = ps.executeQuery();
			result = rs.next();
			
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
