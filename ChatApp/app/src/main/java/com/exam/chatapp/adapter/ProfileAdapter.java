package com.exam.chatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.exam.chatapp.R;

import java.util.List;

public class ProfileAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<String> funcs;

    public ProfileAdapter(Context context, int layout, List<String> funcs) {
        this.context = context;
        this.layout = layout;
        this.funcs = funcs;
    }

    @Override
    public int getCount() {
        return funcs.size();
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

        TextView tvNameFunc = view.findViewById(R.id.textViewNameFunc);
        String nameFuncs = funcs.get(i);
        ImageView imgSignal = view.findViewById(R.id.imageViewSignal);

        if (i == 0) {
            imgSignal.setImageResource(R.drawable.lock);
        } else if (i == 1) {
            imgSignal.setImageResource(R.drawable.profile);
        }
        else if(i == 2){
            imgSignal.setImageResource(R.drawable.log_out);
        }

        tvNameFunc.setText(nameFuncs);

        return view;
    }
}
