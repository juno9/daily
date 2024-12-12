package com.example.myapplication;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Activity_recordinput extends AppCompatActivity {
    EditText 제목입력;
    String ip = ipclass.ip;
    int port = ipclass.httpport;
    String sourceurl="http://"+ip+":"+port+"/";
    EditText 내용입력;
    TextView 시작날짜입력;
    TextView 시작시간입력;
    TextView 종료날짜입력;
    TextView 종료시간입력;
    Button 취소버튼;
    Button 저장버튼;
    PreferenceHelper 프리퍼런스헬퍼;
    DatePickerDialog 날짜선택다이얼로그;
    Dialog 시간선택다이얼로그;
    Date 선택된시작날짜시간date;
    Date 선택된종료날짜시간date;
    Date 오늘날짜시간;
    Date 시작날짜시간;
    Date 종료날짜시간;
    Date 시작일;
    Date 종료일;

    String url;

    String 시작Date객체용스트링;
    String 종료Date객체용스트링;
    SimpleDateFormat 날짜시간변환용포맷 = new SimpleDateFormat("yyyy년 MM월 dd일 (EE) a kk시 mm분");
    SimpleDateFormat date에서스트링변환용 = new SimpleDateFormat("yyyy년 MM월 dd일 (EE)");
    //클래스 내에서 쓰일 데이터들의 타입을 지정하고 메모리를 확보.
    SimpleDateFormat 선택된날짜date변환용 = new SimpleDateFormat("yyyy-MM-dd");//=DB에 넣을 때 써도 됨
    SimpleDateFormat DB넣을시간형식 = new SimpleDateFormat("kk:mm:00");
    SimpleDateFormat 표시날짜형식 = new SimpleDateFormat("yyyy년 MM월 dd일 (EE)");//액티비티 첫 접근시 표시해 줄 날짜형식
    SimpleDateFormat 표시시간형식 = new SimpleDateFormat("a KK시 mm분");//액티비티 첫 접근시 표시해 줄 시간 형식

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordinput);
        제목입력 = findViewById(R.id.입력란_제목);
        시작날짜입력 = findViewById(R.id.기록입력란_시작날짜);
        종료날짜입력 = findViewById(R.id.기록입력란_종료날짜);
        시작시간입력 = findViewById(R.id.기록입력란_시작시간);
        종료시간입력 = findViewById(R.id.기록입력란_종료시간);
        내용입력 = findViewById(R.id.입력란_내용);
        취소버튼 = findViewById(R.id.취소버튼);
        저장버튼 = findViewById(R.id.저장버튼);
        저장버튼.setText("저장");
        취소버튼.setText("취소");
        프리퍼런스헬퍼 = new PreferenceHelper(this);
        Intent intent = getIntent();
        String 받은날짜스트링 = intent.getStringExtra("보낸날짜");
        Log.i("받은날짜 스트링", 받은날짜스트링);//홈 프래그먼트에서 날짜 받아옴
        String 받은시간스트링 = intent.getStringExtra("보낸시간");
        Log.i("받은시간 스트링", 받은시간스트링);//홈 프래그먼트에서 시간 받아옴
        SimpleDateFormat 받은날짜스트링데이트변환용포맷 = new SimpleDateFormat("yyyy-MM-ddkk:mm:00");
        try {
            String 받은날짜시간스트링 = 받은날짜스트링 + 받은시간스트링;
            Log.i("받은날짜시간 스트링","받은날짜시간스트링");
            시작일 = 받은날짜스트링데이트변환용포맷.parse(받은날짜시간스트링);
            종료일 = 받은날짜스트링데이트변환용포맷.parse(받은날짜시간스트링);
            시작날짜시간 = 받은날짜스트링데이트변환용포맷.parse(받은날짜시간스트링);
            종료날짜시간 = 받은날짜스트링데이트변환용포맷.parse(받은날짜시간스트링);
            Log.i("시작일날짜시간", String.valueOf(시작날짜시간));
            Log.i("종료일날짜시간", String.valueOf(종료날짜시간));
        } catch (ParseException e) {
            e.printStackTrace();
        }//받아온 날짜와 시간을 데이트포맷을 바꿔서 현 시점 기준의 Date 객체를 만들어 줌


        String 날짜스트링 = 표시날짜형식.format(시작날짜시간);//현 시점을 기준으로 표시해줄 형식에 따라 년도, 월, 일을 만들어 줌.
        String 시간스트링 = 표시시간형식.format(시작날짜시간);//현 시점을 기준으로 표시해줄 형식에 따라 시, 분, 오전오후 표시해줌.
        시작날짜입력.setText(날짜스트링);
        시작날짜입력.setOnClickListener(new View.OnClickListener() {//이 기록이 언제부터 시작하는지를 선택
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int 년 = cal.get(Calendar.YEAR);
                int 월 = cal.get(Calendar.MONTH);
                int 일 = cal.get(Calendar.DAY_OF_MONTH);

                날짜선택다이얼로그 = new DatePickerDialog(Activity_recordinput.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int 년도, int 월자, int 일자) {
                        월자 = (월자 + 1);
                        //여기서 받은 년도와 날짜를 반영하여 Date객체를 만들자
                        String 선택된날짜스트링값 = 년도 + "-" + 월자 + "-" + 일자;

                        try {
                            Date 받은Date = 선택된날짜date변환용.parse(선택된날짜스트링값);
                            Date 종료날짜 = 표시날짜형식.parse(종료날짜입력.getText().toString());


                            String 받은date스트링변환 = date에서스트링변환용.format(받은Date);
                            if (받은Date.after(종료날짜)) {//만약 선택한 시작 날짜가 종료날짜보다 이후라면
                                시작날짜입력.setText(받은date스트링변환);
                                종료날짜입력.setText(받은date스트링변환);
                            } else {
                                시작날짜입력.setText(받은date스트링변환);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, 년, 월, 일);
                날짜선택다이얼로그.show();
            }
        });//정상작동
        시작시간입력.setText(시간스트링);
        시작시간입력.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int 시 = cal.get(Calendar.HOUR_OF_DAY);

                int 분 = cal.get(Calendar.MINUTE);


                시간선택다이얼로그 = new TimePickerDialog(Activity_recordinput.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int 시간, int 분표시) {

                        //시간 비교용 포멧 선언
                        //날짜까지 있어야 같이 바뀐다
                        String 시작날짜 = (String) 시작날짜입력.getText();
                        String 종료날짜 = (String) 종료날짜입력.getText();
                        String 종료시간 = (String) 종료시간입력.getText();//기존에 입력되어 있던 종료시간을 스트링으로 받아옴
                        String 텍스트뷰용시간;
                        int 넣을시간;
                        String 선택한시작시간표시스트링 = String.valueOf(시간);
                        String 선택한시작분표시스트링 = String.valueOf(분표시);


                        if (분표시 < 10) {
                            선택한시작분표시스트링 = "0" + 분표시;
                        }


                        if (시간 > 11)//12시부터 23시까지 12개
                        {
                            넣을시간 = 시간 - 12;//12시 넘으면 시간에서 12를 뺸다=13시는 1시가 되게끔
                            선택한시작시간표시스트링 = String.valueOf(넣을시간);
                            if (넣을시간 < 10) {//변환한 시간인 넣을시간이 10보다 작으면?
                                선택한시작시간표시스트링 = "0" + String.valueOf(넣을시간);//앞에 0을 붙여준다
                            }//


                            if (시간 == 12) {//12시 일때는 12시 그대로 표현해준다
                                넣을시간 = 시간;
                                선택한시작시간표시스트링 = String.valueOf(넣을시간);
                            }


                            Log.i("시작날짜", 시작날짜);
                            Log.i("시작날짜", 종료날짜);
                            Log.i("선택한 시간", 선택한시작시간표시스트링);
                            Log.i("선택한 분", 선택한시작분표시스트링);
                            시작Date객체용스트링 = 시작날짜 + " PM " + String.valueOf(선택한시작시간표시스트링) + "시 " + String.valueOf(선택한시작분표시스트링) + "분";
                            종료Date객체용스트링 = 종료날짜 + " " + 종료시간;
                            Log.i("시작Date객체용스트링", 시작Date객체용스트링);
                            Log.i("종료Date객체용스트링", 종료Date객체용스트링);

                            텍스트뷰용시간 = "PM " + 선택한시작시간표시스트링 + "시 " + 선택한시작분표시스트링 + "분";
                            시작시간입력.setText(텍스트뷰용시간);
                        } else {//0~11시 사이로 선택하면
                            넣을시간 = 시간;
                            if (넣을시간 < 10) {//변환한 시간인 넣을시간이 10보다 작으면?
                                선택한시작시간표시스트링 = "0" + String.valueOf(넣을시간);//앞에 0을 붙여준다
                            }//
                            Log.i("시작날짜", 시작날짜);
                            Log.i("시작날짜", 종료날짜);
                            Log.i("선택한 시간", 선택한시작시간표시스트링);
                            Log.i("선택한 분", 선택한시작분표시스트링);
                            시작Date객체용스트링 = 시작날짜 + " AM " + String.valueOf(선택한시작시간표시스트링) + "시 " + String.valueOf(선택한시작분표시스트링) + "분";
                            종료Date객체용스트링 = 종료날짜 + " " + 종료시간;
                            Log.i("Date객체용스트링", 시작Date객체용스트링);
                            Log.i("종료Date객체용스트링", 종료Date객체용스트링);
                            텍스트뷰용시간 = "AM " + 선택한시작시간표시스트링 + "시 " + 선택한시작분표시스트링 + "분";
                            시작시간입력.setText(텍스트뷰용시간);

                        }//0시부터 11시까지 12개
                        //13시 1분도 01시 01분으로 나오게 만들었음


                        try {
                            시작날짜시간 = 날짜시간변환용포맷.parse(시작Date객체용스트링);
                            Log.i("비교용 시작Date객체", String.valueOf(시작날짜시간));
                            종료날짜시간 = 날짜시간변환용포맷.parse(종료Date객체용스트링);
                            Log.i("비교용 종료Date객체", String.valueOf(종료날짜시간));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }//시작 date 객체, 종료 date 객체 만들어 짐


                        try {
                            시작일 = date에서스트링변환용.parse(시작날짜);
                            종료일 = date에서스트링변환용.parse(종료날짜);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }//날짜 비교용 시작날짜 date객체, 종료날짜 date객체


                        if (종료일.after(시작일)) {//시작날짜시간이 종료날짜시간보다 이후라면 종료시간과 시작시간 다 바꿔줘야 한다

                            Log.i("종료일이 시작일 이후", "");//종료일이 시작일보다 이후면 여기서 만지는 시작일 시간이 어떻게 되든 상관없다.

                            시작시간입력.setText(텍스트뷰용시간);


                        } else {//시작날짜시간이 종료날짜시간보다 이전 이라면 시작시간 만 바꿔주면 된다
                            Log.i("종료일이 시작일과 같음", "");//종료일이 시작일 이전인거는 불가능하다.

//종료일과 시작일이 같다면 여기서 만지는 시작시간이 종료시간보다 이후일 경우와 이전일 경우를 비교해야 한다.
                            if (시작날짜시간.after(종료날짜시간)) {//시작시간이 종료시간 이후라면
                                시작시간입력.setText(텍스트뷰용시간);
                                종료시간입력.setText(텍스트뷰용시간);
                            } else {
                                시작시간입력.setText(텍스트뷰용시간);
                            }

                        }
                    }
                }, 시, 분, false);

                시간선택다이얼로그.show();
            }
        });

        종료날짜입력.setText(날짜스트링);
        종료날짜입력.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int 년 = cal.get(Calendar.YEAR);
                int 월 = cal.get(Calendar.MONTH);
                int 일 = cal.get(Calendar.DAY_OF_MONTH);

                날짜선택다이얼로그 = new DatePickerDialog(Activity_recordinput.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int 년도, int 월자, int 일자) {
                        월자 = (월자 + 1);
                        //여기서 받은 년도와 날짜를 반영하여 Date객체를 만들자
                        String 선택된날짜스트링값 = 년도 + "-" + 월자 + "-" + 일자;
                        SimpleDateFormat 선택된날짜date변환용 = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date 받은Date = 선택된날짜date변환용.parse(선택된날짜스트링값);//데이트 픽커에서 선택한 종료날짜를 Date객체로 만들어 줌
                            Date 시작날짜 = 표시날짜형식.parse(시작날짜입력.getText().toString());//기존에 입력되어있던 시작날짜를 Date객체로 만들어줌


                            SimpleDateFormat date에서스트링변환용 = new SimpleDateFormat("yyyy년 MM월 dd일 (EE)");
                            String 받은date스트링변환 = date에서스트링변환용.format(받은Date);//데이트픽커에서 선택한 종료날짜를 스트링으로 만들어 줌
                            if (받은Date.after(시작날짜)) {//만약 선택한 종료 날짜가 시작날짜 이후라면
                                종료날짜입력.setText(받은date스트링변환);//종료날짜만 바꿔준다
                            } else {//만약 선택한 종료 날짜가 시작날짜 이전이라면?
                                시작날짜입력.setText(받은date스트링변환);
                                종료날짜입력.setText(받은date스트링변환);
                                //시작날짜 종료날짜 같이 바꿔준다
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, 년, 월, 일);
                날짜선택다이얼로그.show();
            }
        });
        종료시간입력.setText(시간스트링);
        종료시간입력.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int 시 = cal.get(Calendar.HOUR_OF_DAY);

                int 분 = cal.get(Calendar.MINUTE);


                시간선택다이얼로그 = new TimePickerDialog(Activity_recordinput.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int 시간, int 분표시) {

                        //시간 비교용 포멧 선언
                        //날짜까지 있어야 같이 바뀐다
                        String 시작날짜 = (String) 시작날짜입력.getText();
                        String 종료날짜 = (String) 종료날짜입력.getText();
                        String 시작시간 = (String) 시작시간입력.getText();//기존에 입력되어 있던 종료시간을 스트링으로 받아옴
                        String 텍스트뷰용시간;
                        int 넣을시간;
                        String 선택한종료시간표시스트링 = String.valueOf(시간);
                        String 선택한종료분표시스트링 = String.valueOf(분표시);


                        if (분표시 < 10) {
                            선택한종료분표시스트링 = "0" + 분표시;
                        }


                        if (시간 > 11)//12시부터 23시까지 12개
                        {
                            넣을시간 = 시간 - 12;//12시 넘으면 시간에서 12를 뺸다=13시는 1시가 되게끔
                            선택한종료시간표시스트링 = String.valueOf(넣을시간);
                            if (넣을시간 < 10) {//변환한 시간인 넣을시간이 10보다 작으면?
                                선택한종료시간표시스트링 = "0" + String.valueOf(넣을시간);//앞에 0을 붙여준다
                            }//


                            if (시간 == 12) {//12시 일때는 12시 그대로 표현해준다
                                넣을시간 = 시간;
                                선택한종료시간표시스트링 = String.valueOf(넣을시간);
                            }


                            Log.i("시작날짜", 시작날짜);
                            Log.i("시작날짜", 종료날짜);
                            Log.i("선택한 시간", 선택한종료시간표시스트링);
                            Log.i("선택한 분", 선택한종료분표시스트링);
                            종료Date객체용스트링 = 종료날짜 + " PM " + String.valueOf(선택한종료시간표시스트링) + "시 " + String.valueOf(선택한종료분표시스트링) + "분";
                            시작Date객체용스트링 = 시작날짜 + " " + 시작시간;
                            Log.i("시작Date객체용스트링", 시작Date객체용스트링);
                            Log.i("종료Date객체용스트링", 종료Date객체용스트링);

                            텍스트뷰용시간 = "PM " + 선택한종료시간표시스트링 + "시 " + 선택한종료분표시스트링 + "분";
                            종료시간입력.setText(텍스트뷰용시간);
                        } else {//0~11시 사이로 선택하면
                            넣을시간 = 시간;
                            if (넣을시간 < 10) {//변환한 시간인 넣을시간이 10보다 작으면?
                                선택한종료시간표시스트링 = "0" + String.valueOf(넣을시간);//앞에 0을 붙여준다
                            }//
                            Log.i("시작날짜", 시작날짜);
                            Log.i("시작날짜", 종료날짜);
                            Log.i("선택한 시간", 선택한종료시간표시스트링);
                            Log.i("선택한 분", 선택한종료분표시스트링);
                            종료Date객체용스트링 = 시작날짜 + " AM " + String.valueOf(선택한종료시간표시스트링) + "시 " + String.valueOf(선택한종료분표시스트링) + "분";
                            시작Date객체용스트링 = 시작날짜 + " " + 시작시간;
                            Log.i("Date객체용스트링", 시작Date객체용스트링);
                            Log.i("종료Date객체용스트링", 종료Date객체용스트링);
                            텍스트뷰용시간 = "AM " + 선택한종료시간표시스트링 + "시 " + 선택한종료분표시스트링 + "분";
                            종료시간입력.setText(텍스트뷰용시간);

                        }//0시부터 11시까지 12개
                        //13시 1분도 01시 01분으로 나오게 만들었음


                        try {
                            시작날짜시간 = 날짜시간변환용포맷.parse(시작Date객체용스트링);
                            Log.i("비교용 시작Date객체", String.valueOf(시작날짜시간));
                            종료날짜시간 = 날짜시간변환용포맷.parse(종료Date객체용스트링);
                            Log.i("비교용 종료Date객체", String.valueOf(종료날짜시간));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }//시작 date 객체, 종료 date 객체 만들어 짐


                        try {
                            시작일 = date에서스트링변환용.parse(시작날짜);
                            종료일 = date에서스트링변환용.parse(종료날짜);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }//날짜 비교용 시작날짜 date객체, 종료날짜 date객체


                        if (종료일.after(시작일)) {//시작날짜시간이 종료날짜시간보다 이후라면 종료시간과 시작시간 다 바꿔줘야 한다

                            Log.i("종료일이 시작일 이후", "");//종료일이 시작일보다 이후면 여기서 만지는 시작일 시간이 어떻게 되든 상관없다.

                            종료시간입력.setText(텍스트뷰용시간);


                        } else {//시작날짜시간이 종료날짜시간보다 이전 이라면 시작시간 만 바꿔주면 된다
                            Log.i("종료일이 시작일과 같음", "");//종료일이 시작일 이전인거는 불가능하다.

//종료일과 시작일이 같다면 여기서 만지는 시작시간이 종료시간보다 이후일 경우와 이전일 경우를 비교해야 한다.
                            if (종료날짜시간.after(시작날짜시간)) {//시작시간이 종료시간 이후라면
                                Log.i("종료날짜시간이 시작날짜시간일 이후", "");
                                종료시간입력.setText(텍스트뷰용시간);
                                Log.i("종료시간에 들어간 값", 종료시간입력.getText().toString());

                            } else {


                                Log.i("종료날짜시간이 시작날짜시간 이전,같음", "");
                                시작시간입력.setText(텍스트뷰용시간);
                                Log.i("시작시간에 들어간 값", 시작시간입력.getText().toString());
                                종료시간입력.setText(텍스트뷰용시간);
                                Log.i("종료시간에 들어간 값", 종료시간입력.getText().toString());

                            }

                        }
                    }
                }, 시, 분, false);

                시간선택다이얼로그.show();
            }
        });

        저장버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {// Date 객체는 여기서 만들어 줘야 겠네

                    Log.i("시작일", 시작일.toString());
                    Log.i("종료일", 종료일.toString());
                    Log.i("시작날짜시간", 시작날짜시간.toString());
                    Log.i("종료날짜시간", 종료날짜시간.toString());
                    String 보낼시작날짜 = 선택된날짜date변환용.format(시작날짜시간);
                    String 보낼종료날짜 = 선택된날짜date변환용.format(종료날짜시간);
                    String 보낼시작시간 = DB넣을시간형식.format(시작날짜시간);
                    String 보낼종료시간 = DB넣을시간형식.format(종료날짜시간);
                    Log.i("보낼시작날짜", 보낼시작날짜);
                    Log.i("보낼종료날짜", 보낼종료날짜);
                    Log.i("보낼시작시간", 보낼시작시간);
                    Log.i("보낼종료시간", 보낼종료시간);
                    int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                    //연결여부, 연결된 인터넷 경로를 받아온다(wifi, lte등)
                    String 상태 = String.valueOf(status);
                    Log.i("인터넷상태", 상태);
                    if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
                        Log.i("조건문 진입", "진입");
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        Log.i("큐 생성", "큐 생성");
                        String url = sourceurl + "put_record.php";
                        Log.i("url 생성", "유알엘생성");
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        Log.i("응답", response);
                                        if (response.equals("1")) {
                                            Toast.makeText(getApplicationContext(), "기록이 저장되었습니다", Toast.LENGTH_LONG).show();
                                            finish();
                                        } else if (response.equals("2")) {
                                            Toast.makeText(getApplicationContext(), "기록을 저장하지 못했습니다", Toast.LENGTH_LONG).show();

                                        } else if (response.equals("3")) {
                                            Toast.makeText(getApplicationContext(), "수정이 완료되었습니다", Toast.LENGTH_LONG).show();
                                            finish();
                                        } else if (response.equals("4")) {
                                            Toast.makeText(getApplicationContext(), "기록을 수정하는중에 오류가 발생하였습니다", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                String 에러내용 = String.valueOf(error);
                                Log.i("온에러리스폰스", 에러내용);
                            }
                        }) {
                            // 포스트 파라미터 넣기
                            @Override
                            protected Map getParams() {
                                Map params = new HashMap();

                                params.put("user_email", 프리퍼런스헬퍼.getUser_email());
                                Log.i("user_email", 프리퍼런스헬퍼.getUser_email());
                                params.put("start_date", 보낼시작날짜);//시작날짜-데이트피커에서 선택하고 선택된 날짜를 날짜 텍스트뷰에 설정하면 그 값을 스트링으로 받아와 넣어준다.
                                Log.i("start_date", 보낼시작날짜);
                                params.put("start_time", 보낼시작시간);
                                Log.i("start_time", 보낼시작시간);
                                params.put("end_date", 보낼종료날짜);//시작시간-타임피커에서 시간을 선택된 시간을 텍스트뷰에 설정하면 그 값을 스트링으로 받아와 넣어준다.
                                Log.i("end_date", 보낼종료날짜);
                                params.put("end_time", 보낼종료시간);
                                Log.i("end_time", 보낼종료시간);
                                params.put("title", 제목입력.getText().toString());
                                Log.i("보내는 title", 제목입력.getText().toString());
                                params.put("contents", 내용입력.getText().toString());
                                Log.i("보내는 contents", 제목입력.getText().toString());
                                params.put("focus", "2");


                                return params;
                            }
                        };
                        // Add the request to the RequestQueue.
                        queue.add(stringRequest);
                    } else {
                        Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        취소버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}