package com.exam.chatapp.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.exam.chatapp.config.Config;
import com.exam.chatapp.model.Private;
import com.exam.chatapp.model.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;


public class PrivateClientDao {
    private Context context;
    public PrivateClientDao(Context context) {
        this.context = context;
    }


    public void isExist(String converId, VolleyCallback volleyCallback) {
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/private/exist?id="+converId;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            volleyCallback.onSuccess(jsonObject);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Toast.makeText(context, "ton tai", Toast.LENGTH_SHORT).show();
                        Log.d("Conversation Dao", "Kiem tra ton tai thanh cong");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("loiiiiiiiiiiii", "onResponse: " + volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void isMessaging(String id1, String id2, VolleyCallback callback) {
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/private/is_messaging?id1=" + id1 + "&id2=" + id2;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            callback.onSuccess(jsonObject);
                            Log.d("User conver Dao het loi", "kiem tra da nhan tin bao gio chua thanh cong");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onError(volleyError);
                Log.d("User conver Dao loi", "onResponse: " + volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void find(String userId, VolleyCallback volleyCallback) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/private/find?id=" + userId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            volleyCallback.onSuccess(jsonObject);
                            Log.d("User conver Dao het loi", "lay ra cac cuoc tin nhan thanh cong");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyCallback.onError(volleyError);
                Log.d("User conver Dao loi", "onResponse: " + volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void updateLastMessage(Private conversation) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/private/update/last_message?id1=" + conversation.getUser1().getId() + "&id2=" + conversation.getUser2().getId() + "&lastMessage=" + conversation.getLastMessage() + "&lastTime="+conversation.getLastTime();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("User conver Dao het loi", "cap nhap tin nhan moi nhat thanh cong");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("User conver Dao loi", "onResponse: " + volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void createPrivate(Private conversation, VolleyCallback cb) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/private/save?id1="+conversation.getUser1().getId() + "&id2="+conversation.getUser2().getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            cb.onSuccess(jsonObject);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Log.d("User conver Dao het loi", "tao nhom moi thanh cong");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("User conver Dao loi", "onResponse: " + volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
