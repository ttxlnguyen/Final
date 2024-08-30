package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ChannelsTestSamples.*;
import static com.mycompany.myapp.domain.MessagesTestSamples.*;
import static com.mycompany.myapp.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ChannelsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Channels.class);
        Channels channels1 = getChannelsSample1();
        Channels channels2 = new Channels();
        assertThat(channels1).isNotEqualTo(channels2);

        channels2.setId(channels1.getId());
        assertThat(channels1).isEqualTo(channels2);

        channels2 = getChannelsSample2();
        assertThat(channels1).isNotEqualTo(channels2);
    }

    @Test
    void messagesTest() {
        Channels channels = getChannelsRandomSampleGenerator();
        Messages messagesBack = getMessagesRandomSampleGenerator();

        channels.addMessages(messagesBack);
        assertThat(channels.getMessages()).containsOnly(messagesBack);
        assertThat(messagesBack.getChannels()).isEqualTo(channels);

        channels.removeMessages(messagesBack);
        assertThat(channels.getMessages()).doesNotContain(messagesBack);
        assertThat(messagesBack.getChannels()).isNull();

        channels.messages(new HashSet<>(Set.of(messagesBack)));
        assertThat(channels.getMessages()).containsOnly(messagesBack);
        assertThat(messagesBack.getChannels()).isEqualTo(channels);

        channels.setMessages(new HashSet<>());
        assertThat(channels.getMessages()).doesNotContain(messagesBack);
        assertThat(messagesBack.getChannels()).isNull();
    }

    @Test
    void userProfileTest() {
        Channels channels = getChannelsRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        channels.addUserProfile(userProfileBack);
        assertThat(channels.getUserProfiles()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getChannels()).containsOnly(channels);

        channels.removeUserProfile(userProfileBack);
        assertThat(channels.getUserProfiles()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getChannels()).doesNotContain(channels);

        channels.userProfiles(new HashSet<>(Set.of(userProfileBack)));
        assertThat(channels.getUserProfiles()).containsOnly(userProfileBack);
        assertThat(userProfileBack.getChannels()).containsOnly(channels);

        channels.setUserProfiles(new HashSet<>());
        assertThat(channels.getUserProfiles()).doesNotContain(userProfileBack);
        assertThat(userProfileBack.getChannels()).doesNotContain(channels);
    }
}
