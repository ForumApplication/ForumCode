package com.example.abhishekrawat.questionstudy.presenter;

import android.content.Context;

import com.example.abhishekrawat.questionstudy.Model.BaseDTO;
import com.example.abhishekrawat.questionstudy.Model.GroupDTO;
import com.example.abhishekrawat.questionstudy.Model.QuestionDTO;
import com.example.abhishekrawat.questionstudy.Model.UserDTO;
import com.example.abhishekrawat.questionstudy.Util.APICallback;
import com.example.abhishekrawat.questionstudy.Util.ConnectionManager;
import com.example.abhishekrawat.questionstudy.Util.PreferenceManager;
import com.example.abhishekrawat.questionstudy.Util.Util;
import com.example.abhishekrawat.questionstudy.ui.AddQuestionView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AddQuestionPresenter {
    AddQuestionView view;
    public AddQuestionPresenter(AddQuestionView view)
    {
        this.view=view;
    }

    public void SaveQuestion(Context ctx,QuestionDTO question)
    {
        Gson gson=new Gson();
        String userInfo= PreferenceManager.getInstance(ctx).getUserInfo();
        UserDTO user=gson.fromJson(userInfo,UserDTO.class);
        UserDTO userData=new UserDTO();
        userData.id=user.id;
        question.user=userData;
        ConnectionManager.saveQuestion(ctx,question, new APICallback<BaseDTO>(ctx) {
            @Override
            public void onResponseSuccess(BaseDTO response) {
                view.onAddQuestionSuccess();
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
    public void getGroups(final Context ctx,String mobile)
    {
        ConnectionManager.getGroups(ctx,mobile,new APICallback<BaseDTO>(ctx) {
            @Override
            public void onResponseSuccess(BaseDTO response) {
                List<GroupDTO> groupList=new ArrayList<>();
                Gson gson =new Gson();
                ArrayList arrayList=(ArrayList) response.data;
                for(int i=0;i<arrayList.size();i++)
                {
                    String json= gson.toJson(arrayList.get(i));
                    GroupDTO group=gson.fromJson(json,GroupDTO.class);
                   // group.createdDate= Util.getDate(group.createdDate);
                    groupList.add(group);
                }
                view.onGroupListSuccess(groupList);
            }
            @Override
            public void onResponseFailure(String response) {
                // view.(response);
            }

            @Override
            public void onResponseErrors(String data) {
                // loginView.onError(data);
            }
        });
    }
}
