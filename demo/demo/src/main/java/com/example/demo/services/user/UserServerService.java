package com.example.demo.services.user;

import com.example.demo.dao.UserServerDao;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServerService implements IUserServiceGet, IUserServicePost, IUserServicePut {
    @Autowired
    private UserServerDao userDao;

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public List<User> findAllByPhone(User userImpl, User userSearch) {
        User userIdImpl = userDao.findUserByPhone(userImpl.getPhone());
        List<String> s =  userDao.findAllPhoneNumbersExceptCurrentUser(userIdImpl.getId(), userImpl.getPhone(), userSearch.getPhone());
        List<User> users = new ArrayList<>();
        for (String user : s){
            List<String> a = Arrays.stream(user.split(",")).toList();
            User c = new User();
            c.setId(a.get(0));
            c.setFullname(a.get(1));
            c.setPhone(a.get(2));
            c.setAvatar(a.get(3));
            users.add(c);
        }
        return users;
    }

    @Override
    public User getUserById(String id) {
        return userDao.findById(id).orElseThrow(null);

    }

    @Override
    public User getUserByPhone(String phone) {
        return userDao.findUserByPhone(phone);
    }

    @Override
    public String isExistPhone(String phone) {
        boolean isExist = userDao.existsByPhone(phone);
        if(isExist){
            return "Số điện thoại đã tồn tại";
        }
        return "";
    }


    @Override
    public boolean isValidPhone(String phone) {
        return phone.length() == 10; // và goi api kiem tra sdt co ton tai;
    }


    @Override
    public String encodePassword(String password) {
        BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
        return pe.encode(password);
    }

    @Override
    public boolean checkPassword(String password, String encodePassword) {
        BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
        return pe.matches(password, encodePassword);
    }

    @Override
    public boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(regex);
    }

    @Override
    public User isUser(String phone, String password) {
        boolean isPhone = userDao.existsByPhone(phone);
        if (isPhone) {
            String hashedPassword = getUserByPhone(phone).getPassword();
            boolean isPass = checkPassword(password, hashedPassword);
            if (isPass) {
                return userDao.findUserByPhone(phone);
            }
        }

        return null;
    }

    @Override
    public User save(User user) {
        if (isValidPhone(user.getPhone()) && isValidPassword(user.getPassword()) && !userDao.existsByPhone(user.getPhone())) {
            String password = user.getPassword();
            String hashPassword = encodePassword(password);
            user.setPassword(hashPassword);

            return userDao.save(user);
        }
        return null;
    }

    @Override
    public User updatePassword(User user) {
        if (userDao.existsByPhone(user.getPhone())) {
            User user1 = getUserByPhone(user.getPhone());
            String hashPassword = encodePassword(user.getPassword());

            user1.setPassword(hashPassword);
            userDao.save(user1);
            return user1;
        }
        return null;
    }

    @Override
    public User updateVisibility(String phone, int visibility) {
        if (userDao.existsByPhone(phone)) {
            User user = getUserByPhone(phone);

            user.setVisibility(visibility);
            userDao.save(user);
            return user;
        }

        return null;
    }

    @Override
    public User updateAvatar(User user) {
        User found = userDao.findById(user.getId()).orElseThrow(null);

        if(found != null){
            found.setAvatar(user.getAvatar());
            userDao.save(found);
            return found;
        }

        return null;
    }
}
