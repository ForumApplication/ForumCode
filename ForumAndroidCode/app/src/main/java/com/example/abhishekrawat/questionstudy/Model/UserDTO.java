package com.example.abhishekrawat.questionstudy.Model;

import com.google.gson.annotations.SerializedName;

public class UserDTO {
    @SerializedName("name")
    public String name;
    @SerializedName("id")
    public String id;
    @SerializedName("mobile")
    public String mobile;
    @SerializedName("password")
    public String password;
    @SerializedName("createdDate")
    public String createdDate;
    @SerializedName("updatedDate")
    public String updatedDate;
}
