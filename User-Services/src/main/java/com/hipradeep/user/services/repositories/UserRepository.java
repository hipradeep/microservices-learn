package com.hipradeep.user.services.repositories;

import com.hipradeep.user.services.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {

    //Any custom method or query can be implemented here
}
