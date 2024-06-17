package com.example.demo.services.user;

import com.example.demo.model.User;

public interface IUserServicePut {
    User updatePassword(User user);
    User updateVisibility(String phone, int visibility);
    User updateAvatar(User user);
}
