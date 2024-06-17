package com.exam.chatapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.exam.chatapp.R;
import com.exam.chatapp.model.Friend;
import com.exam.chatapp.model.User;
import com.exam.chatapp.model.VolleyCallback;
import com.exam.chatapp.network.SessionManager;
import com.exam.chatapp.services.FriendClientService;
import com.exam.chatapp.services.UserClientService;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserAdapterUser extends BaseAdapter implements Filterable {
    private Context context;
    private int layout;
    private List<User> users;
    private List<User> usersOld;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ;


    public UserAdapterUser(Context context, int layout, List<User> users) {
        this.context = context;
        this.layout = layout;
        this.users = users;
    }

    @Override
    public int getCount() {
        if (users == null) return 0;
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

        ImageView imgView = view.findViewById(R.id.imageViewAvatarRowUser);
        TextView tvName = view.findViewById(R.id.textViewNameRowUser);
        TextView tvPhone = view.findViewById(R.id.textViewPhoneRowUser);

        ImageButton imgButton = view.findViewById(R.id.imageButtonAddFriend);
        User user = users.get(i);

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // them vao db
                addFriend(user);
                // cap nhap lai giao dien
                users.remove(i);
                notifyDataSetChanged();
            }
        });

        String encodedImg = user.getEncodedImage();

        imgView.setImageBitmap(stringToBitmap(encodedImg));
        tvName.setText(user.getFullname());
        tvPhone.setText(user.getPhone());
        return view;
    }

    private void addFriend(User user) {
        SessionManager sessionManager = new SessionManager(context);

        String userImplId = sessionManager.getId();
        String userAddedId = user.getId();
        Friend friend = new Friend();

        User userImpl = new User();
        User userAdded = new User();

        userImpl.setId(userImplId);
        userAdded.setId(userAddedId);

        friend.setUser1(userImpl);
        friend.setUser2(userAdded);

        FriendClientService.getInstance(context).save(friend);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String phone = charSequence.toString();
                if (phone.isEmpty()) {
                    users = usersOld;
                } else {
                   searchUser(phone);
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = users;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                users = (List<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private void searchUser(String phoneSearch) {
        SessionManager sessionManager = new SessionManager(context);
        UserClientService.getInstance(context).searchUser(sessionManager.getPhone(), phoneSearch, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                JSONArray jsonArray = result.getJSONArray("data");
                List<User> list = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    User user = new User();
                    user.setId(jsonObject.getString("id"));
                    user.setEncodedImage(jsonObject.getString("avatar"));
                    user.setFullname(jsonObject.getString("fullname"));
                    user.setPhone(jsonObject.getString("phone"));
                    list.add(user);
                }
                users = list;
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private Bitmap stringToBitmap(String avatar) {
        byte[] bytes = Base64.decode(avatar, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}
