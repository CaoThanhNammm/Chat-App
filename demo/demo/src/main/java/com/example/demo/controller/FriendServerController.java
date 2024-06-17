package com.example.demo.controller;

import com.example.demo.model.Friend;
import com.example.demo.model.ResponseObject;
import com.example.demo.model.User;
import com.example.demo.services.friend.FriendServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping(path = "/api/friends")
public class FriendServerController {
    @Autowired
    private FriendServerService fs;

    @GetMapping("/id/{id}")
    public ResponseEntity<ResponseObject> getFriends(@PathVariable String id) {
        User user = new User();
        user.setId(id);

        List<Friend> friends = fs.getFriends(user);
        if (!friends.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Get friends successfully", friends)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Get friends failed", null)
        );

    }

    @GetMapping("/count_invite")
    public ResponseEntity<ResponseObject> amountOfInvite(@RequestParam String id) {
        User user = new User();
        user.setId(id);

        int amount = fs.amountOfFriendInvite(user);
        if (amount > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Get amount of invite successfully", amount)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Get amount of invite failed", 0)
        );

    }

    @GetMapping("/invite")
    public ResponseEntity<ResponseObject> findAllInvite(@RequestParam String id) {
        User user = new User();
        user.setId(id);

        List<Friend> friends = fs.findAllInvite(user);

        if (!friends.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Find all invite successfully", friends)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Find all invite failed", null)
        );

    }

    @PostMapping("/save")
    public ResponseEntity<ResponseObject> addFriend(@RequestParam(name="id1") String userId1, @RequestParam(name="id2") String userId2) {
        User user1 = new User();
        User user2 = new User();
        Friend f = new Friend();

        user1.setId(userId1);
        user2.setId(userId2);

        f.setUser1(user1);
        f.setUser2(user2);

        System.out.println(f.getUser1());
        System.out.println(f.getUser2());


        Friend fSaved = fs.addFriend(f);

        if (fSaved != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Add friend successfully", fSaved)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Add friends failed", null)
        );


    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseObject> deleteFriend(@RequestParam String id1, @RequestParam String id2) {
        User user1 = new User();
        user1.setId(id1);

        User user2 = new User();
        user2.setId(id2);

        boolean isDeleted = fs.deleteFriends(user1, user2);

        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Delete friend successfully", true)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Delete friends failed", false)
        );


    }

    @PutMapping("/update")
    public ResponseEntity<ResponseObject> updateStatus(@RequestParam String id1, @RequestParam String id2, @RequestParam int status) {
        User user1 = new User();
        user1.setId(id1);

        User user2 = new User();
        user2.setId(id2);
        boolean isUpdated = fs.updateStatus(user1, user2, status);

        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Update friends successfully", true)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Update friends failed", false)
        );


    }
}
