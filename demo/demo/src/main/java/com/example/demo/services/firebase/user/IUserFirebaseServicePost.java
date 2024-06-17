package com.example.demo.services.firebase.user;

import com.example.demo.model.firebase.User;

import java.util.concurrent.ExecutionException;

public interface IUserFirebaseServicePost {
    User save(User user) throws ExecutionException, InterruptedException;
}
