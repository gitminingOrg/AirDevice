package utils;

import java.io.File;
import java.net.URLDecoder;

public class PathUtil {
	public static String retrivePath() {
		String path = PathUtil.class.getResource(File.separator).getPath();
		try {
			path = URLDecoder.decode(path, "utf-8");
		} catch (Exception e) {
			path = PathUtil.class.getResource(File.separator).getPath();
		}
		String temp = new StringBuffer(File.separator).append("WEB-INF").append(File.separator).append("classes")
				.append(File.separator).toString();
		int index = path.lastIndexOf(temp);
		return path.substring(0, index);
	}
}
