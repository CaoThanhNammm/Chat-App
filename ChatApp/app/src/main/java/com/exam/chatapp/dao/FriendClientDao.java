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
import com.exam.chatapp.model.Friend;
import com.exam.chatapp.model.User;
import com.exam.chatapp.model.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class FriendClientDao {
    private Context context;

    public FriendClientDao(Context context) {
        this.context = context;
    }

    public void save(Friend friend){
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/friends/save?id1="+friend.getUser1().getId() + "&id2="+friend.getUser2().getId();

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Toast.makeText(context, "Đã gửi lời mời", Toast.LENGTH_SHORT).show();
                        Log.d("Friend Dao het loi", "onResponse: gui loi moi ket ban thanh cong");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Friend Dao loi", "onResponse: " + volleyError.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void showFriendInvite(String userId, final VolleyCallback callback) {
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/friends/invite?id=" + userId;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("Friend Dao het loi", "onResponse: lay ra loi moi ket ban thanh cong");
                        try {
                            callback.onSuccess(jsonObject);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Friend Dao loi", "onResponse: " + volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void updateStatus(User user1, User user2, int status) {
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/friends/update?id1="+user1.getId()+"&id2="+user2.getId()+"&status="+status;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("Friend Dao het loi", "onResponse: cap nhap trang thai thanh cong");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Friend Dao loi", "onResponse: " + volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void showFriends(String id, final VolleyCallback callback) {
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/friends/id/"+id;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            callback.onSuccess(jsonObject);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Log.d("Friend dao het loiiiiiiiii", "onResponse: lay danh sach ban be thanh cong");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onError(volleyError);
                Log.d("Friend dao loiiiiiiiiii", "onResponse: " + volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    public void deleteFriend(User user, User user1, VolleyCallback callback) {
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/friends/delete?id1="+user.getId()+"&id2="+user1.getId();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            callback.onSuccess(jsonObject);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Log.d("Friend dao het loiiiiiiiiii", "onResponse: xoa ban be thanh cong");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onError(volleyError);
                Log.d("Friend dao loiiiiiiiiii", "onResponse: " + volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void quantityInvite(User user, VolleyCallback callback) {
        String url = "http://" + Config.IP_ADDRESS + ":8080/api/friends/count_invite?id="+user.getId();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            callback.onSuccess(jsonObject);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Log.d("Friend dao het loiiiiiiiiii", "onResponse: lay so luong loi moi ket ban thanh cong");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Friend dao loiiiiiiiiii", "onResponse: " + volleyError.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
