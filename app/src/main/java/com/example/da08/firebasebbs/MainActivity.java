package com.example.da08.firebasebbs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.da08.firebasebbs.util.PermissionControl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// 인증처리 용
public class MainActivity extends AppCompatActivity implements PermissionControl.CallBack{

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();  // 인증처리를 하기 위한 것
    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d("Auth", "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d("Auth", "onAuthStateChanged:signed_out");
            }
            // ...
        }
    };

    // 위젯 정의
    EditText editEmail, editPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionControl.checkPermission(this);
    }

    public void startBbs(View v){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionControl.onResult(this, requestCode, grantResults);
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void init() {

        editEmail = (EditText) findViewById(R.id.editEmail);
        editPw = (EditText)findViewById(R.id.editPw);



//        Intent intent = new Intent(this, NaviActivity.class);
//        startActivity(intent);
//        finish();  // splash 화면은 backstack에 남아있으면 안되므로 종료시켜줘야함
    }

    public void signUp(View v){

        String email = editEmail.getText().toString();
        String pw = editPw.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Auth", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "사용자 등록이되지 않았습니다",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void signIn(View v){
        String email = editEmail.getText().toString();
        String pw = editPw.getText().toString();

        mAuth.signInWithEmailAndPassword(email, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Auth", "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w("Auth", "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, "SignIn이 되지 않았습니다",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this, "SignIn이 되었습니다",
                                    Toast.LENGTH_SHORT).show();

                            goMain();
                        }
                    }
                });
    }

    public void goMain(){
        Intent intent = new Intent(this, NaviActivity.class);
        startActivity(intent);

        finish();
    }
}
