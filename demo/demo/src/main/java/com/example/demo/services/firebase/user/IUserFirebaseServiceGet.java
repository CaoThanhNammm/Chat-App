package com.example.demo.services.firebase.user;

import com.example.demo.model.firebase.User;

import java.util.concurrent.ExecutionException;

public interface IUserFirebaseServiceGet {
    User getUser(String id) throws ExecutionException, InterruptedException;
}
