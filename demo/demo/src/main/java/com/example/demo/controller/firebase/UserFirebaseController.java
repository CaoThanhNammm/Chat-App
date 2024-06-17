package com.example.demo.controller.firebase;

import com.example.demo.model.ResponseObject;
import com.example.demo.model.firebase.User;
import com.example.demo.services.firebase.user.UserFirebaseService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/firebase/user")
public class UserFirebaseController {
    @Autowired
    private UserFirebaseService userFirebaseService;


    @PostMapping("/save")
    public ResponseEntity<ResponseObject> save(@RequestBody User user) throws ExecutionException, InterruptedException {
        User userSaved = userFirebaseService.save(user);

        if(userSaved != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Save successfully", user)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Save failed", null)
        );

    }

    @PutMapping("/update/avaibility")
    public ResponseEntity<ResponseObject> updateAvaibility(@RequestParam String id, @RequestParam int avai) throws ExecutionException, InterruptedException {
        String timeStamp = userFirebaseService.updateAvai(id, avai);

        System.out.println(timeStamp);

        if(timeStamp != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Updated", timeStamp)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Not updated", null)
        );

    }

    @PutMapping("/update/isLogin")
    public ResponseEntity<ResponseObject> updateIsLogin(@RequestParam String id, @RequestParam boolean isLogin) throws ExecutionException, InterruptedException {
        String timeStamp = userFirebaseService.updateIslogin(id, isLogin);
        System.out.println(timeStamp);

        if(timeStamp != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Updated", timeStamp)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Not updated", null)
        );

    }

    @PutMapping("/update/fcm")
    public ResponseEntity<ResponseObject> updateFcm(@RequestParam String id, @RequestParam String fcm) throws ExecutionException, InterruptedException {
        String timeStamp = userFirebaseService.updateFcm(id, fcm);
        System.out.println(fcm);
        if(timeStamp != null){

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Updated", timeStamp)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Not updated", null)
        );

    }

    @GetMapping("/get")
    public ResponseEntity<ResponseObject> getUser(@RequestParam String id) throws ExecutionException, InterruptedException {
        User user = userFirebaseService.getUser(id);
        System.out.println("run");
        if(user != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Get user successfully", user)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Get user failed", null)
        );

    }

}
