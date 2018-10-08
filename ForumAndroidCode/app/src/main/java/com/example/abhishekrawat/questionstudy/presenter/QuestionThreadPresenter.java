package com.example.abhishekrawat.questionstudy.presenter;

import android.content.Context;

import com.example.abhishekrawat.questionstudy.Model.AnswerDTO;
import com.example.abhishekrawat.questionstudy.Model.BaseDTO;
import com.example.abhishekrawat.questionstudy.Model.QuestionDTO;
import com.example.abhishekrawat.questionstudy.Util.APICallback;
import com.example.abhishekrawat.questionstudy.Util.ConnectionManager;
import com.example.abhishekrawat.questionstudy.Util.Util;
import com.example.abhishekrawat.questionstudy.ui.QuestionThreadView;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestionThreadPresenter {
    QuestionThreadView view;
    public QuestionThreadPresenter(QuestionThreadView view)
    {
        this.view=view;
    }
    public void getAnswers(final Context ctx, AnswerDTO request)
    {
        ConnectionManager.getAnswer(ctx,request,new APICallback<BaseDTO>(ctx) {
            @Override
            public void onResponseSuccess(BaseDTO response) {
                List<AnswerDTO> answerList=new ArrayList<>();
                Gson gson =new Gson();
                ArrayList arrayList=(ArrayList) response.data;
                for(int i=0;i<arrayList.size();i++)
                {
                    String json= gson.toJson(arrayList.get(i));
                    AnswerDTO answerDTO=gson.fromJson(json,AnswerDTO.class);
                 //   answerDTO.date= Util.getDate(answerDTO.date);
                    answerList.add(answerDTO);
                }
                view.OnGetAnswers(answerList);
            }
            @Override
            public void onResponseFailure(String response) {
                 view.OnError(response);
            }

            @Override
            public void onResponseErrors(String data) {
                view.OnError(data);
            }
        });
    }
}
