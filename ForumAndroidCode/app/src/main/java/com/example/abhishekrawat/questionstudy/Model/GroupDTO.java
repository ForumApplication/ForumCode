package com.example.abhishekrawat.questionstudy.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GroupDTO implements Serializable {
    public String createdDate;
    public String userId;
    public String id;
    public String title;
    public UserDTO admin;
    public String updatedDate;
    public String description;
    @SerializedName("users")
    public ArrayList members;
}
