package com.tb.sell.tekbook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseJSON {
    public static String[] book_id;
    public static String[] book_class;
    public static String[] book_title;
    public static String[] book_description;
    public static String[] book_price;
    public static String[] book_status;

    public static final String JSON_ARRAY = "result";
    public static final String KEY_BOOK_ID = "book_id";
    public static final String KEY_BOOK_CLASS = "book_class";
    public static final String KEY_BOOK_TITLE = "book_title";
    public static final String KEY_BOOK_DESCRIPTION = "book_description";
    public static final String KEY_BOOK_PRICE = "book_price";
    public static final String KEY_BOOK_STATUS = "book_status";

    private JSONArray books = null;

    private String json;

    public ParseJSON(String json){
        this.json = json;
    }

    protected void parseJSON(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            books = jsonObject.getJSONArray(JSON_ARRAY);

            book_id = new String[books.length()];
            book_class = new String[books.length()];
            book_title = new String[books.length()];
            book_description = new String[books.length()];
            book_price = new String[books.length()];
            book_status = new String[books.length()];

            for(int i=0;i<books.length();i++){
                JSONObject jo = books.getJSONObject(i);
                book_id[i] = jo.getString(KEY_BOOK_ID);
                book_class[i] = jo.getString(KEY_BOOK_CLASS);
                book_title[i] = jo.getString(KEY_BOOK_TITLE);
                book_description[i] = jo.getString(KEY_BOOK_DESCRIPTION);
                book_price[i] = jo.getString(KEY_BOOK_PRICE);
                book_status[i] = jo.getString(KEY_BOOK_STATUS);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}