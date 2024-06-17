package com.exam.chatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.exam.chatapp.R;
import com.exam.chatapp.model.Friend;
import com.exam.chatapp.model.User;
import com.exam.chatapp.model.VolleyCallback;
import com.exam.chatapp.network.SessionManager;
import com.exam.chatapp.services.FriendClientService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class UserAdapterFriendInvite extends BaseAdapter {
    private Context context;
    private int layout;
    private List<User> users;

    public UserAdapterFriendInvite(Context context, int layout, List<User> users) {
        this.context = context;
        this.layout = layout;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(layout, null);
        ImageView imgAvatar = view.findViewById(R.id.imageViewAvatarRowUserFriendInvite);
        TextView tvFullname = view.findViewById(R.id.textViewNameRowUserFriendInvite);
        TextView tvPhone = view.findViewById(R.id.textViewPhoneRowUserFriendInvite);

        User user = users.get(i);

        // doi anh dai dien o dayyyyyyyyyyyyyyyyyyyyyyyyyyyy
        imgAvatar.setImageResource(R.drawable.avatar_default);
        tvFullname.setText(user.getFullname());
        tvPhone.setText(user.getPhone());
        Button btnAgree = view.findViewById(R.id.buttonAgreeFriendInvite);
        Button btnDeny = view.findViewById(R.id.buttonDenyFriendInvite);

        SessionManager sessionManager = new SessionManager(context);
        User user1 = new User();
        user1.setId(sessionManager.getId());

        onClickAgree(btnAgree, user1, user, i);
        onClickDeny(btnDeny, user1, user, i);
        return view;
    }

    private void onClickAgree(Button btnAgree, User user1, User user, int i) {
        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // cap nhap lai trang thai la dong y
                FriendClientService.getInstance(context).updateStatus(user1, user, Friend.AGREE);

                // cap nhap lai giao dien
                users.remove(i);
                notifyDataSetChanged();
            }
        });
    }
    private void onClickDeny(Button btnDeny, User user1, User user, int i) {
        btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // xoa loi moi ket ban
                FriendClientService.getInstance(context).deleteFriend(user1, user, new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) throws JSONException {

                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
                // cap nhap lai giao dien
                users.remove(i);
                notifyDataSetChanged();
            }
        });
    }
}
