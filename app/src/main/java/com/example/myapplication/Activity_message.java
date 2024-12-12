package com.example.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Activity_message extends AppCompatActivity {

    private ListView 채팅리스트뷰;
    private EditText 입력창;
    private Button 보내기버튼;
    private TextView 상대유저이름;
    private ImageView 뒤로가기;

    private ArrayList<Item_message> 메시지목록 = new ArrayList<>();
    private Adapter_message 메시지어댑터;

    private Handler 핸들러;
    private PrintWriter 프린트라이터;
    private BufferedReader 버퍼리더;
    private Service_chat serviceChat;
    private boolean isBound = false;
    private String 유저메일;
    private String 받는유저메일;
    private Socket 소켓;
    private PreferenceHelper 프리퍼런스헬퍼;

    private final String ip = ipclass.ip;
    private final int port = ipclass.httpport;
    private final int chatport = ipclass.chatport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        setupHandlers();

        initUI();

        setupListeners();

        loadChatHistory(유저메일, 받는유저메일);
        Intent intent = new Intent(this, Service_chat.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Service_chat.LocalBinder binder = (Service_chat.LocalBinder) service;
            serviceChat = binder.getService();
            isBound = true;

            // 서비스의 멤버 변수에 접근
            if (serviceChat != null) {

                프린트라이터 = serviceChat.프린트라이터;
                serviceChat.받기쓰레드.set핸들러(핸들러);
                serviceChat.받기쓰레드.set메시지목록(메시지목록);
                버퍼리더 = serviceChat.버퍼리더;
                serviceChat.받기쓰레드.set위치(받는유저메일);
                serviceChat.받기쓰레드.set대화중(true);

                Log.i("[메시지액티비티]프린트라이터 스트링", 프린트라이터.toString());
                Log.i("[메시지액티비티]버퍼리더 스트링", 버퍼리더.toString());
                new Thread(() -> {
                    프린트라이터.println(받는유저메일);
                    프린트라이터.flush();
                    Log.i("[소켓 연결]", "소켓 연결 및 사용자 정보 전송 완료");
                }).start();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceChat = null;
            isBound = false;
        }
    };

    private void initUI() {
        입력창 = findViewById(R.id.입력란_채팅내용);
        보내기버튼 = findViewById(R.id.버튼_전송);
        상대유저이름 = findViewById(R.id.텍스트뷰_상대유저이름);
        뒤로가기 = findViewById(R.id.이미지뷰_뒤로가기2);
        채팅리스트뷰 = findViewById(R.id.리스트뷰_채팅내용);

        프리퍼런스헬퍼 = new PreferenceHelper(getApplicationContext());
        유저메일 = 프리퍼런스헬퍼.getUser_email();

        Intent intent = getIntent();
        받는유저메일 = intent.getStringExtra("받는유저메일");
        상대유저이름.setText(받는유저메일);

        메시지어댑터 = new Adapter_message(메시지목록, this, intent.getStringExtra("이미지url스트링"));
        채팅리스트뷰.setAdapter(메시지어댑터);
    }

    private void setupHandlers() {
        핸들러 = new Handler(msg -> {
            if (msg.what == 1) {
                메시지어댑터.notifyDataSetChanged();
                채팅리스트뷰.setSelection(메시지목록.size() - 1);
                return true;
            }
            return false;
        });


    }

    private void setupListeners() {
        보내기버튼.setOnClickListener(v -> {
            String 보낼메시지 = 입력창.getText().toString().trim();
            if (!보낼메시지.isEmpty()) {
                sendMessage(보낼메시지);
            } else {
                Toast.makeText(this, "메시지를 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        뒤로가기.setOnClickListener(v -> finish());
    }


    private void sendMessage(String 보낼메시지) {
        new Thread(() -> {
            try {
                if (프린트라이터 == null) {
                    Log.e("[메시지 전송 오류]", "PrintWriter가 null입니다.");
                    runOnUiThread(() -> Toast.makeText(this, "서버와 연결되지 않았습니다.", Toast.LENGTH_SHORT).show());
                    return;
                }
                Item_message 보낼메시지아이템 = new Item_message("sentMessage", getCurrentTime(), 보낼메시지, 유저메일, 받는유저메일);
                메시지목록.add(보낼메시지아이템);

                핸들러.sendEmptyMessage(1);

                Log.i("[메시지 전송]", "전송 메시지: " + 보낼메시지);
                프린트라이터.println(보낼메시지);
                프린트라이터.flush();
                입력창.setText("");
            } catch (Exception e) {
                Log.e("[메시지 전송 오류]", e.getMessage());
            }
        }).start();
    }


    private void loadChatHistory(String 보내는유저, String 받는유저) {
        if (NetworkStatus.getConnectivityStatus(getApplicationContext()) == NetworkStatus.TYPE_NOT_CONNECTED) {
            Toast.makeText(this, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + ip + ":" + port + "/get_message.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray messagesArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < messagesArray.length(); i++) {
                            JSONObject messageJson = messagesArray.getJSONObject(i);

                            String senderEmail = messageJson.getString("sender_email");
                            String receiverEmail = messageJson.getString("receiver_email");
                            String time = messageJson.getString("time");
                            String content = messageJson.getString("contents");

                            Item_message message = senderEmail.equals(유저메일)
                                    ? new Item_message("sentMessage", time, content, senderEmail, receiverEmail)
                                    : new Item_message("receivedMessage", time, content, senderEmail, receiverEmail);

                            메시지목록.add(message);
                        }

                        메시지어댑터.notifyDataSetChanged();
                        채팅리스트뷰.setSelection(메시지목록.size() - 1);
                    } catch (JSONException e) {
                        Log.e("[채팅 기록 로드 오류]", e.getMessage());
                    }
                },
                error -> Log.e("[채팅 기록 로드 오류]", error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("sender_email", 보내는유저);
                params.put("receiver_email", 받는유저);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("hh:mm:ss").format(new Date());
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            serviceChat.받기쓰레드.set위치("");
            serviceChat.받기쓰레드.set대화중(false);
            unbindService(connection);
            isBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (프린트라이터 != null) 프린트라이터.close();
            if (버퍼리더 != null) 버퍼리더.close();
            if (소켓 != null && !소켓.isClosed()) 소켓.close();
        } catch (IOException e) {
            Log.e("[소켓 종료 오류]", e.getMessage());
        }
    }
}
