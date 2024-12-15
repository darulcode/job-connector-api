package com.enigma.jobConnector.entity;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.constants.EntityStatus;
import com.enigma.jobConnector.constants.TestStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = Constant.TEST_TABLE)
@EntityListeners(AuditingEntityListener.class)
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deadline_at", nullable = false)
    private LocalDateTime deadlineAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TestStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_test_id")
    private FileTest fileTest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_bd_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "isDeleted")
    private EntityStatus isDeleted;

    @OneToMany(mappedBy = "test")
    private List<TestDetail> userDetails;
}
