package com.wayrunny.runway.util;

import org.springframework.beans.factory.annotation.Value;

public class KeyService {
    @Value("spring.key.base-url")
    private static String baseURL;

    @Value("spring.key.rest-api")
    private static String restAPIKey;

    @Value("spring.key.weather-api")
    private static String weatherAPIKey;

    public static  String addBaseURLToImage(String imageURL){
        return baseURL + imageURL;
    }

    public static String getBaseURL(){
        return baseURL;
    }

    public static String getWeatherAPIKey(){return weatherAPIKey;}

    public static String getRestAPIKey(){
        return restAPIKey;
    }
}