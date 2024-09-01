package com.mycompany.myapp.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.mycompany.myapp.domain.Channels;
import com.mycompany.myapp.repository.ChannelsRepository;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {

    @Autowired
    private ChannelsRepository channelsRepository;

    @Test
    public void testFindAllMessagesById() {
        List<Channels> channels = channelsRepository.findAllMessagesById(4L);
        assertNotNull(channels);
        assertFalse(channels.isEmpty());
        // Additional assertions based on expected results
    }
}
