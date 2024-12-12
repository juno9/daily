package com.example.myapplication;

public class Item_record {
    String 유저메일;
    String 시작날짜;
    String 시작시간;
    String 종료날짜;
    String 종료시간;
    String 제목;
    String 내용;
    String 키값;


    public Item_record(String 유저메일, String 시작날짜, String 시작시간, String 종료날짜, String 종료시간, String 받은제목, String 받은내용, String 키값) {
        this.유저메일 = 유저메일;
        this.시작날짜 = 시작날짜;
        this.시작시간 = 시작시간;
        this.종료날짜 = 종료날짜;
        this.종료시간 = 종료시간;
        this.제목 = 받은제목;
        this.내용 = 받은내용;
        this.키값 = 키값;
    }//생성자

    public String get키값() {
        return 키값;
    }

    public void set키값(String 키값) {
        this.키값 = 키값;
    }

    public void set유저메일(String 유저메일) {
        this.유저메일 = 유저메일;
    }

    public String get유저메일() {
        return 유저메일;
    }



    public String get내용() {
        return 내용;
    }

    public String get시작날짜() {
        return 시작날짜;
    }

    public String get시작시간() {
        return 시작시간;
    }

    public String get제목() {
        return 제목;
    }

    public String get종료날짜() {
        return 종료날짜;
    }

    public String get종료시간() {
        return 종료시간;
    }

    public void set내용(String 내용) {
        this.내용 = 내용;
    }

    public void set시작날짜(String 시작날짜) {
        this.시작날짜 = 시작날짜;
    }

    public void set시작시간(String 시작시간) {
        this.시작시간 = 시작시간;
    }

    public void set제목(String 제목) {
        this.제목 = 제목;
    }

    public void set종료날짜(String 종료날짜) {
        this.종료날짜 = 종료날짜;
    }

}

