package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false, name = "phone")
    private String phone;

    @Column(nullable = false, name = "password")
    private String password;


    @Column(nullable = false, name = "fullname")
    private String fullname;

    @Column(nullable = false, name = "dob")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dob;

    @Column
    private LocalDate createdAt;

    @Column(columnDefinition = "LONGTEXT")
    private String avatar;

    @Column
    private int visibility;

    @Column
    private String fcmToken;

    public User(String s, String s1, String s2, String s3) {
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now();
        this.visibility = HIDDEN;
    }

    public static final int HIDDEN = 1;
    public static final int UNHIDDEN = 2;
}
