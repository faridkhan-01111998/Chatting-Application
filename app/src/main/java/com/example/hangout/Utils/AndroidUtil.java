package com.example.hangout.Utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.hangout.Model.UserModel;

public class AndroidUtil {
    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
    public static void passUserModelAsIntent(Intent intent, UserModel model){
        intent.putExtra("username",model.getUsername());
        intent.putExtra("phone", model.getPhone());
        intent.putExtra("userId", model.getUserId());
    }

    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel userModel = new UserModel();
        userModel.setUsername(intent.getStringExtra("username"));
        userModel.setUsername(intent.getStringExtra("phone"));
        userModel.setUsername(intent.getStringExtra("userId"));
        return userModel;
    }
}
