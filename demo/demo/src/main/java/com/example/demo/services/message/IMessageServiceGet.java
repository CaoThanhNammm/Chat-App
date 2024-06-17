package com.example.demo.services.message;

import com.example.demo.model.Private;
import com.example.demo.model.Message;

import java.util.List;

public interface IMessageServiceGet {
    Message findMessage(Message message);
    List<Message> findAllMessage(Private c);
}
