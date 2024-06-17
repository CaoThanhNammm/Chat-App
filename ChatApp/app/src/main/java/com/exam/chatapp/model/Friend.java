package com.exam.chatapp.model;

import java.io.Serializable;

public class Friend implements Serializable {
    private String id;
    private User user1;
    private User user2;
    private int status;
    public static final int WAITING = 0;
    public static final int AGREE = 1;
    public static final int DENIED = 2;

    public Friend(String id, User user1, User user2, int status) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.status = status;
    }

    public Friend() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id='" + id + '\'' +
                ", user1=" + user1 +
                ", user2=" + user2 +
                ", status=" + status +
                '}';
    }
}
