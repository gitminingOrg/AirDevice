package air.cleaner.utils;

import java.util.Arrays;

public class ByteUtil {
	public static byte[] concatAll(byte[] first, byte[]... rest) {  
		  int totalLength = first.length;  
		  for (byte[] array : rest) {  
		    totalLength += array.length;  
		  }  
		  byte[] result = Arrays.copyOf(first, totalLength);  
		  int offset = first.length;  
		  for (byte[] array : rest) {  
		    System.arraycopy(array, 0, result, offset, array.length);  
		    offset += array.length;  
		  }  
		  return result;  
	}
	
	public static int byteArrayToInt(byte[] source){
		if(source.length > 8){
			return -1;
		}
		int power = 1;
		int length = source.length;
		int result = 0;
		for (int i=length - 1; i >= 0; i--) {
			int base = (int)source[i] & 0x000000ff;
			result += base * power;
			power*=256;
		}
		return result;
	}
	
	public static byte[] intToByteArray(int source, int size){
		byte[] result = new byte[size];
		int index = result.length - 1;
		while(source > 0){
			int remain = source % 256;
			result[index] = (byte) remain;
			source = source / 256;
			index--;
		}
		
		return result;
	}
	
	public static long byteArrayToLong(byte[] source){
		if(source.length > 16){
			return -1;
		}
		long power = 1;
		int length = source.length;
		long result = 0;
		for (int i=length - 1; i >= 0; i--) {
			long base = (int)source[i] & 0x000000ff;
			result += base * power;
			power*=256L;
		}
		return result;
	}
	
	public static byte[] longToByteArray(long source, int size){
		byte[] result = new byte[size];
		int index = result.length - 1;
		while(source > 0){
			long remain = source % 256;
			result[index] = (byte) remain;
			source = source / 256;
			index--;
		}
		
		return result;
	}
	
	public static String byteToServer(byte[] source){
		StringBuffer sb = new StringBuffer();
		for (byte b : source) {
			int value = (int)b & 0x000000ff;
			if(value <= 0){
				break;
			}
			char item = (char) value;
			sb.append(item);
		}
		return sb.toString();
	}
	
	public static byte[] serverToByte(String server , int length){
		byte[] result = new byte[length];
		char[] serverArray = server.toCharArray();
		for (int i=0; i<serverArray.length; i++) {
			result[i] = (byte) serverArray[i];
			System.err.println(serverArray[i]  +  " : " + result[i]);
		}
		return result;
	}
	
	public static String byteToHex(byte[] input) {
		StringBuffer sb = new StringBuffer();
		for (byte b : input) {
			sb.append(String.format("%02x", b).toUpperCase());
		}
		return sb.toString();
	}
	
	public static byte[] hexStringByteTo(String hex, int length) {
		if(hex.length() % 2 != 0){
			hex = "0" + hex;
		}
		byte[] result = new byte[length];
		int index = length - 1;
		for(int i=hex.length()-1; i>=1; i=i-2){
			if(index < 0){
				break;
			}
			int low = hexCharToInt(hex.charAt(i));
			int high = hexCharToInt(hex.charAt(i - 1));
			int value = high * 16 + low;
			result[index] = (byte)value; 
			index--;
		}
		return result;
	}
	
	public static int hexCharToInt(char i){
		if(i >= '0' && i<='9'){
			return i-'0';
		}else if (i >= 'A' && i<='F') {
			return i - 'A' + 10;
		}else if (i >= 'a' && i <='f') {
			return i - 'a' + 10;
		}else{
			return 0;
		}
	}
}
