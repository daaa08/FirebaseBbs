package com.example.da08.firebasebbs;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.da08.firebasebbs.domain.Bbs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    EditText editTitle, editAuthor, editContent;
    Button btnSave;

    FirebaseDatabase database;
    DatabaseReference bbsRef;
    // storage
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // database 레퍼런스
        database = FirebaseDatabase.getInstance();
        bbsRef = database.getReference("bbs");
        // storage 레퍼런스
        mStorageRef = FirebaseStorage.getInstance().getReference("images");  // getReference를 기준으로 객체들이 들어감, mStorageRef도 동일

        editTitle = (EditText)findViewById(R.id.editTitle);
        editAuthor = (EditText)findViewById(R.id.editAuthor);
        editContent = (EditText)findViewById(R.id.editContent);
        btnSave  = (Button)findViewById(R.id.btnSave);
    }


    // storage _upload a file
    public void upLoadFile(String filePath){
        File file = new File(filePath);  // 스마트폰에있는 파일 업로드 경로
        Uri uri = Uri.fromFile(file);
        // 파이어 베이스의 파일 업로드 경로
        String fileName = file.getName();
        StorageReference riversRef = mStorageRef.child(fileName);

        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
//                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.e("storage","success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Log.e("storage","fail"+exception.getMessage());
                    }
                });
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
