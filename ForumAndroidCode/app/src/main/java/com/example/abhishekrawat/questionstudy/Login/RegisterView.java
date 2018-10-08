package com.example.abhishekrawat.questionstudy.Login;

import com.example.abhishekrawat.questionstudy.Model.UserDTO;

public interface RegisterView {
    void onOtpSend();
    void onOtpVerify();
    void onRegisterSuccess();
    void onError(String message);
}
