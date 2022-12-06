package com.rebfabric.poc.contact.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rebfabric.poc.contact.IntegrationTest;
import com.rebfabric.poc.contact.domain.RFUser;
import com.rebfabric.poc.contact.repository.RFUserRepository;
import com.rebfabric.poc.contact.service.dto.RFUserDTO;
import com.rebfabric.poc.contact.service.mapper.RFUserMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RFUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RFUserResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rf-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RFUserRepository rFUserRepository;

    @Autowired
    private RFUserMapper rFUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRFUserMockMvc;

    private RFUser rFUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RFUser createEntity(EntityManager em) {
        RFUser rFUser = new RFUser().name(DEFAULT_NAME);
        return rFUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RFUser createUpdatedEntity(EntityManager em) {
        RFUser rFUser = new RFUser().name(UPDATED_NAME);
        return rFUser;
    }

    @BeforeEach
    public void initTest() {
        rFUser = createEntity(em);
    }

    @Test
    @Transactional
    void createRFUser() throws Exception {
        int databaseSizeBeforeCreate = rFUserRepository.findAll().size();
        // Create the RFUser
        RFUserDTO rFUserDTO = rFUserMapper.toDto(rFUser);
        restRFUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rFUserDTO)))
            .andExpect(status().isCreated());

        // Validate the RFUser in the database
        List<RFUser> rFUserList = rFUserRepository.findAll();
        assertThat(rFUserList).hasSize(databaseSizeBeforeCreate + 1);
        RFUser testRFUser = rFUserList.get(rFUserList.size() - 1);
        assertThat(testRFUser.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createRFUserWithExistingId() throws Exception {
        // Create the RFUser with an existing ID
        rFUser.setId(1L);
        RFUserDTO rFUserDTO = rFUserMapper.toDto(rFUser);

        int databaseSizeBeforeCreate = rFUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRFUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rFUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RFUser in the database
        List<RFUser> rFUserList = rFUserRepository.findAll();
        assertThat(rFUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRFUsers() throws Exception {
        // Initialize the database
        rFUserRepository.saveAndFlush(rFUser);

        // Get all the rFUserList
        restRFUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rFUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getRFUser() throws Exception {
        // Initialize the database
        rFUserRepository.saveAndFlush(rFUser);

        // Get the rFUser
        restRFUserMockMvc
            .perform(get(ENTITY_API_URL_ID, rFUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rFUser.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingRFUser() throws Exception {
        // Get the rFUser
        restRFUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRFUser() throws Exception {
        // Initialize the database
        rFUserRepository.saveAndFlush(rFUser);

        int databaseSizeBeforeUpdate = rFUserRepository.findAll().size();

        // Update the rFUser
        RFUser updatedRFUser = rFUserRepository.findById(rFUser.getId()).get();
        // Disconnect from session so that the updates on updatedRFUser are not directly saved in db
        em.detach(updatedRFUser);
        updatedRFUser.name(UPDATED_NAME);
        RFUserDTO rFUserDTO = rFUserMapper.toDto(updatedRFUser);

        restRFUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rFUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rFUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the RFUser in the database
        List<RFUser> rFUserList = rFUserRepository.findAll();
        assertThat(rFUserList).hasSize(databaseSizeBeforeUpdate);
        RFUser testRFUser = rFUserList.get(rFUserList.size() - 1);
        assertThat(testRFUser.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingRFUser() throws Exception {
        int databaseSizeBeforeUpdate = rFUserRepository.findAll().size();
        rFUser.setId(count.incrementAndGet());

        // Create the RFUser
        RFUserDTO rFUserDTO = rFUserMapper.toDto(rFUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRFUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rFUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rFUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RFUser in the database
        List<RFUser> rFUserList = rFUserRepository.findAll();
        assertThat(rFUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRFUser() throws Exception {
        int databaseSizeBeforeUpdate = rFUserRepository.findAll().size();
        rFUser.setId(count.incrementAndGet());

        // Create the RFUser
        RFUserDTO rFUserDTO = rFUserMapper.toDto(rFUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRFUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rFUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RFUser in the database
        List<RFUser> rFUserList = rFUserRepository.findAll();
        assertThat(rFUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRFUser() throws Exception {
        int databaseSizeBeforeUpdate = rFUserRepository.findAll().size();
        rFUser.setId(count.incrementAndGet());

        // Create the RFUser
        RFUserDTO rFUserDTO = rFUserMapper.toDto(rFUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRFUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rFUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RFUser in the database
        List<RFUser> rFUserList = rFUserRepository.findAll();
        assertThat(rFUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRFUserWithPatch() throws Exception {
        // Initialize the database
        rFUserRepository.saveAndFlush(rFUser);

        int databaseSizeBeforeUpdate = rFUserRepository.findAll().size();

        // Update the rFUser using partial update
        RFUser partialUpdatedRFUser = new RFUser();
        partialUpdatedRFUser.setId(rFUser.getId());

        restRFUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRFUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRFUser))
            )
            .andExpect(status().isOk());

        // Validate the RFUser in the database
        List<RFUser> rFUserList = rFUserRepository.findAll();
        assertThat(rFUserList).hasSize(databaseSizeBeforeUpdate);
        RFUser testRFUser = rFUserList.get(rFUserList.size() - 1);
        assertThat(testRFUser.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateRFUserWithPatch() throws Exception {
        // Initialize the database
        rFUserRepository.saveAndFlush(rFUser);

        int databaseSizeBeforeUpdate = rFUserRepository.findAll().size();

        // Update the rFUser using partial update
        RFUser partialUpdatedRFUser = new RFUser();
        partialUpdatedRFUser.setId(rFUser.getId());

        partialUpdatedRFUser.name(UPDATED_NAME);

        restRFUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRFUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRFUser))
            )
            .andExpect(status().isOk());

        // Validate the RFUser in the database
        List<RFUser> rFUserList = rFUserRepository.findAll();
        assertThat(rFUserList).hasSize(databaseSizeBeforeUpdate);
        RFUser testRFUser = rFUserList.get(rFUserList.size() - 1);
        assertThat(testRFUser.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingRFUser() throws Exception {
        int databaseSizeBeforeUpdate = rFUserRepository.findAll().size();
        rFUser.setId(count.incrementAndGet());

        // Create the RFUser
        RFUserDTO rFUserDTO = rFUserMapper.toDto(rFUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRFUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rFUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rFUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RFUser in the database
        List<RFUser> rFUserList = rFUserRepository.findAll();
        assertThat(rFUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRFUser() throws Exception {
        int databaseSizeBeforeUpdate = rFUserRepository.findAll().size();
        rFUser.setId(count.incrementAndGet());

        // Create the RFUser
        RFUserDTO rFUserDTO = rFUserMapper.toDto(rFUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRFUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rFUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RFUser in the database
        List<RFUser> rFUserList = rFUserRepository.findAll();
        assertThat(rFUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRFUser() throws Exception {
        int databaseSizeBeforeUpdate = rFUserRepository.findAll().size();
        rFUser.setId(count.incrementAndGet());

        // Create the RFUser
        RFUserDTO rFUserDTO = rFUserMapper.toDto(rFUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRFUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rFUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RFUser in the database
        List<RFUser> rFUserList = rFUserRepository.findAll();
        assertThat(rFUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRFUser() throws Exception {
        // Initialize the database
        rFUserRepository.saveAndFlush(rFUser);

        int databaseSizeBeforeDelete = rFUserRepository.findAll().size();

        // Delete the rFUser
        restRFUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, rFUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RFUser> rFUserList = rFUserRepository.findAll();
        assertThat(rFUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
