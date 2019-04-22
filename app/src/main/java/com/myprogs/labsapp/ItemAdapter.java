package com.myprogs.labsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    String[] labs;
    String[] themes;

    public ItemAdapter(Context context, String[] labs, String[] themes) {
        this.labs = labs;
        this.themes = themes;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return labs.length;
    }

    @Override
    public Object getItem(int position) {
        return labs[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.labs_listview_detail, null);
        TextView labTextView = view.findViewById(R.id.labTextView);
        TextView themeTextView = view.findViewById(R.id.themeTextView);

        String labName = labs[position];
        String themeName = themes[position];

        labTextView.setText(labName);
        themeTextView.setText(themeName);
        return view;
    }
}
