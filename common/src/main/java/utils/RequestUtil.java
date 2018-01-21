package utils;

import model.userdevice.Device;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by sunshine on 06/01/2018.
 */
public class RequestUtil {

    public static Device tell(HttpServletRequest request) {
        String agent = request.getHeader("user-agent");

        if (agent.contains("Android")) {
            return Device.ANDROID;
        }
        if (agent.contains("iPhone")) {
            return Device.IPHONE;
        }
        return Device.WEB_BROWSER;
    }
}