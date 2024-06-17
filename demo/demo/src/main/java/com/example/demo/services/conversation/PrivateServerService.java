package com.example.demo.services.conversation;


import com.example.demo.dao.PrivateServerDao;
import com.example.demo.model.Private;
import com.example.demo.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrivateServerService implements IPrivateServiceGet, IPrivateServicePost, IPrivateServicePut, IPrivateServiceDelete {
    @Autowired
    private PrivateServerDao privateDao;

    @Override
    public List<Private> findAll() {
        return privateDao.findAll();
    }

    @Override
    public Private isExist(Private c) {
        boolean res =  privateDao.existsById(c.getId());
        if(res){
            return privateDao.findById(c.getId()).orElseThrow();
        }

        return null;
    }

    @Override
    public boolean deleteById(String id) {
        if (privateDao.existsById(id)) {
            privateDao.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Private> findPrivate(User user) {
        return privateDao.findAllByUser1(user);
    }

    @Override
    public Private isMessaging(User user1, User user2) {
        return privateDao.findByUser1AndUser2(user1, user2);
    }

    @Override
    public Private createPrivate(Private uc) {
        User user1 = uc.getUser1();
        User user2 = uc.getUser2();

        Private found = privateDao.findByUser1AndUser2(user1, user2);
        if(found != null){
            return null;
        }

        Private uc1 = new Private();
        uc1.setUser1(user1);
        uc1.setUser2(user2);
        privateDao.save(uc1);

        Private uc2 = new Private();
        uc2.setUser1(user2);
        uc2.setUser2(user1);
        return privateDao.save(uc2);
    }


    @Override
    @Transactional
    public boolean deletePrivate(User user1) {
        if (privateDao.findAllByUser1(user1) != null) {
            privateDao.deleteByUser1(user1);
            return true;
        }

        return false;
    }

    @Override
    public Private updateLastMessage(Private conversation) {
        Private private1 = privateDao.findByUser1AndUser2(conversation.getUser1(), conversation.getUser2());
        Private private2 = privateDao.findByUser1AndUser2(conversation.getUser2(), conversation.getUser1());
        if(private1 != null){
            private1.setLastMessage(conversation.getLastMessage());
            private1.setLastTime(conversation.getLastTime());
            privateDao.save(private1);
        }

        if(private2 != null){
            private2.setLastMessage(conversation.getLastMessage());
            private2.setLastTime(conversation.getLastTime());
            privateDao.save(private2);
        }

        return private1;
    }

}
