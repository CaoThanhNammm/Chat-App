package com.exam.chatapp.model;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private String id;
    private User user;
    private Private conversation;
    private String message;
    private int status;
    private LocalDateTime time;

    public JSONObject toJson() throws JSONException {
        String json = (new Gson()).toJson(this);
        JSONObject jsonObject = new JSONObject(json);
        return jsonObject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Private getConversation() {
        return conversation;
    }

    public void setConversation(Private conversation) {
        this.conversation = conversation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Message(String id, User user, Private conversation, String message, int status, LocalDateTime time) {
        this.id = id;
        this.user = user;
        this.conversation = conversation;
        this.message = message;
        this.status = status;
        this.time = time;
    }

    public Message() {
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", conversation=" + conversation +
                ", message='" + message + '\'' +
                ", status=" + status +
                ", time=" + time +
                '}';
    }
}
