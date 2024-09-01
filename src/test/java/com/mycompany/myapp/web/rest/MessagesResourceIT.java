package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.MessagesAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Messages;
import com.mycompany.myapp.repository.MessagesRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MessagesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MessagesResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_SENT_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SENT_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EDITED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EDITED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/messages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMessagesMockMvc;

    private Messages messages;

    private Messages insertedMessages;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Messages createEntity(EntityManager em) {
        Messages messages = new Messages()
            .content(DEFAULT_CONTENT)
            .sentAt(DEFAULT_SENT_AT)
            .editedAt(DEFAULT_EDITED_AT)
            .isDeleted(DEFAULT_IS_DELETED);
        return messages;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Messages createUpdatedEntity(EntityManager em) {
        Messages messages = new Messages()
            .content(UPDATED_CONTENT)
            .sentAt(UPDATED_SENT_AT)
            .editedAt(UPDATED_EDITED_AT)
            .isDeleted(UPDATED_IS_DELETED);
        return messages;
    }

    @BeforeEach
    public void initTest() {
        messages = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedMessages != null) {
            messagesRepository.delete(insertedMessages);
            insertedMessages = null;
        }
    }

    @Test
    @Transactional
    void createMessages() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Messages
        var returnedMessages = om.readValue(
            restMessagesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messages)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Messages.class
        );

        // Validate the Messages in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMessagesUpdatableFieldsEquals(returnedMessages, getPersistedMessages(returnedMessages));

        insertedMessages = returnedMessages;
    }

    @Test
    @Transactional
    void createMessagesWithExistingId() throws Exception {
        // Create the Messages with an existing ID
        messages.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessagesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messages)))
            .andExpect(status().isBadRequest());

        // Validate the Messages in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMessages() throws Exception {
        // Initialize the database
        insertedMessages = messagesRepository.saveAndFlush(messages);

        // Get all the messagesList
        restMessagesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messages.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].sentAt").value(hasItem(DEFAULT_SENT_AT.toString())))
            .andExpect(jsonPath("$.[*].editedAt").value(hasItem(DEFAULT_EDITED_AT.toString())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getMessages() throws Exception {
        // Initialize the database
        insertedMessages = messagesRepository.saveAndFlush(messages);

        // Get the messages
        restMessagesMockMvc
            .perform(get(ENTITY_API_URL_ID, messages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(messages.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.sentAt").value(DEFAULT_SENT_AT.toString()))
            .andExpect(jsonPath("$.editedAt").value(DEFAULT_EDITED_AT.toString()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingMessages() throws Exception {
        // Get the messages
        restMessagesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMessages() throws Exception {
        // Initialize the database
        insertedMessages = messagesRepository.saveAndFlush(messages);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the messages
        Messages updatedMessages = messagesRepository.findById(messages.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMessages are not directly saved in db
        em.detach(updatedMessages);
        updatedMessages.content(UPDATED_CONTENT).sentAt(UPDATED_SENT_AT).editedAt(UPDATED_EDITED_AT).isDeleted(UPDATED_IS_DELETED);

        restMessagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMessages.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMessages))
            )
            .andExpect(status().isOk());

        // Validate the Messages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMessagesToMatchAllProperties(updatedMessages);
    }

    @Test
    @Transactional
    void putNonExistingMessages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messages.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, messages.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messages))
            )
            .andExpect(status().isBadRequest());

        // Validate the Messages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMessages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messages.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessagesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(messages))
            )
            .andExpect(status().isBadRequest());

        // Validate the Messages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMessages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messages.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessagesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(messages)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Messages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMessagesWithPatch() throws Exception {
        // Initialize the database
        insertedMessages = messagesRepository.saveAndFlush(messages);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the messages using partial update
        Messages partialUpdatedMessages = new Messages();
        partialUpdatedMessages.setId(messages.getId());

        partialUpdatedMessages.content(UPDATED_CONTENT).editedAt(UPDATED_EDITED_AT);

        restMessagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMessages.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMessages))
            )
            .andExpect(status().isOk());

        // Validate the Messages in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMessagesUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMessages, messages), getPersistedMessages(messages));
    }

    @Test
    @Transactional
    void fullUpdateMessagesWithPatch() throws Exception {
        // Initialize the database
        insertedMessages = messagesRepository.saveAndFlush(messages);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the messages using partial update
        Messages partialUpdatedMessages = new Messages();
        partialUpdatedMessages.setId(messages.getId());

        partialUpdatedMessages.content(UPDATED_CONTENT).sentAt(UPDATED_SENT_AT).editedAt(UPDATED_EDITED_AT).isDeleted(UPDATED_IS_DELETED);

        restMessagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMessages.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMessages))
            )
            .andExpect(status().isOk());

        // Validate the Messages in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMessagesUpdatableFieldsEquals(partialUpdatedMessages, getPersistedMessages(partialUpdatedMessages));
    }

    @Test
    @Transactional
    void patchNonExistingMessages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messages.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, messages.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(messages))
            )
            .andExpect(status().isBadRequest());

        // Validate the Messages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMessages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messages.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessagesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(messages))
            )
            .andExpect(status().isBadRequest());

        // Validate the Messages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMessages() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        messages.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessagesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(messages)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Messages in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMessages() throws Exception {
        // Initialize the database
        insertedMessages = messagesRepository.saveAndFlush(messages);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the messages
        restMessagesMockMvc
            .perform(delete(ENTITY_API_URL_ID, messages.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return messagesRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Messages getPersistedMessages(Messages messages) {
        return messagesRepository.findById(messages.getId()).orElseThrow();
    }

    protected void assertPersistedMessagesToMatchAllProperties(Messages expectedMessages) {
        assertMessagesAllPropertiesEquals(expectedMessages, getPersistedMessages(expectedMessages));
    }

    protected void assertPersistedMessagesToMatchUpdatableProperties(Messages expectedMessages) {
        assertMessagesAllUpdatablePropertiesEquals(expectedMessages, getPersistedMessages(expectedMessages));
    }
}
