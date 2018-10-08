package com.example.abhishekrawat.questionstudy.Model;

import com.google.gson.annotations.SerializedName;

public class AnswerDTO {
    @SerializedName("answer")
    public String answer;
    @SerializedName("user")
    public UserDTO user;
    @SerializedName("questionId")
    public String questionId;
    @SerializedName("createdDate")
    public String date;

}
