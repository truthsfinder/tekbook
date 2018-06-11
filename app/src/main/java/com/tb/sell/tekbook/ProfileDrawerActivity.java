package com.tb.sell.tekbook;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class ProfileDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fragmentManager = getFragmentManager();
    public static final String UPDATE_PROFILE = "http://maroon-and-gold.000webhostapp.com/tekbook/update_profile.php";
    public static final String SEARCH_BOOKS = "http://maroon-and-gold.000webhostapp.com/tekbook/get_search_all_user_with_books.php?search=";
    public static final String USER_PROFILE = "http://maroon-and-gold.000webhostapp.com/tekbook/get_search_all_user_with_books_by_user_id.php?user_id=";
    public final String CHECK_URL = "http://maroon-and-gold.000webhostapp.com/tekbook/check_email_address.php";

    public static final String JSON_ARRAY = "result";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_CONTACT_NUMBER = "contact_number";
    public static final String KEY_BIRTHDATE = "birthdate";
    public static final String KEY_EMAIL_ADDRESS = "email_address";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_REPUTATION = "reputation";
    public static final String KEY_PROFILE_PICTURE = "profile_picture";
    public static final String KEY_STATUS = "status";

    String lastname, firstname, contact_number, birthdate, reputation, email_address, password, gender, profile_picture, status;
    private String user_profile_id;

    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent intent = getIntent();
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String name = prefs.getString("username", null);
        toolbar.setTitle("Hello, " + name);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment home = new ProfileFragment();
        FragmentManager FM = getFragmentManager();
        FM
                .beginTransaction()
                .replace(R.id.content_frame, home)
                .commit();
    }

    public void edit_profile(View view){
        startActivity(new Intent(ProfileDrawerActivity.this, EditProfilePictureActivity.class));
    }

    public void cancel(View view){
        fragmentManager.beginTransaction().replace(R.id.content_frame, new ProfileFragment()).commit();
    }

    public void add_or_remove(View view){
        fragmentManager.beginTransaction().replace(R.id.content_frame, new AddBooksFragment()).commit();
    }

    public void profile_search(View view){
        EditText profile_search_books = (EditText) findViewById(R.id.profile_search_books);

        if(profile_search_books.getText().toString().trim().isEmpty()){
            Toast.makeText(ProfileDrawerActivity.this, "No search is specified!", Toast.LENGTH_SHORT).show();
        }else {
            //Put the value
            SearchBooksFragment searchBooksFragment = new SearchBooksFragment();
            Bundle args = new Bundle();
            args.putString("search", profile_search_books.getText().toString().trim());
            searchBooksFragment.setArguments(args);

            //Inflate the fragment
            fragmentManager.beginTransaction().add(R.id.content_frame, searchBooksFragment).commit();
        }
    }

    public final void search(View view){

        EditText et_search = (EditText) findViewById(R.id.search_books);

        String search = et_search.getText().toString().trim();

        String LINK_URL =  SEARCH_BOOKS + search;

        if(!IsReachable(ProfileDrawerActivity.this, LINK_URL)){
            Toast.makeText(ProfileDrawerActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
            loading.dismiss();
        }else {
            if (search.isEmpty()) {
                Toast.makeText(ProfileDrawerActivity.this, "Fields are empty!", Toast.LENGTH_LONG).show();
            } else {
                loading = ProgressDialog.show(this,"Status", "Searching for "+search, false, false);
                StringRequest stringRequest = new StringRequest(LINK_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            loading.dismiss();
                            showJSON(response);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ProfileDrawerActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        });

                RequestQueue requestQueue = Volley.newRequestQueue(ProfileDrawerActivity.this);
                requestQueue.add(stringRequest);

            }
        }

    }

    private void showJSON(String json){
        ListView listView = (ListView) findViewById(R.id.user_books_display);
        ParseJSON_SEARCH parse = new ParseJSON_SEARCH(json);
        parse.ParseJSON_SEARCH();
        final SearchBooksAdapter booksAdapter = new SearchBooksAdapter(ProfileDrawerActivity.this, ParseJSON_SEARCH.user_id, ParseJSON_SEARCH.firstname, ParseJSON_SEARCH.lastname, ParseJSON_SEARCH.book_class, ParseJSON_SEARCH.book_price, ParseJSON_SEARCH.book_status);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int userid = Integer.parseInt(booksAdapter.getItem(position));

                StringRequest stringRequest = new StringRequest(Request.Method.GET, USER_PROFILE + userid,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
                                    JSONObject collegeData = result.getJSONObject(0);

                                    status = collegeData.getString(KEY_STATUS);
                                    user_profile_id = collegeData.getString(KEY_USER_ID);
                                    lastname = collegeData.getString(KEY_LASTNAME);
                                    firstname = collegeData.getString(KEY_FIRSTNAME);
                                    contact_number = collegeData.getString(KEY_CONTACT_NUMBER);
                                    reputation = collegeData.getString(KEY_REPUTATION);
                                    email_address = collegeData.getString(KEY_EMAIL_ADDRESS);
                                    profile_picture = collegeData.getString(KEY_PROFILE_PICTURE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try{
                                    if(status.equals("success")){

                                        Intent intent = new Intent(ProfileDrawerActivity.this, UserProfileActivity.class);

                                        intent.putExtra("user_id", user_profile_id);
                                        intent.putExtra("lastname", lastname);
                                        intent.putExtra("firstname", firstname);
                                        intent.putExtra("contact_number", contact_number);
                                        intent.putExtra("reputation", reputation);
                                        intent.putExtra("email_address", email_address);
                                        intent.putExtra("profile_picture", profile_picture);

                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(ProfileDrawerActivity.this, response, Toast.LENGTH_LONG).show();
                                    }
                                }catch (Exception e){
                                    Toast.makeText(ProfileDrawerActivity.this, response, Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ProfileDrawerActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                            }
                        }) {
                };

                RequestQueue requestQueue = Volley.newRequestQueue(ProfileDrawerActivity.this);
                requestQueue.add(stringRequest);
            }
        });
    }

    public void update_profile(View view){
        EditText et_contact_number = (EditText) findViewById(R.id.contact_number);
        EditText et_birthdate = (EditText) findViewById(R.id.birthdate);
        final EditText et_email_address = (EditText) findViewById(R.id.email_address);
        EditText et_password = (EditText) findViewById(R.id.password);
        Spinner et_gender = (Spinner) findViewById(R.id.gender);

        Intent intent = getIntent();
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        final String user_id = prefs.getInt("user_id", 0)+"";

        contact_number = et_contact_number.getText().toString().trim();
        birthdate = et_birthdate.getText().toString().trim();
        email_address = et_email_address.getText().toString().trim();
        password = et_password.getText().toString().trim();
        gender = et_gender.getSelectedItem().toString().trim();

        if(!IsReachable(this, UPDATE_PROFILE)){
            Toast.makeText(this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }else {
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, CHECK_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response1) {
                            //get logged in data
                            SharedPreferences data = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            final String user_id = data.getInt("user_id", 0)+"";

                            if (!(response1.toString().trim().equals("empty")) && !(response1.toString().trim().equals(user_id))) {
                                Toast.makeText(ProfileDrawerActivity.this, "Email address already exists!", Toast.LENGTH_LONG).show();
                            } else {
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_PROFILE,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Toast.makeText(ProfileDrawerActivity.this, "Successfully updated!", Toast.LENGTH_LONG).show();
                                                fragmentManager.beginTransaction().replace(R.id.content_frame, new ProfileFragment()).commit();

                                                SharedPreferences settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                                SharedPreferences.Editor prefEditor = settings.edit();
                                                //remove the saved user credentials
                                                prefEditor.remove("contact_number");
                                                prefEditor.remove("birthdate");
                                                prefEditor.remove("email_address");
                                                prefEditor.remove("password");
                                                prefEditor.remove("gender");
                                                prefEditor.commit();
                                                //set user credentials session
                                                prefEditor.putString("contact_number", contact_number);
                                                prefEditor.putString("password", md5(password));
                                                prefEditor.putString("birthdate", birthdate);
                                                prefEditor.putString("email_address", email_address);
                                                prefEditor.putString("gender", gender);
                                                prefEditor.commit();
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(ProfileDrawerActivity.this, "Failed editing your profile!", Toast.LENGTH_LONG).show();
                                            }
                                        }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put(KEY_USER_ID, user_id);
                                        params.put(KEY_CONTACT_NUMBER, contact_number);
                                        params.put(KEY_BIRTHDATE, birthdate);
                                        params.put(KEY_EMAIL_ADDRESS, email_address);
                                        params.put(KEY_PASSWORD, password);
                                        params.put(KEY_GENDER, gender);

                                        return params;
                                    }

                                };

                                RequestQueue requestQueue = Volley.newRequestQueue(ProfileDrawerActivity.this);
                                requestQueue.add(stringRequest);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ProfileDrawerActivity.this, "Error in connecting the server!", Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params1 = new HashMap<String, String>();
                    params1.put("email", email_address);

                    return params1;
                }

            };

            RequestQueue requestQueue1 = Volley.newRequestQueue(ProfileDrawerActivity.this);
            requestQueue1.add(stringRequest1);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_profile) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ProfileFragment()).commit();
        } else if (id == R.id.nav_search_books) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new SearchBooksFragment()).commit();
        } else if (id == R.id.nav_add_books) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new AddBooksFragment()).commit();
        } else if (id == R.id.nav_edit_profile) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new EditProfileFragment()).commit();
        } else if (id == R.id.nav_logout) {
            SharedPreferences settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            final SharedPreferences.Editor prefEditor = settings.edit();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to logout?");
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                    prefEditor.clear();
                    prefEditor.commit();

                    Intent intent = new Intent(ProfileDrawerActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }else{
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ProfileFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    //Encrypt password
    public static final String md5(final String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return "";
        }
    }
}
