package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ChannelsTestSamples.*;
import static com.mycompany.myapp.domain.MessagesTestSamples.*;
import static com.mycompany.myapp.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfile.class);
        UserProfile userProfile1 = getUserProfileSample1();
        UserProfile userProfile2 = new UserProfile();
        assertThat(userProfile1).isNotEqualTo(userProfile2);

        userProfile2.setId(userProfile1.getId());
        assertThat(userProfile1).isEqualTo(userProfile2);

        userProfile2 = getUserProfileSample2();
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }

    @Test
    void messagesTest() {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Messages messagesBack = getMessagesRandomSampleGenerator();

        userProfile.addMessages(messagesBack);
        assertThat(userProfile.getMessages()).containsOnly(messagesBack);
        assertThat(messagesBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.removeMessages(messagesBack);
        assertThat(userProfile.getMessages()).doesNotContain(messagesBack);
        assertThat(messagesBack.getUserProfile()).isNull();

        userProfile.messages(new HashSet<>(Set.of(messagesBack)));
        assertThat(userProfile.getMessages()).containsOnly(messagesBack);
        assertThat(messagesBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.setMessages(new HashSet<>());
        assertThat(userProfile.getMessages()).doesNotContain(messagesBack);
        assertThat(messagesBack.getUserProfile()).isNull();
    }

    @Test
    void channelsTest() {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Channels channelsBack = getChannelsRandomSampleGenerator();

        userProfile.addChannels(channelsBack);
        assertThat(userProfile.getChannels()).containsOnly(channelsBack);

        userProfile.removeChannels(channelsBack);
        assertThat(userProfile.getChannels()).doesNotContain(channelsBack);

        userProfile.channels(new HashSet<>(Set.of(channelsBack)));
        assertThat(userProfile.getChannels()).containsOnly(channelsBack);

        userProfile.setChannels(new HashSet<>());
        assertThat(userProfile.getChannels()).doesNotContain(channelsBack);
    }
}
