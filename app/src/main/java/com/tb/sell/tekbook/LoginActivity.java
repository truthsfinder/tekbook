package com.tb.sell.tekbook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    public static final String LOGIN_URL = "http://maroon-and-gold.000webhostapp.com/tekbook/db_get_data.php";
    public static final String JSON_ARRAY = "result";
    public static final String KEY_ID = "user_id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_CONTACT_NUMBER = "contact_number";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_REPUTATION = "reputation";
    public static final String KEY_BIRTHDATE = "birthdate";
    public static final String KEY_EMAIL_ADDRESS = "email_address";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_PROFILE_PICTURE = "profile_picture";
    public static final String KEY_STATUS = "status";

    public Button btnLogin;

    private String user_id, username, lastname, firstname, contact_number, password, reputation, email_address, birthdate, gender, profile_picture, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.sign_in_button);
        try {
            Intent intent = getIntent();
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String name = prefs.getString("username", null);

            if(name!=null){
                startActivity(new Intent(this, ProfileDrawerActivity.class));
                finish();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //sends to forgot password activity
    public void forgot_password(View view){
        startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
    }

    // Checks the login status
    public void login(View view) {
        EditText et_username = (EditText) findViewById(R.id.username);
        EditText et_password = (EditText) findViewById(R.id.password);

        username = et_username.getText().toString().trim();
        password = et_password.getText().toString().trim();

        //Showing the progress dialog
        final ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);

        if(!IsReachable(LoginActivity.this)){
            Toast.makeText(LoginActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
            bar.setVisibility(View.GONE);
        }else {
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Fields are empty!", Toast.LENGTH_LONG).show();
            } else {
                bar.setVisibility(View.GONE);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
                                    JSONObject collegeData = result.getJSONObject(0);

                                    status = collegeData.getString(KEY_STATUS);
                                    user_id = collegeData.getString(KEY_ID);
                                    username = collegeData.getString(KEY_USERNAME);
                                    lastname = collegeData.getString(KEY_LASTNAME);
                                    firstname = collegeData.getString(KEY_FIRSTNAME);
                                    contact_number = collegeData.getString(KEY_CONTACT_NUMBER);
                                    password = collegeData.getString(KEY_PASSWORD);
                                    reputation = collegeData.getString(KEY_REPUTATION);
                                    birthdate = collegeData.getString(KEY_BIRTHDATE);
                                    email_address = collegeData.getString(KEY_EMAIL_ADDRESS);
                                    gender = collegeData.getString(KEY_GENDER);
                                    profile_picture = collegeData.getString(KEY_PROFILE_PICTURE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                bar.setVisibility(View.GONE);
                                try{
                                    if(status.equals("success")){
                                        Toast.makeText(LoginActivity.this, "Welcome " + username, Toast.LENGTH_LONG).show();

                                        SharedPreferences settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                        SharedPreferences.Editor prefEditor = settings.edit();
                                        prefEditor.putInt("user_id", Integer.parseInt(user_id));
                                        prefEditor.putString("username", username);
                                        prefEditor.putString("lastname", lastname);
                                        prefEditor.putString("firstname", firstname);
                                        prefEditor.putString("contact_number", contact_number);
                                        prefEditor.putString("password", password);
                                        prefEditor.putInt("reputation", Integer.parseInt(reputation));
                                        prefEditor.putString("birthdate", birthdate);
                                        prefEditor.putString("email_address", email_address);
                                        prefEditor.putString("gender", gender);
                                        prefEditor.putString("profile_picture", profile_picture);
                                        prefEditor.commit();

                                        Intent intent = new Intent(LoginActivity.this, ProfileDrawerActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                                    }
                                }catch (Exception e){
                                    Toast.makeText(LoginActivity.this, "Incorrect login credentials!", Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(LoginActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                                bar.setVisibility(View.GONE);
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put(KEY_USERNAME, username);
                        map.put(KEY_PASSWORD, password);
                        return map;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }
        }
    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    // Checking if there's internet connection
    public boolean IsReachable(Context context) {

        // First, check we have any sort of connectivity
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        boolean isReachable = false;

        if (netInfo != null && netInfo.isConnected()) {

            try {
                URL url = new URL(LOGIN_URL);

                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(1000);

                return true;

            } catch (IOException e) {

            }
        }
        return isReachable;
    }
}

