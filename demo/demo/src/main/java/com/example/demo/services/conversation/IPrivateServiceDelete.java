package com.example.demo.services.conversation;

import com.example.demo.model.User;

public interface IPrivateServiceDelete {
    boolean deleteById(String id);
    boolean deletePrivate(User user1);
}
