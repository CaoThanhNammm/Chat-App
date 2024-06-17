package com.exam.chatapp.services;

import android.content.Context;

import com.exam.chatapp.dao.MessageClientDao;
import com.exam.chatapp.model.Message;
import com.exam.chatapp.model.VolleyCallback;

public class MessageClientService {
    private MessageClientDao messageDao;
    public static MessageClientService instance;

    public MessageClientService(Context context) {
        messageDao = new MessageClientDao(context);
    }

    public static MessageClientService getInstance(Context context){
        if(instance == null) instance = new MessageClientService(context);
        return instance;
    }

    public void findAllMessageByUserId(String converId, final VolleyCallback callback){
        messageDao.findAllMessageByUserId(converId, callback);
    }

    public void save(Message msg, VolleyCallback cb) {
        messageDao.save(msg, cb);
    }
}
