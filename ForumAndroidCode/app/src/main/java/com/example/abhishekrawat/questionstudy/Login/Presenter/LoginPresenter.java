package com.example.abhishekrawat.questionstudy.Login.Presenter;

import android.content.Context;

import com.example.abhishekrawat.questionstudy.Login.LoginView;
import com.example.abhishekrawat.questionstudy.Model.BaseDTO;
import com.example.abhishekrawat.questionstudy.Model.UserDTO;
import com.example.abhishekrawat.questionstudy.Util.APICallback;
import com.example.abhishekrawat.questionstudy.Util.APIInterface;
import com.example.abhishekrawat.questionstudy.Util.APIUtils;
import com.example.abhishekrawat.questionstudy.Util.ConnectionManager;
import com.example.abhishekrawat.questionstudy.Util.PreferenceManager;
import com.example.abhishekrawat.questionstudy.Util.Util;
import com.google.gson.Gson;

import java.util.ArrayList;

public class LoginPresenter {
    LoginView loginView;
    APIInterface apiInterface;
    public LoginPresenter(LoginView view) {
        this.loginView = view;
        apiInterface= APIUtils.getAPIService();
    }

    public void login(final Context ctx,String mobileNumber,String password)
    {
        UserDTO user = new UserDTO();
        user.mobile = mobileNumber;
        user.password = Util.md5(password);
        ConnectionManager.login(ctx,user,new APICallback<BaseDTO>(ctx) {
            @Override
            public void onResponseSuccess(BaseDTO response) {
                PreferenceManager.getInstance(ctx).setLoggedInStatus(true);
                Gson gson=new Gson();
                String userInfo = gson.toJson(((ArrayList) response.data).get(0));
                PreferenceManager.getInstance(ctx).setUserInfo(userInfo);
                loginView.onLoginSuccess();
            }
            @Override
            public void onResponseFailure(String response) {
                loginView.onError(response);
            }

            @Override
            public void onResponseErrors(String data) {
                loginView.onError(data);
            }
        });
    }
}
