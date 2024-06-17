package com.exam.chatapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.VolleyError;
import com.exam.chatapp.R;
import com.exam.chatapp.adapter.MessageAdapter;
import com.exam.chatapp.config.Constant;
import com.exam.chatapp.model.firebase.FirebaseMessage;
import com.exam.chatapp.model.Private;
import com.exam.chatapp.model.Message;
import com.exam.chatapp.network.SendNotification;
import com.exam.chatapp.model.User;
import com.exam.chatapp.model.VolleyCallback;
import com.exam.chatapp.services.PrivateClientService;
import com.exam.chatapp.services.MessageClientService;
import com.exam.chatapp.services.firebase.MessageFirebaseClientService;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat extends AppCompatActivity {
    private TextView tvConversationName;
    private ImageButton imgButtonOption;
    private ListView lvMessage;
    private EditText edtMessage;
    private ImageButton imgButtonSend;
    private List<FirebaseMessage> chatTransfers;
    private MessageAdapter messageAdapter;
    private Handler handler = new Handler(Looper.getMainLooper());
    private User user1;
    private User user2;
    private Private conversation;
    private FirebaseFirestore db;
    private ImageView imgAvatarChat;
    private ProgressBar loadingChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mapping();
        setAdapterMessage();

        try {
            onClicklvFriend();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        onClickOption();
        onClickSend();
        listenForMessages();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendMessage(FirebaseMessage transfer) {
        MessageFirebaseClientService.getInstance(this).save(transfer);
    }

    public void listenForMessages() {
        MessageFirebaseClientService.getInstance(this).listenMessage(new VolleyCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                String messageJson1 = result.getString("message");
                JSONObject senderJson = result.getJSONObject("sender");
                JSONObject receiverJson = result.getJSONObject("receiver");
                JSONObject timeJson = result.getJSONObject("time");
                FirebaseMessage chatTransfer = new FirebaseMessage();
                User sender = new User();
                User receiver = new User();

                sender.setId(senderJson.getString("id"));
                sender.setEncodedImage(senderJson.getString("encodedImage"));

                receiver.setId(receiverJson.getString("id"));
                receiver.setFullname(receiverJson.getString("fullname"));
                receiver.setEncodedImage(receiverJson.getString("encodedImage"));
                Timestamp timestamp = new Timestamp(Instant.ofEpochSecond(timeJson.getInt("seconds"), timeJson.getInt("nanoseconds")));

                Instant instant = timestamp.toDate().toInstant();
                LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                String dateAndTime = localDateTime.getDayOfMonth() + "/" + localDateTime.getMonthValue() + "/" + localDateTime.getYear() + " " + localDateTime.getHour() + ":" + localDateTime.getMinute();
                chatTransfer.setSender(sender);
                chatTransfer.setReceiver(receiver);
                chatTransfer.setMessage(messageJson1);
                chatTransfer.setTime(dateAndTime);

                if (chatTransfer.getReceiver().getId().equals(user1.getId()) && chatTransfer.getSender().getId().equals(user2.getId())
                        || chatTransfer.getReceiver().getId().equals(user2.getId()) && chatTransfer.getSender().getId().equals(user1.getId())) {
                    chatTransfers.add(chatTransfer);
                    lvMessage.smoothScrollToPosition(chatTransfers.size() - 1);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    public void onClicklvFriend() throws URISyntaxException {
        PrivateClientService.getInstance(this).isMessaging(user1.getId(), user2.getId(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                isLoading(true);
                JSONObject jsonObject = result.getJSONObject("data");
                String converId = jsonObject.getString("id");

                JSONObject user2Json = jsonObject.getJSONObject("user2");

                String avatar = user2Json.getString("avatar");
                imgAvatarChat.setImageBitmap(stringToBitmap(avatar));

                conversation.setId(converId);
                tvConversationName.setText(user2.getFullname());
                isLoading(false);
            }

            @Override
            public void onError(VolleyError error) {
                imgAvatarChat.setImageBitmap(stringToBitmap(user2.getEncodedImage()));
                tvConversationName.setText(user2.getFullname());
            }
        });
    }

    private Bitmap stringToBitmap(String avatar) {
        byte[] bytes = Base64.decode(avatar, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private void onClickSend() {
        imgButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {
                        String message = edtMessage.getText().toString().trim();
                        if (!message.isEmpty()) {
                            LocalDateTime lastTime = LocalDateTime.now();

                            FirebaseMessage transfer = new FirebaseMessage();
                            transfer.setSender(user1);
                            transfer.setReceiver(user2);
                            transfer.setMessage(message);
                            transfer.setTime(lastTime + "");

                            // them vao database fire store
                            sendMessage(transfer);
                            // tao bang private
                            createPrivate(message, lastTime + "");
                            // thong bao tin nhan moi khi nguoi dung kia offline
                            MessageFirebaseClientService.getInstance(Chat.this).sendNotification(user2, user1, message);
                            // lam trong tin nhan
                            edtMessage.setText("");
                        }
                    }
                });
            }
        });
    }

    private void createPrivate(String lastMessage, String lastTime) {
        // kiểm tra đã nhắn tin hay chưa
        PrivateClientService.getInstance(this).isMessaging(user1.getId(), user2.getId(), new VolleyCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                JSONObject jsonObject = result.getJSONObject("data");
                String converId = jsonObject.getString("id");
                Private c = new Private();
                c.setId(converId);
                // đã nhắn tin
                Message message = new Message();
                message.setUser(user1);
                message.setConversation(c);
                message.setMessage(lastMessage);

                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                LocalDateTime parsedDate = LocalDateTime.parse(lastTime, formatter);
                message.setTime(parsedDate);

                addMessage(message);
            }

            @Override
            public void onError(VolleyError error) {
                // chưa nhắn tin
                Private conversation = new Private();

                conversation.setUser1(user1);
                conversation.setUser2(user2);

                PrivateClientService.getInstance(Chat.this).createPrivate(conversation, new VolleyCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(JSONObject result) throws JSONException {
                        JSONObject jsonObject = result.getJSONObject("data");
                        String converId = jsonObject.getString("id");
                        Private c = new Private();
                        c.setId(converId);

                        // cap nhap database, bang messages
                        Message message = new Message();
                        message.setUser(user1);
                        message.setConversation(c);
                        message.setMessage(lastMessage);

                        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                        LocalDateTime parsedDate = LocalDateTime.parse(lastTime, formatter);
                        message.setTime(parsedDate);

                        addMessage(message);
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
            }
        });
    }

    private void updateLastMessage(Private conversation) {
        PrivateClientService.getInstance(this).updateLastMessage(conversation);
    }

    private void addMessage(Message msg) {
        MessageClientService.getInstance(this).save(msg, new VolleyCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                JSONObject jsonObject = result.getJSONObject("data");

                Private conversation = new Private();
                String lastMessage = jsonObject.getString("message");
                String lastTime = jsonObject.getString("time");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
                LocalDateTime lastTimeFormatter = LocalDateTime.parse(lastTime, formatter);

                conversation.setUser1(user1);
                conversation.setUser2(user2);
                conversation.setLastMessage(lastMessage);
                conversation.setLastTime(lastTimeFormatter);

                updateLastMessage(conversation);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

    }

    private void setAdapterMessage() {
        messageAdapter = new MessageAdapter(this, chatTransfers);
        lvMessage.setAdapter(messageAdapter);
    }

    private void showMessageOnScreen(String converId) {
        MessageClientService.getInstance(this).findAllMessageByUserId(converId, new VolleyCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                JSONArray jsonArray = result.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject message = jsonArray.getJSONObject(i);
                    JSONObject user = message.getJSONObject("user");
                    User user1 = new User();

                    String id = user.getString("id");
                    String fullname = user.getString("fullname");

                    String content = message.getString("message");
                    String time = message.getString("time");

                    user1.setId(id);
                    user1.setFullname(fullname);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
                    Message message1 = new Message();

                    message1.setUser(user1);
                    message1.setMessage(content);
                    try {
                        LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
                        message1.setTime(dateTime);
                    } catch (DateTimeParseException e) {
                        e.printStackTrace();
                    }
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void onClickOption() {
        imgButtonOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chat.this, Option.class);
                Bundle bundle = new Bundle();

                bundle.putString("conversationName", tvConversationName.getText().toString());
                intent.putExtra("title", bundle);

                startActivity(intent);
            }
        });
    }

    private void isLoading(boolean loading) {
        if (loading) {
            loadingChat.setVisibility(View.VISIBLE);
        } else {
            loadingChat.setVisibility(View.INVISIBLE);
        }
    }


    private void mapping() {
        tvConversationName = findViewById(R.id.textViewConversationName);
        imgButtonOption = findViewById(R.id.imageButtonOption);
        lvMessage = findViewById(R.id.listViewMessage);
        lvMessage.setDividerHeight(0);
        edtMessage = findViewById(R.id.editTextMessage);
        imgButtonSend = findViewById(R.id.imageButtonSend);
        imgAvatarChat = findViewById(R.id.imageViewAvatarChat);
        chatTransfers = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        loadingChat = findViewById(R.id.progressBarChat);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");

        user1 = (User) bundle.getSerializable("user1");
        user2 = (User) bundle.getSerializable("user2");

        conversation = new Private();
    }
}