package com.example.demo.services.firebase.user;

import com.example.demo.model.Constant;
import com.example.demo.model.firebase.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class UserFirebaseService implements IUserFirebaseServiceGet, IUserFirebaseServicePost, IUserFirebaseServicePut, IUserFirebaseServiceDelete {
    private Firestore db = FirestoreClient.getFirestore();

    @Override
    public User save(User user) throws ExecutionException, InterruptedException {
        DocumentReference users = db.collection(Constant.KEY_COLLECTION_USER).document(user.getId());
        ApiFuture<WriteResult> future = users.set(user);
        return user;
    }

    @Override
    public String updateAvai(String id, int avai) throws ExecutionException, InterruptedException {
        DocumentReference userDocRef = db.collection(Constant.KEY_COLLECTION_USER).document(id);
        ApiFuture<WriteResult> future = userDocRef.update(Constant.KEY_AVAIBILITY_USER, avai);
        return future.get().getUpdateTime().toString();
    }

    @Override
    public String updateIslogin(String id, boolean isLogin) throws ExecutionException, InterruptedException {
        DocumentReference userDocRef = db.collection(Constant.KEY_COLLECTION_USER).document(id);
        ApiFuture<WriteResult> future = userDocRef.update(Constant.KEY_IS_LOGIN_USER, isLogin);
        return future.get().getUpdateTime().toString();
    }

    @Override
    public String updateFcm(String id, String fcm) throws ExecutionException, InterruptedException {
        DocumentReference userDocRef = db.collection(Constant.KEY_COLLECTION_USER).document(id);
        ApiFuture<WriteResult> future = userDocRef.update(Constant.KEY_FCM_USER, fcm);
        return future.get().getUpdateTime().toString();
    }

    @Override
    public User getUser(String id) throws ExecutionException, InterruptedException {
        DocumentReference document = db.collection(Constant.KEY_COLLECTION_USER).document(id);
        ApiFuture<DocumentSnapshot> future = document.get();
        DocumentSnapshot snapshot = future.get();
        User user;
        if (snapshot.exists()) {
            user = snapshot.toObject(User.class);
            return user;
        }
        return null;
    }
}
