package com.exam.chatapp.model.firebase;

import com.exam.chatapp.model.Message;
import com.exam.chatapp.model.User;

import java.time.LocalDateTime;

public class FirebaseMessage {
    private User sender;
    private User receiver;
    private String message;
    private String time;

    public FirebaseMessage(User sender, User receiver, String message, String time) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.time = time;
    }

    public FirebaseMessage() {
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
