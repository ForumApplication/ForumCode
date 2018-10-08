package com.example.abhishekrawat.questionstudy.ui;

import com.example.abhishekrawat.questionstudy.Model.GroupDTO;

import java.util.List;

public interface AddQuestionView {
    void onAddQuestionSuccess();
    void onError(String message);
    void onGroupListSuccess(List<GroupDTO> groupList);
    void fileUploadSuccess(String Url);
}
