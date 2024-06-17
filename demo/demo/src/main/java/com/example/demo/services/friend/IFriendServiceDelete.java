package com.example.demo.services.friend;

import com.example.demo.model.User;

public interface IFriendServiceDelete {
    boolean deleteFriends(User userImpl, User userDeleted);
}
