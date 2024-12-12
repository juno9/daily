package com.example.myapplication.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.myapplication.Activity_recordinput;
import com.example.myapplication.Activity_recordupdate;
import com.example.myapplication.Adapter_record;
import com.example.myapplication.Adapter_week;
import com.example.myapplication.Item_day;
import com.example.myapplication.Item_record;
import com.example.myapplication.NetworkStatus;
import com.example.myapplication.R;
import com.example.myapplication.ipclass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class fragment_home extends Fragment {
    String ip= ipclass.ip;
    int port = ipclass.httpport;
    String sourceurl="http://"+ip+":"+port+"/";
    String TAG = "홈프래그먼트";
    ViewGroup 뷰;
    RecyclerView week_recycler;//주 날짜 표시 리사이클러뷰 선언
    Adapter_week adapter_week;
    TextView 오늘날짜표시텍스트;
    ArrayList<Item_day> 날짜목록 = new ArrayList<>();
    RecyclerView record_recycler;//기록 표시 리사이클러뷰 선언
    Adapter_record _adapterRecord;//빈거 하나 만들어 둠
    ArrayList<Item_record> 기록리스트 = new ArrayList<>();
    String user_email;//스트링으로 로그인 한 유저의 이메일 값 선언
    FloatingActionButton 기록추가버튼;
    ViewPager2 뷰페이저2;
    TextView 월표시텍스트뷰;
    DatePickerDialog 날짜선택다이얼로그;
    String 합친날짜스트링;
    SimpleDateFormat 날짜형식 = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat 날짜비교용일자만 = new SimpleDateFormat("dd");
    SimpleDateFormat 월표시형식 = new SimpleDateFormat("yyyy년 MM월");

    int 임의값 = -1;
    Date date;//오늘 날짜 기준 Date객체 생성



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        뷰 = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        week_recycler = 뷰.findViewById(R.id.weekrecyclerview);//자바 파일의 객체와 레이아웃의 뷰를 연결
        record_recycler = 뷰.findViewById(R.id.recordrecyclerview);//자바 파일의 객체와 레이아웃의 뷰를 연결
        월표시텍스트뷰 = 뷰.findViewById(R.id.년월텍스트뷰);
        기록추가버튼 = 뷰.findViewById(R.id.기록추가버튼);
        오늘날짜표시텍스트 = 뷰.findViewById(R.id.오늘날짜표시);

        Bundle bundle = getArguments();
        if (bundle != null) {
            user_email = bundle.getString("user_email");
        }//액티비티에서 전달 받은 유저의 이메일 값.

        Calendar cal = Calendar.getInstance();
        int 년 = cal.get(Calendar.YEAR);
        int 월 = cal.get(Calendar.MONTH);
        int 일 = cal.get(Calendar.DAY_OF_MONTH);

        오늘날짜표시텍스트.setText(String.valueOf(일));
        오늘날짜표시텍스트.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = new Date();//오늘날짜기준
                SimpleDateFormat 월표시형식 = new SimpleDateFormat("yyyy년 MM월");
                SimpleDateFormat 일자표시형식 = new SimpleDateFormat("dd");
                SimpleDateFormat 날짜형식 = new SimpleDateFormat("yyyy-MM-dd");
                String 일자 = 일자표시형식.format(date);
                합친날짜스트링 = 날짜형식.format(date);
                String 년도월표시 = 월표시형식.format(date);
                월표시텍스트뷰.setText(년도월표시);

                getweek(date);
                get_record(date);
                for (int i = 0; i < 7; i++) {
                    if (날짜목록.get(i).getDaynum().equals(일자)) {
                        날짜목록.get(i).setChecked(true);

                    } else {
                        날짜목록.get(i).setChecked(false);
                    }
                    adapter_week.notifyItemChanged(i);
                }
            }
        });
        Date now = new Date();//오늘 날짜 생성팅
        Log.i("처음 넣어주는 날짜", String.valueOf(now));

        String 년도월표시 = 월표시형식.format(now);
        월표시텍스트뷰.setText(년도월표시);
        월표시텍스트뷰.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                날짜선택다이얼로그.show();
            }
        });


        week_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        adapter_week = new Adapter_week(날짜목록);
        adapter_week.setOnItemClickListener(new Adapter_week.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {//날짜아이템 하나 선택하면 이 날을 기준으로 Date객체를 만들고 전역변수인 date에 연결해주면 된다.
                날짜목록.get(pos).setChecked(true);
                for (int i = 0; i < 7; i++) {
                    if (i == pos) {
                        날짜목록.get(i).setChecked(true);
                        String 년도 = 날짜목록.get(i).getDayyear();
                        String 선택한월자 = 날짜목록.get(i).getDaymonth();
                        String 선택한일자 = 날짜목록.get(i).getDaynum();
                        합친날짜스트링 = 년도 + "-" + 선택한월자 + "-" + 선택한일자;
                        SimpleDateFormat 날짜형식 = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            date = 날짜형식.parse(합친날짜스트링);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        get_record(date);
                    } else {
                        날짜목록.get(i).setChecked(false);
                    }
                    adapter_week.notifyItemChanged(i);
                }

            }
        });
        getweek(now);
        week_recycler.setAdapter(adapter_week);
        날짜선택다이얼로그 = new DatePickerDialog(getActivity(), (datePicker, 년도, 월자, 일자) -> {
            try {
                // 월 값 보정 (1월이 0으로 반환되므로 +1 필요)
                월자 += 1;

                // 선택된 날짜를 문자열로 포맷 (yyyy-MM-dd 형식)
                String 선택된날짜 = String.format(Locale.KOREA, "%04d-%02d-%02d", 년도, 월자, 일자);
                SimpleDateFormat 날짜형식 = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                Date 날짜객체 = 날짜형식.parse(선택된날짜);

                // 선택한 날짜를 기준으로 캘린더 설정
                Calendar cal2 = Calendar.getInstance(Locale.KOREA);
                cal2.setTime(날짜객체);
                cal2.setFirstDayOfWeek(Calendar.MONDAY);

                // 주차 데이터를 갱신
                for (int i = 0; i < 7; i++) {
                    cal2.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY + i);
                    String year = String.valueOf(cal2.get(Calendar.YEAR));
                    String month = String.format("%02d", cal2.get(Calendar.MONTH) + 1);
                    String day = String.format("%02d", cal2.get(Calendar.DAY_OF_MONTH));
                    String dayname = cal2.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.NARROW_FORMAT, Locale.KOREAN);

                    Item_day dayitem = new Item_day(year, month, day, dayname, day.equals(String.format("%02d", 일자)));
                    if (날짜목록.size() < 7) {
                        날짜목록.add(dayitem);
                    } else {
                        날짜목록.set(i, dayitem);
                    }
                }

                // RecyclerView 갱신
                adapter_week.notifyDataSetChanged();

                // 선택된 날짜로 기록 로드 및 UI 업데이트
                date = 날짜객체; // 전역 변수 date 갱신
                get_record(date);
                월표시텍스트뷰.setText(String.format(Locale.KOREA, "%04d년 %02d월", 년도, 월자));
            } catch (ParseException e) {
                Log.e("DatePickerDialog", "날짜 파싱 실패: " + e.getMessage());
            }
        }, 년, 월, 일);
        date = new Date();
        //로그인 하고 처음 기록을 불러올때는 오늘을 기준으로 불러온다.

        합친날짜스트링 = 날짜형식.format(date);
        String 일자 = 날짜비교용일자만.format(date);//오늘날짜 일자만
        for (int i = 0; i < 7; i++) {
            if (날짜목록.get(i).getDaynum().equals(일자)) {
                날짜목록.get(i).setChecked(true);
            } else {
                날짜목록.get(i).setChecked(false);
            }
            adapter_week.notifyItemChanged(i);
        }
        get_record(date);
        record_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        _adapterRecord = new Adapter_record(기록리스트);//여기 넣은 내용물을 확인 해봐야 한다.
        _adapterRecord.setOnItemClickListener(new Adapter_record.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Log.i("기록리사이클러뷰 온클릭리스너 작동, 포지션: ", String.valueOf(pos));
                String 시작시간 = 기록리스트.get(pos).get시작시간();
                String 시작날짜 = 기록리스트.get(pos).get시작날짜();
                String 종료시간 = 기록리스트.get(pos).get종료시간();
                String 종료날짜 = 기록리스트.get(pos).get종료날짜();
                String 내용 = 기록리스트.get(pos).get내용();
                String 제목 = 기록리스트.get(pos).get제목();
                String 키값 = 기록리스트.get(pos).get키값();
//                Log.i("시작시간 ", 시작시간);
//                Log.i("시작날짜 ", 시작날짜);
//                Log.i("종료시간", 종료시간);
//                Log.i("종료날짜", 종료날짜);
//                Log.i("내용", 내용);
//                Log.i("제목", 제목);
                //시작시간,시작날짜,종료시간,종료날짜, 내용, 제목,
                Intent intent = new Intent(getActivity(), Activity_recordupdate.class);
                intent.putExtra("시작시간", 시작시간);
                intent.putExtra("시작날짜", 시작날짜);
                intent.putExtra("종료시간", 종료시간);
                intent.putExtra("종료날짜", 종료날짜);
                intent.putExtra("내용", 내용);
                intent.putExtra("제목", 제목);
                intent.putExtra("키값", 키값);
                intent.putExtra("포지션값", pos);
                intent.putExtra("from", "보기");
                startActivity(intent);

            }
        });
        record_recycler.setAdapter(_adapterRecord);
        기록추가버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_recordinput.class);
                SimpleDateFormat 시간만=new SimpleDateFormat("kk:mm:00");
                Date 현재시간용=new Date();
                String 시간=시간만.format(현재시간용);
                합친날짜스트링 = 날짜형식.format(date);
                intent.putExtra("보낸시간",시간);
                intent.putExtra("보낸날짜", 합친날짜스트링);
                startActivity(intent);

            }
        });
        return 뷰;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("[fragment_home]생명주기", "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("[fragment_home]생명주기", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("[fragment_home]생명주기", "onDetach");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("[fragment_home]생명주기", "onViewCreated");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("[fragment_home]생명주기", "onStop");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            String 복원된날짜 = savedInstanceState.getString("selected_date");
            if (복원된날짜 != null) {
                try {
                    date = 날짜형식.parse(복원된날짜);
                    Log.i(TAG, "복원된 날짜: " + 복원된날짜);
                    getweek(date);
                    get_record(date);
                } catch (ParseException e) {
                    Log.e(TAG, "날짜 복원 실패: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("[fragment_home]생명주기", "onDestroyView");

    }

    @Override
    public void onResume() {
        super.onResume();

        get_record(date);
        Log.i("[fragment_home]생명주기", "onResume");

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (date != null) {
            outState.putString("selected_date", 날짜형식.format(date));
            Log.i(TAG, "현재 날짜 저장: " + 날짜형식.format(date));
        }
    }

    public void getweek(Date date) {
        // 매개 변수로 오늘 날짜 date를 받음 - 해당 날짜가 속한 주의 날짜들을 얻어옴
        SimpleDateFormat dayonly = new SimpleDateFormat("dd");
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY); // 한 주의 첫날을 월요일로 설정

        // 날짜 목록 초기화 및 주 날짜 생성
        for (int i = 0; i < 7; i++) {
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY + i); // 해당 주의 i번째 날로 설정
            String year = String.valueOf(cal.get(Calendar.YEAR));
            int 월자보정 = cal.get(Calendar.MONTH) + 1; // 월값을 보정 (1월이 0이므로 +1)
            String month = String.format("%02d", 월자보정); // 두 자리로 포맷
            String day = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)); // 두 자리로 포맷
            String dayname = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.NARROW_FORMAT, Locale.KOREAN);

            // Item_day 객체 생성
            Item_day dayitem = new Item_day(year, month, day, dayname, false);

            // 날짜목록 갱신
            if (날짜목록.size() < 7) {
                날짜목록.add(dayitem);
            } else {
                날짜목록.set(i, dayitem);
            }
        }

        // RecyclerView 어댑터에 변경 사항 알림
        adapter_week.notifyDataSetChanged();
    }


    public void get_record(Date 받은date객체) {
        int status = NetworkStatus.getConnectivityStatus(getActivity());
        if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            String url = sourceurl + "get_record.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onResponse(String response) {
                            // 기존 기록 리스트 초기화
                            기록리스트.clear();

                            // RecyclerView를 초기화된 상태로 갱신
                            _adapterRecord.notifyDataSetChanged();

                            if (response.equals("기록없음")) {
                                Log.i(TAG, "기록 없음");
                            } else {
                                try {
                                    JSONObject 제이슨객체 = new JSONObject(response);
                                    JSONArray 제이슨어레이 = new JSONArray(제이슨객체.getString("data"));

                                    for (int i = 0; i < 제이슨어레이.length(); i++) {
                                        JSONObject 아이템제이슨 = 제이슨어레이.getJSONObject(i);

                                        Item_record 기록아이템 = new Item_record(
                                                아이템제이슨.getString("user_email"),
                                                아이템제이슨.getString("start_date"),
                                                아이템제이슨.getString("start_time"),
                                                아이템제이슨.getString("end_date"),
                                                아이템제이슨.getString("end_time"),
                                                아이템제이슨.getString("title"),
                                                아이템제이슨.getString("contents"),
                                                아이템제이슨.getString("record_id")
                                        );

                                        기록리스트.add(기록아이템);
                                    }

                                    // 새로운 데이터를 RecyclerView에 반영
                                    _adapterRecord.setarraylist(기록리스트);
                                    _adapterRecord.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    Log.e(TAG, "JSON 파싱 에러: " + e.getMessage());
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Volley Error: " + error.getMessage());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    SimpleDateFormat DB저장날짜형식 = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                    params.put("user_email", user_email);
                    params.put("start_date", DB저장날짜형식.format(받은date객체));
                    return params;
                }
            };

            queue.add(stringRequest);
        } else {
            Log.w(TAG, "네트워크 연결 상태가 비정상적입니다.");
        }
    }




}
