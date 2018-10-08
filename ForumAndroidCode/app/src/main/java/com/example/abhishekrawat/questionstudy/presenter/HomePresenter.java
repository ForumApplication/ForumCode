package com.example.abhishekrawat.questionstudy.presenter;

import android.content.Context;

import com.example.abhishekrawat.questionstudy.Model.BaseDTO;
import com.example.abhishekrawat.questionstudy.Model.QuestionDTO;
import com.example.abhishekrawat.questionstudy.Model.SearchDTO;
import com.example.abhishekrawat.questionstudy.Model.UserDTO;
import com.example.abhishekrawat.questionstudy.Util.APICallback;
import com.example.abhishekrawat.questionstudy.Util.ConnectionManager;
import com.example.abhishekrawat.questionstudy.Util.DataUtil;
import com.example.abhishekrawat.questionstudy.Util.PreferenceManager;
import com.example.abhishekrawat.questionstudy.Util.Util;
import com.example.abhishekrawat.questionstudy.ui.HomeView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomePresenter {
    private HomeView view;
    public HomePresenter(HomeView view)
    {
        this.view=view;
    }
    public void getQuestions(final Context ctx)
    {
        SearchDTO searchDTO=new SearchDTO();
        ConnectionManager.getQuestions(ctx,searchDTO,new APICallback<BaseDTO>(ctx) {
            @Override
            public void onResponseSuccess(BaseDTO response) {
                List<QuestionDTO> questionList=new ArrayList<>();
                Gson gson =new Gson();
                ArrayList arrayList=(ArrayList) response.data;
               for(int i=0;i<arrayList.size();i++)
               {
                  String json= gson.toJson(arrayList.get(i));
                  json=json.replace("fileUrls","mediaUrl");
                  QuestionDTO questionDTO=gson.fromJson(json,QuestionDTO.class);
                  questionList.add(questionDTO);
               }
               view.onQuestionListSuccess(questionList);
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
