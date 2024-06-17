package com.example.demo.services.friend;

import com.example.demo.model.Friend;
import com.example.demo.model.User;
import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.List;
import java.util.UUID;

public interface IFriendServiceGet {
    List<Friend> getFriends(User id);

    List<Friend> findAllInvite(User user);
    int amountOfFriendInvite(User user);
}
