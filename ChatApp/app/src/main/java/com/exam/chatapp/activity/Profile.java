package com.exam.chatapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.VolleyError;
import com.exam.chatapp.R;
import com.exam.chatapp.adapter.ProfileIn4Adapter;
import com.exam.chatapp.model.User;
import com.exam.chatapp.model.VolleyCallback;
import com.exam.chatapp.network.SessionManager;
import com.exam.chatapp.services.UserClientService;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {
    private ListView lvIn4;
    private ProfileIn4Adapter profileIn4Adapter;
    private ImageView imgViewAvatarProfileIn4;
    private FrameLayout frameLayoutAvatarProfileIn4;
    private List<String> in4s;
    private FirebaseFirestore db;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mapping();
        setAdapterIn4();
        showProfile();
        onClickUpdateAvatar();
    }

    private void setAdapterIn4() {
        profileIn4Adapter = new ProfileIn4Adapter(this, R.layout.row_in4_profile, in4s);
        lvIn4.setAdapter(profileIn4Adapter);
    }

    // hiển thị các thông tin cá nhân ra màn hình
    private void showProfile() {
        UserClientService.getInstance(this).getUserById(sessionManager.getId(), new VolleyCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                JSONObject jsonObject = result.getJSONObject("data");

                String name = jsonObject.getString("fullname");
                String phone = jsonObject.getString("phone");
                String dob = jsonObject.getString("dob");
                String created = jsonObject.getString("createdAt");
                String avatar = jsonObject.getString("avatar");
                imgViewAvatarProfileIn4.setImageBitmap(stringToBitmap(avatar));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate dobFormatter = LocalDate.parse(dob, formatter);
                LocalDate createFormatter = LocalDate.parse(created, formatter);

                in4s.add(name);
                in4s.add(phone);
                in4s.add(dobFormatter.getDayOfMonth() + "/" + dobFormatter.getMonthValue() + "/" + dobFormatter.getYear());
                in4s.add(createFormatter.getDayOfMonth() + "/" + createFormatter.getMonthValue() + "/" + createFormatter.getYear());

                profileIn4Adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    // chuyển từ String sang bitmap
    private Bitmap stringToBitmap(String encodeImage) {
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    // ấn thay đổi avatar
    private void onClickUpdateAvatar() {
        frameLayoutAvatarProfileIn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);
            }
        });
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null) {
                            Uri imageUri = result.getData().getData();
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                imgViewAvatarProfileIn4.setImageBitmap(bitmap);

                                // cập nhập ảnh vào csdl
                                updateAvatar(bitmap);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }
                }
            }
    );

    // cập nhập lên firebase
    private void updateAvatar(Bitmap bitmap) {
        String base64 = bitmapToString(bitmap);

        User user = new User();
        user.setId(sessionManager.getId());
        user.setEncodedImage(base64);

        UserClientService.getInstance(this).updateAvatar(user);
    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.NO_WRAP);
        return temp;
    }

    // Chuyển từ bitmap sang string
    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void mapping() {
        imgViewAvatarProfileIn4 = findViewById(R.id.imageViewAvatarProfileIn4);
        frameLayoutAvatarProfileIn4 = findViewById(R.id.frameLayoutAvatarProfileIn4);
        lvIn4 = findViewById(R.id.lvIn4Profile);
        db = FirebaseFirestore.getInstance();
        in4s = new ArrayList<>();

        sessionManager = new SessionManager(this);
    }
}