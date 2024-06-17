package com.example.demo.services.message;

import com.example.demo.dao.MessageServerDao;
import com.example.demo.model.Private;
import com.example.demo.model.Message;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServerService implements IMessageServiceGet, IMessageServicePost, IMessageServicePut, IMessageServiceDelete {
    @Autowired
    private MessageServerDao md;


    @Override
    public Message findMessage(Message message) {
        return md.findByUserAndConversationAndTime(message.getUser(), message.getConversation(), message.getTime());
    }

    @Override
    public List<Message> findAllMessage(Private c) {
        if(!md.existsByConversation(c)){
            return new ArrayList<>();
        }

        return md.findAllByConversationAndStatusOrderByTime(c, 1);
    }

    @Override
    @Transactional
    public boolean delete(Message message) {
        if (md.existsByUserAndConversationAndTime(message.getUser(), message.getConversation(), message.getTime())) {
            md.deleteByUserAndConversationAndTime(message.getUser(), message.getConversation(), message.getTime());
            return true;
        }
        return false;
    }

    @Override
    public Message save(Message message) {
        return md.save(message);
    }

    @Override
    public boolean updateStatus(Message message) {
        Message findMessage = findMessage(message);
        if (findMessage != null) {
            findMessage.setStatus(message.getStatus());
            save(findMessage);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateMessage(Message message) {

        Message findMessage = findMessage(message);
        if (findMessage != null) {
            findMessage.setMessage(message.getMessage());
            save(findMessage);
            return true;
        }
        return false;
    }
}
