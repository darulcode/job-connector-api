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
@Table(name = Constant.FILE_TABLE)
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name= "name")
    private String name;

    @Column(name = "public_id", unique = true, nullable = false)
    private String publicId;

    @Column(name = "media_type", nullable = false)
    private String mediaType;

    @Column(name = "url_path", unique = true, nullable = false)
    private String urlPath;
}
