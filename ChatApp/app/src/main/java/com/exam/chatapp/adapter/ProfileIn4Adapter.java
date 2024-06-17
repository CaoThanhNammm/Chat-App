package com.exam.chatapp.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.exam.chatapp.R;
import com.exam.chatapp.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ProfileIn4Adapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<String> in4s;

    public ProfileIn4Adapter(Context context, int layout, List<String> in4s) {
        this.context = context;
        this.layout = layout;
        this.in4s = in4s;
    }

    @Override
    public int getCount() {
        return in4s.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(layout, null);
        String in4 = in4s.get(i);

        TextView tvTitle = view.findViewById(R.id.textViewTitleIn4Row);
        TextView tvIn4 = view.findViewById(R.id.textViewIn4Row);

        if (i == 0) {
            tvTitle.setText("Họ và tên:");
        } else if (i == 1) {
            tvTitle.setText("Số điện thoại:");
        } else if (i == 2) {
            tvTitle.setText("Ngày sinh:");
        } else if (i == 3) {
            tvTitle.setText("Ngày tham gia:");
        }

        tvIn4.setText(in4);
        return view;
    }
}
