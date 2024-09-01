package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Channels;
import com.mycompany.myapp.domain.Messages;
import com.mycompany.myapp.domain.UserProfile;
import com.mycompany.myapp.repository.MessagesRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagesService {

    MessagesRepository repository;

    public MessagesService(@Autowired MessagesRepository repository) {
        this.repository = repository;
    }

    public List<Messages> getMessagesByChannel(Long id) {
        return repository.findAllMessagesByChannelId(id);
    }

    public Messages postMessagesByChannel(Long channelID, Long userProfileID, Messages messages) {
        Channels channels = new Channels();
        UserProfile userProfile = new UserProfile();
        channels.setId(channelID);
        userProfile.setId(userProfileID);

        messages.setChannels(channels);
        messages.setUserProfile(userProfile);

        return repository.save(messages);
    }

    public Messages postMessagesByUserAndChannel(Long channelID, String username, Messages messages) {
        Channels channels = new Channels();
        UserProfile userProfile1 = new UserProfile();
        channels.setId(channelID);
        userProfile1.setUsername(username);

        messages.setChannels(channels);
        messages.setUserProfile(userProfile1);

        return repository.save(messages);
    }
}
