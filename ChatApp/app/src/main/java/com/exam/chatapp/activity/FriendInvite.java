package com.exam.chatapp.activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.VolleyError;
import com.exam.chatapp.R;
import com.exam.chatapp.adapter.UserAdapterFriendInvite;
import com.exam.chatapp.model.User;
import com.exam.chatapp.model.VolleyCallback;
import com.exam.chatapp.network.SessionManager;
import com.exam.chatapp.services.FriendClientService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendInvite extends AppCompatActivity {
    private UserAdapterFriendInvite userAdapterFriendInvite;
    private List<User> users;
    private ListView lvFriendInvite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_friend_invite);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mapping();
        showFriendInvite();
    }

    private void showFriendInvite() {
        SessionManager sessionManager = new SessionManager(this);
        String userId = sessionManager.getId();
        FriendClientService.getInstance(this).showFriendInvite(userId, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                JSONArray jsonArray = result.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    JSONObject jsonObject1 = jsonObject.getJSONObject("user1");

                    String id = jsonObject1.getString("id");
                    String name = jsonObject1.getString("fullname");
                    String phone = jsonObject1.getString("phone");

                    User user = new User();
                    user.setId(id);
                    user.setFullname(name);
                    user.setPhone(phone);

                    users.add(user);
                }
                UserAdapterFriendInvite adapterFriendInvite = new UserAdapterFriendInvite(FriendInvite.this, R.layout.row_user_friend_invite, users);
                lvFriendInvite.setAdapter(adapterFriendInvite);
//                userAdapterFriendInvite.notifyDataSetChanged();
            }
            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void mapping(){
        users = new ArrayList<>();
        lvFriendInvite = findViewById(R.id.listViewFriendInvite);
    }
}