package com.example.abhishekrawat.questionstudy.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.abhishekrawat.questionstudy.Model.BaseDTO;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class APICallback<T> implements Callback<T> {
    private Context mCtx;

    private APICallback() {
    }

    public APICallback(Context ctx) {
        mCtx = ctx;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Gson gson = new Gson();
        try {
            if (response.isSuccessful()) {
                onResponseSuccess(response.body());
            } else if (response.code() == 400) {
                onResponseFailure(gson.fromJson(response.errorBody().string(), BaseDTO.class).errorMessage);
            } else {
                onResponseFailure(gson.fromJson(response.errorBody().string(), BaseDTO.class).message);
            }
        } catch (IOException e) {
        }
    }


    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onResponseErrors("Connection Error!");
    }

    public abstract void onResponseSuccess(T response);

    public abstract void onResponseFailure(String errorMessage);

    public abstract void onResponseErrors(String data);
}