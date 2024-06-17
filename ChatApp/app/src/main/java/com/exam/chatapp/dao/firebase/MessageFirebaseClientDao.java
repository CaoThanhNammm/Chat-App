package com.exam.chatapp.dao.firebase;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import okhttp3.OkHttpClient;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import com.exam.chatapp.config.Constant;
import com.exam.chatapp.model.User;
import com.exam.chatapp.model.VolleyCallback;
import com.exam.chatapp.model.firebase.FirebaseMessage;
import com.exam.chatapp.network.SendNotification;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.ZoneOffset;
import org.json.JSONException;
import org.json.JSONObject;
public class MessageFirebaseClientDao {
    private final String TAG = "Message firebase Dao";
    private Context context;
    private FirebaseFirestore db;

    public MessageFirebaseClientDao(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void save(FirebaseMessage transfer) {
        Map<String, Object> messages = new HashMap<>();
        messages.put("sender", transfer.getSender());
        messages.put("receiver", transfer.getReceiver());
        messages.put("message", transfer.getMessage());

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(transfer.getTime(), formatter);
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Timestamp timestamp = new Timestamp(instant.getEpochSecond(), instant.getNano());
        messages.put("time", timestamp);

        db.collection(Constant.KEY_COLLECTION_MESSAGE).add(messages);
    }


    public void listenMessage(VolleyCallback cb) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constant.KEY_COLLECTION_MESSAGE)
                .orderBy(Constant.KEY_TIME_MESSAGE, Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    // Xử lý tin nhắn mới
                                    Map<String, Object> messageJson = dc.getDocument().getData();
                                    try {
                                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                                        String json = gson.toJson(messageJson);
                                        JSONObject jsonObject = new JSONObject(json);
                                        cb.onSuccess(jsonObject);
                                    } catch (JSONException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                    break;
                                case MODIFIED:
                                    // Xử lý tin nhắn đã sửa đổi
                                    break;
                                case REMOVED:
                                    // Xử lý tin nhắn đã bị xóa
                                    break;
                            }
                        }
                    }
                });
    }

    public void sendNotification(User user2, User user1, String message) {
        DocumentReference docRef = db.collection(Constant.KEY_COLLECTION_USER).document(user2.getId());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) return;

                if (value != null) {
                    long avaibility = value.getLong(Constant.KEY_AVAIBILITY_USER);
                    Log.d(TAG, "onEvent: " + avaibility);
                    if (avaibility == 0) {
                        // gửi thông báo đến người nhận
                        SendNotification sendNotification = new SendNotification(user2.getFcmToken(), "Thông báo", user1.getFullname() + " gửi tin nhắn " + message, context);

                        try {
                            sendNotification.sendNotification();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }
}
