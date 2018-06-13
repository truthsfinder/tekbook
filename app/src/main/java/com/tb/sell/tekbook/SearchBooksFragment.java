package com.tb.sell.tekbook;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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

import static android.content.Context.MODE_PRIVATE;


public class SearchBooksFragment extends Fragment{
    public static final String DATA_URL = "http://maroon-and-gold.000webhostapp.com/tekbook/get_all_user_with_books.php";
    public static final String SEARCH_BOOKS = "http://maroon-and-gold.000webhostapp.com/tekbook/get_search_all_user_with_books.php?search=";
    public static final String USER_PROFILE = "http://maroon-and-gold.000webhostapp.com/tekbook/get_search_all_user_with_books_by_user_id.php?user_id=";

    public static final String JSON_ARRAY = "result";
    public static final String KEY_ID = "user_id";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_CONTACT_NUMBER = "contact_number";
    public static final String KEY_REPUTATION = "reputation";
    public static final String KEY_EMAIL_ADDRESS = "email_address";
    public static final String KEY_PROFILE_PICTURE = "profile_picture";
    public static final String KEY_STATUS = "status";

    View view;
    private ProgressDialog loading;
    private String lastname, firstname, contact_number, reputation, email_address, profile_picture, status;
    private String user_profile_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_search_books, container, false);

        try{
            //Retrieve the value
            String value = getArguments().getString("search");
            EditText search_books = (EditText) view.findViewById(R.id.search_books);

            if(value != null){
                search_books.setText(value);
                search_books(view);
            }else{
                getData();
            }
        }catch (Exception e){
            getData();
        }

        return view;
    }

    public void getData() {
        if(!IsReachable(getActivity(), DATA_URL)){
            Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }else {
            StringRequest stringRequest = new StringRequest(DATA_URL, new Response.Listener<String>() {
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
                            Toast.makeText(getActivity().getApplicationContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    private void showJSON(String json){
        ListView listView = (ListView) view.findViewById(R.id.user_books_display);
        ParseJSON_SEARCH parse = new ParseJSON_SEARCH(json);
        parse.ParseJSON_SEARCH();
        final SearchBooksAdapter booksAdapter = new SearchBooksAdapter(getActivity(), ParseJSON_SEARCH.user_id, ParseJSON_SEARCH.firstname, ParseJSON_SEARCH.lastname, ParseJSON_SEARCH.book_class, ParseJSON_SEARCH.book_price, ParseJSON_SEARCH.book_status);
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
                                user_profile_id = collegeData.getString(KEY_ID);
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

                                    Intent intent = new Intent(getActivity().getApplication(), UserProfileActivity.class);

                                    intent.putExtra("user_id", user_profile_id);
                                    intent.putExtra("lastname", lastname);
                                    intent.putExtra("firstname", firstname);
                                    intent.putExtra("contact_number", contact_number);
                                    intent.putExtra("reputation", reputation);
                                    intent.putExtra("email_address", email_address);
                                    intent.putExtra("profile_picture", profile_picture);

                                    SharedPreferences settings = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor prefEditor = settings.edit();
                                    prefEditor.putString("user_selected_reputation", reputation);
                                    prefEditor.commit();

                                    startActivity(intent);


                                }else{
                                    Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                                }
                            }catch (Exception e){
                                Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                        }
                    }) {
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(stringRequest);
            }
        });

    }

    private void search_books(View view){

        EditText et_search = (EditText) view.findViewById(R.id.search_books);

        String search = et_search.getText().toString().trim();

        String LINK_URL =  SEARCH_BOOKS + search;

        if(!IsReachable(getActivity(), LINK_URL)){
            Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
            loading.dismiss();
        }else {
            if (search.isEmpty()) {
                Toast.makeText(getActivity(), "Fields are empty!", Toast.LENGTH_LONG).show();
            } else {
                loading = ProgressDialog.show(getActivity(),"Status", "Searching for "+search, false, false);
                StringRequest stringRequest = new StringRequest(LINK_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            loading.dismiss();
                            showJSON2(response);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        });

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(stringRequest);

            }
        }

    }

    private void showJSON2(String json){
        ListView listView = (ListView) view.findViewById(R.id.user_books_display);
        ParseJSON_SEARCH parse = new ParseJSON_SEARCH(json);
        parse.ParseJSON_SEARCH();
        final SearchBooksAdapter booksAdapter = new SearchBooksAdapter(getActivity(), ParseJSON_SEARCH.user_id, ParseJSON_SEARCH.firstname, ParseJSON_SEARCH.lastname, ParseJSON_SEARCH.book_class, ParseJSON_SEARCH.book_price, ParseJSON_SEARCH.book_status);
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
                                    user_profile_id = collegeData.getString(KEY_ID);
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

                                        Intent intent = new Intent(getActivity().getApplication(), UserProfileActivity.class);

                                        intent.putExtra("user_id", user_profile_id);
                                        intent.putExtra("lastname", lastname);
                                        intent.putExtra("firstname", firstname);
                                        intent.putExtra("contact_number", contact_number);
                                        intent.putExtra("reputation", reputation);
                                        intent.putExtra("email_address", email_address);
                                        intent.putExtra("profile_picture", profile_picture);

                                        SharedPreferences settings = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                        SharedPreferences.Editor prefEditor = settings.edit();
                                        prefEditor.putString("user_selected_reputation", reputation);
                                        prefEditor.commit();

                                        startActivity(intent);


                                    }else{
                                        Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                                    }
                                }catch (Exception e){
                                    Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                            }
                        }) {
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(stringRequest);
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
