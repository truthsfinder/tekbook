package com.tb.sell.tekbook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Map;


public class UserProfileActivity  extends AppCompatActivity {
    public static final String DATA_URL = "http://maroon-and-gold.000webhostapp.com/tekbook/get_user_books.php?user_id=";
    public static final String REPUTATION_VOTES = "http://maroon-and-gold.000webhostapp.com/tekbook/db_get_all_reputation_votes.php?voter_id=";
    public static final String VOTE = "http://maroon-and-gold.000webhostapp.com/tekbook/db_update_user_when_voted.php";

    public static final String JSON_ARRAY = "result";
    public static final String KEY_REPUTATION_VOTES_ID = "reputation_votes_id";
    public static final String KEY_VOTER_ID = "voter_id";
    public static final String KEY_VOTED_ID = "voted_id";
    public static final String KEY_UPVOTE = "upvote";
    public static final String KEY_DOWNVOTE = "downvote";
    public static final String KEY_STATUS = "status";

    private String reputation_votes_id, voter_id, voted_id, upvote, downvote, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Profile");

        String userid = getIntent().getExtras().getString("user_id");
        String lastname = getIntent().getExtras().getString("lastname");
        String firstname = getIntent().getExtras().getString("firstname");
        String contact_number = getIntent().getExtras().getString("contact_number");
        String reputation = getIntent().getExtras().getString("reputation");
        String email_address = getIntent().getExtras().getString("email_address");
        String profile_picture = getIntent().getExtras().getString("profile_picture");

        TextView et_username = (TextView) findViewById(R.id.username);
        TextView et_contactnumber = (TextView) findViewById(R.id.contactnumber);
        TextView et_emailaddress = (TextView) findViewById(R.id.emailaddress);
        TextView et_reputation = (TextView) findViewById(R.id.reputation);
        ImageView iv_profile_picture = (ImageView) findViewById(R.id.profile_picture);

        et_username.setText(firstname + " " + lastname);
        et_contactnumber.setText(contact_number.replaceAll("\\w(?=\\w{4})", "*"));
        et_emailaddress.setText(email_address);
        et_reputation.setText(reputation);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            URL URL = new URL("http://maroon-and-gold.000webhostapp.com/tekbook/images/"+profile_picture);
            HttpURLConnection connection = (HttpURLConnection) URL.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            iv_profile_picture.setImageBitmap(myBitmap);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
        }
        getData(Integer.parseInt(userid));
    }

    @Override
    public boolean onSupportNavigateUp() {
        SharedPreferences settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();
        //remove reputation
        prefEditor.remove("user_selected_reputation");
        prefEditor.commit();

        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        SharedPreferences settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();
        //remove reputation
        prefEditor.remove("user_selected_reputation");
        prefEditor.commit();

        finish();
    }

    public void upvote(View view){
        try{
            Intent intent = getIntent();
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            final String user_voter_id = prefs.getInt("user_id", 0)+"";
            final String user_voted_id = getIntent().getExtras().getString("user_id");
            final String user_reps = prefs.getString("user_selected_reputation", null);
            final TextView et_reps = (TextView) findViewById(R.id.reputation);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, REPUTATION_VOTES+user_voter_id+"&voted_id="+user_voted_id,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                if(!response.toString().trim().equals("not_voted_yet")){
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
                                    JSONObject collegeData = result.getJSONObject(0);

                                    status = collegeData.getString(KEY_STATUS);
                                    reputation_votes_id = collegeData.getString(KEY_REPUTATION_VOTES_ID);
                                    voter_id = collegeData.getString(KEY_VOTER_ID);
                                    voted_id = collegeData.getString(KEY_VOTED_ID);
                                    upvote = collegeData.getString(KEY_UPVOTE);
                                    downvote = collegeData.getString(KEY_DOWNVOTE);
                            }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try{
                                if(status.equals("success")){
                                    if(user_voter_id.toString().trim().equals(user_voted_id)){
                                        Toast.makeText(UserProfileActivity.this, "You cannot vote yourself!", Toast.LENGTH_SHORT).show();
                                    }else{
                                        if(upvote.toString().equals("TRUE")){
                                            Toast.makeText(UserProfileActivity.this, "You already voted this user!", Toast.LENGTH_SHORT).show();
                                        }else {
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, VOTE,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                Toast.makeText(UserProfileActivity.this, "You voted successfully!", Toast.LENGTH_LONG).show();
                                                                final String reps = formatValue(Double.parseDouble(user_reps) + 1) + "";

                                                                et_reps.setText(reps);

                                                                SharedPreferences settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                                                SharedPreferences.Editor prefEditor = settings.edit();
                                                                //remove reputation
                                                                prefEditor.remove("user_selected_reputation");
                                                                prefEditor.commit();
                                                                //set user credentials session
                                                                prefEditor.putString("user_selected_reputation", reps);
                                                                prefEditor.commit();
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(UserProfileActivity.this, "Internal server error occurs!"+error.toString(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }) {
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<String, String>();
                                                    final String reps = formatValue(Double.parseDouble(user_reps) + 1) + "";
                                                    params.put(KEY_VOTER_ID, user_voter_id);
                                                    params.put(KEY_VOTED_ID, user_voted_id);
                                                    params.put(KEY_UPVOTE, "TRUE");
                                                    params.put(KEY_DOWNVOTE, "FALSE");
                                                    params.put("action", "update");
                                                    params.put("reputation", reps);

                                                    return params;
                                                }

                                            };

                                            RequestQueue requestQueue = Volley.newRequestQueue(UserProfileActivity.this);
                                            requestQueue.add(stringRequest);
                                        }
                                    }
                                }
                            }catch (Exception e){
                                if(user_voter_id.toString().trim().equals(user_voted_id)){
                                    Toast.makeText(UserProfileActivity.this, "You cannot vote yourself!", Toast.LENGTH_SHORT).show();
                                }else {

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, VOTE,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        Toast.makeText(UserProfileActivity.this, "You voted successfully!", Toast.LENGTH_LONG).show();
                                                        final String reps = formatValue(Double.parseDouble(user_reps) + 1) + "";
                                                        et_reps.setText(reps);

                                                        SharedPreferences settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                                        SharedPreferences.Editor prefEditor = settings.edit();
                                                        //remove reputation
                                                        prefEditor.remove("user_selected_reputation");
                                                        prefEditor.commit();
                                                        //set user credentials session
                                                        prefEditor.putString("user_selected_reputation", reps);
                                                        prefEditor.commit();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(UserProfileActivity.this, "Internal server error occurs!"+error.toString(), Toast.LENGTH_LONG).show();
                                                }
                                            }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params = new HashMap<String, String>();
                                            final String reps = formatValue(Double.parseDouble(user_reps) + 1) + "";
                                            params.put(KEY_VOTER_ID, user_voter_id);
                                            params.put(KEY_VOTED_ID, user_voted_id);
                                            params.put(KEY_UPVOTE, "TRUE");
                                            params.put(KEY_DOWNVOTE, "FALSE");
                                            params.put("action", "new");
                                            params.put("reputation", reps);

                                            return params;
                                        }

                                    };

                                    RequestQueue requestQueue = Volley.newRequestQueue(UserProfileActivity.this);
                                    requestQueue.add(stringRequest);
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UserProfileActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                        }
                    }) {
            };

            RequestQueue requestQueue = Volley.newRequestQueue(UserProfileActivity.this);
            requestQueue.add(stringRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void downvote(View view){
        try{
            Intent intent = getIntent();
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            final String user_voter_id = prefs.getInt("user_id", 0)+"";
            final String user_voted_id = getIntent().getExtras().getString("user_id");
            final String user_reps = prefs.getString("user_selected_reputation", null);
            final TextView et_reps = (TextView) findViewById(R.id.reputation);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, REPUTATION_VOTES+user_voter_id+"&voted_id="+user_voted_id,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                if(!response.toString().trim().equals("not_voted_yet")){
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
                                    JSONObject collegeData = result.getJSONObject(0);

                                    status = collegeData.getString(KEY_STATUS);
                                    reputation_votes_id = collegeData.getString(KEY_REPUTATION_VOTES_ID);
                                    voter_id = collegeData.getString(KEY_VOTER_ID);
                                    voted_id = collegeData.getString(KEY_VOTED_ID);
                                    upvote = collegeData.getString(KEY_UPVOTE);
                                    downvote = collegeData.getString(KEY_DOWNVOTE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try{
                                if(status.equals("success")){
                                    if(user_voter_id.toString().trim().equals(user_voted_id)){
                                        Toast.makeText(UserProfileActivity.this, "You cannot downvote yourself!", Toast.LENGTH_SHORT).show();
                                    }else{
                                        if(downvote.toString().equals("TRUE")){
                                            Toast.makeText(UserProfileActivity.this, "You already downvoted this user!", Toast.LENGTH_SHORT).show();
                                        }else {
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, VOTE,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                Toast.makeText(UserProfileActivity.this, "You downvoted successfully!", Toast.LENGTH_LONG).show();
                                                                final String reps = formatValue(Double.parseDouble(user_reps) - 1) + "";
                                                                et_reps.setText(reps);

                                                                SharedPreferences settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                                                SharedPreferences.Editor prefEditor = settings.edit();
                                                                //remove reputation
                                                                prefEditor.remove("user_selected_reputation");
                                                                prefEditor.commit();
                                                                //set user credentials session
                                                                prefEditor.putString("user_selected_reputation", reps);
                                                                prefEditor.commit();
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            Toast.makeText(UserProfileActivity.this, "Internal server error occurs!"+error.toString(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }) {
                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params = new HashMap<String, String>();
                                                    final String reps = formatValue(Double.parseDouble(user_reps) - 1) + "";
                                                    params.put(KEY_VOTER_ID, user_voter_id);
                                                    params.put(KEY_VOTED_ID, user_voted_id);
                                                    params.put(KEY_UPVOTE, "FALSE");
                                                    params.put(KEY_DOWNVOTE, "TRUE");
                                                    params.put("action", "update");
                                                    params.put("reputation", reps);

                                                    return params;
                                                }

                                            };

                                            RequestQueue requestQueue = Volley.newRequestQueue(UserProfileActivity.this);
                                            requestQueue.add(stringRequest);
                                        }
                                    }
                                }
                            }catch (Exception e){
                                if(user_voter_id.toString().trim().equals(user_voted_id)){
                                    Toast.makeText(UserProfileActivity.this, "You cannot downvote yourself!", Toast.LENGTH_SHORT).show();
                                }else {

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, VOTE,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        Toast.makeText(UserProfileActivity.this, "You downvoted successfully!", Toast.LENGTH_LONG).show();
                                                        final String reps = formatValue(Double.parseDouble(user_reps) - 1) + "";
                                                        et_reps.setText(reps);

                                                        SharedPreferences settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                                        SharedPreferences.Editor prefEditor = settings.edit();
                                                        //remove reputation
                                                        prefEditor.remove("user_selected_reputation");
                                                        prefEditor.commit();
                                                        //set user credentials session
                                                        prefEditor.putString("user_selected_reputation", reps);
                                                        prefEditor.commit();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(UserProfileActivity.this, "Internal server error occurs!"+error.toString(), Toast.LENGTH_LONG).show();
                                                }
                                            }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params = new HashMap<String, String>();
                                            final String reps = formatValue(Double.parseDouble(user_reps) - 1) + "";
                                            params.put(KEY_VOTER_ID, user_voter_id);
                                            params.put(KEY_VOTED_ID, user_voted_id);
                                            params.put(KEY_UPVOTE, "FALSE");
                                            params.put(KEY_DOWNVOTE, "TRUE");
                                            params.put("action", "new");
                                            params.put("reputation", reps);

                                            return params;
                                        }

                                    };

                                    RequestQueue requestQueue = Volley.newRequestQueue(UserProfileActivity.this);
                                    requestQueue.add(stringRequest);
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UserProfileActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                        }
                    }) {
            };

            RequestQueue requestQueue = Volley.newRequestQueue(UserProfileActivity.this);
            requestQueue.add(stringRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String formatValue(double d){
        String dStr = String.valueOf(d);
        String value = dStr.matches("\\d+\\.\\d*[1-9]\\d*") ? dStr : dStr.substring(0,dStr.indexOf("."));
        return value;
    }

    private void getData(int user_id) {

        String url = DATA_URL + user_id;

        if(!IsReachable(UserProfileActivity.this, url)){
            Toast.makeText(UserProfileActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }else {
            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        showJSON(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UserProfileActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(UserProfileActivity.this);
            requestQueue.add(stringRequest);
        }
    }

    private void showJSON(String json){
        ListView listView = (ListView) findViewById(R.id.books_display);
        ParseJSON parse = new ParseJSON(json);
        parse.parseJSON();
        BooksAdapter booksAdapter = new BooksAdapter(UserProfileActivity.this, ParseJSON.book_id, ParseJSON.book_class, ParseJSON.book_title, ParseJSON.book_description, ParseJSON.book_price, ParseJSON.book_status);
        listView.setAdapter(booksAdapter);

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3){
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    // Checking if there's internet connection
    public boolean IsReachable(Context context, String LINK_URL) {

        // First, check we have any sort of connectivity
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        boolean isReachable = false;

        if (netInfo != null && netInfo.isConnected()) {

            try {
                URL url = new URL(LINK_URL);

                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(1000);

                return true;

            } catch (IOException e) {

            }
        }
        return isReachable;
    }
}
