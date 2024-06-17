package com.exam.chatapp.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.ctc.wstx.shaded.msv_core.verifier.jarv.Const;
import com.exam.chatapp.config.Constant;
import com.exam.chatapp.model.User;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    // Constructor
    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveUser(User user) {
        editor.putString(Constant.KEY_ID_USER, user.getId());
        editor.putString(Constant.KEY_PHONE_USER, user.getPhone());
        editor.putBoolean(Constant.KEY_IS_LOGIN_USER, true);
        editor.putString(Constant.KEY_AVATAR_USER, user.getEncodedImage());
        editor.putString(Constant.KEY_NAME_USER, user.getFullname());
        editor.commit();
    }

    public String getId(){
        return pref.getString(Constant.KEY_ID_USER, "");
    }
    public String getPhone(){
        return pref.getString(Constant.KEY_PHONE_USER, "");
    }

    public String getAvatar(){
        return pref.getString(Constant.KEY_AVATAR_USER, "");
    }

    public String getFullname(){
        return pref.getString(Constant.KEY_NAME_USER, "");
    }

    public String getFcm(){
        return pref.getString(Constant.KEY_FCM_USER, "");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(Constant.KEY_IS_LOGIN_USER, false);
    }

    public void removeSession() {
        editor.remove(Constant.KEY_ID_USER);
        editor.remove(Constant.KEY_PHONE_USER);
        editor.remove(Constant.KEY_AVATAR_USER);
        editor.remove(Constant.KEY_NAME_USER);
        editor.remove(Constant.KEY_IS_LOGIN_USER);
        editor.remove(Constant.KEY_FCM_USER);
        editor.commit();
    }

    public void saveFcm(String s) {
        editor.putString(Constant.KEY_FCM_USER, s);
        editor.commit();
    }
}
