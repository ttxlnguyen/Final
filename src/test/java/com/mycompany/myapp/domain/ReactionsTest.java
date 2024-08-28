package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MessagesTestSamples.*;
import static com.mycompany.myapp.domain.ReactionsTestSamples.*;
import static com.mycompany.myapp.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReactionsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reactions.class);
        Reactions reactions1 = getReactionsSample1();
        Reactions reactions2 = new Reactions();
        assertThat(reactions1).isNotEqualTo(reactions2);

        reactions2.setId(reactions1.getId());
        assertThat(reactions1).isEqualTo(reactions2);

        reactions2 = getReactionsSample2();
        assertThat(reactions1).isNotEqualTo(reactions2);
    }

    @Test
    void userTest() {
        Reactions reactions = getReactionsRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        reactions.setUser(userProfileBack);
        assertThat(reactions.getUser()).isEqualTo(userProfileBack);

        reactions.user(null);
        assertThat(reactions.getUser()).isNull();
    }

    @Test
    void messagesTest() {
        Reactions reactions = getReactionsRandomSampleGenerator();
        Messages messagesBack = getMessagesRandomSampleGenerator();

        reactions.setMessages(messagesBack);
        assertThat(reactions.getMessages()).isEqualTo(messagesBack);

        reactions.messages(null);
        assertThat(reactions.getMessages()).isNull();
    }
}
