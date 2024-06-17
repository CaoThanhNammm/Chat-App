package com.example.demo.services.firebase.user;

import com.example.demo.model.firebase.User;

import java.util.concurrent.ExecutionException;

public interface IUserFirebaseServicePut {
    String updateAvai(String id, int avai) throws ExecutionException, InterruptedException;
    String updateIslogin(String id, boolean isLoggin) throws ExecutionException, InterruptedException;
    String updateFcm(String id, String fcm) throws ExecutionException, InterruptedException;
}
