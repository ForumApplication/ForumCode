package com.example.abhishekrawat.questionstudy.ui;

import com.example.abhishekrawat.questionstudy.Model.QuestionDTO;

import java.util.List;

public interface HomeView {
    void onQuestionListSuccess(List<QuestionDTO> questionList);
}
