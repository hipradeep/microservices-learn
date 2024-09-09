package com.hipradeep.authservice.service;

import com.hipradeep.authservice.dto.UserCredentialDto;
import com.hipradeep.authservice.entity.UserCredential;

public interface UserCredentialService {

    //GET Single User by UserId
    UserCredentialDto getUserCredential(String userId);
}
