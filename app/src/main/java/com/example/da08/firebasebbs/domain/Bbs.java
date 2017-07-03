package com.example.da08.firebasebbs.domain;

/**
 * Created by Da08 on 2017. 7. 3..
 */

public class Bbs {

    public String id;   // 파이어베이스의 푸쉬를 이용해서 자동 생성
    public String title;
    public String author;
    public String content;
    public long date;
    public long count;  // 조회수

    public Bbs(){

    }

    public Bbs(String title, String author, String content, long date){
        this.title = title;
        this.author = author;
        this.content = content;
        this.date = date;
    }
}
