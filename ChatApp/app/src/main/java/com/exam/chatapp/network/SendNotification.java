package com.exam.chatapp.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.exam.chatapp.config.AccessToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendNotification {
    private String userFcmToken;
    private String title;
    private String body;
    private Context context;

    private final String postUrl = "https://fcm.googleapis.com/v1/projects/chat-app-for-final-ae33d/messages:send";

    public SendNotification(String userFcmToken, String title, String body, Context context) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.context = context;
    }

    public void sendNotification() throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        JSONObject messageObject = new JSONObject();
        JSONObject notificationObject = new JSONObject();
        notificationObject.put("title", title);
        notificationObject.put("body", body);

        messageObject.put("token", userFcmToken);
        messageObject.put("notification", notificationObject);

        jsonObject.put("message", messageObject);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {
                try {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("message");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("notification");

                    String fcm = jsonObject1.getString("token");
                    String body = jsonObject2.getString("body");

                    Toast.makeText(context, body, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("notifyyyyyyyy", "onErrorResponse: " + volleyError.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                AccessToken accessToken = new AccessToken();
                String accessKey = accessToken.getAccessToken();
                Map<String, String> header = new HashMap<>();
                header.put("content-type", "application/json");
                header.put("authorization", "Bearer " + accessKey);

                Log.d("getHeaders", "getHeaders: " + accessKey);
                return header;
            }
        };

        requestQueue.add(request);
    }
}
