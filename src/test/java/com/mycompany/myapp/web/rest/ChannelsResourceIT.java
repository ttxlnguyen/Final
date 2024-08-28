package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ChannelsAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Channels;
import com.mycompany.myapp.repository.ChannelsRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ChannelsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChannelsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/channels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ChannelsRepository channelsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChannelsMockMvc;

    private Channels channels;

    private Channels insertedChannels;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Channels createEntity(EntityManager em) {
        Channels channels = new Channels().name(DEFAULT_NAME);
        return channels;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Channels createUpdatedEntity(EntityManager em) {
        Channels channels = new Channels().name(UPDATED_NAME);
        return channels;
    }

    @BeforeEach
    public void initTest() {
        channels = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedChannels != null) {
            channelsRepository.delete(insertedChannels);
            insertedChannels = null;
        }
    }

    @Test
    @Transactional
    void createChannels() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Channels
        var returnedChannels = om.readValue(
            restChannelsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(channels)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Channels.class
        );

        // Validate the Channels in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertChannelsUpdatableFieldsEquals(returnedChannels, getPersistedChannels(returnedChannels));

        insertedChannels = returnedChannels;
    }

    @Test
    @Transactional
    void createChannelsWithExistingId() throws Exception {
        // Create the Channels with an existing ID
        channels.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChannelsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(channels)))
            .andExpect(status().isBadRequest());

        // Validate the Channels in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllChannels() throws Exception {
        // Initialize the database
        insertedChannels = channelsRepository.saveAndFlush(channels);

        // Get all the channelsList
        restChannelsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(channels.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getChannels() throws Exception {
        // Initialize the database
        insertedChannels = channelsRepository.saveAndFlush(channels);

        // Get the channels
        restChannelsMockMvc
            .perform(get(ENTITY_API_URL_ID, channels.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(channels.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingChannels() throws Exception {
        // Get the channels
        restChannelsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChannels() throws Exception {
        // Initialize the database
        insertedChannels = channelsRepository.saveAndFlush(channels);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the channels
        Channels updatedChannels = channelsRepository.findById(channels.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedChannels are not directly saved in db
        em.detach(updatedChannels);
        updatedChannels.name(UPDATED_NAME);

        restChannelsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedChannels.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedChannels))
            )
            .andExpect(status().isOk());

        // Validate the Channels in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedChannelsToMatchAllProperties(updatedChannels);
    }

    @Test
    @Transactional
    void putNonExistingChannels() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        channels.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChannelsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, channels.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(channels))
            )
            .andExpect(status().isBadRequest());

        // Validate the Channels in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChannels() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        channels.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChannelsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(channels))
            )
            .andExpect(status().isBadRequest());

        // Validate the Channels in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChannels() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        channels.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChannelsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(channels)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Channels in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChannelsWithPatch() throws Exception {
        // Initialize the database
        insertedChannels = channelsRepository.saveAndFlush(channels);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the channels using partial update
        Channels partialUpdatedChannels = new Channels();
        partialUpdatedChannels.setId(channels.getId());

        partialUpdatedChannels.name(UPDATED_NAME);

        restChannelsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChannels.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChannels))
            )
            .andExpect(status().isOk());

        // Validate the Channels in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChannelsUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedChannels, channels), getPersistedChannels(channels));
    }

    @Test
    @Transactional
    void fullUpdateChannelsWithPatch() throws Exception {
        // Initialize the database
        insertedChannels = channelsRepository.saveAndFlush(channels);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the channels using partial update
        Channels partialUpdatedChannels = new Channels();
        partialUpdatedChannels.setId(channels.getId());

        partialUpdatedChannels.name(UPDATED_NAME);

        restChannelsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChannels.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChannels))
            )
            .andExpect(status().isOk());

        // Validate the Channels in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChannelsUpdatableFieldsEquals(partialUpdatedChannels, getPersistedChannels(partialUpdatedChannels));
    }

    @Test
    @Transactional
    void patchNonExistingChannels() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        channels.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChannelsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, channels.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(channels))
            )
            .andExpect(status().isBadRequest());

        // Validate the Channels in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChannels() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        channels.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChannelsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(channels))
            )
            .andExpect(status().isBadRequest());

        // Validate the Channels in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChannels() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        channels.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChannelsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(channels)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Channels in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChannels() throws Exception {
        // Initialize the database
        insertedChannels = channelsRepository.saveAndFlush(channels);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the channels
        restChannelsMockMvc
            .perform(delete(ENTITY_API_URL_ID, channels.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return channelsRepository.count();
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

    protected Channels getPersistedChannels(Channels channels) {
        return channelsRepository.findById(channels.getId()).orElseThrow();
    }

    protected void assertPersistedChannelsToMatchAllProperties(Channels expectedChannels) {
        assertChannelsAllPropertiesEquals(expectedChannels, getPersistedChannels(expectedChannels));
    }

    protected void assertPersistedChannelsToMatchUpdatableProperties(Channels expectedChannels) {
        assertChannelsAllUpdatablePropertiesEquals(expectedChannels, getPersistedChannels(expectedChannels));
    }
}
