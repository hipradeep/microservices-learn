package com.hipradeep.user.services.repositories;

import com.hipradeep.user.services.entities.User;
import com.hipradeep.user.services.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile,Integer> {

    //Any custom method or query can be implemented here
}
