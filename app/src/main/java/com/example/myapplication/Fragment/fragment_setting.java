package com.example.myapplication.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.Activity_login;
import com.example.myapplication.PreferenceHelper;
import com.example.myapplication.Activity_profileupdate;
import com.example.myapplication.Activity_pwchange;
import com.example.myapplication.R;
import com.example.myapplication.Service_chat;
import com.example.myapplication.ipclass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class fragment_setting extends Fragment {

    private ViewGroup view;
    String ip = ipclass.ip;
    int port = ipclass.httpport;
    String sourceurl= "http://"+ip+":"+port+"/";

    private String TAG = "세팅프래그먼트";

    TextView 프로필변경;
    TextView 비밀번호재설정;
    TextView 로그아웃;
    String user_email;
    PreferenceHelper 프리퍼런스헬퍼;
    Socket 소켓;
    BufferedReader 버퍼리더;
    PrintWriter 프린트라이터;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);
        프로필변경 = (TextView) view.findViewById(R.id.프로필수정버튼);

        프리퍼런스헬퍼 = new PreferenceHelper(getActivity());

        Bundle extra = getArguments();
        if (extra != null) {
            user_email = extra.getString("user_email");
            Log.i("유저 이름 받기 성공", user_email);
        }


        프로필변경.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_profileupdate.class);
                startActivity(intent);
            }
        });


        비밀번호재설정 = (TextView) view.findViewById(R.id.비밀번호변경버튼);
        비밀번호재설정.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityString(Activity_pwchange.class, "user_email", user_email);
            }
        });


        로그아웃 = (TextView) view.findViewById(R.id.로그아웃버튼);
        로그아웃.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                프리퍼런스헬퍼.setUser_email(null);
                프리퍼런스헬퍼.setLogin(false);
                프리퍼런스헬퍼.set_service_running(false);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
//                            Service_chat.프린트라이터.println("/exit");
//                            Service_chat.프린트라이터.flush();
                        } catch (Exception e) {
                        }
                    }
                }.start();
                if (getActivity() != null) {
                    getActivity().finish();
                }
                startActivityC(Activity_login.class);

            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        onSaveInstanceState();
    }

    private void onSaveInstanceState() {
    }

    // 문자열 인텐트 전달 함수
    public void startActivityString(Class c, String name, String sendString) {
        Intent intent = new Intent(getActivity(), c);
        intent.putExtra(name, sendString);
        startActivity(intent);
        // 화면전환 애니메이션 없애기

    }

    public void startActivityC(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivity(intent);

        // 화면전환 애니메이션 없애기

    }

    public void startActivityflag(Class c) {
        Intent intent = new Intent(getActivity(), c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        // 화면전환 애니메이션 없애기
    }


}
