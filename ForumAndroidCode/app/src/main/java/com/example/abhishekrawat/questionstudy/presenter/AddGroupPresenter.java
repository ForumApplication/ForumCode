package com.example.abhishekrawat.questionstudy.presenter;

import android.content.Context;

import com.example.abhishekrawat.questionstudy.Model.BaseDTO;
import com.example.abhishekrawat.questionstudy.Model.GroupDTO;
import com.example.abhishekrawat.questionstudy.Util.APICallback;
import com.example.abhishekrawat.questionstudy.Util.ConnectionManager;
import com.example.abhishekrawat.questionstudy.ui.AddGroupView;

public class AddGroupPresenter {
    AddGroupView view;

    public AddGroupPresenter(AddGroupView view) {
        this.view = view;
    }

    public void addGroup(Context ctx, GroupDTO group) {
        ConnectionManager.saveGroup(ctx, group, new APICallback<BaseDTO>(ctx) {
            @Override
            public void onResponseSuccess(BaseDTO response) {
                view.onAddGroupSuccess();
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
