package com.example.abhishekrawat.questionstudy.Model;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.pchmn.materialchips.model.ChipInterface;

public class ContactsChip implements ChipInterface {

    private int id;

    private String mobile;
    private String name;

    public ContactsChip( int id,String name, String mobile) {
        this.id=id;
        this.name = name;
        this.mobile = mobile;
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public Uri getAvatarUri() {
        return null;
    }

    @Override
    public Drawable getAvatarDrawable() {
        return null;
    }

    @Override
    public String getLabel() {
        return name;
    }

    @Override
    public String getInfo() {
        return mobile;
    }
}
