package com.example.demo.services.friend;

import com.example.demo.dao.FriendServerDao;
import com.example.demo.model.Friend;
import com.example.demo.model.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FriendServerService implements IFriendServiceGet, IFriendServicePost, IFriendServicePut, IFriendServiceDelete {
    @Autowired
    private FriendServerDao friendDao;

    @Override
    public List<Friend> getFriends(User user) {
        return friendDao.findAllByUser1AndStatus(user, Friend.AGREE);
    }

    @Override
    public List<Friend> findAllInvite(User user) {
        return friendDao.findAllByUser2AndStatus(user, Friend.WAITING);
    }

    @Override
    public int amountOfFriendInvite(User user) {
        return friendDao.countFriendByUser2AndStatus(user, Friend.WAITING);
    }

    @Override
    @Transactional
    public boolean deleteFriends(User userImpl, User userDeleted) {
        Friend friend = friendDao.findByUser1AndUser2(userDeleted, userImpl);
        if (friend != null) {
            friendDao.deleteFriendsByUser1AndUser2(userDeleted, userImpl);
            friendDao.deleteFriendsByUser1AndUser2(userImpl, userDeleted);
            return true;
        }
        return false;
    }

    @Override
    public Friend addFriend(Friend friend) {
        Friend f = friendDao.findByUser1AndUser2(friend.getUser1(), friend.getUser2());
        if (f == null) {
            return friendDao.save(friend);
        }
        return null;
    }

    @Override
    public boolean updateStatus(User userImpl, User userUpdated, int status) {
        System.out.println(userImpl.getId());
        System.out.println(userUpdated.getId());

        if (status == Friend.AGREE) {
            Friend friend = new Friend();
            friend.setUser1(userImpl);
            friend.setUser2(userUpdated);
            addFriend(friend);
        }

        Friend f1 = friendDao.findByUser1AndUser2(userImpl, userUpdated);
        Friend f2 = friendDao.findByUser1AndUser2(userUpdated, userImpl);

        if (f2 != null) {
            f1.setStatus(status);
            friendDao.save(f1);

            f2.setStatus(status);
            friendDao.save(f2);
            return true;
        }

        return false;
    }
}
