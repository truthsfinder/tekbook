package com.tb.sell.tekbook;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class EditProfileFragment extends Fragment{
    View view;
    String[] gender = new String[]{"Select Gender", "Male", "Female"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_edit_profile, container, false);


        Spinner spinner = (Spinner) view.findViewById(R.id.gender);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, gender);
        spinner.setAdapter(adapter);

        Intent intent = getActivity().getIntent();
        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        final String user_id = prefs.getInt("user_id", 0)+"";
        final String contact_number = prefs.getString("contact_number", null);
        final String birthdate = prefs.getString("birthdate", null);
        final String email_address = prefs.getString("email_address", null);
        final String gender = prefs.getString("gender", null);

        EditText et_contact_number = (EditText) view.findViewById(R.id.contact_number);
        EditText et_birthdate = (EditText) view.findViewById(R.id.birthdate);
        EditText et_email_address = (EditText) view.findViewById(R.id.email_address);
        Spinner sp_gender = (Spinner) view.findViewById(R.id.gender);

        et_contact_number.setText(contact_number);
        et_birthdate.setText(birthdate);
        et_email_address.setText(email_address);
        if(gender == "Male"){
            sp_gender.setSelection(adapter.getPosition("Male"));
        }else if(gender == "Female"){
            sp_gender.setSelection(adapter.getPosition("Female"));
        }else{
            sp_gender.setSelection(adapter.getPosition("Select Gender"));
        }

        return view;
    }

}
