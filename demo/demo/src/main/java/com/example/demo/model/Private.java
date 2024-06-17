package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
public class Private {
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
    private String lastMessage;

    @Column
    private LocalDateTime lastTime;

    @PrePersist
    public void prePersist() {
        this.lastTime = LocalDateTime.now();
    }
}
