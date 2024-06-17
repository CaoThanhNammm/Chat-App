package com.exam.chatapp.services;

import android.content.Context;

import com.exam.chatapp.dao.FriendClientDao;
import com.exam.chatapp.model.Friend;
import com.exam.chatapp.model.User;
import com.exam.chatapp.model.VolleyCallback;

public class FriendClientService {
    private FriendClientDao friendDao;
    public static FriendClientService instance;

    public FriendClientService(Context context) {
        friendDao = new FriendClientDao(context);
    }

    public static FriendClientService getInstance(Context context){
        if(instance == null) instance = new FriendClientService(context);
        return instance;
    }

    public void save(Friend friend){
        friendDao.save(friend);
    }

    public void showFriendInvite(String userId, final VolleyCallback callback) {
        friendDao.showFriendInvite(userId, callback);
    }

    public void updateStatus(User user1, User user2, int status) {
        friendDao.updateStatus(user1, user2, status);
    }

    public void showFriends(String id, VolleyCallback callback) {
        friendDao.showFriends(id, callback);
    }

    public void deleteFriend(User user, User user1, VolleyCallback cb) {
        friendDao.deleteFriend(user, user1, cb);
    }

    public void quantityInvite(User user, final VolleyCallback callback) {
        friendDao.quantityInvite(user, callback);
    }
}
