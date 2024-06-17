package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@ToString
@Table(name ="friends")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user1", referencedColumnName = "id")
    private User user1;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user2", referencedColumnName = "id")
    private User user2;

    @Column
    private int status;

    @PrePersist
    public void prePersist() {
        this.status = WAITING;
    }

    public static final int WAITING = 0;
    public static final int AGREE = 1;
    public static final int DENIED = 2;
}
