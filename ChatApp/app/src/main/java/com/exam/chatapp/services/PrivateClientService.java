package com.exam.chatapp.services;

import android.content.Context;
import com.exam.chatapp.dao.PrivateClientDao;
import com.exam.chatapp.model.Private;
import com.exam.chatapp.model.VolleyCallback;

public class PrivateClientService {
    private PrivateClientDao privateDao;
    public static PrivateClientService instance;

    public PrivateClientService(Context context) {
        privateDao = new PrivateClientDao(context);
    }

    public static PrivateClientService getInstance(Context context) {
        if (instance == null) instance = new PrivateClientService(context);
        return instance;
    }

    public void isMessaging(String userId, String id, final VolleyCallback callback) {
        privateDao.isMessaging(userId, id, callback);
    }

    public void find(String userId, VolleyCallback volleyCallback) {
        privateDao.find(userId, volleyCallback);
    }

    public void updateLastMessage(Private conversation) {
        privateDao.updateLastMessage(conversation);
    }

    public void createPrivate(Private conversation, VolleyCallback cb) {
        privateDao.createPrivate(conversation, cb);
    }
}
