package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Messages;
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
}
