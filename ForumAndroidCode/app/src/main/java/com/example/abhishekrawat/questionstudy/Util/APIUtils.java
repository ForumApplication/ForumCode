package com.example.abhishekrawat.questionstudy.Util;

import com.example.abhishekrawat.questionstudy.R;

public class APIUtils {
    private APIUtils() {}

    public static final String BASE_URL ="http://18.222.198.214:3000";

    public static APIInterface getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIInterface.class);
    }
    public static String getFinalurl(String api)
    {
        return BASE_URL+api;
    }
}