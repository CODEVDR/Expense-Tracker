package com.example.expense_tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class customAdapter extends ArrayAdapter {

    private final String[]arr;
    public customAdapter(@NonNull Context context, int resource,@NonNull String[]arr) {
        super(context, resource, arr);
        this.arr=arr;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return arr[position];
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_layout,parent,false);
        TextView t = convertView.findViewById(R.id.textView);
        t.setText(getItem(position));
        return convertView;
    }
}
