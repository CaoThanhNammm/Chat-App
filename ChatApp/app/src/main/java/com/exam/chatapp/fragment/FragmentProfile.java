package com.exam.chatapp.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.exam.chatapp.activity.ChangePassword;
import com.exam.chatapp.activity.Login;
import com.exam.chatapp.activity.Profile;
import com.exam.chatapp.R;
import com.exam.chatapp.adapter.ProfileAdapter;
import com.exam.chatapp.model.VolleyCallback;
import com.exam.chatapp.network.SessionManager;
import com.exam.chatapp.services.UserClientService;
import com.exam.chatapp.services.firebase.UserFirebaseClientService;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentProfile extends Fragment {
    private ListView lvNameFuncs;
    private ProfileAdapter profileAdapter;
    private ImageView imgAvatar;
    private List<String> nameFuncs;
    private TextView tvNameUser;
    private SessionManager sessionManager;
    private FirebaseFirestore db;
    private ProgressBar loadingAvatar;
    private ProgressBar loadingFuncProfile;
    private FrameLayout frameLayoutProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mapping(view);
        showNameFuncs();
        onClickLvNameFuncs();
        showNameAndAvatarUser();
        return view;
    }


    @Override
    public void onStop() {
        super.onStop();
        isLoadingFuncProfile(false);
    }

    private void showNameAndAvatarUser() {
        isLoading(true);
        UserClientService.getInstance(getContext()).getUserById(sessionManager.getId(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                JSONObject userJson = result.getJSONObject("data");
                imgAvatar.setImageBitmap(stringToBitmap(userJson.getString("avatar")));
                tvNameUser.setText(userJson.getString("fullname"));
            }

            @Override
            public void onError(VolleyError error) {
                isLoading(false);
            }
        });
    }

    private void showNameFuncs() {
        profileAdapter = new ProfileAdapter(getContext(), R.layout.row_profile, nameFuncs);
        lvNameFuncs.setAdapter(profileAdapter);
    }

    private void onClickLvNameFuncs() {
        lvNameFuncs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isLoadingFuncProfile(true);
                if (i == 0) {
                    changePassword();
                } else if (i == 1) {
                    changeProfile();
                } else if (i == 2) {
                    logOut();
                }
            }
        });
    }

    private void logOut() {
        deleteFcmInfirebase();
        updateAvaibility();
        updateIsLogin();
        sessionManager.removeSession();
        Intent intent = new Intent(getContext(), Login.class);
        startActivity(intent);
    }

    private void updateIsLogin() {
        UserFirebaseClientService.getInstance(getContext()).updateIsLogin(sessionManager.getId(), false);
    }

    private void updateAvaibility() {
        UserFirebaseClientService.getInstance(getContext()).updateAvaibility(sessionManager.getId(), 0);
    }

    private void deleteFcmInfirebase() {
//        UserFirebaseClientService.getInstance(getContext()).updateFcm(sessionManager.getId(), "");
    }

    private void changeProfile() {
        Intent intent = new Intent(getContext(), Profile.class);
        startActivity(intent);
    }

    private void changePassword() {
        Intent intent = new Intent(getContext(), ChangePassword.class);
        startActivity(intent);
    }

    private Bitmap stringToBitmap(String encodeImage) {
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private void isLoading(boolean loading) {
        if (loading) {
            loadingAvatar.setVisibility(View.VISIBLE);
        } else {
            loadingAvatar.setVisibility(View.INVISIBLE);
        }
    }

    private void isLoadingFuncProfile(boolean loading) {
        if (loading) {
            loadingFuncProfile.setVisibility(View.VISIBLE);
            frameLayoutProfile.setAlpha(0.5f);
        } else {
            loadingFuncProfile.setVisibility(View.INVISIBLE);
            frameLayoutProfile.setAlpha(1f);
        }
    }

    private void mapping(View view) {
        imgAvatar = view.findViewById(R.id.imageViewAvatarProfile);
        tvNameUser = view.findViewById(R.id.textViewNameUser);
        nameFuncs = new ArrayList<>();
        nameFuncs.add("Đổi mật khẩu");
        nameFuncs.add("Thông tin cá nhân");
        nameFuncs.add("Đăng xuất");
        lvNameFuncs = view.findViewById(R.id.listViewNameFuncs);
        db = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(getContext());
        loadingAvatar = view.findViewById(R.id.progressBarProfile);
        loadingFuncProfile = view.findViewById(R.id.progressBarFuncsProfile);
        frameLayoutProfile = view.findViewById(R.id.frameLayoutProfile);
    }

}