package com.exam.chatapp.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Private implements Serializable {
    private String id;
    private User user1;
    private User user2;
    private String lastMessage;
    private LocalDateTime lastTime;

    public Private() {
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

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public LocalDateTime getLastTime() {
        return lastTime;
    }

    public void setLastTime(LocalDateTime lastTime) {
        this.lastTime = lastTime;
    }

    @Override
    public String toString() {
        return "Private{" +
                "id='" + id + '\'' +
                ", user1=" + user1 +
                ", user2=" + user2 +
                ", lastMessage=" + lastMessage +
                '}';
    }
}
