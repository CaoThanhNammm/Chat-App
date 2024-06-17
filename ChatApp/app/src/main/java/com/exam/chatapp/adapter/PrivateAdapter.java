package com.exam.chatapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.exam.chatapp.R;
import com.exam.chatapp.model.Private;

import java.time.LocalDateTime;
import java.util.List;

public class PrivateAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Private> privateList;

    public PrivateAdapter(Context context, int layout, List<Private> privateList) {
        this.context = context;
        this.layout = layout;
        this.privateList = privateList;
    }

    @Override
    public int getCount() {
        return privateList.size();
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
        TextView tvNameConversation = view.findViewById(R.id.textViewNameConversation);
        TextView tvLastMessage = view.findViewById(R.id.textViewLastMess);
        TextView tvTimeLastMess = view.findViewById(R.id.textViewTimeLastMess);
        ImageView imgViewAvatarConversation = view.findViewById(R.id.imageViewAvatarRowConversation);

        Private conversation = privateList.get(i);

        String encodedImg = conversation.getUser2().getEncodedImage();

        if(encodedImg != null){
            imgViewAvatarConversation.setImageBitmap(stringToBitmap(encodedImg));
        }else{
            imgViewAvatarConversation.setImageResource(R.drawable.avatar_default);
        }

        tvNameConversation.setText(conversation.getUser2().getFullname());
        tvLastMessage.setText(conversation.getLastMessage());
        tvLastMessage.setEllipsize(TextUtils.TruncateAt.END);
        LocalDateTime localDateTime = conversation.getLastTime();
        String timeStamp = localDateTime.getHour() + ":" + localDateTime.getMinute() + ":"+localDateTime.getSecond();
        tvTimeLastMess.setText(timeStamp);

        return view;
    }

    private Bitmap stringToBitmap(String avatar){
        byte[] bytes = Base64.decode(avatar, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
