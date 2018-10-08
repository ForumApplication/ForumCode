package com.example.abhishekrawat.questionstudy.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class QuestionDTO implements Serializable {

    @SerializedName("title")
    public String question;
    public String id;
    public String description;
    public String groupId;
    @SerializedName("createdDate")
    public String date;
    public UserDTO user;
    public List<String> fileUrls;
    public List<MediaFilesDTO> mediaUrl;
}
