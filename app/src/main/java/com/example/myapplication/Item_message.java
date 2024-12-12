package com.example.myapplication;

public class Item_message {

    //종류는 세가지-알림, 상대방, 나, 시간
    //알림은 가운대, 상대방 메시지는 왼쪽에 나의 메시지는 오른쪽에
    //상대가 보낸 메시지에는 메일, 시간,내용,프로필사진 이 나와야 한다.
    //나의 메시지에는 시간,내용이 보여야 한다.
    //알림으로 메시지 시간 읽고 몇시에 보낸건지 이정표 역할을 해 줄 문구가 필요하다.
    //서비스를 적용하여 앱 구동중이 아닐 때 알림을 받을 수 있어야 하고 알림 끄기 기능도 있어야 한다.

    String 유형;//내가 보낸 메시지, 상대가 보낸 메시지, 알림
    String 시간;
    String 메시지내용;
    String 보낸유저메일;
    String 받은유저메일;


    Item_message(String 받은유형, String 받은시간, String 받은메시지내용,String 보낸유저메일, String 받은유저메일) {
        this.유형 = 받은유형;
        this.시간 = 받은시간;
        this.메시지내용 = 받은메시지내용;
        this.보낸유저메일 = 보낸유저메일;
        this.받은유저메일 = 받은유저메일;

    }

    public String get시간() {
        return 시간;
    }

    public String get메시지내용() {
        return 메시지내용;
    }

    public String get받은유저메일() {
        return 받은유저메일;
    }

    public String get보낸유저메일() {
        return 보낸유저메일;
    }

    public String get유형() {
        return 유형;
    }

    public void set시간(String 시간) {
        this.시간 = 시간;
    }

    public void set메시지내용(String 메시지내용) {
        this.메시지내용 = 메시지내용;
    }

    public void set받은유저메일(String 받은유저메일) {
        this.받은유저메일 = 받은유저메일;
    }

    public void set보낸유저메일(String 보낸유저메일) {
        this.보낸유저메일 = 보낸유저메일;
    }

    public void set유형(String 유형) {
        this.유형 = 유형;
    }
}
