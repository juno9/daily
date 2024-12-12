package com.example.myapplication.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Activity_find_user;
import com.example.myapplication.Activity_user_profile;
import com.example.myapplication.Adapter_search;
import com.example.myapplication.Item_user;
import com.example.myapplication.NetworkStatus;
import com.example.myapplication.PreferenceHelper;
import com.example.myapplication.R;
import com.example.myapplication.ipclass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class fragment_friends extends Fragment {
String ip= ipclass.ip;
int port = ipclass.httpport;
    String sourceurl="http://"+ip+":"+port+"/";
    private View view;
    ImageView 검색버튼;
    private String TAG = "[fragment_friends]";
    ListView 유저리스트뷰;
    ArrayList<Item_user> 유저목록=new ArrayList<>();
    Adapter_search 유저목록어댑터;
    PreferenceHelper 프리퍼런스헬퍼;
   Bitmap 비트맵이미지;

    @Override
    public void onResume() {
        super.onResume();
        get_following();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_friends, container, false);
        검색버튼 = view.findViewById(R.id.검색버튼);
        유저리스트뷰 = view.findViewById(R.id.유저표시리스트뷰);
        검색버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), Activity_find_user.class);
                startActivity(intent);

            }
        });
        프리퍼런스헬퍼 = new PreferenceHelper(getActivity());
        유저목록어댑터 = new Adapter_search(유저목록, getActivity());
        유저리스트뷰.setAdapter(유저목록어댑터);
        유저리스트뷰.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(i + "번째 아이템 선택됨", String.valueOf(i));

                Intent intent = new Intent(getActivity(), Activity_user_profile.class);
                String user_email=유저목록.get(i).get이메일();
                intent.putExtra("user_email",user_email);
                startActivity(intent);
            }
        });


        return view;
    }

    public void get_following() {

            Log.i("입력된 값이 있다", "있다면");
            유저목록.clear();
            Log.i("유저목록 클리어 돌아감", 유저목록.toString());


            Log.i("검색어입력", "입력됨");
            int status = NetworkStatus.getConnectivityStatus(getActivity());
            //연결여부, 연결된 인터넷 경로를 받아온다(wifi, lte등)
            String 상태 = String.valueOf(status);
            Log.i("인터넷상태", 상태);
            if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
                Log.i("조건문 진입", "진입");

//                    프로그레스바.setVisibility(View.VISIBLE);
//                    Log.i("프로그래스바 돌아가기시작", "시작");
                RequestQueue queue = Volley.newRequestQueue(getActivity());
//                Log.i("큐 생성", "큐 생성");
//
                        String url = sourceurl+"get_followingdata.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
//                                    프로그레스바.setVisibility(View.INVISIBLE);
//                                    Log.i("프로그래스바 멈춤", "멈춤");
//                                Log.i("응답", response);
                                if (response.equals("기록없음")) {
                                    유저목록.clear();
                                    유저목록어댑터.setarraylist(유저목록);
                                    유저목록어댑터.notifyDataSetChanged();
                                } else{
                                    try {
                                        JSONObject 제이슨객체 = new JSONObject(response);//data:{"기록1,기록2,기록3"}
//                                        Log.i("제이슨객체", 제이슨객체.toString());
                                        String data = 제이슨객체.getString("data");
//                                        Log.i("제이슨 객체 내 data", data);
                                        JSONArray 제이슨어레이 = new JSONArray(data);
//                                        Log.i("제이슨어레이", 제이슨어레이.toString());
                                        int 어레이길이 = 제이슨어레이.length();
//                                        Log.i("제이슨어레이 길이", String.valueOf(어레이길이));

                                        for (int i = 0; i < 어레이길이; i++) {
                                            String 제이슨아이템 = 제이슨어레이.get(i).toString();//첫번째 기록 값을 스트링으로 받는다
//                                            Log.i("제이슨어레이 아이템", 제이슨아이템);
                                            JSONObject 아이템제이슨 = new JSONObject(제이슨아이템);
//                                            Log.i("제이슨 아이템" + (i + 1) + "번째: ", 제이슨아이템);
                                            String 유저메일 = 아이템제이슨.getString("user_email");
//                                            Log.i("유저메일", 유저메일);
                                            String 유저이름 = 아이템제이슨.getString("user_name");
//                                            Log.i("유저이름", 유저이름);
                                            String 프로필이미지스트링 = 아이템제이슨.getString("profile_image");
//                                            Log.i("프로필이미지", 프로필이미지스트링);

                                            Thread uThread = new Thread() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        //서버에 올려둔 이미지 URL
                                                        URL url2 = new URL(sourceurl+"images/" + 프로필이미지스트링);
                                                        HttpURLConnection conn = (HttpURLConnection) url2.openConnection();
                                                        conn.setDoInput(true); //Server 통신에서 입력 가능한 상태로 만듦
                                                        conn.connect(); //연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)
                                                        InputStream is = conn.getInputStream(); //inputStream 값 가져오기
                                                        비트맵이미지 = BitmapFactory.decodeStream(is); // Bitmap으로 반환
//                                                    프로필이미지.setImageBitmap(비트맵이미지);
                                                    } catch (MalformedURLException e) {
                                                        e.printStackTrace();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };
                                            uThread.start(); // 작업 Thread 실행
                                            try {
                                                //메인 Thread는 별도의 작업을 완료할 때까지 대기한다!
                                                //join() 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다림
                                                //join() 메서드는 InterruptedException을 발생시킨다.
                                                uThread.join();
                                                //작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
                                                //UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지 지정
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }


                                            Item_user 유저데이터 = new Item_user(비트맵이미지, 유저이름, 유저메일, null);
                                            //기록 아이템은 만들어 줌
                                            유저목록.add(i, 유저데이터);
                                        }//제이슨 파싱하는 반복문
                                        유저목록어댑터.setarraylist(유저목록);
                                        유저목록어댑터.notifyDataSetChanged();
                                    } catch (JSONException e) {

                                        e.printStackTrace();
                                    }

                            }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String 에러내용 = String.valueOf(error);
                        Log.i("feagment_friends","온에러리스폰스"+ 에러내용);
                    }
                }) {
                    // 포스트 파라미터 넣기
                    @Override
                    protected Map getParams() {
                        Map params = new HashMap();
                        params.put("user_email", 프리퍼런스헬퍼.getUser_email());
                        Log.i("user_email", 프리퍼런스헬퍼.getUser_email());
                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            } else {
                Toast.makeText(getActivity(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            }

    }


}


