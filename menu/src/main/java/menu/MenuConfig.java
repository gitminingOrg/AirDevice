package menu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MenuConfig {
    //private final static String wechat_appid = "wxe8ca0dc55e5f81fd";
    private final static String wechat_appid = "wx7ba6d2f313ff5d53";
    //private final static String wechat_secret = "f059ce8503d18423654d6c1d7a8129e2";
    private final static String wechat_secret = "2d41411d6ebe6f7badf8d969564c9840";

    /**
     * 调用此方法需传入appid和secret，现默认不使用该方法获取
     *
     * @param appId
     * @param secret
     * @return
     */
    public static String queryAccessToken(String appId, String secret) {
        String result = "";
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret="
                + secret;
        try {
            URL address = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) address.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
            System.setProperty("sun.net.client.defaultReadTimeout", "30000");
            connection.connect();
            InputStream is = connection.getInputStream();
            int size = is.available();
            byte[] bytes = new byte[size];
            is.read(bytes);
            String message = new String(bytes, "UTF-8");
            JSONObject object = JSON.parseObject(message);
            result = object.getString("access_token");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String createMenu(String token) {
        //关于果麦
        JSONObject gm_introduction = new JSONObject();
        gm_introduction.put("name", "认识果麦");
        gm_introduction.put("type", "view");
        try {
            gm_introduction.put("url", "https://mp.weixin.qq.com/s/bfAo_vzy_uOeRe49aHt0aw");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject gm_guidance = new JSONObject();
        gm_guidance.put("name", "新风指南");
        gm_guidance.put("type", "view");
        try {
            gm_guidance.put("url", "https://mp.weixin.qq.com/s/4G9z1b0d33f17MWcl2ln-w");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //产品评测
        JSONObject assessment = new JSONObject();
        assessment.put("name", "产品评测");
        assessment.put("type", "view");
        try {
            assessment.put("url", "https://mp.weixin.qq.com/s/p9hnEQwLxIc5BASZhOhivA");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONArray gm = new JSONArray();
        gm.add(gm_introduction);
        gm.add(gm_guidance);
        gm.add(assessment);

        JSONObject gm_menu = new JSONObject();
        gm_menu.put("name", "关于果麦");
        gm_menu.put("sub_button", gm);

        //我的空气
        JSONObject mine_device = new JSONObject();
        mine_device.put("name", "我的空气");
        mine_device.put("type", "click");
        mine_device.put("key", "gmair");

        //售后服务
        JSONObject contact_us = new JSONObject();
        contact_us.put("name", "联系我们");
        contact_us.put("type", "view");
        try {
            contact_us.put("url", "https://mp.weixin.qq.com/s/h8MxaTEKHKbP5y7wA3AYZg");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //在线购买
        JSONObject online_purchase = new JSONObject();
        online_purchase.put("name", "在线购买");
        online_purchase.put("type", "view");
        try {
            online_purchase.put("url", "https://h5.youzan.com/v2/feature/Xe2Ffvvcq6");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //卡券兑换
        JSONObject card_exchange = new JSONObject();
        card_exchange.put("name", "卡券兑换");
        card_exchange.put("type", "view");
        try{
            card_exchange.put("url", "http://one.fw1860.com/recinzaixiantihuoxitongimages/wxth/njgm/wxth.aspx");
        }catch (Exception e) {
            e.printStackTrace();
        }

        JSONArray after_support = new JSONArray();
        after_support.add(contact_us);
        after_support.add(online_purchase);
        after_support.add(card_exchange);

        JSONObject as_menu = new JSONObject();
        as_menu.put("name", "购买与服务");
        as_menu.put("sub_button", after_support);

        JSONArray buttons = new JSONArray();
        buttons.add(gm_menu);
        buttons.add(mine_device);
        buttons.add(as_menu);

        JSONObject menu = new JSONObject();
        menu.put("button", buttons);
        System.out.println(JSON.toJSONString(menu));
        String link = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + token;
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
            System.setProperty("sun.net.client.defaultReadTimeout", "30000");
            connection.connect();
            OutputStream os = connection.getOutputStream();
            os.write(menu.toString().getBytes());
            os.flush();
            os.close();

            InputStream is = connection.getInputStream();
            int size = is.available();
            byte[] bytes = new byte[size];
            is.read(bytes);
            String message = new String(bytes, "UTF-8");
            return "返回信息" + message;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "创建菜单失败";


    }

    public static String deleteMenu(String token) {
        String link = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=" + token;
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
            System.setProperty("sun.net.client.defaultReadTimeout", "30000");
            connection.connect();
            OutputStream os = connection.getOutputStream();
            os.flush();
            os.close();
            InputStream is = connection.getInputStream();
            int size = is.available();
            byte[] bytes = new byte[size];
            is.read(bytes);
            String message = new String(bytes, "UTF-8");
            return "返回信息:" + message;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "删除菜单失败";
    }

    public static void main(String[] args) {
        String token = "6_7hDrv96MW7Ae4G5W5k8b4SOnW6kCDnBEozLz6Pweanksio9WlQd6wYVPwZHNTvbXJp0mKLBauE0pTxSxMYIY8MklGWoNtiuzMuwxgjatDFZfzVk1u9Lf_MxMGxlAvUDGtrr7RK7uucrzNqtGFENgAGAXWF";
        String deleteMessage = MenuConfig.deleteMenu(token);
        System.out.println("删除操作: " + deleteMessage);
        String createMessage = MenuConfig.createMenu(token);
        System.out.println("创建操作" + createMessage);
    }
}
