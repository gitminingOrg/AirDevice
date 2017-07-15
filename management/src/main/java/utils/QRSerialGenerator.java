package utils;

import java.util.Random;

public class QRSerialGenerator {
	private static final Random seed = new Random();
    private static final char[] code = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private static int num(int min, int max) {
        return min + seed.nextInt(max - min);
    }

    private static char gen() {
        return code[num(0, code.length)];
    }

    public static String generate() {
        char[] temp = new char[8];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = gen();
        }
        StringBuffer result = new StringBuffer();
        Random random = new Random();
        result.append(new String(temp));
        result.append(random.nextInt(99));
        return result.toString();
    }
}
