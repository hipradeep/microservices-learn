package com.hipradeep.user.services.entities;


import com.hipradeep.user.services.utils.CompositeId;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@IdClass(CompositeId.class)
@Table(name="micro_users")
public class User {

    @Id
    private String userId;

    @Id
    @GeneratedValue(generator="entry_id", strategy=GenerationType.AUTO )
    @SequenceGenerator(name="entry_id", sequenceName = "user_sequence", initialValue = 10, allocationSize = 1)
    @Column(name = "ENTRY_ID")
    private String entryId;

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
