package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Channels;
import com.mycompany.myapp.domain.Messages;
import com.mycompany.myapp.repository.ChannelsRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelsService {

    private ChannelsRepository repository;

    public ChannelsService(@Autowired ChannelsRepository repository) {
        this.repository = repository;
    }

    //public List<Messages> getAllMessagesByChannel(Long id){
    ////    final Iterable<Messages> iterable = repository.findAll();
    //
    //    return repository.getMessagesById(id);
    //}

    public List<Channels> findAllChannelMessagesByID(Long id) {
        return repository.findAllMessagesById(id);
    }
}
