package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Fragment.fragment_friends;
import com.example.myapplication.Fragment.fragment_home;
import com.example.myapplication.Fragment.fragment_setting;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.InetAddress;
import java.net.Socket;

public class Activity_home extends AppCompatActivity {

    BottomNavigationView 바텀내비게이션;

    Fragment fragment_home;//프래그먼트클래스 메모리 할당. 값은 들어가 있지 않음
    Fragment fragment_group;//프래그먼트클래스 메모리 할당. 값은 들어가 있지 않음
    Fragment fragment_setting;//프래그먼트클래스 메모리 할당. 값은 들어가 있지 않음
    PreferenceHelper 프리퍼런스헬퍼;

    private String TAG = "프래그먼트";



    @Override
    protected void onCreate(Bundle savedInstanceState) {//액티비티 생명주기 상 oncreate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.i("셋 컨텐트뷰", "레이아웃과 클래스 파일을 연결");





        Log.i("프래그먼트홈", String.valueOf(fragment_home));
        프리퍼런스헬퍼=new PreferenceHelper(getApplicationContext());
        String email = 프리퍼런스헬퍼.getUser_email();
        fragment_home = new fragment_home();//메모리를 할당해 둔 fragment_home 변수에 home_fragment라는 클래스의 객체를 할당한다.
        Log.i("홈 액티비티", "홈 프래그먼트 초기화");
        fragment_group = new fragment_friends();
        Log.i("홈 액티비티", "친구프래그먼트 초기화");

        fragment_setting = new fragment_setting();
        Log.i("홈 액티비티", "세팅프래그먼트 초기화");



        Bundle bundle = new Bundle(1); // 파라미터의 숫자는 전달하려는 값의 갯수
        bundle.putString("user_email", email);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_home).commitAllowingStateLoss();

        fragment_home.setArguments(bundle);
        바텀내비게이션 = findViewById(R.id.bottomNavigationView);

        바텀내비게이션.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem 내비아이템) {



                switch (내비아이템.getItemId()) {
                    case R.id.home:

                        Bundle bundle=new Bundle();
                        bundle.putString("user_email", email);
                        fragment_setting.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_home).commitAllowingStateLoss();
                        return true;

                    case R.id.friends:

                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_group).commitAllowingStateLoss();
                        return true;
                    case R.id.setting:


                        Bundle bundle2 = new Bundle();
                        bundle2.putString("user_email", email);
                        fragment_setting.setArguments(bundle2);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_setting).commitAllowingStateLoss();
                        return true;
                }
                return true;
            }
        });


    }
    // 액티비티 전환 함수


    @Override
    protected void onDestroy() {
        super.onDestroy();

//        finishAffinity();
//        System.runFinalization();
//        System.exit(0);
        프리퍼런스헬퍼.set_service_running(false);

        stopService(new Intent(getApplicationContext(),Service_chat.class));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }






}