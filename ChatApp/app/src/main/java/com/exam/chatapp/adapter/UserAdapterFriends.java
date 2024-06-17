package com.exam.chatapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.android.volley.VolleyError;
import com.exam.chatapp.R;
import com.exam.chatapp.config.Constant;
import com.exam.chatapp.model.User;
import com.exam.chatapp.model.VolleyCallback;
import com.exam.chatapp.network.SessionManager;
import com.exam.chatapp.services.firebase.UserFirebaseClientService;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class UserAdapterFriends extends BaseAdapter {
    private Context context;
    private int layout;
    private List<User> users;

    public UserAdapterFriends(Context context, int layout, List<User> users) {
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

        LinearLayout backgroundStatusUser = view.findViewById(R.id.backgroundStatusUser);
        TextView tvStatusUser = view.findViewById(R.id.textViewStatusUser);

        ImageView imgView = view.findViewById(R.id.imageViewAvatarRowUserFriend);
        TextView tvName = view.findViewById(R.id.textViewNameRowUserFriend);
        TextView tvPhone = view.findViewById(R.id.textViewPhoneRowUserFriend);

        User user = users.get(i);

        if (user.getEncodedImage() == null) {
            imgView.setImageResource(R.drawable.avatar_default);
        }
        else{
            imgView.setImageBitmap(stringToBitmap(user.getEncodedImage()));
        }
        listenSetStatusUser(backgroundStatusUser, tvStatusUser, user.getId());

        tvName.setText(user.getFullname());
        tvPhone.setText(user.getPhone());

        return view;
    }

    private void listenSetStatusUser(LinearLayout backgroundStatusUser, TextView tvStatusUser, String userId){
        UserFirebaseClientService.getInstance(context).updateStatus(userId, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                long avaibility = result.getLong(Constant.KEY_AVAIBILITY_USER);

                if(avaibility == 1){
                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.bg_online);
                    backgroundStatusUser.setBackground(drawable);
                    tvStatusUser.setText("Online");
                }else if(avaibility == 0){
                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.bg_offline);
                    backgroundStatusUser.setBackground(drawable);
                    tvStatusUser.setText("Offline");
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private Bitmap stringToBitmap(String avatar){
        byte[] bytes = Base64.decode(avatar, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
