package com.exam.chatapp.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.android.volley.VolleyError;
import com.exam.chatapp.activity.Chat;
import com.exam.chatapp.R;
import com.exam.chatapp.adapter.UserAdapterUser;
import com.exam.chatapp.adapter.PrivateAdapter;
import com.exam.chatapp.model.Private;
import com.exam.chatapp.model.User;
import com.exam.chatapp.model.VolleyCallback;
import com.exam.chatapp.network.SessionManager;
import com.exam.chatapp.services.PrivateClientService;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {
    private SearchView svIconSearch;
    private ListView lvConversation;
    private List<Private> privateList;
    private PrivateAdapter privateAdapter;
    private UserAdapterUser userAdapterUser;
    private ListView lvUserSearch;
    private ImageButton imgButtonCreateGroup;
    private List<User> users;
    private FirebaseFirestore db;
    private ProgressBar loadingConversation;
    private ProgressBar loadingChangeToChat;
    private FrameLayout frameLayoutHome;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        mapping(view);
        onClickSearchIcon();
        showPrivate();
        onClickConversation();
        return view;
    }

    private void onClickConversation() {
        lvConversation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isLoadingChangeToChat(true);
                User user1 = new User();
                user1.setId(sessionManager.getId());
                user1.setPhone(sessionManager.getPhone());
                user1.setFullname(sessionManager.getFullname());
                user1.setEncodedImage(sessionManager.getAvatar());

                User user2 = privateList.get(i).getUser2();

                Intent intent = new Intent(getContext(), Chat.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user1", user1);
                bundle.putSerializable("user2", user2);

                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
    }

    private void onClickSearchIcon() {
        userAdapterUser = new UserAdapterUser(getContext(), R.layout.row_user, users);
        lvUserSearch.setAdapter(userAdapterUser);

        svIconSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                userAdapterUser.getFilter().filter(s.trim());
                userAdapterUser.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                userAdapterUser.getFilter().filter(s.trim());
                userAdapterUser.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void showPrivate() {
        privateAdapter = new PrivateAdapter(getContext(), R.layout.row_conversation, privateList);
        lvConversation.setAdapter(privateAdapter);
        readUserConversationJson();
    }

    private void readUserConversationJson() {
        PrivateClientService.getInstance(getContext()).find(sessionManager.getId(), new VolleyCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                isLoading(true);
                try {
                    JSONArray jsonArray = result.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        String jsonString = jsonArray.getString(i);
                        JSONObject jsonObject1 = new JSONObject(jsonString);

                        String user2Json = jsonObject1.getString("user2");
                        JSONObject jsonObject4 = new JSONObject(user2Json);
                        String user2Id = jsonObject4.getString("id");
                        String user2Name = jsonObject4.getString("fullname");
                        String user2Phone = jsonObject4.getString("phone");
                        String avatar = jsonObject4.getString("avatar");

                        Private conversation = new Private();
                        String lastMessage = jsonObject1.getString("lastMessage");
                        String lastTime = jsonObject1.getString("lastTime");
                        LocalDateTime timeStamp = LocalDateTime.parse(lastTime);

                        User user2 = new User();
                        user2.setEncodedImage(avatar);
                        user2.setId(user2Id);
                        user2.setFullname(user2Name);
                        user2.setPhone(user2Phone);

                        conversation.setUser2(user2);
                        conversation.setLastMessage(lastMessage);
                        conversation.setLastTime(timeStamp);

                        privateList.add(conversation);
                        privateAdapter.notifyDataSetChanged();
                    }

                    privateAdapter.notifyDataSetChanged();
                    isLoading(false);
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

    @Override
    public void onStop() {
        super.onStop();
        isLoadingChangeToChat(false);
    }

    private void isLoading(boolean loading) {
        if (loading) {
            loadingConversation.setVisibility(View.VISIBLE);
            return;
        }

        loadingConversation.setVisibility(View.INVISIBLE);
    }

    private void isLoadingChangeToChat(boolean loading) {
        if (loading) {
            loadingChangeToChat.setVisibility(View.VISIBLE);
            frameLayoutHome.setAlpha(0.5f);
        } else {
            loadingChangeToChat.setVisibility(View.INVISIBLE);
            frameLayoutHome.setAlpha(1f);
        }
    }

    private void mapping(View view) {
        svIconSearch = view.findViewById(R.id.logoSearchMain);
        svIconSearch.setInputType(InputType.TYPE_CLASS_PHONE);

        lvUserSearch = view.findViewById(R.id.listViewUserSearchMain);
        lvConversation = view.findViewById(R.id.listViewConversationMain);
        privateList = new ArrayList<>();
        users = new ArrayList<>();

        sessionManager = new SessionManager(getContext());
        db = FirebaseFirestore.getInstance();

        loadingConversation = view.findViewById(R.id.progressBarHome);
        loadingChangeToChat = view.findViewById(R.id.progressBarChangeToChatFromHome);
        frameLayoutHome = view.findViewById(R.id.frameLayoutHome);

        imgButtonCreateGroup = view.findViewById(R.id.imageButtonCreateGroup);
    }
}