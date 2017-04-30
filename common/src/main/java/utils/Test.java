package utils;

import java.lang.reflect.Field;

import model.CleanerStatus;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int a = 0xEF;
		byte b = (byte)a;
		CleanerStatus cs = new CleanerStatus();
		Field[] fields = cs.getClass().getDeclaredFields();
		for (Field field : fields) {
			System.out.println(field.getGenericType().getTypeName());
		}
	}

}
