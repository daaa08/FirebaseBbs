package com.example.da08.firebasebbs;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    Button btnSave, btnGallery;
    TextView txtImgName;

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
        btnGallery  = (Button)findViewById(R.id.btnGallery);
        txtImgName = (TextView)findViewById(R.id.txtImgName);
    }


    // storage _upload a file
    public void upLoadFile(String filePath){
        File file = new File(filePath);  // 스마트폰에있는 파일 업로드 경로
        Uri uri = Uri.fromFile(file);
        // 파이어 베이스의 파일 업로드 경로
        String fileName = file.getName();
        // 데이터 베이스의 키가 값과 동일한 구조
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

    // 화면의 gallery버튼 자동 링크
    public void openGallery(View v){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 가. 이미지 선택창 호출
        startActivityForResult( Intent.createChooser(intent, "앱을 선택하세요") , 100);  // 코드가 100

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode) {
                // 나. 이미지 선택창에서 선택된 이미지의 경로 추출
                case 100:
                    Uri imageUri = data.getData();
//                    File file = new File(imageUri.getPath());   // fileName을 가져옴
//                    txtImgName.setText(file.getName());
//                    txtImgName.setText(imageUri.getPath());   // 경로가있는 file을 가져올때 사용

                    // file전체 경로를 가져 옴
                    String filePath = getPsthFromUri(this,imageUri);
                    txtImgName.setText(filePath);
                    break;
            }
        }
    }

    // Uri 에서 실제 경로 꺼내는 함수
    public static String getPsthFromUri(Context context, Uri uri){
        String realPath = "";
        Cursor cu = context.getContentResolver().query(uri,null,null,null,null);
        if(cu.moveToNext()){
            realPath = cu.getString(cu.getColumnIndex("_date"));
        }
        cu.close();
        return realPath;
    }
}
