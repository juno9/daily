package com.example.myapplication;

import android.os.Bundle;
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

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class Activity_password_reset extends AppCompatActivity {
String ip=ipclass.ip;
int port = ipclass.httpport;
    TextView 안내텍스트뷰;
    String sourceurl="http://"+ip+":"+port+"/";
    EditText 이메일주소입력;
    Button 메일전송버튼;
    String 임시비밀번호;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        안내텍스트뷰=findViewById(R.id.가입이메일입력안내);
        이메일주소입력=findViewById(R.id.이메일주소_입력);
        메일전송버튼=findViewById(R.id.임시비밀번호_전송버튼);



        안내텍스트뷰.setText("아이디로 사용한 이메일을 입력해 주세요.\n임시비밀번호를 보내 드립니다.");

        메일전송버튼.setOnClickListener(new View.OnClickListener() {//비밀번호 재설정 메일 발송버튼 클릭시
            @Override
            public void onClick(View view) {
                임시비밀번호=인증코드생성();
                //여기서 인증번호 만들고
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        GmailSender gMailSender = new GmailSender("joonho340@gmail.com", "fmjyziwxaplloryx");
                        //GMailSender.sendMail(제목, 본문내용, 받는사람);
                        try {
                            Log.i("임시비밀번호", 임시비밀번호);
                            gMailSender.sendMail("DailyReport 임시비밀번호입니다.. ", "임시비밀번호:" + 임시비밀번호, 이메일주소입력.getText().toString());
                            //온클릭과 함께 만들어진 인증번호 이메일에 넣어서 보내고
                        } catch (SendFailedException e) {

                            //쓰레드에서는 Toast를 띄우지 못하여 runOnUiThread를 호출해야 한다.
                            Activity_password_reset.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } catch (MessagingException e) {
                            System.out.println("인터넷 문제" + e);
                            //쓰레드에서는 Toast를 띄우지 못하여 runOnUiThread를 호출해야 한다.
                            Activity_password_reset.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해 주십시오", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //쓰레드에서는 Toast를 띄우지 못하여 runOnUiThread를 호출해야 한다.
                        Activity_password_reset.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //여기서 난수를 db에 저장하는 쿼리를 돌리고 결과값을 받아야 한다.
                                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                Log.i("que", "que");
                                String url = sourceurl+"password_reset.php";
                                Log.i("url", url);
                                // Request a string response from the provided URL.
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                // 프로그래스바 안보이게 처리

                                                Log.i("응답", response);
                                                if (response.equals("1")) {

                                                    Log.i("비밀번호저장됨", response);

                                                } else {
                                                    Log.i("비밀번호 저장되지 않음", response);

                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //textView.setText("That didn't work!");

                                        String 에러내용 = String.valueOf(error);
                                        Log.i("Activity_password_reset", "온에러리스폰스"+에러내용);
                                    }
                                }) {
                                    // 포스트 파라미터 넣기
                                    @Override
                                    protected Map getParams() {
                                        Map params = new HashMap();

                                        params.put("user_email", 이메일주소입력.getText().toString().trim());
                                        Log.i("이메일입력값", 이메일주소입력.getText().toString());
                                        params.put("user_temp_password", 임시비밀번호.trim());
                                        Log.i("임시비밀번호", 임시비밀번호);
                                        return params;
                                    }

                                };
                                // Add the request to the RequestQueue.
                                queue.add(stringRequest);

                                Toast.makeText(getApplicationContext(), "입력하신 이메일 주소로 임시 비밀번호를 전송하였습니다.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }.start();


            }
        });//비밀번호 재설정 메일 발송버튼 클릭시

        //안드로이드에서 할 일
        //1)비밀번호를 재설정할 유저의 이메일 받기(Edittext)
        //2)임의의 난수 생성하기-숫자 문자 조합 8자리
        //3)url로 입력받은 이메일, 난수 보내기
        //php에서 할 일
        //1) 받은 이메일, 임의 비밀번호(난수) 변수에 담기
        //2) update 쿼리문 작성-UPDATE user SET user_password = hex WHERE user_email = '$_POST[user_email]';
        //3) 쿼리 결과에 따라 echo 1; 또는 echo 0;







    }



    private String 인증코드생성() { //이메일 인증코드 생성
        String[] str = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
                "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String newCode = new String();

        for (int x = 0; x < 8; x++) {
            int random = (int) (Math.random() * str.length);
            newCode += str[random];
        }

        return newCode;
    }


}
