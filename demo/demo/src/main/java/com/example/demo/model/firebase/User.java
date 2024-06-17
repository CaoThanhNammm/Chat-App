package com.example.demo.model.firebase;


import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class User {
    private String id;
    private int avaibility;
    private String fcm;
    private boolean isLogin;
}
