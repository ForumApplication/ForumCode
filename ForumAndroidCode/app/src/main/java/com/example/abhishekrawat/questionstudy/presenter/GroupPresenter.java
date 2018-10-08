package com.example.abhishekrawat.questionstudy.presenter;

import android.content.Context;

import com.example.abhishekrawat.questionstudy.Model.BaseDTO;
import com.example.abhishekrawat.questionstudy.Model.GroupDTO;
import com.example.abhishekrawat.questionstudy.Util.APICallback;
import com.example.abhishekrawat.questionstudy.Util.ConnectionManager;
import com.example.abhishekrawat.questionstudy.Util.Util;
import com.example.abhishekrawat.questionstudy.ui.GroupView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class GroupPresenter {
    private GroupView view;
    public GroupPresenter(GroupView view)
    {
        this.view=view;
    }
    public void getGroups(final Context ctx,String mobileNumber)
    {
        ConnectionManager.getGroups(ctx,mobileNumber,new APICallback<BaseDTO>(ctx) {
            @Override
            public void onResponseSuccess(BaseDTO response) {
                List<GroupDTO> groupList=new ArrayList<>();
                Gson gson =new Gson();
                ArrayList arrayList=(ArrayList) response.data;
                for(int i=0;i<arrayList.size();i++)
                {
                    String json= gson.toJson(arrayList.get(i));
                    GroupDTO group=gson.fromJson(json,GroupDTO.class);
                    //group.createdDate= Util.getDate(group.createdDate);
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
