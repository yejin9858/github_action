package com.wayrunny.runway.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Component
public class WeatherService {
    public HashMap<String, Object> getWeather(Double latitude, Double longitude){

        HashMap<String, Object> weatherSet = new LinkedHashMap<>();
        latitude = Math.round(latitude*100)/100.0;
        longitude = Math.round(longitude*100)/100.0;
        StringBuilder urlBuilder = new StringBuilder("https://api.openweathermap.org/data/2.5/weather?" +
                "lat=" + latitude+
                "&lon="+ longitude+
                "&appid=" + KeyService.getWeatherAPIKey() + "&lang=kr");
        
        try {
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }

            br.close();
            conn.disconnect();

            JsonElement element = JsonParser.parseString(result);
            JsonElement weather = element.getAsJsonObject().get("weather").getAsJsonArray().get(0);

            String weatherDescription = weather.getAsJsonObject().get("description").getAsString();
            Integer weatherId = weather.getAsJsonObject().get("id").getAsInt();
            Double temp = element.getAsJsonObject().get("main")
                    .getAsJsonObject().get("temp").getAsDouble();

            weatherSet.put("weather_description", weatherDescription);
            weatherSet.put("weather_image", weatherImageUrl(weatherId));
            weatherSet.put("temp", temperatureFToC(temp));



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return weatherSet;
    }

    private Double temperatureFToC(Double temp){
        return Math.round((temp - 273.15) * 100)/100.0;
    }

    private String weatherImageUrl(Integer weatherId){
        String weatherImageUrl = KeyService.getBaseURL() +"/upload/weather/";

        if(weatherId > 800){
            weatherImageUrl += "clouds.svg";
        }
        else if(weatherId == 800){
            weatherImageUrl += "clear.svg";
        }
        else if(weatherId >= 700){
            weatherImageUrl += "atmosphere.svg";
        }
        else if(weatherId >= 600){
            weatherImageUrl += "snow.svg";
        }
        else if(weatherId >= 511){
            weatherImageUrl += "snow_rain.svg";
        }
        else if(weatherId >= 300){
            weatherImageUrl += "rain.svg";
        }
        else{
            weatherImageUrl += "thunderstorm.svg";
        }

        return weatherImageUrl;
    }
}