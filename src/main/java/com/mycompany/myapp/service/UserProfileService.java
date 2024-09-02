package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.UserProfile;
import com.mycompany.myapp.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserProfileService {

    UserProfileRepository userProfileRepository;

    public UserProfileService(@Autowired UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfile findUserProfileByUserName(String username) {
        return userProfileRepository.findByUsername(username);
    }
}
