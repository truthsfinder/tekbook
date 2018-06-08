package com.tb.sell.tekbook;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    public static final String REGISTER_URL = "http://maroon-and-gold.000webhostapp.com/tekbook/db_create.php";
    public static final String CHECK_USERNAME_URL = "http://maroon-and-gold.000webhostapp.com/tekbook/check_username.php";
    public static final String CHECK_EMAIL_ADDRESS_URL = "http://maroon-and-gold.000webhostapp.com/tekbook/check_email_if_exists.php";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_EMAIL_ADDRESS = "email_address";
    public static final String KEY_PASSWORD = "password";

    private String username;
    private String lastname;
    private String firstname;
    private String email_address;
    private String password;
    private String password2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void register(View view){
        //initialization
        EditText et_username = (EditText) findViewById(R.id.username);
        EditText et_lastname = (EditText) findViewById(R.id.lastname);
        EditText et_firstname = (EditText) findViewById(R.id.firstname);
        EditText et_email_address = (EditText) findViewById(R.id.email_address);
        EditText et_password = (EditText) findViewById(R.id.password);
        EditText et_password2 = (EditText) findViewById(R.id.password2);

        username = et_username.getText().toString().trim();
        lastname = et_lastname.getText().toString().trim();
        firstname = et_firstname.getText().toString().trim();
        email_address = et_email_address.getText().toString().trim();
        password = et_password.getText().toString().trim();
        password2 = et_password2.getText().toString().trim();

        if(!IsReachable(RegisterActivity.this)){
            Toast.makeText(RegisterActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }else {
            if (username.isEmpty() || password.isEmpty() || lastname.isEmpty() || firstname.isEmpty() || email_address.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Fields are empty!", Toast.LENGTH_LONG).show();
            }else if(!password.toString().trim().equals(password2)){
                Toast.makeText(RegisterActivity.this, "Password does not match!", Toast.LENGTH_LONG).show();
            } else {
                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, CHECK_USERNAME_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(final String response1) {
                                if (response1.toString().trim().equals("failed")) {
                                    Toast.makeText(RegisterActivity.this, "Failed in executing the query!", Toast.LENGTH_LONG).show();
                                }else if(response1.toString().trim().equals("success")){
                                    Toast.makeText(RegisterActivity.this, "Username or email address already exists!", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(RegisterActivity.this, response1, Toast.LENGTH_LONG).show();
                                    StringRequest stringRequest2 = new StringRequest(Request.Method.POST, CHECK_EMAIL_ADDRESS_URL,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(final String response2) {
                                                    if (response2.toString().trim().equals("failed")) {
                                                        Toast.makeText(RegisterActivity.this, "Failed in executing the query!", Toast.LENGTH_LONG).show();
                                                    }else if(response2.toString().trim().equals("success")){
                                                        Toast.makeText(RegisterActivity.this, "Email address already exists!", Toast.LENGTH_LONG).show();
                                                    }else{
                                                        Toast.makeText(RegisterActivity.this, response2, Toast.LENGTH_LONG).show();
                                                        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, REGISTER_URL,
                                                                new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String response3) {
                                                                        Toast.makeText(RegisterActivity.this, "Successfully Registered!", Toast.LENGTH_LONG).show();
                                                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                                        finish();
                                                                    }
                                                                },
                                                                new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        Toast.makeText(RegisterActivity.this, "Registration Failed!", Toast.LENGTH_LONG).show();
                                                                    }
                                                                }) {
                                                            @Override
                                                            protected Map<String, String> getParams() {
                                                                Map<String, String> params3 = new HashMap<String, String>();
                                                                params3.put(KEY_USERNAME, username);
                                                                params3.put(KEY_LASTNAME, lastname);
                                                                params3.put(KEY_FIRSTNAME, firstname);
                                                                params3.put(KEY_EMAIL_ADDRESS, email_address);
                                                                params3.put(KEY_PASSWORD, password);

                                                                return params3;
                                                            }

                                                        };

                                                        RequestQueue requestQueue3 = Volley.newRequestQueue(RegisterActivity.this);
                                                        requestQueue3.add(stringRequest3);
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(RegisterActivity.this, "Error in connecting the server!", Toast.LENGTH_LONG).show();
                                                }
                                            }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params2 = new HashMap<String, String>();
                                            params2.put("username", username);

                                            return params2;
                                        }

                                    };
                                    RequestQueue requestQueue2 = Volley.newRequestQueue(RegisterActivity.this);
                                    requestQueue2.add(stringRequest2);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(RegisterActivity.this, "Error in connecting the server!", Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params1 = new HashMap<String, String>();
                        params1.put("username", username);

                        return params1;
                    }
                };

                RequestQueue requestQueue1 = Volley.newRequestQueue(RegisterActivity.this);
                requestQueue1.add(stringRequest1);
            }
        }
    }

    public boolean[] check_username_if_exists(String url, final String username){
        final boolean[] exists = {false};

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        if (response.toString().trim().equals("failed")) {
                            Toast.makeText(RegisterActivity.this, "Failed in executing the query!", Toast.LENGTH_LONG).show();
                            exists[0] = true;
                        }else if(response.toString().trim().equals("success")){
                            Toast.makeText(RegisterActivity.this, "Username already exists!", Toast.LENGTH_LONG).show();
                            exists[0] = true;
                        }else{
                            exists[0] = false;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "Error in connecting the server!", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.add(stringRequest);

        return exists;
    }

    public boolean[] check_email_address_if_exists(String url, final String email_address){
        final boolean[] exists = {false};

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        if (response.toString().trim().equals("failed")) {
                            Toast.makeText(RegisterActivity.this, "Failed in executing the query!", Toast.LENGTH_LONG).show();
                            exists[0] = true;
                        }else if(response.toString().trim().equals("empty")){
                            exists[0] = false;
                        }else{
                            Toast.makeText(RegisterActivity.this, "Email address already exists!", Toast.LENGTH_LONG).show();
                            exists[0] = true;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, "Error in connecting the server!", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email_address);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.add(stringRequest);

        return exists;
    }

    // Checking if there's internet connection
    public boolean IsReachable(Context context) {

        // First, check we have any sort of connectivity
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        boolean isReachable = false;

        if (netInfo != null && netInfo.isConnected()) {

            try {
                URL url = new URL(REGISTER_URL);

                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(1000);

                return true;

            } catch (IOException e) {

            }
        }
        return isReachable;
    }
}

