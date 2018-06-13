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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import static android.content.Context.MODE_PRIVATE;



public class AddBooksFragment extends Fragment{
    public static final String DATA_URL = "http://maroon-and-gold.000webhostapp.com/tekbook/get_user_books.php?user_id=";
    public static final String DELETE_URL = "http://maroon-and-gold.000webhostapp.com/tekbook/db_delete_book.php?user_id=";
    public static final String ADD_BOOK_URL = "http://maroon-and-gold.000webhostapp.com/tekbook/add_book.php";
    public static final String UPDATE_BOOK_URL = "http://maroon-and-gold.000webhostapp.com/tekbook/set_book_status.php?book_id=";
    View view;

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_BOOK_CLASS = "book_class";
    public static final String KEY_BOOK_TITLE = "book_title";
    public static final String KEY_BOOK_PRICE = "book_price";

    private String user_id;
    private String book_class;
    private String book_title;
    private String book_price;

    EditText et_book_class;
    EditText et_book_title;
    EditText et_book_price;

    private ProgressDialog loading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_add_books, container, false);

        getData();

        Button add_books = (Button) view.findViewById(R.id.addbook);
        add_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.addbook)
                {
                    try {
                        et_book_class = (EditText) view.findViewById(R.id.add_book_class);
                        et_book_title = (EditText) view.findViewById(R.id.add_book_title);
                        et_book_price = (EditText) view.findViewById(R.id.add_book_price);

                        Intent intent = getActivity().getIntent();
                        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        final String user_id = prefs.getInt("user_id", 0) + "";

                        book_class = et_book_class.getText().toString().trim();
                        book_title = et_book_title.getText().toString().trim();
                        book_price = et_book_price.getText().toString().trim();

                        if (!IsReachable(getActivity().getApplication(), ADD_BOOK_URL)) {
                            Toast.makeText(getActivity().getApplication(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                            loading.dismiss();
                        } else {
                            if (book_class.isEmpty() || book_title.isEmpty() || book_price.isEmpty()) {
                                Toast.makeText(getActivity().getApplication(), "Fields are empty!", Toast.LENGTH_LONG).show();
                            } else {
                                loading = ProgressDialog.show(getActivity(), "Status", "Adding books to personal bookshelf...", false, false);
                                try {
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_BOOK_URL,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    loading.dismiss();

                                                    Toast.makeText(getActivity().getApplication(), "Successfully Added!", Toast.LENGTH_LONG).show();
                                                    et_book_class.setText("");
                                                    et_book_title.setText("");
                                                    et_book_price.setText("");

                                                    getData();
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getActivity().getApplication(), "Registration Failed!", Toast.LENGTH_LONG).show();
                                                }
                                            }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params = new HashMap<String, String>();
                                            params.put(KEY_USER_ID, user_id);
                                            params.put(KEY_BOOK_CLASS, book_class);
                                            params.put(KEY_BOOK_TITLE, book_title);
                                            params.put(KEY_BOOK_PRICE, book_price);

                                            return params;
                                        }

                                    };

                                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplication());
                                    requestQueue.add(stringRequest);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }catch (Exception e){
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

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
        final BooksAdapter booksAdapter = new BooksAdapter(getActivity(), ParseJSON.book_id, ParseJSON.book_class, ParseJSON.book_title, ParseJSON.book_description, ParseJSON.book_price, ParseJSON.book_status);
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
                final int book_id = Integer.parseInt(booksAdapter.getItem(position));

                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(getActivity());
                alertDialogBuilder1.setMessage("SELECT ACTION TO MAKE: ");
                alertDialogBuilder1.setPositiveButton("MARK AS SOLD", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String set_book_status_url = UPDATE_BOOK_URL + book_id;

                        if(!IsReachable(getActivity().getApplication(), set_book_status_url)){
                            Toast.makeText(getActivity().getApplication(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                        }else {
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, set_book_status_url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Toast.makeText(getActivity().getApplication(), "Book status was set to sold!" + response, Toast.LENGTH_LONG).show();
                                            try {
                                                getData();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getActivity().getApplication(), "Transaction failed!", Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplication());
                            requestQueue.add(stringRequest);
                        }
                    }
                });

                alertDialogBuilder1.setNegativeButton("DELETE ITEM",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setMessage("Are you sure you want to delete this book?");
                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                int user_id = prefs.getInt("user_id", 0);

                                String add_book_url = DELETE_URL + user_id + "&book_id=" + book_id;

                                if(!IsReachable(getActivity().getApplication(), add_book_url)){
                                    Toast.makeText(getActivity().getApplication(), "Please check your internet connection!", Toast.LENGTH_LONG).show();
                                }else {
                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, add_book_url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Toast.makeText(getActivity().getApplication(), "Successfully deleted!", Toast.LENGTH_LONG).show();
                                                    try {
                                                        getData();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(getActivity().getApplication(), "Deletion failed!", Toast.LENGTH_LONG).show();
                                                }
                                            }) {
                                    };

                                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplication());
                                    requestQueue.add(stringRequest);
                                }
                            }
                        });

                        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                });

                AlertDialog alertDialog1 = alertDialogBuilder1.create();
                alertDialog1.show();


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
