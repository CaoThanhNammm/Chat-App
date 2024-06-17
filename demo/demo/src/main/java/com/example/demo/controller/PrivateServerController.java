package com.example.demo.controller;

import com.example.demo.model.Private;
import com.example.demo.model.ResponseObject;
import com.example.demo.model.User;
import com.example.demo.services.conversation.PrivateServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/private")
public class PrivateServerController {
    @Autowired
    private PrivateServerService cs;


    @DeleteMapping("/delete")
    public ResponseEntity<ResponseObject> deleteById(@RequestParam String id) {
        boolean deleted = cs.deleteById(id);

        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Deleted successfully", true)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Deleted failed", false)
        );
    }

    @GetMapping("/exist")
    public ResponseEntity<ResponseObject> isExist(@RequestParam String id) {
        Private c = new Private();
        c.setId(id);

        Private conversationExist = cs.isExist(c);

        if (conversationExist != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Exist", conversationExist)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Not exist", new Private())
        );
    }

    @GetMapping("/find")
    public ResponseEntity<ResponseObject> findPrivate(@RequestParam(name = "id") String userId) {
        User user = new User();
        user.setId(userId);
        List<Private> userFounds = cs.findPrivate(user);

        if (!userFounds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Get group by user successfully", userFounds)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("Failed", "Get group by user failed", null)
        );
    }

    @GetMapping("/is_messaging")
    public ResponseEntity<ResponseObject> isMessaging(@RequestParam String id1, @RequestParam String id2) {

        User user1 = new User();
        User user2 = new User();

        user1.setId(id1);
        user2.setId(id2);

        Private Private = cs.isMessaging(user1, user2);

        if (Private != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Is messaging", Private)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("Failed", "Is not messaging", null)
        );
    }


    @PostMapping("/save")
    public ResponseEntity<ResponseObject> createPrivate(@RequestParam String id1, @RequestParam String id2) {
        Private Private = new Private();

        User user1 = new User();
        user1.setId(id1);

        User user2 = new User();
        user2.setId(id2);


        Private.setUser1(user1);
        Private.setUser2(user2);


        Private saved = cs.createPrivate(Private);

        if (saved != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Create group successfully", saved)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("Failed", "Create group failed", null)
        );
    }


    @PutMapping("/update/last_message")
    public ResponseEntity<ResponseObject> updateLastMessage(@RequestParam String id1, @RequestParam String id2, @RequestParam String lastMessage, @RequestParam String lastTime) {
        User user1 = new User();
        User user2 = new User();

        user1.setId(id1);
        user2.setId(id2);

        Private conversation = new Private();
        conversation.setUser1(user1);
        conversation.setUser2(user2);
        conversation.setLastMessage(lastMessage);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime parsedDate = LocalDateTime.parse(lastTime, formatter);

        conversation.setLastTime(parsedDate);
        Private isUpdated = cs.updateLastMessage(conversation);

        if (isUpdated != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Last message is updated", true)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("Failed", "Last message not updated", false)
        );
    }

    @DeleteMapping("/delete/private")
    public ResponseEntity<ResponseObject> deletePrivate(@RequestParam String id) {
        User user = new User();
        user.setId(id);
        boolean deleted = cs.deletePrivate(user);

        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Deleted successfully", true)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("Failed", "Deleted failed", false)
        );
    }

}



