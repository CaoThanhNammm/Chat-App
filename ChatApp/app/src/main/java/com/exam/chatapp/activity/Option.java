package com.exam.chatapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.exam.chatapp.R;
import com.exam.chatapp.adapter.OptionAdapter;

import java.util.ArrayList;
import java.util.List;

public class Option extends AppCompatActivity {
    private List<String> options;
    private OptionAdapter optionAdapter;
    private ListView lvOption;
    private TextView tvConverName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_option);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mapping();
        setAdapterOption();
        showOption();
        showConversationName();
    }

    private void showOption() {
        options.add("Đổi hình nền");
        options.add("Đổi ảnh đại diện");
        optionAdapter.notifyDataSetChanged();
    }

    private void showConversationName(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("title");

        String converName = bundle.getString("conversationName");
        tvConverName.setText(converName);
    }

    private void setAdapterOption(){
        optionAdapter = new OptionAdapter(this, R.layout.row_option, options);
        lvOption.setAdapter(optionAdapter);
    }

    private void mapping(){
        tvConverName = findViewById(R.id.textViewConversationNameOption);

        options = new ArrayList<>();
        lvOption = findViewById(R.id.listViewOption);
    }

}