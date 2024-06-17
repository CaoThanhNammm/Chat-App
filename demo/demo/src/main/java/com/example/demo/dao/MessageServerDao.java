package com.example.demo.dao;

import com.example.demo.model.Private;
import com.example.demo.model.Message;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageServerDao extends JpaRepository<Message, String> {
    Message findByUserAndConversationAndTime(User user, Private conversation, LocalDateTime time);
    List<Message> findAllByConversationAndStatusOrderByTime(Private c, int status);

    boolean existsByUserAndConversationAndTime(User user, Private conversation, LocalDateTime time);
    void deleteByUserAndConversationAndTime(User user, Private conversation, LocalDateTime time);

    boolean existsByConversation(Private c);
}
