package com.exam.chatapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.exam.chatapp.R;
import com.exam.chatapp.model.firebase.FirebaseMessage;
import com.exam.chatapp.network.SessionManager;

import java.util.List;

public class MessageAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<FirebaseMessage> chatTransfers;

    public MessageAdapter(Context context, List<FirebaseMessage> chatTransfers) {
        this.context = context;
        this.chatTransfers = chatTransfers;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return chatTransfers.size();
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
    public int getViewTypeCount() {
        return 2; // Tin nhắn của người khác và tin nhắn của bạn
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (!chatTransfers.isEmpty()) {
            String senderId = chatTransfers.get(i).getSender().getId();
            SessionManager sessionManager = new SessionManager(context);
            String currentId = sessionManager.getId();

            if (senderId != null) {
                if (senderId.equals(currentId)) {
                    view = inflater.inflate(R.layout.row_message_right, viewGroup, false);
                } else {
                    view = inflater.inflate(R.layout.row_message_left, viewGroup, false);
                }
            }

            ImageView avatar = view.findViewById(R.id.imageViewAvatarMessage);
            TextView name = view.findViewById(R.id.textViewNameMessage);
            TextView messageContent = view.findViewById(R.id.textViewMessageContentMessage);
            TextView messageTime = view.findViewById(R.id.textViewMessageTimeMessage);


            FirebaseMessage chatTransfer = chatTransfers.get(i);
            String encodedImg = chatTransfer.getSender().getEncodedImage();
            String senderName = chatTransfer.getSender().getFullname();
            String content = chatTransfer.getMessage();
            String time = chatTransfer.getTime();

            if (encodedImg != null) {
                avatar.setImageBitmap(stringToBitmap(encodedImg));
            } else {
                avatar.setImageResource(R.drawable.avatar_default);
            }

            if (senderName != null) {
                name.setText(senderName);
            }

            if (content != null) {
                messageContent.setText(content);
                messageContent.setEllipsize(null);
            }


            if (time != null) {
                messageTime.setText(time);
            }

            FrameLayout frameLayout = view.findViewById(R.id.frameLayoutMessage);
            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (messageTime.getVisibility() == View.INVISIBLE) {
                        messageTime.setVisibility(View.VISIBLE);
                        Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
                        messageTime.startAnimation(slideUp);
                    } else {
                        messageTime.setVisibility(View.INVISIBLE);
                    }
                }
            });
            return view;
        }
        return null;
    }

    private Bitmap stringToBitmap(String avatar) {
        byte[] bytes = Base64.decode(avatar, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
