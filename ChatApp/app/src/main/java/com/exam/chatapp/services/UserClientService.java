package com.exam.chatapp.services;

import android.content.Context;

import com.exam.chatapp.dao.UserClientDao;
import com.exam.chatapp.model.User;
import com.exam.chatapp.model.VolleyCallback;

import org.json.JSONException;

public class UserClientService {
    private UserClientDao userDao;
    public static UserClientService instance;

    public UserClientService(Context context) {
        userDao = new UserClientDao(context);
    }

    public static UserClientService getInstance(Context context) {
        if (instance == null) instance = new UserClientService(context);
        return instance;
    }

    public void searchUser(String phoneImpl, String phoneSearch, final VolleyCallback callback) {
        userDao.searchUser(phoneImpl, phoneSearch, callback);
    }

    public void checkUser(String phone, String password, VolleyCallback cb) {
        userDao.checkUser(phone, password, cb);
    }

    public void changePassword(String phone, String newPass, String reNewPass, VolleyCallback cb) {
        userDao.changePassword(phone, newPass, reNewPass, cb);
    }

    public void getUserById(String id, VolleyCallback volleyCallback) {
        userDao.getUserById(id, volleyCallback);
    }

    public void add(User user, VolleyCallback volleyCallback)  {
        userDao.add(user, volleyCallback);
    }

    public void isExistPhone(String phone, VolleyCallback cb) {
        userDao.isExistPhone(phone, cb);
    }

    public void updateAvatar(User user) {
        userDao.updateAvatar(user);
    }
}
