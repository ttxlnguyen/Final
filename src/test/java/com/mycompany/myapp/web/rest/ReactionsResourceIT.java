package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ReactionsAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Reactions;
import com.mycompany.myapp.repository.ReactionsRepository;
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
 * Integration tests for the {@link ReactionsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReactionsResourceIT {

    private static final String DEFAULT_REACTION = "AAAAAAAAAA";
    private static final String UPDATED_REACTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReactionsRepository reactionsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReactionsMockMvc;

    private Reactions reactions;

    private Reactions insertedReactions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reactions createEntity(EntityManager em) {
        Reactions reactions = new Reactions().reaction(DEFAULT_REACTION);
        return reactions;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reactions createUpdatedEntity(EntityManager em) {
        Reactions reactions = new Reactions().reaction(UPDATED_REACTION);
        return reactions;
    }

    @BeforeEach
    public void initTest() {
        reactions = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedReactions != null) {
            reactionsRepository.delete(insertedReactions);
            insertedReactions = null;
        }
    }

    @Test
    @Transactional
    void createReactions() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Reactions
        var returnedReactions = om.readValue(
            restReactionsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reactions)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Reactions.class
        );

        // Validate the Reactions in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertReactionsUpdatableFieldsEquals(returnedReactions, getPersistedReactions(returnedReactions));

        insertedReactions = returnedReactions;
    }

    @Test
    @Transactional
    void createReactionsWithExistingId() throws Exception {
        // Create the Reactions with an existing ID
        reactions.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReactionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reactions)))
            .andExpect(status().isBadRequest());

        // Validate the Reactions in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReactions() throws Exception {
        // Initialize the database
        insertedReactions = reactionsRepository.saveAndFlush(reactions);

        // Get all the reactionsList
        restReactionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reactions.getId().intValue())))
            .andExpect(jsonPath("$.[*].reaction").value(hasItem(DEFAULT_REACTION)));
    }

    @Test
    @Transactional
    void getReactions() throws Exception {
        // Initialize the database
        insertedReactions = reactionsRepository.saveAndFlush(reactions);

        // Get the reactions
        restReactionsMockMvc
            .perform(get(ENTITY_API_URL_ID, reactions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reactions.getId().intValue()))
            .andExpect(jsonPath("$.reaction").value(DEFAULT_REACTION));
    }

    @Test
    @Transactional
    void getNonExistingReactions() throws Exception {
        // Get the reactions
        restReactionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReactions() throws Exception {
        // Initialize the database
        insertedReactions = reactionsRepository.saveAndFlush(reactions);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reactions
        Reactions updatedReactions = reactionsRepository.findById(reactions.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReactions are not directly saved in db
        em.detach(updatedReactions);
        updatedReactions.reaction(UPDATED_REACTION);

        restReactionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReactions.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedReactions))
            )
            .andExpect(status().isOk());

        // Validate the Reactions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReactionsToMatchAllProperties(updatedReactions);
    }

    @Test
    @Transactional
    void putNonExistingReactions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reactions.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReactionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reactions.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reactions))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reactions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReactions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reactions.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reactions))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reactions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReactions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reactions.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactionsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reactions)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reactions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReactionsWithPatch() throws Exception {
        // Initialize the database
        insertedReactions = reactionsRepository.saveAndFlush(reactions);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reactions using partial update
        Reactions partialUpdatedReactions = new Reactions();
        partialUpdatedReactions.setId(reactions.getId());

        restReactionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReactions.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReactions))
            )
            .andExpect(status().isOk());

        // Validate the Reactions in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReactionsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReactions, reactions),
            getPersistedReactions(reactions)
        );
    }

    @Test
    @Transactional
    void fullUpdateReactionsWithPatch() throws Exception {
        // Initialize the database
        insertedReactions = reactionsRepository.saveAndFlush(reactions);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reactions using partial update
        Reactions partialUpdatedReactions = new Reactions();
        partialUpdatedReactions.setId(reactions.getId());

        partialUpdatedReactions.reaction(UPDATED_REACTION);

        restReactionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReactions.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReactions))
            )
            .andExpect(status().isOk());

        // Validate the Reactions in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReactionsUpdatableFieldsEquals(partialUpdatedReactions, getPersistedReactions(partialUpdatedReactions));
    }

    @Test
    @Transactional
    void patchNonExistingReactions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reactions.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReactionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reactions.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reactions))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reactions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReactions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reactions.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reactions))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reactions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReactions() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reactions.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactionsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reactions)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reactions in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReactions() throws Exception {
        // Initialize the database
        insertedReactions = reactionsRepository.saveAndFlush(reactions);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reactions
        restReactionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, reactions.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reactionsRepository.count();
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

    protected Reactions getPersistedReactions(Reactions reactions) {
        return reactionsRepository.findById(reactions.getId()).orElseThrow();
    }

    protected void assertPersistedReactionsToMatchAllProperties(Reactions expectedReactions) {
        assertReactionsAllPropertiesEquals(expectedReactions, getPersistedReactions(expectedReactions));
    }

    protected void assertPersistedReactionsToMatchUpdatableProperties(Reactions expectedReactions) {
        assertReactionsAllUpdatablePropertiesEquals(expectedReactions, getPersistedReactions(expectedReactions));
    }
}
