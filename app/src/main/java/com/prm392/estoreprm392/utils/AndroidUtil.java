package com.prm392.estoreprm392.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.prm392.estoreprm392.service.model.User;

public class AndroidUtil {

   public static  void showToast(Context context,String message){
       Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    public static void passUserModelAsIntent(Intent intent, User model){
       intent.putExtra("username",model.getName());
       intent.putExtra("phone",model.getPhoneNumber());
       intent.putExtra("userId",model.getUid());
       intent.putExtra("protoUrl",model.getProtoUrl());


    }

    public static User getUserModelFromIntent(Intent intent){
        User userModel = new User();
        userModel.setName(intent.getStringExtra("username"));
        userModel.setPhoneNumber(intent.getStringExtra("phone"));
        userModel.setUid(intent.getStringExtra("userId"));
        userModel.setProtoUrl(intent.getStringExtra("protoUrl"));
        return userModel;
    }

    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
}
