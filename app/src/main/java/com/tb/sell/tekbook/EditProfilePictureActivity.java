package com.tb.sell.tekbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

public class EditProfilePictureActivity extends AppCompatActivity implements View.OnClickListener  {

    private Button buttonChoose;
    private Button buttonUpload;

    private ImageView imageView;

    private EditText profile_picture_name;

    private Bitmap bitmap;

    private int PICK_IMAGE_REQUEST = 1;

    private String UPLOAD_URL ="http://maroon-and-gold.000webhostapp.com/tekbook/update_profile_picture.php";

    private String KEY_PROFILE_PICTURE = "image";
    private String KEY_PICTURE_NAME = "image_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_picture);

        buttonChoose = (Button) findViewById(R.id.choose_image);
        buttonUpload = (Button) findViewById(R.id.update_profile_picture);

        imageView  = (ImageView) findViewById(R.id.profile_picture_display);

        profile_picture_name  = (EditText) findViewById(R.id.profile_picture_name);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String profile_picture = prefs.getString("profile_picture", null);

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
            imageView.setImageBitmap(myBitmap);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
        }

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        if(profile_picture_name.getText().toString().trim().isEmpty()){
            Toast.makeText(EditProfilePictureActivity.this, "Please input an image name!" , Toast.LENGTH_SHORT).show();
        }else {
            try{
                StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                //Dismissing the progress dialog
                                loading.dismiss();

                                SharedPreferences settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor prefEditor = settings.edit();
                                //remove the saved user credentials
                                prefEditor.remove("profile_picture");
                                prefEditor.commit();

                                //set user credentials session
                                prefEditor.putString("profile_picture", s);
                                prefEditor.commit();

                                //Showing toast message of the response
                                Toast.makeText(EditProfilePictureActivity.this, "Successfully uploaded!", Toast.LENGTH_LONG).show();

                                startActivity(new Intent(EditProfilePictureActivity.this, ProfileDrawerActivity.class));
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                //Dismissing the progress dialog
                                loading.dismiss();

                                //Showing toast
                                Toast.makeText(EditProfilePictureActivity.this, "Please select image from a file!", Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        //Converting Bitmap to String
                        String image = getStringImage(bitmap);
                        String image_name = profile_picture_name.getText().toString().trim();
                        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        int user_id = prefs.getInt("user_id", 0);

                        //Creating parameters
                        Map<String, String> params = new Hashtable<String, String>();

                        //Adding parameters
                        params.put(KEY_PROFILE_PICTURE, image);
                        params.put(KEY_PICTURE_NAME, image_name);
                        params.put("user_id", user_id + "");

                        //returning parameters
                        return params;
                    }
                };

                //Creating a Request Queue
                RequestQueue requestQueue = Volley.newRequestQueue(this);

                //Adding request to the queue
                requestQueue.add(stringRequest);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {

        if(v == buttonChoose){
            showFileChooser();
        }

        if(v == buttonUpload){
            uploadImage();
        }
    }
}