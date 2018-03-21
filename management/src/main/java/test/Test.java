package test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Test {

    public static void main(String[] args) {
        String s = "{\n" +
                "    \"status\": 0,\n" +
                "    \"message\": \"query ok\",\n" +
                "    \"result\": {\n" +
                "        \"title\": \"海淀西大街74号\",\n" +
                "        \"location\": {\n" +
                "            \"lng\": 116.30676,\n" +
                "            \"lat\": 39.98296\n" +
                "        },\n" +
                "        \"address_components\": {\n" +
                "            \"province\": \"北京市\",\n" +
                "            \"city\": \"北京市\",\n" +
                "            \"district\": \"海淀区\",\n" +
                "            \"street\": \"海淀西大街\",\n" +
                "            \"street_number\": \"74\"\n" +
                "        },\n" +
                "        \"similarity\": 0.8,\n" +
                "        \"deviation\": 1000,\n" +
                "        \"reliability\": 7\n" +
                "    }\n" +
                "}";
        JSONObject json = JSON.parseObject(s);
        double lng = json.getJSONObject("result").getJSONObject("location").getDouble("lng");
        double lat = json.getJSONObject("result").getJSONObject("location").getDouble("lat");

        System.out.println("lng: " + lng + ",lat: " + lat);
    }
}
