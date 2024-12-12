package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

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

public class Activity_user_profile extends AppCompatActivity {
    private final String TAG = "[Activity_user_profile]";

    private ImageView backImageView, profileImageView;
    private TextView headerNameTextView, profileNameTextView, selfIntroTextView, yearMonthTextView;
    private Button sendMessageButton, followButton;
    private RecyclerView weekRecyclerView, recordsRecyclerView;

    private Adapter_week weekAdapter;
    private Adapter_record recordAdapter;

    private ArrayList<Item_day> dayList = new ArrayList<>();
    private ArrayList<Item_record> recordList = new ArrayList<>();

    private PreferenceHelper preferenceHelper;

    private String profileUserEmail, loggedInUserEmail, profileImageString;
    private String serverIp = ipclass.ip;
    private int serverPort = ipclass.httpport;
    private String baseUrl = "http://" + serverIp + ":" + serverPort + "/";
    private Date selectedDate = new Date();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initializeUI();
        initializeRecyclerViews();
        setupListeners();

        loggedInUserEmail = preferenceHelper.getUser_email();
        profileUserEmail = getIntent().getStringExtra("user_email");

        updateUI();
        fetchUserData(profileUserEmail);
        fetchRecordData(selectedDate);
        fetchWeekData(selectedDate);
        fetchFollowData();
    }

    private void initializeUI() {
        backImageView = findViewById(R.id.이미지뷰_뒤로가기);
        profileImageView = findViewById(R.id.이미지뷰_프로필이미지);
        headerNameTextView = findViewById(R.id.텍스트뷰_이름);
        profileNameTextView = findViewById(R.id.텍스트뷰_이름2);
        selfIntroTextView = findViewById(R.id.텍스트뷰_자기소개);
        yearMonthTextView = findViewById(R.id.텍스트뷰_년월표시);
        sendMessageButton = findViewById(R.id.버튼_메시지보내기);
        followButton = findViewById(R.id.버튼_팔로우);

        preferenceHelper = new PreferenceHelper(getApplicationContext());
    }

    private void initializeRecyclerViews() {
        weekRecyclerView = findViewById(R.id.리사이클러뷰_1주일표시);
        recordsRecyclerView = findViewById(R.id.리사이클러뷰_기록표시);

        weekAdapter = new Adapter_week(dayList);
        recordAdapter = new Adapter_record(recordList);

        weekRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recordsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        weekRecyclerView.setAdapter(weekAdapter);
        recordsRecyclerView.setAdapter(recordAdapter);

        weekAdapter.setOnItemClickListener((v, pos) -> {
            updateSelectedDate(pos);
            fetchRecordData(selectedDate);
        });
    }

    private void setupListeners() {
        backImageView.setOnClickListener(v -> finish());

        followButton.setOnClickListener(v -> {
            if (followButton.getText().toString().equals("팔로우")) {
                toggleFollow(true);
            } else {
                toggleFollow(false);
            }
        });

        sendMessageButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_message.class);
            intent.putExtra("받는유저메일", profileUserEmail);
            intent.putExtra("이미지url스트링", baseUrl + "images/" + profileImageString);
            startActivity(intent);
        });
    }

    private void updateUI() {
        String yearMonth = new SimpleDateFormat("yyyy년 MM월", Locale.getDefault()).format(selectedDate);
        yearMonthTextView.setText(yearMonth);
    }

    private void updateSelectedDate(int position) {
        for (int i = 0; i < dayList.size(); i++) {
            dayList.get(i).setChecked(i == position);
        }
        weekAdapter.notifyDataSetChanged();

        Item_day selectedDay = dayList.get(position);
        String selectedDateString = String.format("%s-%s-%s", selectedDay.getDayyear(), selectedDay.getDaymonth(), selectedDay.getDaynum());
        try {
            selectedDate = dateFormat.parse(selectedDateString);
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: " + e.getMessage());
        }
    }

    private void fetchUserData(String email) {
        if (!isNetworkConnected()) return;

        String url = baseUrl + "get_userdata.php";
        sendPostRequest(url, params -> params.put("user_email", email), response -> {
            try {
                JSONObject jsonObject = new JSONObject(response).getJSONArray("data").getJSONObject(0);

                String name = jsonObject.getString("user_name");
                profileImageString = jsonObject.getString("profile_image");
                String intro = jsonObject.getString("user_selfintro");

                headerNameTextView.setText(name);
                profileNameTextView.setText(name);
                selfIntroTextView.setText(intro.equals("null") ? "" : intro);

                Glide.with(this).load(baseUrl + "images/" + profileImageString).into(profileImageView);
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing user data: " + e.getMessage());
            }
        });
    }

    private void fetchRecordData(Date date) {
        if (!isNetworkConnected()) return;

        String url = baseUrl + "get_record.php";
        String selectedDate = dateFormat.format(date);

        sendPostRequest(url, params -> {
            params.put("user_email", profileUserEmail);
            params.put("start_date", selectedDate);
        }, response -> {
            try {
                recordList.clear();
                JSONArray jsonArray = new JSONObject(response).getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonRecord = jsonArray.getJSONObject(i);
                    recordList.add(new Item_record(
                            jsonRecord.getString("user_email"),
                            jsonRecord.getString("start_date"),
                            jsonRecord.getString("start_time"),
                            jsonRecord.getString("end_date"),
                            jsonRecord.getString("end_time"),
                            jsonRecord.getString("title"),
                            jsonRecord.getString("contents"),
                            jsonRecord.getString("record_seq")
                    ));
                }
                recordAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing record data: " + e.getMessage());
            }
        });
    }

    private void fetchWeekData(Date date) {
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        dayList.clear();
        for (int i = 0; i < 7; i++) {
            String year = String.valueOf(cal.get(Calendar.YEAR));
            String month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
            String day = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
            String dayName = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.KOREAN);

            dayList.add(new Item_day(year, month, day, dayName, false));
            cal.add(Calendar.DATE, 1);
        }
        weekAdapter.notifyDataSetChanged();
    }

    private void fetchFollowData() {
        if (!isNetworkConnected()) return;

        String url = baseUrl + "get_followdata.php";
        sendPostRequest(url, params -> {
            params.put("user_email", loggedInUserEmail);
            params.put("profile_user_email", profileUserEmail);
        }, response -> {
            followButton.setText(response.equals("following") ? "팔로잉" : "팔로우");
        });
    }

    private void toggleFollow(boolean follow) {
        if (!isNetworkConnected()) return;

        String url = baseUrl + (follow ? "follow.php" : "unfollow.php");
        sendPostRequest(url, params -> {
            params.put("user_email", loggedInUserEmail);
            params.put("profile_user_email", profileUserEmail);
        }, response -> fetchFollowData());
    }

    private void sendPostRequest(String url, VolleyParamsBuilder paramsBuilder, Response.Listener<String> listener) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, error -> {
            Log.e(TAG, "Error during request: " + error.toString());
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                paramsBuilder.buildParams(params);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private boolean isNetworkConnected() {
        int status = NetworkStatus.getConnectivityStatus(this);
        if (status == NetworkStatus.TYPE_NOT_CONNECTED) {
            Toast.makeText(this, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private interface VolleyParamsBuilder {
        void buildParams(Map<String, String> params);
    }
}
