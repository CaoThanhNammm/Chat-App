package com.example.demo.controller;

import com.example.demo.model.Private;
import com.example.demo.model.Message;
import com.example.demo.model.ResponseObject;
import com.example.demo.model.User;
import com.example.demo.services.message.MessageServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/message")
public class MessageServerController {
    @Autowired
    private MessageServerService ms;


    @GetMapping("/find")
    public ResponseEntity<ResponseObject> findMessage(@RequestBody Message message) {
        Message messageFound = ms.findMessage(message);

        if (messageFound != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Find message successfully", messageFound)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Find message failed", null)
        );
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseObject> save(@RequestParam String userId, @RequestParam String converId, @RequestParam String content, @RequestParam String lastTime) {
        Message msg = new Message();
        User user = new User();
        Private conversation = new Private();

        user.setId(userId);
        conversation.setId(converId);

        msg.setUser(user);
        msg.setConversation(conversation);
        msg.setMessage(content);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime parsedDate = LocalDateTime.parse(lastTime, formatter);

        msg.setTime(parsedDate);

        Message messageSaved = ms.save(msg);

        if (messageSaved != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Add message successfully", messageSaved)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Add message failed", null)
        );
    }

    @PutMapping("/update/status")
    public ResponseEntity<ResponseObject> updateStatus(@RequestBody Message message) {
        boolean updated = ms.updateStatus(message);

        if (updated) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Updated status successfully", true)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Updated status failed", false)
        );
    }

    @PutMapping("/update/message")
    public ResponseEntity<ResponseObject> updateMessage(@RequestBody Message message) {
        boolean updated = ms.updateMessage(message);

        if (updated) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Updated message successfully", true)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Updated message failed", false)
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseObject> delete(@RequestBody Message message) {
        boolean deleted = ms.delete(message);

        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Deleted message successfully", true)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Deleted message failed", false)
        );
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> findAllMessage(@RequestParam String converId) {
        Private conversation = new Private();

        conversation.setId(converId);

        List<Message> messageList = ms.findAllMessage(conversation);

        if (!messageList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Find all message successfully", messageList)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Find all message failed", messageList)
        );
    }


}
