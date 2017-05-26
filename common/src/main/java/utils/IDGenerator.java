package utils;

import java.util.Random;

public class IDGenerator {
    private static final Random seed = new Random();
    private static final char[] code = {'z', 'h', 'a', 'n', 'g', 'x', 'u', 'f', 'w', 'i', 'l', 'o', 'v', 'e', 'y', 'r'};

    private static int num(int min, int max) {
        return min + seed.nextInt(max - min);
    }

    public static char generate() {
        return code[num(0, code.length)];
    }

    public static String generate(String prefix) {
        char[] temp = new char[6];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = generate();
        }
        StringBuffer result = new StringBuffer();
        Random random = new Random();
        result.append(prefix.toUpperCase());
        result.append(new String(temp));
        result.append(random.nextInt(99));
        return result.toString();
    }
}
