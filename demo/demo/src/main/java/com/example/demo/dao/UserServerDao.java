package com.example.demo.dao;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserServerDao extends JpaRepository<User, String> {
    User findUserByPhone(String name);

    boolean existsByPhone(String phone);

    @Query("SELECT u.id, u.fullname, u.phone, u.avatar FROM User u WHERE u.phone <> :phoneImpl AND u.phone LIKE %:phoneSearch% and u.phone <> :phoneImpl AND u.id NOT IN (SELECT f.user2.id FROM Friend f WHERE f.user1.id = :idImpl)")
    List<String> findAllPhoneNumbersExceptCurrentUser(@Param("idImpl") String idImpl, @Param("phoneImpl") String phoneImpl, @Param("phoneSearch") String phoneSearch);

    List<User> findAllByPhoneIsContainingIgnoreCaseAndPhoneIsNotContainingIgnoreCase(String phoneImpl, String phoneSearch);
}
