package util;

import java.util.Random;

public class TokenGenerator {
	public static String generateNCharString(int length){
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			char next = 'a';
			int offset = random.nextInt(26);
			next+=offset;
			boolean capital = random.nextBoolean();
			if (capital) {
				next = (char) (next - 'a' + 'A');
			}
			sb.append(next);
			
		}
		
		return sb.toString();
	}
}
