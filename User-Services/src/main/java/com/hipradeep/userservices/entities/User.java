package com.hipradeep.userservices.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment
    private Long userId;

    @Column(name = "ENTRY_ID")
    private Integer entryId;

    @Column(name = "USERNAME")
    private String userName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "ABOUT")
    private String about;

    @Column(name = "PASSWORD")
    private String password;

    //if role base user are enable
//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
//    @Column(name = "role")
//    private Set<String> roles = new HashSet<>();

}
