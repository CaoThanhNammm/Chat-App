package com.exam.chatapp.model;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public interface VolleyCallback {
    void onSuccess(JSONObject result) throws JSONException;

    void onError(VolleyError error);
}
