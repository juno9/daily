package com.example.myapplication;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Activity_pwchange extends AppCompatActivity {
    String ip=ipclass.ip;
    int port = ipclass.httpport;
    String sourceurl="http://"+ip+":"+port+"/";
    EditText 현재비밀번호;
    EditText 새비밀번호;
    EditText 새비밀번호확인;
    TextView 비밀번호메시지;
    TextView 비밀번호일치메시지;
    Button 비밀번호변경완료버튼;
    String user_email;
    int 비밀번호형식체크;
    int 비밀번호일치체크;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwchange);

        현재비밀번호 = findViewById(R.id.입력란_현재비밀번호);
        새비밀번호 = findViewById(R.id.입력란_새비밀번호);
        새비밀번호확인 = findViewById(R.id.입력란_새비밀번호확인);
        비밀번호변경완료버튼 = findViewById(R.id.비밀번호변경완료버튼);
        비밀번호메시지 = findViewById(R.id.안내메시지_비밀번호);
        비밀번호일치메시지 = findViewById(R.id.안내메시지_비밀번호확인);
        Intent intent = getIntent();
        user_email = intent.getStringExtra("user_email");

        새비밀번호.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String 비밀번호체크 = checkPassword(새비밀번호.getText().toString());
                if (비밀번호체크.equals("")) {
                    비밀번호메시지.setTextColor(Color.parseColor("#009900"));
                    비밀번호메시지.setText(비밀번호체크);
                    비밀번호형식체크 = 1;
                    Log.i("비밀번호형식체크", String.valueOf(비밀번호형식체크));
                } else {
                    비밀번호메시지.setTextColor(Color.parseColor("#ff0000"));
                    비밀번호메시지.setText(비밀번호체크);
                    비밀번호형식체크 = 0;
                    Log.i("비밀번호형식체크", String.valueOf(비밀번호형식체크));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        새비밀번호확인.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String 비밀번호체크 = recheckPassword(새비밀번호.getText().toString(), 새비밀번호확인.getText().toString());
                if (비밀번호체크.equals("비밀번호가 일치합니다.")) {
                    비밀번호일치메시지.setTextColor(Color.parseColor("#009900"));
                    비밀번호일치메시지.setText(비밀번호체크);
                    비밀번호일치체크 = 1;
                    Log.i("비밀번호일치체크", String.valueOf(비밀번호일치체크));
                } else {
                    비밀번호일치메시지.setTextColor(Color.parseColor("#ff0000"));
                    비밀번호일치메시지.setText(비밀번호체크);
                    비밀번호일치체크 = 0;
                    Log.i("비밀번호일치체크", String.valueOf(비밀번호일치체크));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        비밀번호변경완료버튼.setOnClickListener(new View.OnClickListener() {
            //입력되지 않은 칸 있으면 찾아서 입력하라고 세팅해야
            //1.입력창들 다 채워져 있는지.
            //2.이메일 형식 맞는지
            //3.인증 되어있는지
            //4.비밀번호 일치 하는지
            @Override
            public void onClick(View view) {//온클릭메소드
                Log.i("회원가입 버튼 클릭", "클릭됨");
                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                //연결여부, 연결된 인터넷 경로를 받아온다(wifi, lte등)
                String 상태 = String.valueOf(status);
                Log.i("인터넷상태", 상태);
                if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
                    Log.i("조건문 진입", 상태);
                    // EditText값 예외처리
                    if (현재비밀번호.getText().toString().trim().length() > 0 &
                            새비밀번호.getText().toString().trim().length() > 0 &
                            새비밀번호확인.getText().toString().trim().length() > 0
                    ) {//입력여부 체크
                        if (새비밀번호.getText().toString().equals(새비밀번호확인.getText().toString())) {
                            if (비밀번호형식체크 == 1) {
                                if (비밀번호일치체크 == 1) {
                                    // 프로그래스바 보이게 처리
                                    // Instantiate the RequestQueue.
                                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                    Log.i("que", String.valueOf(queue));
                                    String url = sourceurl+"password_change.php";
                                    Log.i("url", url);
                                    // Request a string response from the provided URL.
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    // 프로그래스바 안보이게 처리
                                                    Log.i("응답", response);
                                                    if (response.equals("1")) {
                                                        Log.i("비밀번호 재설정 성공", response);
                                                        Toast.makeText(getApplicationContext(), "비밀번호가 변경되었습니다", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    } else if (response.equals("비번에러")) {
                                                        Log.i("현재 비밀번호 맞지 않음", response);
                                                        Toast.makeText(getApplicationContext(), "현재 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Log.i("비밀번호 재설정 실패", response);
                                                        Toast.makeText(getApplicationContext(), "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            //textView.setText("That didn't work!");

                                            String 에러내용 = String.valueOf(error);
                                            Log.i("Activity_pwchange","온에러리스폰스"+에러내용);
                                        }
                                    }) {
                                        // 포스트 파라미터 넣기
                                        @Override
                                        protected Map getParams() {
                                            Map params = new HashMap();
                                            params.put("user_email", user_email.trim());
                                            Log.i("로그인 유저 이메일", user_email);
                                            params.put("user_password", 현재비밀번호.getText().toString().trim());
                                            Log.i("비밀번호 입력값", 현재비밀번호.getText().toString());
                                            params.put("user_newpassword", 새비밀번호확인.getText().toString().trim());
                                            Log.i("새 비번 입력값", 새비밀번호확인.getText().toString());
                                            return params;
                                        }
                                    };
                                    // Add the request to the RequestQueue.
                                    queue.add(stringRequest);
                                }//비밀번호 일치 체크
                                else {
                                    Toast.makeText(getApplicationContext(), "새 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();

                                }
                            }//비밀번호 형식 체크
                            else {
                                Toast.makeText(getApplicationContext(), "비밀번호는 영문과 특수문자 숫자를 포함하며 8자 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
                            }
                        }//일치여부 체크
                        else {//일치하지 않았을 때
                            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                            Log.i("새비번", 새비밀번호.getText().toString());
                            Log.i("새비번 확인", 새비밀번호확인.getText().toString()
                            );
                        }
                    } //입력여부 체크
                    else {//입력되지 않았을 때
                        Toast.makeText(getApplicationContext(), "입력되지 않은 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }//온클릭 메소드
        });


    }

    public String checkPassword(String pwd) {
        // 비밀번호 포맷 확인(영문, 특수문자, 숫자 포함 8자 이상)
        Pattern passPattern1 = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$");
        Matcher passMatcher1 = passPattern1.matcher(pwd);

        if (!passMatcher1.find()) {//패턴이 일치하지 않음
            return "비밀번호는 영문과 특수문자 숫자를 포함하며 8자 이상이어야 합니다.";
        } else {//일치
            return "";
        }
    }
    public String recheckPassword(String pwd, String repwd) {
        // 비밀번호 포맷 확인(영문, 특수문자, 숫자 포함 8자 이상)

        if (pwd.equals(repwd)) {//패턴이 일치하지 않음
            return "비밀번호가 일치합니다.";
        } else {//일치
            return "비밀번호가 일치하지 않습니다.";
        }
    }

}
