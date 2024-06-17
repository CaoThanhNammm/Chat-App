package com.example.demo.controller;

import com.example.demo.model.ResponseObject;
import com.example.demo.model.User;
import com.example.demo.services.user.UserServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/users")
public class UserServerController {
    @Autowired
    private UserServerService us;

    @GetMapping("/id/{id}")
    public ResponseEntity<ResponseObject> getUserById(@PathVariable String id) {
        User user = us.getUserById(id);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Get user successfully", user)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Cannot find user with id = " + id, user)
        );

    }

    @GetMapping("")
    public ResponseEntity<ResponseObject> findAll() {
        List<User> users = us.findAll();
        if (!users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Get list of user successfully", users)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Failed", "List of user is empty", users)
            );
        }
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<ResponseObject> getUserByPhone(@PathVariable String phone) {
        User user = us.getUserByPhone(phone);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Get user by phone successfully", user)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Cannot find user with phone = " + phone, null)
        );

    }

    @GetMapping("/isUser")
    public ResponseEntity<ResponseObject> isUser(@RequestParam(name = "phone") String username, @RequestParam String password) {

        User isUser = us.isUser(username, password);
        if (isUser != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "User is available", isUser)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "User is unavailable", null)
        );

    }

    @GetMapping("/search")
    public ResponseEntity<ResponseObject> searchUser(@RequestParam(name = "phone1") String phoneImpl, @RequestParam(name = "phone2") String phoneSearch) {
        User userImpl = new User();
        userImpl.setPhone(phoneImpl);

        User userSearch = new User();
        userSearch.setPhone(phoneSearch);

        List<User> users = us.findAllByPhone(userImpl, userSearch);

        if (!users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Search user successfully", users)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Search user failed", null)
        );

    }

    @GetMapping("/exist_phone")
    public ResponseEntity<ResponseObject> isExistPhone(@RequestParam String phone) {
        String exist = us.isExistPhone(phone);

        if (!exist.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Valid phone", exist)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Invalid phone", exist)
        );

    }

    @PostMapping(path = "/save")
    public ResponseEntity<ResponseObject> save(@RequestBody User user) {
//        User user = new User();
//        @RequestParam String phone, @RequestParam String password, @RequestParam String fullname, @RequestParam String avatar, @RequestParam String dob
        user.setPhone(user.getPhone());
        user.setPassword(user.getPassword());
        user.setFullname(user.getFullname());
        user.setAvatar(user.getAvatar());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(user.getDob() + "", formatter);

        user.setDob(date);
        User userSaved = us.save(user);

        if (userSaved != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Insert user successfully", user)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Insert user failed", null)
        );

    }


    @PutMapping("/updatePassword")
    public ResponseEntity<ResponseObject> updatePassword(@RequestParam(name = "phone") String phone, @RequestParam String newPass, @RequestParam String reNewPassword) {
        if (newPass.trim().equals(reNewPassword.trim())) {
            User user = new User();

            user.setPhone(phone.trim());
            user.setPassword(newPass.trim());
            User newUser = us.updatePassword(user);

            if (newUser != null) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("Ok", "Password updated", newUser)
                );
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("Failed", "Not updated", null)
        );
    }

    @PutMapping("/updateVisibility")
    public ResponseEntity<ResponseObject> updatePassword(@RequestParam(name = "username") String username, @RequestParam(name = "vis") int vis) {
        User newUser = us.updateVisibility(username, vis);
        if (newUser != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Visibility updated", newUser)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("Failed", "Not updated", null)
        );

    }


    @PutMapping("/update/avatar")
    public ResponseEntity<ResponseObject> updateAvatar(@RequestBody User user) {
        User userUpdated = us.updateAvatar(user);

        if (userUpdated != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "Avatar updated", userUpdated)
            );
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("Failed", "Avatar not updated", null)
        );
    }
}
