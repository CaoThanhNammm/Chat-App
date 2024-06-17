package com.exam.chatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.exam.chatapp.R;

import java.util.List;

public class OptionAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<String> options;

    public OptionAdapter(Context context, int layout, List<String> options) {
        this.context = context;
        this.layout = layout;
        this.options = options;
    }

    @Override
    public int getCount() {
        return options.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(layout, null);
        String option = options.get(i);

        TextView tvTitle = view.findViewById(R.id.textViewTitleOption);
        tvTitle.setText(option);

        return view;
    }
}
