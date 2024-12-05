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
@Table(name = Constant.CLIENT_TABLE)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;
}
