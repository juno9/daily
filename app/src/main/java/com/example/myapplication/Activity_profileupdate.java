package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_profileupdate extends AppCompatActivity {
    String ip = ipclass.ip;
    int port = ipclass.httpport;
    String sourceurl = "http://" + ip + ":" + port + "/";

    String TAG = "Activity_profileupdate";
    CircleImageView 프로필이미지;
    ImageView 카메라이미지;
    TextView 저장버튼, 이름표시, 자기소개표시;
    PreferenceHelper 프리퍼런스헬퍼;
    String 유저메일;
    Bitmap 비트맵;
    String 인코드이미지스트링;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileupdate);

        // UI 요소 초기화
        프로필이미지 = findViewById(R.id.이미지뷰_프로필이미지);
        카메라이미지 = findViewById(R.id.이미지뷰_사진아이콘);
        저장버튼 = findViewById(R.id.저장하기_텍스트);
        이름표시 = findViewById(R.id.닉네임_입력);
        자기소개표시 = findViewById(R.id.자기소개_입력);

        // 사용자 이메일 가져오기
        프리퍼런스헬퍼 = new PreferenceHelper(getApplicationContext());
        유저메일 = 프리퍼런스헬퍼.getUser_email();

        // 사용자 데이터 로드
        getuserdata();

        // 프로필 이미지 클릭 리스너
        View.OnClickListener selectImageListener = v -> openGallery();
        카메라이미지.setOnClickListener(selectImageListener);
        프로필이미지.setOnClickListener(selectImageListener);

        // 저장 버튼 클릭 리스너
        저장버튼.setOnClickListener(v -> saveUserData());
    }

    // 갤러리 열기
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    // 데이터 저장
    private void saveUserData() {
        if (NetworkStatus.getConnectivityStatus(getApplicationContext()) == NetworkStatus.TYPE_NOT_CONNECTED) {
            Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = sourceurl + "update_userprofile.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.i("응답내용", response);
                    if ("File Uploaded Successfully".equals(response)) {
                        Toast.makeText(getApplicationContext(), "프로필이 성공적으로 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                error -> Log.e("Activity_profileupdate","온에러리스폰스"+ String.valueOf(error))) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_email", 유저메일);
                params.put("user_name", 이름표시.getText().toString());
                params.put("user_selfintro", 자기소개표시.getText().toString());
                params.put("profile_image", 인코드이미지스트링 != null ? 인코드이미지스트링 : "");
                return params;
            }
        };

        queue.add(stringRequest);
    }

    // onActivityResult 개선
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri filePath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                비트맵 = BitmapFactory.decodeStream(inputStream);
                프로필이미지.setImageBitmap(비트맵);
                encodeBitmapImage(비트맵);
            } catch (Exception e) {
                Log.e(TAG, "이미지 로드 실패", e);
            }
        }
    }

    // 사용자 데이터 로드
    private void getuserdata() {
        if (NetworkStatus.getConnectivityStatus(getApplicationContext()) == NetworkStatus.TYPE_NOT_CONNECTED) {
            Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = sourceurl + "get_userdata.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.i("응답", response);
                    try {
                        JSONObject responseJson = new JSONObject(response);
                        JSONArray dataArray = responseJson.getJSONArray("data");

                        if (dataArray.length() > 0) {
                            JSONObject userData = dataArray.getJSONObject(0);
                            이름표시.setText(userData.getString("user_name"));
                            자기소개표시.setText("null".equals(userData.getString("user_selfintro")) ? "" : userData.getString("user_selfintro"));

                            // 이미지 로드
                            String profileImageUrl = sourceurl + "images/" + userData.getString("profile_image");
                            loadImageFromUrl(profileImageUrl);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON 파싱 오류", e);
                    }
                },
                error -> Log.e("Activity_profileupdate","온에러리스폰스"+ String.valueOf(error))) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_email", 유저메일);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    // 이미지 URL 로드
    private void loadImageFromUrl(String url) {
        new Thread(() -> {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                runOnUiThread(() -> 프로필이미지.setImageBitmap(bitmap));
            } catch (Exception e) {
                Log.e(TAG, "이미지 로드 실패", e);
            }
        }).start();
    }

    // 비트맵을 Base64 문자열로 변환
    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream); // 품질 80으로 설정
        byte[] bytesOfImage = byteArrayOutputStream.toByteArray();
        인코드이미지스트링 = Base64.encodeToString(bytesOfImage, Base64.DEFAULT);
    }
}
