package com.example.abhishekrawat.questionstudy.presenter;

import android.content.Context;

import com.example.abhishekrawat.questionstudy.Model.AnswerDTO;
import com.example.abhishekrawat.questionstudy.Model.BaseDTO;
import com.example.abhishekrawat.questionstudy.Model.UserDTO;
import com.example.abhishekrawat.questionstudy.Util.APICallback;
import com.example.abhishekrawat.questionstudy.Util.ConnectionManager;
import com.example.abhishekrawat.questionstudy.Util.PreferenceManager;
import com.example.abhishekrawat.questionstudy.ui.AddAnswerView;
import com.google.gson.Gson;

public class AddAnswerPresenter {
    AddAnswerView view;
    public AddAnswerPresenter(AddAnswerView view)
    {
        this.view=view;
    }
    public void SaveAnswer(Context ctx,AnswerDTO answer)
    {
        Gson gson=new Gson();
        String userInfo= PreferenceManager.getInstance(ctx).getUserInfo();
        UserDTO user=gson.fromJson(userInfo,UserDTO.class);
        UserDTO userData=new UserDTO();
        userData.id=user.id;
        answer.user=user;
        ConnectionManager.saveAnswer(ctx,answer, new APICallback<BaseDTO>(ctx) {
            @Override
            public void onResponseSuccess(BaseDTO response) {
                view.onSuccess();
            }

            @Override
            public void onResponseFailure(String errorMessage) {
                view.onError(errorMessage);
            }

            @Override
            public void onResponseErrors(String errorMessage) {
                view.onError(errorMessage);
            }
        });
    }
}
