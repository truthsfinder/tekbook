package com.tb.sell.tekbook;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SearchBooksAdapter extends ArrayAdapter<String> {
    private String[] user_id;
    private String[] firstname;
    private String[] lastname;
    private String[] book_class;
    private String[] book_price;
    private String[] book_status;
    private Activity context;

    public SearchBooksAdapter(Activity context, String[] user_id, String[] firstname, String[] lastname, String[] book_class, String[] book_price, String[] book_status) {
        super(context, R.layout.custom_listview_display_search, user_id);
        this.context = context;
        this.user_id = user_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.book_class = book_class;
        this.book_price = book_price;
        this.book_status = book_status;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.custom_listview_display_search, null, true);
        TextView tvUserName = (TextView) listViewItem.findViewById(R.id.user_name);
        TextView tvBookClass = (TextView) listViewItem.findViewById(R.id.book_class);
        TextView tvPrice = (TextView) listViewItem.findViewById(R.id.book_price);
        TextView tvStatus = (TextView) listViewItem.findViewById(R.id.book_status);
        tvUserName.setText(firstname[position] + " " + lastname[position]);
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