package com.zxyang.wxpush.service.impl;

import com.zxyang.wxpush.constant.ConfigConstant;
import com.zxyang.wxpush.service.TianqiService;
import com.zxyang.wxpush.utils.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zx.yang
 * @DateTime: 2022/8/23 12:55
 * @Description: TODO
 */
@Service
public class TianqiServiceImpl implements TianqiService {
    @Autowired
    private ConfigConstant configConstant;
    @Override
    public JSONObject getWeatherByCity() {
        String TemperatureUrl = "https://www.yiketianqi.com/free/day" +
                "?appid=" + configConstant.getWeatherAppId() +
                "&appsecret=" + configConstant.getWeatherAppSecret()+
                "&city=" + configConstant.getCity()+
                "&unescape=1";
        String sendGet = HttpUtil.sendGet(TemperatureUrl, null);
        return JSONObject.parseObject(sendGet);
    }

    @Override
    public JSONObject getNextWeatherByCity() {
        String TemperatureUrl = "https://yiketianqi.com/free/week" +
                "?appid=" + configConstant.getWeatherAppId() +
                "&appsecret=" + configConstant.getWeatherAppSecret()+
                "&city=" + configConstant.getCity()+
                "&unescape=1";
        String sendGet = HttpUtil.sendGet(TemperatureUrl, null);
        return JSONObject.parseObject(sendGet);
    }

    @Override
    public Map<String, String> getTheNextThreeDaysWeather() {
        Map<String, String> map =  new HashMap<>();
        try {
            OkHttpClient client = new OkHttpClient.Builder().build();
            HttpUrl url = new HttpUrl.Builder()
                    .host("yiketianqi.com")
                    .addPathSegments("free/week")
                    .scheme("https")
                    .addQueryParameter("appid",configConstant.getWeatherAppId())
                    .addQueryParameter("appsecret",configConstant.getWeatherAppSecret())
                    .addQueryParameter("city",configConstant.getCity())
                    .addQueryParameter("unescape","1")
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            String responseResult = response.body().string();
            LocalDate now = LocalDate.now();
            //封装今天，明天，后天的时间
//            Map<String,String> daySet = new HashMap<>();
//            daySet.put(String.valueOf(now.getDayOfMonth()),"今");
//            daySet.put(String.valueOf(now.plusDays(1L).getDayOfMonth()),"明");
//            daySet.put(String.valueOf(now.plusDays(2L).getDayOfMonth()),"后");
//            //过滤，提取结果
//            map = JSONObject.parseObject(responseResult).getJSONArray("data").stream()
//                    .peek(o -> {
//                        String date = ((JSONObject) o).getString("date").substring(8);
//                        ((JSONObject) o).put("date",date);
//                    })
//                    .filter(o-> daySet.containsKey(((JSONObject) o).getString("date")))
//                    .collect(Collectors.toMap(o -> daySet.get(((JSONObject) o).getString("date")),
//                            o -> ((JSONObject) o).getString("wea")));
            JSONArray data = JSONObject.parseObject(responseResult).getJSONArray("data");
            String wea1 = data.getJSONObject(0).getString("wea");
            String wea2 = data.getJSONObject(1).getString("wea");
            String wea3 = data.getJSONObject(2).getString("wea");
            map.put("今",wea1);
            map.put("明",wea2);
            map.put("后",wea3);
        } catch (IOException e) {
            throw new RuntimeException("获取失败");
        }
        return map;
    }

    @Override
    public JSONObject getWeatherByIP() {
        String TemperatureUrl = "https://www.yiketianqi.com/free/day?appid=" + configConstant.getWeatherAppId() + "&appsecret=" + configConstant.getWeatherAppSecret() + "&unescape=1";
        String sendGet = HttpUtil.sendGet(TemperatureUrl, null);
        return JSONObject.parseObject(sendGet);
    }
    private String hasTextOrDefault(String s){
        return StringUtils.hasText(s)?s:"无法识别";
    }
}
