package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Activity_find_user extends AppCompatActivity {

    private static final String TAG = "Activity_find_user";

    private ArrayList<Item_user> userList = new ArrayList<>();
    private Adapter_search userSearchAdapter;
    private EditText searchInput;
    private ListView searchResultListView;
    private ProgressBar progressBar;

    private String ip = ipclass.ip;
    private int port = ipclass.httpport;
    private String sourceUrl = "http://" + ip + ":" + port + "/";
    private final ExecutorService executorService = Executors.newFixedThreadPool(4); // 비동기 이미지 로딩
    private RequestQueue requestQueue; // Volley 요청 큐 전역 변수로 유지

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        initializeViews();
        setupListeners();
    }

    /**
     * Initialize views and set initial states.
     */
    private void initializeViews() {
        searchInput = findViewById(R.id.입력란_검색어);
        searchResultListView = findViewById(R.id.검색결과리스트뷰);
        progressBar = findViewById(R.id.프로그레스바_검색);

        findViewById(R.id.뒤로가기).setOnClickListener(view -> finish());

        userSearchAdapter = new Adapter_search(userList, this);
        searchResultListView.setAdapter(userSearchAdapter);

        // Prevent keyboard from resizing layout
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        // Initialize RequestQueue
        requestQueue = Volley.newRequestQueue(this);
    }

    /**
     * Set up event listeners.
     */
    private void setupListeners() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                Log.d(TAG, "Before text changed: " + charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                Log.d(TAG, "On text changed: " + charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString().trim();
                if (query.isEmpty()) {
                    clearSearchResults();
                } else {
                    fetchSearchResults(query);
                }
            }
        });

        searchResultListView.setOnItemClickListener((adapterView, view, position, id) -> {
            Item_user selectedUser = userList.get(position);
            Intent intent = new Intent(getApplicationContext(), Activity_user_profile.class);
            intent.putExtra("user_email", selectedUser.get이메일());
            startActivity(intent);
        });
    }

    /**
     * Clear search results and update the list view.
     */
    private void clearSearchResults() {
        userList.clear();
        userSearchAdapter.notifyDataSetChanged();
        Log.d(TAG, "Search results cleared");
    }

    /**
     * Fetch search results from the server.
     */
    private void fetchSearchResults(String query) {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        String url = sourceUrl + "search_user.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                this::handleSearchResponse,
                this::handleSearchError) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("search_word", query);
                Log.d(TAG, "Search query sent: " + query);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    /**
     * Handle the server response for search results.
     */
    private void handleSearchResponse(String response) {
        progressBar.setVisibility(View.INVISIBLE);
        Log.d(TAG, "Server response: " + response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            userList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userObject = jsonArray.getJSONObject(i);
                String userEmail = userObject.getString("user_email");
                String userName = userObject.getString("user_name");
                String profileImageString = userObject.getString("profile_image");

                executorService.execute(() -> {
                    Bitmap profileImage = fetchImageFromUrl(sourceUrl + "images/" + profileImageString);
                    if (profileImage != null) {
                        runOnUiThread(() -> {
                            userList.add(new Item_user(profileImage, userName, userEmail, null));
                            userSearchAdapter.notifyDataSetChanged();
                        });
                    }
                });
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response", e);
        }
    }

    /**
     * Handle search errors.
     */
    private void handleSearchError(VolleyError error) {
        progressBar.setVisibility(View.INVISIBLE);
        Log.e(TAG, "Error fetching search results", error);
        Toast.makeText(this, "검색에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Fetch an image from a URL.
     */
    private Bitmap fetchImageFromUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            Log.e(TAG, "Error fetching image: " + urlString, e);
            return null;
        }
    }

    /**
     * Check if network is available.
     */
    private boolean isNetworkAvailable() {
        int status = NetworkStatus.getConnectivityStatus(this);
        boolean isAvailable = status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI;
        Log.d(TAG, "Network available: " + isAvailable);
        return isAvailable;
    }
}
