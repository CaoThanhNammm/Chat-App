package com.example.demo.dao;

import com.example.demo.model.Private;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrivateServerDao extends JpaRepository<Private, String> {
    List<Private> findAllByUser1(User user);

    Private findByUser1AndUser2(User user1, User user2);

    void deleteByUser1(User user1);

}
