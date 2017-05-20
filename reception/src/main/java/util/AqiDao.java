package util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AqiDao extends BasicDao{
	
	public void insertCityAqi(String cityName, String city_pinyin, String time, int aqi_value, String aqi_grade){
		try {
			PreparedStatement ps = conn.prepareStatement("insert into city_aqi(city_name,city_pinyin,time,aqi_data,aqi_grade) values(?,?,?,?,?)");
			ps.setString(1, cityName);
			ps.setString(2, city_pinyin);
			ps.setString(3, time);
			ps.setInt(4, aqi_value);
			ps.setString(5, aqi_grade);
			ps.execute();
			
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertCity(String cityName, String cityPinYin, String initial){
		try {
			PreparedStatement ps = conn.prepareStatement("insert into city_list(city_name,city_pinyin,initial) values(?,?,?)");
			ps.setString(1, cityName);
			ps.setString(2, cityPinYin);
			ps.setString(3, initial);
			ps.execute();
			
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean findCity(String cityName){
		boolean result = false;
		
		try {
			PreparedStatement ps = conn.prepareStatement("select * from city_list where city_name=?");
			ps.setString(1, cityName);
			ResultSet rs = ps.executeQuery();
			result = rs.next();
			
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
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
	
	public HashMap<String, String> getCityList(){
		HashMap<String, String> result = new HashMap<String, String>();
		
		try {
			PreparedStatement ps = conn.prepareStatement("select * from city_list");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				result.put(rs.getString("city_name"), rs.getString("city_pinyin"));
			}
			
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
