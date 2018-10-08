package com.example.abhishekrawat.questionstudy.Model;

import com.google.gson.annotations.SerializedName;

public class BaseDTO {
    @SerializedName("status")
    public String status;
    @SerializedName("data")
    public Object data;
    @SerializedName("message")
    public String message;
    @SerializedName("errorMessage")
    public String errorMessage;


}
