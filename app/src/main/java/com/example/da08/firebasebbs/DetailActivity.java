package com.example.da08.firebasebbs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.da08.firebasebbs.domain.Bbs;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    EditText editTitle, editAuthor, editContent;
    Button btnSave;

    FirebaseDatabase database;
    DatabaseReference bbsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        database = FirebaseDatabase.getInstance();
        bbsRef = database.getReference("bbs");

        editTitle = (EditText)findViewById(R.id.editTitle);
        editAuthor = (EditText)findViewById(R.id.editAuthor);
        editContent = (EditText)findViewById(R.id.editContent);
        btnSave  = (Button)findViewById(R.id.btnSave);
    }

    // 데이터 전송
    public void postData(View v ){

        String title = editTitle.getText().toString();
        String author = editAuthor.getText().toString();
        String content = editContent.getText().toString();
        Date date = new Date();
        long bbsdate = date.getTime();


        // 파이어 베이스에 데이터 넣기
        // 1 데이터 객체 생성
        Bbs bbs = new Bbs(title, author,content,bbsdate);
        // 2. 입력할 데이터의 키 생성
        String bbsKey = bbsRef.push().getKey(); // 자동생성된 키를 가져온다
        // 3. 생성된 키를 레퍼런스로 데이터를 입력
        bbsRef.child(bbsKey).setValue(bbs);
        //    update : bbsRef.child(bbsKey).setValue(bbs);
        //    delete : bbsRef.child(bbsKey).setValue(null);
        // 데이터 입력후 창 닫기
        finish();
    }
}
