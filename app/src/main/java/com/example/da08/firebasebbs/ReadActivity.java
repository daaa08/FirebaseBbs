package com.example.da08.firebasebbs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
    }
}
