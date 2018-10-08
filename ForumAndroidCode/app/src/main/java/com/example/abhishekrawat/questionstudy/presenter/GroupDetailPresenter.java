package com.example.abhishekrawat.questionstudy.presenter;

import android.content.Context;

import com.example.abhishekrawat.questionstudy.Model.BaseDTO;
import com.example.abhishekrawat.questionstudy.Model.QuestionDTO;
import com.example.abhishekrawat.questionstudy.Model.SearchDTO;
import com.example.abhishekrawat.questionstudy.Model.UserDTO;
import com.example.abhishekrawat.questionstudy.Util.APICallback;
import com.example.abhishekrawat.questionstudy.Util.ConnectionManager;
import com.example.abhishekrawat.questionstudy.ui.GroupDetailView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class GroupDetailPresenter {
    private GroupDetailView view;
    public GroupDetailPresenter(GroupDetailView view)
    {
        this.view=view;
    }
    public void getQuestions(final Context ctx,String groupId)
    {
        SearchDTO searchDTO=new SearchDTO();
        searchDTO.groupId=groupId;
        ConnectionManager.getQuestions(ctx,searchDTO,new APICallback<BaseDTO>(ctx) {
            @Override
            public void onResponseSuccess(BaseDTO response) {
                List<QuestionDTO> questionList=new ArrayList<>();
                Gson gson =new Gson();
                ArrayList arrayList=(ArrayList) response.data;
                for(int i=0;i<arrayList.size();i++)
                {
                    String json= gson.toJson(arrayList.get(i));
                    QuestionDTO questionDTO=gson.fromJson(json,QuestionDTO.class);
                    questionList.add(questionDTO);
                }
                view.onQuestionLoaded(questionList);
            }
            @Override
            public void onResponseFailure(String message) {
                 view.onError(message);
            }

            @Override
            public void onResponseErrors(String message) {
                view.onError(message);
            }
        });
    }
    public void getMembers(final Context ctx,String groupId) {
        ConnectionManager.getGroupMembers(ctx,groupId,new APICallback<BaseDTO>(ctx) {
            @Override
            public void onResponseSuccess(BaseDTO response) {
                List<UserDTO> memberList=new ArrayList<>();
                Gson gson =new Gson();
                ArrayList arrayList=(ArrayList) response.data;
                for(int i=0;i<arrayList.size();i++)
                {
                    String json= gson.toJson(arrayList.get(i));
                    UserDTO member=gson.fromJson(json,UserDTO.class);
                    memberList.add(member);
                }
                view.onMembersLoaded(memberList);
            }
            @Override
            public void onResponseFailure(String message) {
                view.onError(message);
            }

            @Override
            public void onResponseErrors(String message) {
                view.onError(message);
            }
        });

    }
}
