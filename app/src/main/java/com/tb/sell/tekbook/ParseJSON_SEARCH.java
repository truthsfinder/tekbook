package com.tb.sell.tekbook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseJSON_SEARCH {
    public static String[] user_id;
    public static String[] firstname;
    public static String[] lastname;
    public static String[] book_class;
    public static String[] book_price;
    public static String[] book_status;

    public static final String JSON_ARRAY = "result";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_BOOK_CLASS = "book_class";
    public static final String KEY_BOOK_PRICE = "book_price";
    public static final String KEY_BOOK_STATUS = "book_status";

    private JSONArray books = null;

    private String json;

    public ParseJSON_SEARCH(String json){
        this.json = json;
    }

    protected void ParseJSON_SEARCH(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            books = jsonObject.getJSONArray(JSON_ARRAY);

            user_id = new String[books.length()];
            firstname = new String[books.length()];
            lastname = new String[books.length()];
            book_class = new String[books.length()];
            book_price = new String[books.length()];
            book_status = new String[books.length()];

            for(int i=0;i<books.length();i++){
                JSONObject jo = books.getJSONObject(i);
                user_id[i] = jo.getString(KEY_USER_ID);
                firstname[i] = jo.getString(KEY_FIRSTNAME);
                lastname[i] = jo.getString(KEY_LASTNAME);
                book_class[i] = jo.getString(KEY_BOOK_CLASS);
                book_price[i] = jo.getString(KEY_BOOK_PRICE);
                book_status[i] = jo.getString(KEY_BOOK_STATUS);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}