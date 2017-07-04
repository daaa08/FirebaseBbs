package com.example.da08.firebasebbs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // 사용자가 가입을하면 토큰을 생성해야함
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("Message", "Refreshed token:" + refreshedToken);
    }
}
