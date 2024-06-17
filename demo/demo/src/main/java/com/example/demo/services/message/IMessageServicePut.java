package com.example.demo.services.message;

import com.example.demo.model.Message;

public interface IMessageServicePut {
    boolean updateStatus(Message message);
    boolean updateMessage(Message message);
}
