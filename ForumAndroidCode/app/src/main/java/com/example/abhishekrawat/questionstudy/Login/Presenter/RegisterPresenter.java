package com.example.abhishekrawat.questionstudy.Login.Presenter;

import android.app.Activity;
import android.content.Context;

import com.example.abhishekrawat.questionstudy.Login.RegisterView;
import com.example.abhishekrawat.questionstudy.Model.BaseDTO;
import com.example.abhishekrawat.questionstudy.Model.UserDTO;
import com.example.abhishekrawat.questionstudy.Util.APICallback;
import com.example.abhishekrawat.questionstudy.Util.APIInterface;
import com.example.abhishekrawat.questionstudy.Util.APIUtils;
import com.example.abhishekrawat.questionstudy.Util.ConnectionManager;
import com.example.abhishekrawat.questionstudy.Util.PreferenceManager;
import com.example.abhishekrawat.questionstudy.Util.Util;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RegisterPresenter {
    RegisterView registerView;
     APIInterface apiInterface;
    public RegisterPresenter(RegisterView view) {
        this.registerView = view;
        apiInterface= APIUtils.getAPIService();

    }

    public void startPhoneNumberVerification(Activity activity, String phoneNumber, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                activity,               // Activity (for callback binding)
                mCallbacks);

    }

    public void registerUser(final Context ctx, String name, String mobileNumber, String password) {
        UserDTO user = new UserDTO();
        user.mobile = mobileNumber;
        user.name = name;

        user.password = Util.md5(password);
        ConnectionManager.registerUser(ctx,user,new APICallback<BaseDTO>(ctx) {
            @Override
            public void onResponseSuccess(BaseDTO response) {
                PreferenceManager.getInstance(ctx).setLoggedInStatus(true);
                Gson gson=new Gson();
                String userInfo = gson.toJson(((ArrayList) response.data).get(0));
                PreferenceManager.getInstance(ctx).setUserInfo(userInfo);
                registerView.onRegisterSuccess();
            }

            @Override
            public void onResponseFailure(String  errorMessage) {
                registerView.onError(errorMessage);
            }

            @Override
            public void onResponseErrors(String data) {
                registerView.onError(data);
            }
        });
    }
}
