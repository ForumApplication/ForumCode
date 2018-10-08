package com.example.abhishekrawat.questionstudy.Util;

import com.example.abhishekrawat.questionstudy.Model.QuestionDTO;

import java.util.ArrayList;
import java.util.List;

public class DataUtil {
    public static String[] tabs = {"Home", "Group"};

    public interface TabsTag {
        int HOME = 0;
        int GROUP = 1;
    }

    public static int getTab(String tab) {
        switch (tab) {
            case "Home":
                return TabsTag.HOME;
            case "Group":
                return TabsTag.GROUP;
            default:
                return TabsTag.HOME;

        }
    }
}
