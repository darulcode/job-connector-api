package com.enigma.jobConnector.entity;

import com.enigma.jobConnector.constants.Constant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = Constant.NOTIFICATION_TOKEN_TABLE)
public class NotificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "notification_token", nullable = false)
    private String notificationToken;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
