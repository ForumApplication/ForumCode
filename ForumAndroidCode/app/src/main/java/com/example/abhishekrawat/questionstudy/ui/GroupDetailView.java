package com.example.abhishekrawat.questionstudy.ui;

import com.example.abhishekrawat.questionstudy.Model.QuestionDTO;
import com.example.abhishekrawat.questionstudy.Model.UserDTO;

import java.util.List;

public interface GroupDetailView {
    void onQuestionLoaded(List<QuestionDTO> questions);
    void onMembersLoaded(List<UserDTO> members);
    void onError(String message);
}
