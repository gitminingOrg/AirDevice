package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class BasicDao {
	Connection conn;
	public BasicDao(){
		String driver = "com.mysql.jdbc.Driver"; 
		String url = "jdbc:mysql://commander.qingair.net:3306/airdevice?useUnicode=true&characterEncoding=utf-8";    
		String user = "wonderwoman";
		String password = "2bBatman";
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
