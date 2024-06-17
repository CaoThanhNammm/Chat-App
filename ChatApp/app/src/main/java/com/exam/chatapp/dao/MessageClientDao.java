package com.exam.chatapp.dao;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.exam.chatapp.config.Config;
import com.exam.chatapp.model.Message;
import com.exam.chatapp.model.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageClientDao {
    private Context context;

    public MessageClientDao(Context context) {
        this.context = context;
    }

    public void findAllMessageByUserId(String converId, final VolleyCallback callback) {
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/message?converId="+converId;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            callback.onSuccess(jsonObject);
                            Log.d("Message Dao het loi", "lay tin nhan cua conver thanh cong");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Message Dao loi", "onResponse: " + volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void save(Message msg, VolleyCallback cb) {
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/message/save?userId="+msg.getUser().getId()+"&converId="+msg.getConversation().getId()+"&content="+msg.getMessage()+"&lastTime="+msg.getTime();

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            cb.onSuccess(jsonObject);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Log.d("Message Dao het loi", "luu tin nhan thanh cong");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                cb.onError(volleyError);
                Log.d("Message Dao loi", "onResponse: " + volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

//    public void listenForMessages(VolleyCallback cb) {
//        String url = "http://" + Config.IP_ADDRESS + ":8080/api/message/save?userId="+msg.getUser().getId()+"&converId="+msg.getConversation().getId()+"&content="+msg.getMessage();
//
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject jsonObject) {
//                        try {
//                            cb.onSuccess(jsonObject);
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                        Log.d("Message Dao het loi", "luu tin nhan thanh cong");
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                cb.onError(volleyError);
//                Log.d("Message Dao loi", "onResponse: " + volleyError.toString());
//            }
//        });
//
//        requestQueue.add(jsonObjectRequest);
//    }
}
