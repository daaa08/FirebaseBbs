package com.example.da08.firebasebbs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.da08.firebasebbs.domain.Bbs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapter adapter;

    FirebaseDatabase database;
    DatabaseReference bbsRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        database = FirebaseDatabase.getInstance();
        bbsRef = database.getReference("bbs");

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        adapter = new RecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }

    // 파이어 베이스 연동
    public void loadData(){
        Data.list.clear();
        bbsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot item : dataSnapshot.getChildren()){
                    // json 데이터를 Bbs 인스턴스로 변환오류 발생 가능성 있음
                    // 그래서 예외처리 필요
                    try {
                        Bbs bbs = item.getValue(Bbs.class);
                        Data.list.add(bbs);
                    }catch(Exception e){
                        Log.e("Firebase",e.getMessage());
                    }
                }
                refreshList(Data.list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void refreshList(List<Bbs> data){
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    // 데이터 전송처리
    public void postData(View v){
        Intent intent = new Intent(this, WriteActivity.class);
        startActivity(intent);
    }
}
