package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class BasicDao {
	Connection conn;
	public BasicDao(){
		String driver = "com.mysql.jdbc.Driver"; 
		String url = "jdbc:mysql://localhost:3306/airdevice?useUnicode=true&characterEncoding=utf-8";    
		String user = "root";
		String password = "guomaixinfeng";
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
