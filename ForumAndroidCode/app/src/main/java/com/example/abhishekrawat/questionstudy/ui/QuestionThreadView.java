package com.example.abhishekrawat.questionstudy.ui;

import com.example.abhishekrawat.questionstudy.Model.AnswerDTO;

import java.util.List;

public interface QuestionThreadView {
    void OnGetAnswers(List<AnswerDTO> answerList);
    void OnError(String message);

}
