package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Channels;
import com.mycompany.myapp.domain.Messages;
import com.mycompany.myapp.domain.UserProfile;
import com.mycompany.myapp.repository.ChannelsRepository;
import com.mycompany.myapp.repository.UserProfileRepository;
import com.mycompany.myapp.security.SecurityUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelsService {

    private ChannelsRepository repository;
    private UserProfileRepository userProfileRepository;

    public ChannelsService(@Autowired ChannelsRepository repository, @Autowired UserProfileRepository userProfileRepository) {
        this.repository = repository;
        this.userProfileRepository = userProfileRepository;
    }

    //public List<Messages> getAllMessagesByChannel(Long id){
    ////    final Iterable<Messages> iterable = repository.findAll();
    //
    //    return repository.getMessagesById(id);
    //}

    public List<Channels> findAllChannelMessagesByID(Long id) {
        return repository.findAllMessagesById(id);
    }

    public List<Channels> findChannelsByUsername(String username) {
        return repository.findAllChannelsByUsername(username);
    }

    public Channels createChannelByUsername(String username, Channels channels) {
        Boolean privacy = true;
        UserProfile userProfile = userProfileRepository.findByUsername(username);

        String userLogin = SecurityUtils.getCurrentUserLogin().orElse("Default Value");
        UserProfile userProfile1 = userProfileRepository.findByUsername(userLogin);

        channels.addUserProfile(userProfile1);
        channels.addUserProfile(userProfile);
        channels.setPrivacy(privacy);

        return repository.save(channels);
    }

    public Channels createPublicChannelByUsername(Channels channels) {
        Boolean privacy = false;
        //        UserProfile userProfile = userProfileRepository.findByUsername(username);

        String userLogin = SecurityUtils.getCurrentUserLogin().orElse("Default Value");
        UserProfile userProfile1 = userProfileRepository.findByUsername(userLogin);

        channels.addUserProfile(userProfile1);
        //        channels.addUserProfile(userProfile);
        channels.setPrivacy(privacy);

        return repository.save(channels);
    }

    public List<Channels> findPrivateChannelsByUsername(String username) {
        return repository.findAllPrivateChannelsByUsername(username);
    }

    public List<Channels> getAllPublicChannels() {
        return repository.listAllPublicChannels();
    }
}
