package com.tb.sell.tekbook;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BooksAdapter extends ArrayAdapter<String> {
    private String[] book_id;
    private String[] book_class;
    private String[] book_title;
    private String[] book_description;
    private String[] book_price;
    private String[] book_status;
    private Activity context;

    public BooksAdapter(Activity context, String[] book_id, String[] book_class, String[] book_title, String[] book_description, String[] book_price, String[] book_status) {
        super(context, R.layout.custom_listview_display, book_id);
        this.context = context;
        this.book_id = book_id;
        this.book_class = book_class;
        this.book_title = book_title;
        this.book_description = book_description;
        this.book_price = book_price;
        this.book_status = book_status;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.custom_listview_display, null, true);
        TextView tvBookClass = (TextView) listViewItem.findViewById(R.id.book_class);
        TextView tvPrice = (TextView) listViewItem.findViewById(R.id.book_price);
        TextView tvStatus = (TextView) listViewItem.findViewById(R.id.book_status);
        tvBookClass.setText(book_class[position]);
        tvPrice.setText("₱"+book_price[position]);
        tvStatus.setText(book_status[position]);

        if(tvStatus.getText().toString().trim().equals("sold")){
            tvStatus.setTextColor(Color.parseColor("#DF0000"));
        }else{
            tvStatus.setTextColor(Color.parseColor("#5eba7d"));
        }

        return listViewItem;

    }
}