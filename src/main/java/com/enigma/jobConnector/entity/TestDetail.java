package com.enigma.jobConnector.entity;

import com.enigma.jobConnector.constants.Constant;
import com.enigma.jobConnector.constants.SubmissionStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = Constant.TEST_DETAIL_TABLE)
public class TestDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "submission_text")
    private String submissionText;

    @Column(name = "status", nullable = false)
    private SubmissionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    private Test test;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "file_submission_id")
    private FileSubmissionTest fileSubmissionTest;

}
