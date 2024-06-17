package com.exam.chatapp.services.firebase;

import android.content.Context;
import android.os.Build;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import com.exam.chatapp.adapter.MessageAdapter;
import com.exam.chatapp.dao.firebase.MessageFirebaseClientDao;
import com.exam.chatapp.dao.firebase.UserFirebaseClientDao;
import com.exam.chatapp.model.User;
import com.exam.chatapp.model.VolleyCallback;
import com.exam.chatapp.model.firebase.FirebaseMessage;

import java.util.List;

public class MessageFirebaseClientService {
    private MessageFirebaseClientDao dao;
    public static MessageFirebaseClientService instance;

    public MessageFirebaseClientService(Context context) {
        dao = new MessageFirebaseClientDao(context);
    }

    public static MessageFirebaseClientService getInstance(Context context) {
        if (instance == null) instance = new MessageFirebaseClientService(context);
        return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void save(FirebaseMessage transfer) {
        dao.save(transfer);
    }

    public void listenMessage(VolleyCallback cb) {
        dao.listenMessage(cb);
    }

    public void sendNotification(User user2, User user1, String message) {
        dao.sendNotification(user2, user1, message);
    }
}
