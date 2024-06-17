package com.example.demo.services.conversation;

import com.example.demo.model.Private;
import com.example.demo.model.User;

import java.util.List;

public interface IPrivateServiceGet {
    List<Private> findAll();
    Private isExist(Private c);
    Private isMessaging(User user1, User user2);
    List<Private> findPrivate(User user);
}
