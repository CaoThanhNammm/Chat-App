package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "conversation", referencedColumnName = "id")
    private Private conversation;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user", referencedColumnName = "id")
    private User user;

    @Column(name = "message")
    private String message;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime time;

    @Column
    private int status;

    @PrePersist
    public void prePersist() {
        this.status = UNSEEN;
    }

    public static final int UNSEEN = 1;
    public static final int SEEN = 2;
}
