package com.example.da08.firebasebbs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.da08.firebasebbs.domain.Bbs;

import java.text.SimpleDateFormat;

public class ReadActivity extends AppCompatActivity {

    TextView txtRtitle, txtRauthor, txtRcontent, txtRdate;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);



        txtRtitle = (TextView)findViewById(R.id.txtRtitle);
        txtRauthor = (TextView)findViewById(R.id.txtRauthor);
        txtRcontent = (TextView)findViewById(R.id.txtRcontent);
        txtRdate = (TextView)findViewById(R.id.txtRdate);
        imageView = (ImageView)findViewById(R.id.imageView);

        txtRcontent.setMovementMethod(new ScrollingMovementMethod());  // textView scroll

        setData();
    }

    public void setData(){
        // 목록에서 넘어온 position 값을 이용해 상세보기 데이터를 결정
        Intent intent = getIntent();
        int position = intent.getIntExtra("LIST_POSITION", -1);

        if(position > -1){
            Bbs bbs = Data.list.get(position);
            // 이미지 세팅
            if(bbs.fileUriString != null && !"".equals(bbs.fileUriString)){
                Glide.with(this)
                        .load(bbs.fileUriString)
                        .into(imageView);
            }
            // 값 세팅
            txtRtitle.setText(bbs.title);
            txtRauthor.setText(bbs.author);
            txtRcontent.setText(bbs.content);
            txtRdate.setText(convertLongToString(bbs.date));

        }

    }
    private String convertLongToString(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        return sdf.format(date);
    }



}


