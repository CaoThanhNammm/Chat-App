package com.example.demo.dao;

import com.example.demo.model.Friend;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendServerDao extends JpaRepository<Friend, String> {
    List<Friend> findAllByUser1AndStatus(User user1, int status);
    List<Friend> findAllByUser2AndStatus(User user2, int waiting);

    void deleteFriendsByUser1AndUser2(User user1, User user2);
    int countFriendByUser1AndStatus(User user, int status);
    int countFriendByUser2AndStatus(User user, int status);

    Friend findByUser1AndUser2(User userImpl, User userUpdated);


}
