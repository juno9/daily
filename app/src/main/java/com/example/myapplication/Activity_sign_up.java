package com.example.myapplication;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class Activity_sign_up extends AppCompatActivity {
    String ip=ipclass.ip;

    int port = ipclass.httpport;
    String sourceurl="http://"+ip+":"+port+"/";
    EditText 닉네임입력;
    EditText 이메일입력;
    EditText 인증번호입력;
    EditText 비밀번호입력;
    EditText 비밀번호확인입력;

    TextView 이메일안내메시지;
    TextView 비밀번호안내메시지;
    TextView 비밀번호확인안내메시지;
    TextView 인증안내메시지;


    Button 인증번호전송버튼;
    Button 이메일인증버튼;
    Button 회원가입완료버튼;


    int 이메일형식체크;
    int 비밀번호형식체크;
    int 인증여부체크;
    int 비밀번호일치체크;

    String 인증번호;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // UI 요소 연결
        닉네임입력 = findViewById(R.id.입력란_닉네임);
        이메일입력 = findViewById(R.id.입력란_이메일);
        인증번호입력 = findViewById(R.id.입력란_인증번호);
        비밀번호입력 = findViewById(R.id.입력란_비밀번호);
        비밀번호확인입력 = findViewById(R.id.입력란_비밀번호확인);

        이메일안내메시지 = findViewById(R.id.안내메시지_이메일);
        인증안내메시지 = findViewById(R.id.안내메시지_인증번호);
        비밀번호안내메시지 = findViewById(R.id.안내메시지_비밀번호);


        인증번호전송버튼 = findViewById(R.id.인증번호전송버튼);
        이메일인증버튼 = findViewById(R.id.인증버튼);
        회원가입완료버튼 = findViewById(R.id.회원가입버튼);

        이메일입력.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력되는 텍스트에 변화가 있을 때 호출된다.
                String 체크 = checkEmailForm(이메일입력.getText().toString());
                if (체크.equals("올바른 형식의 이메일 입니다")) {
                    이메일안내메시지.setTextColor(Color.parseColor("#009900"));
                    이메일형식체크 = 1;
                    Log.i("이메일형식체크", String.valueOf(이메일형식체크));
                    이메일안내메시지.setText(체크);
                } else {
                    이메일안내메시지.setTextColor(Color.parseColor("#ff0000"));
                    이메일안내메시지.setText(체크);
                    이메일형식체크 = 0;
                    Log.i("이메일형식체크", String.valueOf(이메일형식체크));
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력이 끝났을 때 호출된다.
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        인증번호전송버튼.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View view) {
                                            인증번호 = createNumberCode();
                                            //여기서 인증번호 만들고
                                            new Thread() {
                                                @Override
                                                public void run() {
                                                    super.run();
                                                    GmailSender gMailSender = new GmailSender("joonho340@gmail.com", "fmjyziwxaplloryx");
                                                    //GMailSender.sendMail(제목, 본문내용, 받는사람);
                                                    try {
                                                        Log.i("인증번호", 인증번호);
                                                        gMailSender.sendMail("DailyReport 인증메일입니다. ", "인증번호:" + 인증번호, 이메일입력.getText().toString());
                                                        //온클릭과 함께 만들어진 인증번호 이메일에 넣어서 보내고
                                                    } catch (SendFailedException e) {

                                                        //쓰레드에서는 Toast를 띄우지 못하여 runOnUiThread를 호출해야 한다.
                                                        Activity_sign_up.this.runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                    } catch (MessagingException e) {
                                                        System.out.println("인터넷 문제" + e);
                                                        //쓰레드에서는 Toast를 띄우지 못하여 runOnUiThread를 호출해야 한다.
                                                        Activity_sign_up.this.runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해 주십시오", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    //쓰레드에서는 Toast를 띄우지 못하여 runOnUiThread를 호출해야 한다.
                                                    Activity_sign_up.this.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getApplicationContext(), "송신 완료", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }.start();


                                        }
                                    }//인증번호 전송버튼 온클릭 리스너
        );


        이메일인증버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String 입력된인증번호 = 인증번호입력.getText().toString();
                Log.i("입력된 인증번호", 입력된인증번호);
                if (인증번호 == null) {
                    Toast.makeText(getApplicationContext(), "인증번호가 올바르지 않습니다", Toast.LENGTH_SHORT).show();
                } else{
                    Log.i("저장된 인증번호", 인증번호);
                if (입력된인증번호.equals(인증번호)) {
                    인증안내메시지.setTextColor(Color.parseColor("#009900"));
                    인증안내메시지.setText("인증이 완료되었습니다.");
                    이메일인증버튼.setClickable(false);
                    인증번호전송버튼.setClickable(false);
                    인증번호입력.setClickable(false);
                    인증번호입력.setFocusable(false);
                    이메일입력.setClickable(false);
                    이메일입력.setFocusable(false);
                    이메일입력.setBackgroundColor(Color.parseColor("#808080"));
                    인증번호입력.setBackgroundColor(Color.parseColor("#808080"));
                    인증여부체크 = 1;
                } else {
                    인증안내메시지.setTextColor(Color.parseColor("#ff0000"));
                    인증안내메시지.setText("인증번호가 일치하지 않습니다.");
                    인증여부체크 = 0;
                }
            }
            }
        });

        비밀번호입력.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String 비밀번호체크 = checkPassword(비밀번호입력.getText().toString());
                if (비밀번호체크.equals("")) {
                    비밀번호안내메시지.setTextColor(Color.parseColor("#009900"));
                    비밀번호안내메시지.setText(비밀번호체크);
                    비밀번호형식체크 = 1;
                    Log.i("비밀번호형식체크", String.valueOf(비밀번호형식체크));
                } else {
                    비밀번호안내메시지.setTextColor(Color.parseColor("#ff0000"));
                    비밀번호안내메시지.setText(비밀번호체크);
                    비밀번호형식체크 = 0;
                    Log.i("비밀번호형식체크", String.valueOf(비밀번호형식체크));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        비밀번호확인입력.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String 비밀번호체크 = recheckPassword(비밀번호입력.getText().toString(), 비밀번호확인입력.getText().toString());
                if (비밀번호체크.equals("비밀번호가 일치합니다.")) {
                    비밀번호안내메시지.setTextColor(Color.parseColor("#009900"));
                    비밀번호안내메시지.setText(비밀번호체크);
                    비밀번호일치체크 = 1;
                    Log.i("비밀번호일치체크", String.valueOf(비밀번호일치체크));
                } else {
                    비밀번호안내메시지.setTextColor(Color.parseColor("#ff0000"));
                    비밀번호안내메시지.setText(비밀번호체크);
                    비밀번호일치체크 = 0;
                    Log.i("비밀번호일치체크", String.valueOf(비밀번호일치체크));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        회원가입완료버튼.setOnClickListener(new View.OnClickListener() {
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
                    if (닉네임입력.getText().toString().trim().length() > 0 ||
                            이메일입력.getText().toString().trim().length() > 0 ||
                            인증번호입력.getText().toString().trim().length() > 0 ||
                            비밀번호입력.getText().toString().trim().length() > 0 ||
                            비밀번호확인입력.getText().toString().trim().length() > 0) {//입력여부 체크
                        if (이메일형식체크 == 1) {
                            if (비밀번호형식체크 == 1) {
                                if (인증여부체크 == 1) {
                                    if (비밀번호일치체크 == 1) {
                                        // 프로그래스바 보이게 처리
                                        findViewById(R.id.cpb).setVisibility(View.VISIBLE);

                                        // Instantiate the RequestQueue.
                                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                        Log.i("큐 생성함", String.valueOf(queue));
                                        String url = sourceurl+"signup.php";
                                        Log.i("url주소 스트링으로 선언", url);
                                        // Request a string response from the provided URL.
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {//온 리스폰스

                                                // 프로그래스바 안보이게 처리
                                                findViewById(R.id.cpb).setVisibility(View.GONE);
                                                Log.i("응답", response);
                                                if (response.equals("1")) {
                                                    startActivityflag(Activity_login.class);
                                                    Log.i("가입성공", response);
                                                    Toast.makeText(getApplicationContext(), "회원가입에 성공 했습니다.", Toast.LENGTH_SHORT).show();
                                                } else if(response.equals("o")) {
                                                    Log.i("가입실패", response);
                                                    Toast.makeText(getApplicationContext(), "회원가입에 실패 했습니다.", Toast.LENGTH_SHORT).show();
                                                } else if(response.equals("already")){
                                                    Log.i("가입실패", response);
                                                    Toast.makeText(getApplicationContext(), "이미 가입된 이메일 입니다.\n이전 화면으로 돌아가 로그인 해주세요", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        }//온 리스폰스
                                                , new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                //textView.setText("That didn't work!");

                                                String 에러내용 = String.valueOf(error);
                                                Log.i("Activity_sign_up", "온에러리스폰스"+에러내용);
                                            }
                                        }//온에러리스폰스
                                        )
                                        {
                                            // 포스트 파라미터 넣기
                                            @Override
                                            protected Map getParams() {
                                                Map params = new HashMap();
                                                params.put("user_name", 닉네임입력.getText().toString().trim());
                                                Log.i("닉네임입력값", 닉네임입력.getText().toString());
                                                params.put("user_email", 이메일입력.getText().toString().trim());
                                                Log.i("이메일입력값", 이메일입력.getText().toString());
                                                params.put("user_password", 비밀번호확인입력.getText().toString().trim());
                                                Log.i("비밀번호입력값", 비밀번호확인입력.getText().toString());
                                                return params;
                                            }

                                        };
                                        // Add the request to the RequestQueue.
                                        queue.add(stringRequest);

                                    }//비밀번호 일치여부 체크
                                    else{
                                        Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                                        //입력창 포커스 기능도 넣자
                                    }
                                }//인증여부체크
                                else{
                                    Toast.makeText(getApplicationContext(), "이메일 인증을 완료해 주세요", Toast.LENGTH_SHORT).show();
                                    //입력창 포커스 기능도 넣자
                                }
                            }//비밀번호형식체크
                            else {
                                Toast.makeText(getApplicationContext(), "비밀번호는 영문과 특수문자 숫자를 포함하며 8자 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
                                //입력창 포커스 기능도 넣자
                            }
                        }//이메일형식 체크
                        else {
                            Toast.makeText(getApplicationContext(), "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                            //입력창 포커스 기능도 넣자
                        }

                    } //입력여부 체크
                    else//입력여부 조건문
                    {
                        Toast.makeText(getApplicationContext(), "입력되지 않은 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }//온클릭 메소드
        });


    }

    // 액티비티 전환 함수

    // 인텐트 액티비티 전환함수
    public void startActivityC(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
        // 화면전환 애니메이션 없애기
        overridePendingTransition(0, 0);
    }
    // 인텐트 화면전환 하는 함수
    // FLAG_ACTIVITY_CLEAR_TOP = 불러올 액티비티 위에 쌓인 액티비티 지운다.
    public void startActivityflag(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        // 화면전환 애니메이션 없애기
        overridePendingTransition(0, 0);
    }

    // 문자열 인텐트 전달 함수
    public void startActivityString(Class c, String name, String sendString) {
        Intent intent = new Intent(getApplicationContext(), c);
        intent.putExtra(name, sendString);
        startActivity(intent);
        // 화면전환 애니메이션 없애기
        overridePendingTransition(0, 0);
    }

    // 백스택 지우고 새로 만들어 전달
    public void startActivityNewTask(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        // 화면전환 애니메이션 없애기
        overridePendingTransition(0, 0);
    }

    /***
     *  이메일 형식 체크 해주는 기능
     *
     *  @param  inputText - 이메일 형식을 체크하고싶은 텍스트
     *
     *  @return String 이메일 형식에 맞을 때 : "올바른 이메일 입니다."
     *                 이메일 형식에 맞지 않을 때 : "올바른 이메일을 입력해주세요."
     */
    public String checkEmailForm(String inputText) {

        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(inputText);

        if (matcher.find()) {
            //이메일 형식에 맞을 때
            return "올바른 형식의 이메일 입니다";

        } else {
            //이메일 형식에 맞지 않을 때
            return "이메일 형식이 올바르지 않습니다.";

        }
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

    private String createNumberCode() { //이메일 인증코드 생성
        String[] str = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        String newCode = new String();

        for (int x = 0; x < 6; x++) {
            int random = (int) (Math.random() * str.length);
            newCode += str[random];
        }

        return newCode;
    }


}