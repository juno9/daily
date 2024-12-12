package com.example.myapplication;


import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service_chat extends Service {
    Socket 소켓;
    String ip = ipclass.ip;
    String sourceurl = ipclass.url;
    int port = ipclass.chatport;
    public static PrintWriter 프린트라이터;
    public static BufferedReader 버퍼리더;
    String 로그인한유저메일;
    static Thread_receiver 받기쓰레드;
    PreferenceHelper 프리퍼런스헬퍼;
    Handler 핸들러;
    private final IBinder binder = new LocalBinder();
    boolean 작동스위치;

    public static Intent serviceIntent = null;

    public Service_chat() {
        Log.i("[Service_chat]", "생성자");
    }


    public class LocalBinder extends Binder {
        public Service_chat getService() {
            return Service_chat.this;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("[Service_chat]onStartCommand", "온 스타트커맨드");
        프리퍼런스헬퍼 = new PreferenceHelper(getApplicationContext());
        String 로그인한유저메일 = 프리퍼런스헬퍼.getUser_email();
        Log.i("[Service_chat]onStartCommand", "받은 유저 메일:" + 로그인한유저메일);


        if (!프리퍼런스헬퍼.get_service_running()) {
            new java.lang.Thread() {
                public void run() {
                    try {
                        프리퍼런스헬퍼.set_service_running(true);
                        Thread[] threads = new Thread[Thread.activeCount()];
                        Thread.enumerate(threads);
                        for (Thread t : threads) {
                            Log.i("[Service_chat]onStartCommand", "Thread name: " + t.getName() + ", Thread ID: " + t.getId());
                        }
                        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);
                        for (ActivityManager.RunningServiceInfo service : runningServices) {
                            Log.i("MyApp", "Running Service: " + service.service.getClassName());
                        }
                        InetAddress serverAddr = InetAddress.getByName(ip);
                        소켓 = new Socket(serverAddr, port);
                        Log.i("[Service_chat]onStartCommand", "소켓 생성");
                        InputStreamReader 인풋스트림 = new InputStreamReader(소켓.getInputStream());
                        Log.i("[Service_chat]onStartCommand", "인풋스트림리더 생성");
                        OutputStreamWriter 아웃풋스트림라이터 = new OutputStreamWriter(소켓.getOutputStream());
                        Log.i("[Service_chat]onStartCommand", "아웃풋스트림라이터 생성");
                        프린트라이터 = new PrintWriter(아웃풋스트림라이터);
                        Log.i("[Service_chat]onStartCommand", "프린트라이터 생성, 서비스 내의 변수에 할당");
                        버퍼리더 = new BufferedReader(인풋스트림);
                        Log.i("[Service_chat]onStartCommand", "버퍼리더 생성, 서비스 내의 변수에 할당");
                        프린트라이터.println(로그인한유저메일);
                        Log.i("[Service_chat]onStartCommand", "프린트라이터에 현재 로그인되어있는 유저의 이메일을 담음");
                        프린트라이터.flush();//소켓 연결과 함께 유저의 이메일만 보내줌
                        Log.i("[Service_chat]onStartCommand", "프린트라이터에 담긴 데이터 서버로 흘려보냄");

                        받기쓰레드 = new Thread_receiver(getApplication(), 로그인한유저메일, 버퍼리더, 프린트라이터);
                        set받기쓰레드(받기쓰레드);
                        Log.i("[Service_chat]onStartCommand", "받기 쓰레드 생성함 ID:" + 받기쓰레드.getId() + " name: " + 받기쓰레드.getName());
                        받기쓰레드.start();//접속하고 채팅 시작 하는데까지는 문제 없음

                        Log.i("[Service_chat]onStartCommand", "서버로부터 메시지 받아오는 스레드 시작");
                    } catch (
                            IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();//받기 쓰레드 시작
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        프리퍼런스헬퍼 = new PreferenceHelper(getApplicationContext());
        Log.i("[Service_chat]", "온디스트로이");
        프리퍼런스헬퍼.set_service_running(false);
        try {
            프린트라이터.println("/destroy");
            프린트라이터.flush();
            Log.i("[Service_chat]", "서버로 /destroy 메시지 보냄");
        } catch (Exception e) {

        }


        setAlarmTimer();
//        Thread.currentThread().interrupt();
        if (받기쓰레드 != null) {
            받기쓰레드.interrupt();
            Log.i("[Service_chat]", "받기 쓰레드 인터럽트 함");
            받기쓰레드 = null;
            Log.i("[Service_chat]", "서비스 내의 받시 스레드 null할당");
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("[Service_chat]", "온크리에이트");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("[Service_chat]", "온바인드");
        return binder;

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("[Service_chat]", "온언바인드");
        return super.onUnbind(intent);
    }

    public void showToast(final Application application, final String msg) {
        Log.i("[Service_chat]", "쇼 토스트");
        Handler h = new Handler(application.getMainLooper());
        h.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(application, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void setAlarmTimer() {
//        Log.i("[Service_chat]setAlarmTimer", "셋 알람타이머");
        final Calendar c = Calendar.getInstance();
//        Log.i("[Service_chat]setAlarmTimer", "캘린더생성");
        c.setTimeInMillis(System.currentTimeMillis());
        Log.i("[Service_chat]setAlarmTimer", "셋 캘린더 객체 시간 설정");
        c.add(Calendar.SECOND, 1);
        Log.i("[Service_chat]setAlarmTimer", "생성한 캘린더 객체의 시간을 1밀리초 추가함");
        Intent intent = new Intent(getApplicationContext(), Receiver_Alarm.class);
//        Log.i("[Service_chat]setAlarmTimer", "Receiver_Alarm 실행하는 인텐트 생성");
        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
//        Log.i("[Service_chat]setAlarmTimer", "펜딩 인텐트 생성");
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Log.i("[Service_chat]setAlarmTimer", "알람매니저 생성");
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
//        Log.i("[Service_chat]setAlarmTimer", "알람매니저의 시간,펜딩인텐트 설정");
    }

    public static void set받기쓰레드(Thread_receiver 받기쓰레드) {
        Service_chat.받기쓰레드 = 받기쓰레드;
    }

    public class Thread_receiver extends Thread {
        Context context;
        String 로그인한유저;
        String ip = ipclass.ip;
        int port = ipclass.chatport;
        BufferedReader 버퍼리더;
        PrintWriter 프린트라이터;
        Handler 핸들러;
        boolean 대화중 = false;
        String 대화상대;
        ArrayList<Item_message> 메시지목록;
        Date now = new Date();
        SimpleDateFormat 시간변환 = new SimpleDateFormat("hh:mm:ss");
        String 현재시간 = 시간변환.format(now);
        String 위치;
        String 메시지보낸유저;
        PreferenceHelper 프리퍼런스헬퍼;
        // Channel에 대한 id 생성
        private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
        // Channel을 생성 및 전달해 줄 수 있는 Manager 생성
        private NotificationManager mNotificationManager;

        // Notification에 대한 ID 생성
        private static final int NOTIFICATION_ID = 0;
        String 프로필이미지스트링;
        URL url2;

        Thread_receiver(Context 컨텍스트, String 유저, BufferedReader 받은버퍼리더, PrintWriter 받은프린트라이터) {
            this.context = 컨텍스트;
            this.로그인한유저 = 유저;
            this.버퍼리더 = 받은버퍼리더;
            this.프린트라이터 = 받은프린트라이터;
            this.프리퍼런스헬퍼 = new PreferenceHelper(컨텍스트);
            Log.i("[내용받기 쓰레드]프린트라이터 스트링", 프린트라이터.toString());
            Log.i("[내용받기 쓰레드]버퍼리더 스트링", 버퍼리더.toString());
            this.위치 = "";
            createNotificationChannel();
        }


        @Override
        public void run() {
            try {
                while (true) {
                    Log.i("[내용받기쓰레드]반복문 작동여부 확인용 ", "도는 중, 쓰레드 Name: " + currentThread().getName() + " 쓰레드 ID: " + currentThread().getId());
                    메시지보낸유저 = 버퍼리더.readLine();
                    if (메시지보낸유저 == null) {
                        break;

                    }


                    Log.i("[Thread_receiver] 서버로부터 받은 첫 메시지:", 메시지보낸유저);
                    프리퍼런스헬퍼.set_sender_User_email(메시지보낸유저);//이 메시지를 보낸 유저는 받아올 수 있음
                    프리퍼런스헬퍼.set_receiver_User_email(로그인한유저);//이 메시지를 받는 유저도 알 수 있음
                    String 받은텍스트 = 버퍼리더.readLine();
                    Log.i("전달받은 텍스트", 받은텍스트);
                    Log.i("위치", 위치);
                    if (받은텍스트.equals("/destroy")) {
                        this.interrupt();
                    } else {
                        if (대화중 && 위치.equals(메시지보낸유저)) {
                            Message msg2 = 핸들러.obtainMessage();
                            msg2.what = 1;
                            Item_message 메시지아이템 = new Item_message("receivedMessage", 현재시간, 받은텍스트, 대화상대, 로그인한유저);
                            메시지목록.add(메시지아이템);
                            Log.i("[내용받기쓰레드]핸들러에 보낼 메시지 객체 의 what값을 설정함", "설정함");
                            핸들러.sendMessage(msg2);
                        } else {
                            sendNotification(메시지보낸유저, 받은텍스트);

                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        public void createNotificationChannel() {
            //notification manager 생성
            mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            // 기기(device)의 SDK 버전 확인 ( SDK 26 버전 이상인지 - VERSION_CODES.O = 26)
            if (android.os.Build.VERSION.SDK_INT
                    >= android.os.Build.VERSION_CODES.O) {
                //Channel 정의 생성자( construct 이용 )
                NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID
                        , "Test Notification", mNotificationManager.IMPORTANCE_HIGH);
                //Channel에 대한 기본 설정
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setDescription("Notification from Mascot");
                // Manager을 이용하여 Channel 생성
                mNotificationManager.createNotificationChannel(notificationChannel);
            }

        }


        // Notification을 보내는 메소드
        public void sendNotification(String sender, String message) {
            get_userImage(프리퍼런스헬퍼.get_sender_User_email());

            Intent intent = new Intent(context, Activity_login.class);
            Intent intent2 = new Intent(context, Activity_user_profile.class);
            Intent intent3 = new Intent(context, Activity_message.class);
            intent2.putExtra("user_email", 메시지보낸유저);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context.getApplicationContext());

            intent3.putExtra("받는유저메일", 프리퍼런스헬퍼.get_sender_User_email());
            intent3.putExtra("이미지url스트링", 프리퍼런스헬퍼.get_imageurl());

            stackBuilder.addNextIntent(intent);
            stackBuilder.addNextIntent(intent2);
            stackBuilder.addNextIntent(intent3);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE // 플래그 추가
            );

            NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                    .setContentTitle("New Message")
                    .setContentText(sender + ": " + message)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.graycircle)
                    .setAutoCancel(true); // 알림 클릭 시 자동 닫힘 설정

            mNotificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
        }




        public void set대화중(boolean 대화중) {
            this.대화중 = 대화중;
        }

        public void set메시지목록(ArrayList<Item_message> 메시지목록) {
            this.메시지목록 = 메시지목록;
        }

        public ArrayList<Item_message> get메시지목록() {
            return 메시지목록;
        }


        public void set로그인한유저(String 로그인한유저) {
            this.로그인한유저 = 로그인한유저;
        }

        public String get로그인한유저() {
            return 로그인한유저;
        }

        public void set위치(String 위치) {
            this.위치 = 위치;
        }

        public String get위치() {
            return 위치;
        }

        public void set대화상대(String 받는유저) {
            this.대화상대 = 받는유저;
        }

        public String get받는유저() {
            return 대화상대;
        }

        public  void set핸들러(Handler 핸들러) {
            this.핸들러 = 핸들러;
        }

        public Handler get핸들러() {
            return 핸들러;
        }

        public void set버퍼리더(BufferedReader 버퍼리더) {
            this.버퍼리더 = 버퍼리더;
        }

        public void set프린트라이터(PrintWriter 프린트라이터) {
            this.프린트라이터 = 프린트라이터;
        }

        public BufferedReader get버퍼리더() {
            return 버퍼리더;
        }

        public PrintWriter get프린트라이터() {
            return 프린트라이터;
        }

        public void get_userImage(String email) {

            int status = NetworkStatus.getConnectivityStatus(context);
            //연결여부, 연결된 인터넷 경로를 받아온다(wifi, lte등)
            String 상태 = String.valueOf(status);
//        Log.i("인터넷상태", 상태);
            if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
//            Log.i("조건문 진입", "진입");

                RequestQueue queue = Volley.newRequestQueue(context);
//            Log.i("큐 생성", "큐 생성");

                String url = sourceurl + "get_userdata.php";
//            Log.i("url 생성", "유알엘생성");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
//                            Log.i("응답", response);
                                try {
                                    JSONObject 제이슨객체 = new JSONObject(response);//data:{"기록1,기록2,기록3"}

                                    String data = 제이슨객체.getString("data");

                                    JSONArray 제이슨어레이 = new JSONArray(data);

                                    int 어레이길이 = 제이슨어레이.length();


                                    for (int i = 0; i < 어레이길이; i++) {
                                        String 제이슨아이템 = 제이슨어레이.get(i).toString();//첫번째 기록 값을 스트링으로 받는다

                                        JSONObject 아이템제이슨 = new JSONObject(제이슨아이템);

                                        프로필이미지스트링 = 아이템제이슨.getString("profile_image");
//                                    Log.i("프로필이미지", 프로필이미지스트링);
                                        url2 = new URL("http://" + ip + "/images/" + 프로필이미지스트링);
//                                    Log.i("프로필이미지url", url2.toString());
                                        프리퍼런스헬퍼.set_imageurl(url2.toString());

                                    }
                                } catch (JSONException e) {

                                    e.printStackTrace();
                                } catch (MalformedURLException e) {
                                    throw new RuntimeException(e);
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String 에러내용 = String.valueOf(error);
                        Log.i("Service_chat","온에러리스폰스"+ 에러내용);
                    }
                }) {
                    // 포스트 파라미터 넣기
                    @Override
                    protected Map getParams() {
                        Map params = new HashMap();
                        params.put("user_email", email);
                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            } else {
                Toast.makeText(context, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            }


        }

    }

}