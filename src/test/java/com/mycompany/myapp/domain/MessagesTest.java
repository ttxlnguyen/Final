package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ChannelsTestSamples.*;
import static com.mycompany.myapp.domain.MessagesTestSamples.*;
import static com.mycompany.myapp.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MessagesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Messages.class);
        Messages messages1 = getMessagesSample1();
        Messages messages2 = new Messages();
        assertThat(messages1).isNotEqualTo(messages2);

        messages2.setId(messages1.getId());
        assertThat(messages1).isEqualTo(messages2);

        messages2 = getMessagesSample2();
        assertThat(messages1).isNotEqualTo(messages2);
    }

    @Test
    void userProfileTest() {
        Messages messages = getMessagesRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        messages.setUserProfile(userProfileBack);
        assertThat(messages.getUserProfile()).isEqualTo(userProfileBack);

        messages.userProfile(null);
        assertThat(messages.getUserProfile()).isNull();
    }

    @Test
    void channelsTest() {
        Messages messages = getMessagesRandomSampleGenerator();
        Channels channelsBack = getChannelsRandomSampleGenerator();

        messages.setChannels(channelsBack);
        assertThat(messages.getChannels()).isEqualTo(channelsBack);

        messages.channels(null);
        assertThat(messages.getChannels()).isNull();
    }
}
