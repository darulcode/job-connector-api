package com.enigma.jobConnector.entity;

import com.enigma.jobConnector.constants.Constant;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = Constant.USER_CATEGORY_TABLE)
public class UserCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "userCategory", fetch = FetchType.LAZY)
    private List<User> users;
}
