package air.cleaner.utils;

public class MethodUtil {
	public static String getFieldGetMethod(String field){
		char[] array = field.toCharArray();
		if(array[0] >= 'a' && array[0] <='z'){
			array[0] = (char) (array[0] - 'a' + 'A') ;
		}
		
		String result = "get" + new String(array);
		return result;
	}
	
	public static String setFieldGetMethod(String field){
		char[] array = field.toCharArray();
		if(array[0] >= 'a' && array[0] <='z'){
			array[0] = (char) (array[0] - 'a' + 'A') ;
		}
		
		String result = "set" + new String(array);
		return result;
	}
}
