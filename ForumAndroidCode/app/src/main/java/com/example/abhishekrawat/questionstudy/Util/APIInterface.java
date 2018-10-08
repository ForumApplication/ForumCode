package com.example.abhishekrawat.questionstudy.Util;

import android.icu.lang.UScript;

import com.example.abhishekrawat.questionstudy.Model.AnswerDTO;
import com.example.abhishekrawat.questionstudy.Model.BaseDTO;
import com.example.abhishekrawat.questionstudy.Model.GroupDTO;
import com.example.abhishekrawat.questionstudy.Model.QuestionDTO;
import com.example.abhishekrawat.questionstudy.Model.SearchDTO;
import com.example.abhishekrawat.questionstudy.Model.UserDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface APIInterface {
    @POST
    Call<BaseDTO> registerUser(@Url String url, @Body UserDTO request);
    @POST
    Call<BaseDTO> login(@Url String url, @Body UserDTO request);
    @POST
    Call<BaseDTO> getQuestions(@Url String url, @Body SearchDTO request);
    @POST
    Call<BaseDTO> saveQuestion(@Url String url, @Body QuestionDTO request);
    @POST
    Call<BaseDTO> saveAnswer(@Url String url, @Body AnswerDTO request);
    @GET
    Call<BaseDTO> getAnswer(@Url String url);
    @POST
    Call<BaseDTO> saveGroup(@Url String url, @Body GroupDTO request);
    @GET
    Call<BaseDTO> getGroups(@Url String url);
    @GET
    Call<BaseDTO> getGroupMembers(@Url String url);


}
