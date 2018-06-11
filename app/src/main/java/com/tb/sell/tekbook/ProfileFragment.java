package com.tb.sell.tekbook;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;



public class ProfileFragment extends Fragment{
    View view;
    public static final String DATA_URL = "http://maroon-and-gold.000webhostapp.com/tekbook/get_user_books.php?user_id=";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_profile, container, false);
        ImageView iv_profile_picture = (ImageView)view.findViewById(R.id.profile_picture);
        TextView tv_reputation = (TextView)view.findViewById(R.id.reputation);

        getData();

        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String profile_picture = prefs.getString("profile_picture", null);
        int reputation = prefs.getInt("reputation", 0);

        tv_reputation.setText(reputation+" reps");

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
            return null;
        }
        return view;
    }

    private void getData() {
        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int user_id = prefs.getInt("user_id", 0);

        String url = DATA_URL + user_id;

        if(!IsReachable(getActivity(), url)){
            Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getActivity().getApplicationContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    private void showJSON(String json){
        ListView listView = (ListView) view.findViewById(R.id.books_display);
        ParseJSON parse = new ParseJSON(json);
        parse.parseJSON();
        BooksAdapter booksAdapter = new BooksAdapter(getActivity(), ParseJSON.book_id, ParseJSON.book_class, ParseJSON.book_title, ParseJSON.book_description, ParseJSON.book_price, ParseJSON.book_status);
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
