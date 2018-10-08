package com.example.abhishekrawat.questionstudy.Util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIServiceUtil {
    private static APIServiceUtil sInstance;
    private final String ACCEPT_KEY = "Accept";
    private final String ACCEPT_VALUE = "application/json";
    private final String CONTENT_TYPE = "Content-Type";
    private Retrofit mRetrofit;
    private APIServiceUtil() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder builder = request.newBuilder().addHeader(ACCEPT_KEY, ACCEPT_VALUE);
                         builder.addHeader(CONTENT_TYPE, ACCEPT_VALUE);
                        return chain.proceed(builder.build());
                    }
                })
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS).build();

        mRetrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl("https://lockthedeal.com")
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
    }

    public static APIServiceUtil getInstance() {
        if (sInstance == null)
            sInstance = new APIServiceUtil();
        return sInstance;
    }

    public APIInterface getApiInterface() {
        return mRetrofit.create(APIInterface.class);
    }
}
