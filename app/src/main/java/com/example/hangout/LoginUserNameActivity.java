package com.example.hangout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.hangout.Model.UserModel;
import com.example.hangout.Utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginUserNameActivity extends AppCompatActivity {

    EditText login_username;
    Button login_let_me_in_btn;
    ProgressBar login_progress_bar1;

    String phoneNumber;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user_name);

        login_username = findViewById(R.id.login_username);
        login_let_me_in_btn = findViewById(R.id.login_let_me_in_btn);
        login_progress_bar1 = findViewById(R.id.login_progress_bar1);

        phoneNumber = getIntent().getExtras().getString("phone");
        getUserName();

        login_let_me_in_btn.setOnClickListener((v)->{
            setUsername();
        });
    }

    void setInProgress(boolean inProgress) {
        if (inProgress) {
            login_progress_bar1.setVisibility(View.VISIBLE);
            login_let_me_in_btn.setVisibility(View.GONE);
        } else {
            login_progress_bar1.setVisibility(View.GONE);
            login_let_me_in_btn.setVisibility(View.VISIBLE);
        }
    }

    void setUsername(){
        String username = login_username.getText().toString();
        if(username.isEmpty() || username.length() < 3){
            login_username.setError("Username length should be at least 3 char");
            return;
        }

        setInProgress(true);

        if(userModel != null){
            userModel.setUsername(username);
        }else{
            userModel = new UserModel(phoneNumber, username, Timestamp.now(), FirebaseUtil.currentUserId());
        }

        FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginUserNameActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    void getUserName(){
        setInProgress(true);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    userModel = task.getResult().toObject(UserModel.class);
                   if(userModel != null){
                        login_username.setText(userModel.getUsername());
                   }
                }
            }
        });
    }
}