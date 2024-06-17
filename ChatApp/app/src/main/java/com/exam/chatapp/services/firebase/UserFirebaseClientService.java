package com.exam.chatapp.services.firebase;

import android.content.Context;

import com.exam.chatapp.dao.FriendClientDao;
import com.exam.chatapp.dao.firebase.UserFirebaseClientDao;
import com.exam.chatapp.model.VolleyCallback;
import com.exam.chatapp.model.firebase.FirebaseUser;
import com.exam.chatapp.services.FriendClientService;

public class UserFirebaseClientService {
    private UserFirebaseClientDao dao;
    public static UserFirebaseClientService instance;

    public UserFirebaseClientService(Context context) {
        dao = new UserFirebaseClientDao(context);
    }

    public static UserFirebaseClientService getInstance(Context context){
        if(instance == null) instance = new UserFirebaseClientService(context);
        return instance;
    }

    public void add(FirebaseUser user){
        dao.add(user);
    }
    public void updateAvaibility(String id, int avai){
        dao.updateAvaibility(id, avai);
    }
    public void updateIsLogin(String id, boolean isLogin){
        dao.updateIsLogin(id, isLogin);
    }

    public void getUser(String id, VolleyCallback cb){
        dao.getUser(id, cb);
    }

    public void updateFcm(String id) {
        dao.updateFcm(id);
    }

    public void updateStatus(String userId, VolleyCallback volleyCallback) {
        dao.updateStatus(userId, volleyCallback);
    }
}
