package com.exam.chatapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.exam.chatapp.activity.Chat;
import com.exam.chatapp.activity.FriendInvite;
import com.exam.chatapp.R;
import com.exam.chatapp.adapter.UserAdapterFriends;
import com.exam.chatapp.config.Constant;
import com.exam.chatapp.model.User;
import com.exam.chatapp.model.VolleyCallback;
import com.exam.chatapp.network.SessionManager;
import com.exam.chatapp.services.FriendClientService;
import com.exam.chatapp.services.firebase.UserFirebaseClientService;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentContact extends Fragment {
    private UserAdapterFriends userAdapter;
    private TextView tvQuantityInvite;
    private ListView lvUser;
    private boolean isLongClick = false;
    private List<User> users;
    private LinearLayout layoutFriendInvite;
    private SessionManager sessionManager;
    private FirebaseFirestore db;
    private ProgressBar loadingFriends;
    private ProgressBar loadingChangeToChat;
    private FrameLayout frameLayoutContact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        mapping(view);
        onClickItemLvFriend();
        onClickListFriendInvite();
        onLongClickItemLvFriend();
        showQuantiyInvite();
        showFriends();
        return view;
    }

    private void onLongClickItemLvFriend() {
        lvUser.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                isLongClick = true;
                User userImpl = new User();
                User userDeleted = new User();

                userImpl.setId(sessionManager.getId());
                userDeleted.setId(users.get(i).getId());

                FriendClientService.getInstance(getContext()).deleteFriend(userImpl, userDeleted, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) throws JSONException {
                        isLongClick = false;
                    }

                    @Override
                    public void onError(VolleyError error) {
                        isLongClick = false;
                    }
                });

                users.remove(i);
                userAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void showQuantiyInvite() {
        User user = new User();
        user.setId(sessionManager.getId());
        FriendClientService.getInstance(getContext()).quantityInvite(user, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                int quantity = Integer.parseInt(result.getString("data"));
                if (quantity > 0) {
                    tvQuantityInvite.setText("(" + quantity + ")");
                } else {
                    tvQuantityInvite.setText("");
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void onClickItemLvFriend() {
        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!isLongClick) {
                    isLoadingChangeToChat(true);

                    User user1 = new User();
                    user1.setId(sessionManager.getId());
                    user1.setFullname(sessionManager.getFullname());
                    user1.setPhone(sessionManager.getPhone());
                    user1.setEncodedImage(sessionManager.getAvatar());
                    user1.setFcmToken(sessionManager.getFcm());

                    User user2 = users.get(i);
                    Intent intent = new Intent(getContext(), Chat.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user2", user2);
                    bundle.putSerializable("user1", user1);
                    intent.putExtra("data", bundle);

                    startActivity(intent);
                }
            }
        });
    }

    private void showFriends() {
        isLoading(true);

        userAdapter = new UserAdapterFriends(getContext(), R.layout.row_user_friends, users);
        lvUser.setAdapter(userAdapter);

        FriendClientService.getInstance(getContext()).showFriends(sessionManager.getId(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        User user = new User();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("user2");
                        String user2Id = jsonObject2.getString("id");
                        String name2Id = jsonObject2.getString("fullname");
                        String phone2Id = jsonObject2.getString("phone");
                        String avatar2Id = jsonObject2.getString("avatar");

                        UserFirebaseClientService.getInstance(getContext()).getUser(user2Id, new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject result) throws JSONException {
                                JSONObject userJson = result.getJSONObject("data");
                                String fcm = userJson.getString(Constant.KEY_FCM_USER);

                                user.setId(user2Id);
                                user.setFullname(name2Id);
                                user.setPhone(phone2Id);
                                user.setEncodedImage(avatar2Id);
                                user.setFcmToken(fcm);

                                users.add(user);
                                userAdapter.notifyDataSetChanged();
                                isLoading(false);
                            }

                            @Override
                            public void onError(VolleyError error) {
                                isLoading(false);
                            }
                        });
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                isLoading(false);
            }
        });

    }

    private void onClickListFriendInvite() {
        layoutFriendInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FriendInvite.class);
                startActivity(intent);
            }
        });
    }

    private void isLoading(boolean loading) {
        if (loading) {
            loadingFriends.setVisibility(View.VISIBLE);
        } else {
            loadingFriends.setVisibility(View.INVISIBLE);
        }
    }

    private void isLoadingChangeToChat(boolean loading) {
        if (loading) {
            frameLayoutContact.setAlpha(0.5f);
            loadingChangeToChat.setVisibility(View.VISIBLE);
        } else {
            frameLayoutContact.setAlpha(1f);
            loadingChangeToChat.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isLoadingChangeToChat(false);
    }


    private void mapping(View view) {
        users = new ArrayList<>();
        lvUser = view.findViewById(R.id.listViewListFriends);
        layoutFriendInvite = view.findViewById(R.id.layoutFriendInvite);

        tvQuantityInvite = view.findViewById(R.id.textViewQuantityInvite);
        sessionManager = new SessionManager(getContext());
        db = FirebaseFirestore.getInstance();
        loadingFriends = view.findViewById(R.id.progressBarContact);
        loadingChangeToChat = view.findViewById(R.id.progressBarChangeToChat);
        frameLayoutContact = view.findViewById(R.id.frameLayoutContact);
    }
}