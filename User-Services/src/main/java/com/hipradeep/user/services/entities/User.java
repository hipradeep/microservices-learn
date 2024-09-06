package com.hipradeep.user.services.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="micro_users")
public class User {

    @Id
    private String userId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "ABOUT")
    private String about;

    //Other User properties related to the User

    @Transient                      // To not save it in DB, as will be imported from other MicroService
    private List<Rating> ratings = new ArrayList<>();
}
