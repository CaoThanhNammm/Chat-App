package com.exam.chatapp.dao.firebase;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.exam.chatapp.config.Config;
import com.exam.chatapp.config.Constant;
import com.exam.chatapp.model.VolleyCallback;
import com.exam.chatapp.model.firebase.FirebaseUser;
import com.exam.chatapp.network.SessionManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirestoreKt;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserFirebaseClientDao {
    private final String TAG = "User firebase Dao";
    private Context context;

    public UserFirebaseClientDao(Context context) {
        this.context = context;
    }

    public void add(FirebaseUser user) {
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/firebase/user/save";
        JSONObject userJson = new JSONObject();
        try {
            userJson.put("id", user.getId());
            userJson.put("avaibility", user.getAvaibility());
            userJson.put("fcm", user.getFcm());
            userJson.put("isLogin", user.isLogin());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, userJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d(TAG, "onResponse: them nguoi dung len firebase thanh cong");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "onResponse: " + volleyError.toString());
            }
        }){
            @Nullable
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public void updateAvaibility(String id, int avai) {
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/firebase/user/update/avaibility?id="+id +"&avai="+avai;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d(TAG, "onResponse: cap nhap avai thanh cong");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "onResponse: " + volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void updateIsLogin(String id, boolean isLogin) {
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/firebase/user/update/isLogin?id="+id +"&isLogin="+isLogin;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d(TAG, "onResponse: cap nhap islogin thanh cong");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "onResponse: " + volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void getUser(String id, VolleyCallback cb) {
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/firebase/user/get?id="+id;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            cb.onSuccess(jsonObject);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Log.d(TAG, "onResponse: lay nguoi dung thanh cong");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "onResponse: " + volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void updateFcm(String id) {
        getFcm(id);
    }

    public void updateStatus(String userId, VolleyCallback cb) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection(Constant.KEY_COLLECTION_USER).document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    return;
                }

                if(value != null){
                    long avai = value.getLong(Constant.KEY_AVAIBILITY_USER);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(Constant.KEY_AVAIBILITY_USER, avai);
                        cb.onSuccess(jsonObject);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void getFcm(String id) {
        SessionManager sessionManager = new SessionManager(context);
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                sessionManager.saveFcm(s);
                String url = "http://" + Config.IP_ADDRESS + ":8080/api/firebase/user/update/fcm?id="+id+"&fcm="+s;

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Log.d(TAG, "onResponse: cap nhap fcm thanh cong");
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "onResponse: " + volleyError.toString());
                    }
                });

                requestQueue.add(jsonObjectRequest);
            }
        });
    }
}
