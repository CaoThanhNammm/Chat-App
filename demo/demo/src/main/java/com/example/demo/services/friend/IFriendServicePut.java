package com.example.demo.services.friend;

import com.example.demo.model.User;

public interface IFriendServicePut {
    boolean updateStatus(User userImpl, User userUpdated, int status);
}
