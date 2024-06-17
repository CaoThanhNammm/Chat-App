package com.exam.chatapp.model.firebase;

public class FirebaseUser {
    private String id;
    private int avaibility;
    private String fcm;
    private boolean isLogin;

    public FirebaseUser() {
    }

    @Override
    public String toString() {
        return "FirebaseUser{" +
                "id='" + id + '\'' +
                ", avaibility=" + avaibility +
                ", fcm='" + fcm + '\'' +
                ", isLogin=" + isLogin +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAvaibility() {
        return avaibility;
    }

    public void setAvaibility(int avaibility) {
        this.avaibility = avaibility;
    }

    public String getFcm() {
        return fcm;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
