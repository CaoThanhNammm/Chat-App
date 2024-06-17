package com.example.demo.services.user;

import com.example.demo.model.User;

import java.util.List;
import java.util.UUID;

public interface IUserServiceGet {
    List<User> findAll();
    List<User> findAllByPhone(User userImpl, User userSearch);

    User getUserById(String id);

    User getUserByPhone(String phone);

    String isExistPhone(String phone);
    boolean isValidPhone(String phone);

    boolean isValidPassword(String password);

    String encodePassword(String password);

    boolean checkPassword(String password, String encodePassword);

    User isUser(String phone, String password);
}
