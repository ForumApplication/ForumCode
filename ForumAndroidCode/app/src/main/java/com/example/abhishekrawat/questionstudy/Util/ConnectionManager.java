package com.example.abhishekrawat.questionstudy.Util;

import android.content.Context;

import com.example.abhishekrawat.questionstudy.Model.AnswerDTO;
import com.example.abhishekrawat.questionstudy.Model.BaseDTO;
import com.example.abhishekrawat.questionstudy.Model.GroupDTO;
import com.example.abhishekrawat.questionstudy.Model.QuestionDTO;
import com.example.abhishekrawat.questionstudy.Model.SearchDTO;
import com.example.abhishekrawat.questionstudy.Model.UserDTO;
import com.example.abhishekrawat.questionstudy.R;

import retrofit2.Call;

public class ConnectionManager {
    public static void registerUser(Context ctx, UserDTO request, APICallback<BaseDTO> callback) {
        String url = ctx.getString(R.string.api_register_user);
        url = APIUtils.getFinalurl(url);
        Call<BaseDTO> callObj = APIServiceUtil.getInstance().getApiInterface().registerUser(url, request);
        callObj.enqueue(callback);
    }
    public static void login(Context ctx, UserDTO request, APICallback<BaseDTO> callback) {
        String url = ctx.getString(R.string.api_login);
        url = APIUtils.getFinalurl(url);
        Call<BaseDTO> callObj = APIServiceUtil.getInstance().getApiInterface().login(url, request);
        callObj.enqueue(callback);
    }
    public static void getQuestions(Context ctx, SearchDTO request, APICallback<BaseDTO> callback) {
        String url = ctx.getString(R.string.api_get_question);
        url = APIUtils.getFinalurl(url);
        Call<BaseDTO> callObj = APIServiceUtil.getInstance().getApiInterface().getQuestions(url,request);
        callObj.enqueue(callback);
    }
    public static void saveQuestion(Context ctx,QuestionDTO questionDTO, APICallback<BaseDTO> callback) {
        String url = ctx.getString(R.string.api_save_question);
        url = APIUtils.getFinalurl(url);
        Call<BaseDTO> callObj = APIServiceUtil.getInstance().getApiInterface().saveQuestion(url,questionDTO);
        callObj.enqueue(callback);
    }
    public static void saveAnswer(Context ctx, AnswerDTO answerDTO, APICallback<BaseDTO> callback) {
        String url = ctx.getString(R.string.api_save_answer);
        url = APIUtils.getFinalurl(url);
        Call<BaseDTO> callObj = APIServiceUtil.getInstance().getApiInterface().saveAnswer(url,answerDTO);
        callObj.enqueue(callback);
    }
    public static void getAnswer(Context ctx, AnswerDTO answerDTO, APICallback<BaseDTO> callback) {
        String url = ctx.getString(R.string.api_get_answer);
        url = APIUtils.getFinalurl(url);
        url=url+"/"+answerDTO.questionId;
        Call<BaseDTO> callObj = APIServiceUtil.getInstance().getApiInterface().getAnswer(url);
        callObj.enqueue(callback);
    }
    public static void saveGroup(Context ctx, GroupDTO groupDTO, APICallback<BaseDTO> callback) {
        String url = ctx.getString(R.string.api_save_group);
        url = APIUtils.getFinalurl(url);
        Call<BaseDTO> callObj = APIServiceUtil.getInstance().getApiInterface().saveGroup(url,groupDTO);
        callObj.enqueue(callback);
    }
    public static void getGroups(Context ctx,String mobileNumber, APICallback<BaseDTO> callback) {
        String url = ctx.getString(R.string.api_get_groups)+mobileNumber;
        url = APIUtils.getFinalurl(url);
        Call<BaseDTO> callObj = APIServiceUtil.getInstance().getApiInterface().getGroups(url);
        callObj.enqueue(callback);
    }
    public static void getGroupMembers(Context ctx,String groupId, APICallback<BaseDTO> callback) {
        String url = ctx.getString(R.string.api_get_group_members)+groupId;
        url = APIUtils.getFinalurl(url);
        Call<BaseDTO> callObj = APIServiceUtil.getInstance().getApiInterface().getGroupMembers(url);
        callObj.enqueue(callback);
    }
}
