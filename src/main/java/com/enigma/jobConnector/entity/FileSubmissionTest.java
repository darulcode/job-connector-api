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
@Table(name = Constant.FILE_SUBMISSION_TABLE)
public class FileSubmissionTest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "test_detail_id")
    private TestDetail testDetail;

    @OneToOne
    @JoinColumn(name = "file_id", nullable = false)
    private File file;
}
