package com.exam.chatapp.dao;

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
import com.exam.chatapp.model.User;
import com.exam.chatapp.model.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserClientDao {
    private Context context;

    public UserClientDao(Context context) {
        this.context = context;
    }

    public void searchUser(String phoneImpl, String phoneSearch, VolleyCallback callback) {
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/users/search?phone1=" + phoneImpl + "&phone2=" + phoneSearch;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            callback.onSuccess(jsonObject);
                            Log.d("User dao het loi", "tim nguoi dung thanh cong");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("User dao loi", "onResponse: " + volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void checkUser(String phone, String password, VolleyCallback cb) {
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/users/isUser?phone=" + phone + "&password=" + password;
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
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                cb.onError(volleyError);
                Log.d("loiiiiiiiiiiii", "onResponse: " + volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void changePassword(String phone, String newPass, String reNewPass, VolleyCallback cb) {
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/users/updatePassword?phone=" + phone + "&newPass=" + newPass + "&reNewPassword=" + reNewPass;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            cb.onSuccess(jsonObject);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Log.d("User dao het loiiiiiiiiiiii", "onResponse: cap nhap mat khau thanh cong");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("User dao loiiiiiiiiiiii", "onResponse: " + volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void getUserById(String id, VolleyCallback volleyCallback) {
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/users/id/" + id;
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
                        Log.d("User dao het loiiiiiiiiiiii", "onResponse: lay thong tin nguoi dung thanh cong");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("User dao loiiiiiiiiiiii", "onResponse: " + volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void add(User user, VolleyCallback volleyCallback) {
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/users/save";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("phone", user.getPhone());
            jsonObject.put("password", user.getPassword());
            jsonObject.put("fullname", user.getFullname());
            jsonObject.put("dob", user.getDob());
            jsonObject.put("avatar", user.getEncodedImage());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            volleyCallback.onSuccess(jsonObject);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Log.d("User dao het loiiiiiiiiiiii", "onResponse: lay thong tin nguoi dung thanh cong");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("User dao loiiiiiiiiiiii", "onResponse: " + volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void isExistPhone(String phone, VolleyCallback cb) {
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/users/exist_phone?phone=" + phone;
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
                        Log.d("User dao het loiiiiiiiiiiii", "onResponse: kiem tra sdt thanh cong");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                cb.onError(volleyError);
                Log.d("User dao loiiiiiiiiiiii", "onResponse: " + volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void updateAvatar(User user) {
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/users/update/avatar";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", user.getId());
            jsonObject.put("avatar", user.getEncodedImage());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Toast.makeText(context, "Đổi thành công", Toast.LENGTH_SHORT).show();
                        Log.d("User dao het loiiiiiiiiiiii", "onResponse: doi anh dai dien thanh cong");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("User dao loiiiiiiiiiiii", "onResponse: " + volleyError.toString());
            }
        }) {
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
}
