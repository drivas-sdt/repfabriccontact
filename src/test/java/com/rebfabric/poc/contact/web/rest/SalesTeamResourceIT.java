package com.rebfabric.poc.contact.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rebfabric.poc.contact.IntegrationTest;
import com.rebfabric.poc.contact.domain.SalesTeam;
import com.rebfabric.poc.contact.repository.SalesTeamRepository;
import com.rebfabric.poc.contact.service.dto.SalesTeamDTO;
import com.rebfabric.poc.contact.service.mapper.SalesTeamMapper;
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
 * Integration tests for the {@link SalesTeamResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SalesTeamResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sales-teams";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SalesTeamRepository salesTeamRepository;

    @Autowired
    private SalesTeamMapper salesTeamMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSalesTeamMockMvc;

    private SalesTeam salesTeam;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalesTeam createEntity(EntityManager em) {
        SalesTeam salesTeam = new SalesTeam().name(DEFAULT_NAME);
        return salesTeam;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalesTeam createUpdatedEntity(EntityManager em) {
        SalesTeam salesTeam = new SalesTeam().name(UPDATED_NAME);
        return salesTeam;
    }

    @BeforeEach
    public void initTest() {
        salesTeam = createEntity(em);
    }

    @Test
    @Transactional
    void createSalesTeam() throws Exception {
        int databaseSizeBeforeCreate = salesTeamRepository.findAll().size();
        // Create the SalesTeam
        SalesTeamDTO salesTeamDTO = salesTeamMapper.toDto(salesTeam);
        restSalesTeamMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salesTeamDTO)))
            .andExpect(status().isCreated());

        // Validate the SalesTeam in the database
        List<SalesTeam> salesTeamList = salesTeamRepository.findAll();
        assertThat(salesTeamList).hasSize(databaseSizeBeforeCreate + 1);
        SalesTeam testSalesTeam = salesTeamList.get(salesTeamList.size() - 1);
        assertThat(testSalesTeam.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createSalesTeamWithExistingId() throws Exception {
        // Create the SalesTeam with an existing ID
        salesTeam.setId(1L);
        SalesTeamDTO salesTeamDTO = salesTeamMapper.toDto(salesTeam);

        int databaseSizeBeforeCreate = salesTeamRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalesTeamMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salesTeamDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SalesTeam in the database
        List<SalesTeam> salesTeamList = salesTeamRepository.findAll();
        assertThat(salesTeamList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSalesTeams() throws Exception {
        // Initialize the database
        salesTeamRepository.saveAndFlush(salesTeam);

        // Get all the salesTeamList
        restSalesTeamMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salesTeam.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getSalesTeam() throws Exception {
        // Initialize the database
        salesTeamRepository.saveAndFlush(salesTeam);

        // Get the salesTeam
        restSalesTeamMockMvc
            .perform(get(ENTITY_API_URL_ID, salesTeam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(salesTeam.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSalesTeam() throws Exception {
        // Get the salesTeam
        restSalesTeamMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSalesTeam() throws Exception {
        // Initialize the database
        salesTeamRepository.saveAndFlush(salesTeam);

        int databaseSizeBeforeUpdate = salesTeamRepository.findAll().size();

        // Update the salesTeam
        SalesTeam updatedSalesTeam = salesTeamRepository.findById(salesTeam.getId()).get();
        // Disconnect from session so that the updates on updatedSalesTeam are not directly saved in db
        em.detach(updatedSalesTeam);
        updatedSalesTeam.name(UPDATED_NAME);
        SalesTeamDTO salesTeamDTO = salesTeamMapper.toDto(updatedSalesTeam);

        restSalesTeamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salesTeamDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salesTeamDTO))
            )
            .andExpect(status().isOk());

        // Validate the SalesTeam in the database
        List<SalesTeam> salesTeamList = salesTeamRepository.findAll();
        assertThat(salesTeamList).hasSize(databaseSizeBeforeUpdate);
        SalesTeam testSalesTeam = salesTeamList.get(salesTeamList.size() - 1);
        assertThat(testSalesTeam.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSalesTeam() throws Exception {
        int databaseSizeBeforeUpdate = salesTeamRepository.findAll().size();
        salesTeam.setId(count.incrementAndGet());

        // Create the SalesTeam
        SalesTeamDTO salesTeamDTO = salesTeamMapper.toDto(salesTeam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalesTeamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salesTeamDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salesTeamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalesTeam in the database
        List<SalesTeam> salesTeamList = salesTeamRepository.findAll();
        assertThat(salesTeamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSalesTeam() throws Exception {
        int databaseSizeBeforeUpdate = salesTeamRepository.findAll().size();
        salesTeam.setId(count.incrementAndGet());

        // Create the SalesTeam
        SalesTeamDTO salesTeamDTO = salesTeamMapper.toDto(salesTeam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalesTeamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(salesTeamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalesTeam in the database
        List<SalesTeam> salesTeamList = salesTeamRepository.findAll();
        assertThat(salesTeamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSalesTeam() throws Exception {
        int databaseSizeBeforeUpdate = salesTeamRepository.findAll().size();
        salesTeam.setId(count.incrementAndGet());

        // Create the SalesTeam
        SalesTeamDTO salesTeamDTO = salesTeamMapper.toDto(salesTeam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalesTeamMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(salesTeamDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalesTeam in the database
        List<SalesTeam> salesTeamList = salesTeamRepository.findAll();
        assertThat(salesTeamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSalesTeamWithPatch() throws Exception {
        // Initialize the database
        salesTeamRepository.saveAndFlush(salesTeam);

        int databaseSizeBeforeUpdate = salesTeamRepository.findAll().size();

        // Update the salesTeam using partial update
        SalesTeam partialUpdatedSalesTeam = new SalesTeam();
        partialUpdatedSalesTeam.setId(salesTeam.getId());

        partialUpdatedSalesTeam.name(UPDATED_NAME);

        restSalesTeamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalesTeam.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalesTeam))
            )
            .andExpect(status().isOk());

        // Validate the SalesTeam in the database
        List<SalesTeam> salesTeamList = salesTeamRepository.findAll();
        assertThat(salesTeamList).hasSize(databaseSizeBeforeUpdate);
        SalesTeam testSalesTeam = salesTeamList.get(salesTeamList.size() - 1);
        assertThat(testSalesTeam.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSalesTeamWithPatch() throws Exception {
        // Initialize the database
        salesTeamRepository.saveAndFlush(salesTeam);

        int databaseSizeBeforeUpdate = salesTeamRepository.findAll().size();

        // Update the salesTeam using partial update
        SalesTeam partialUpdatedSalesTeam = new SalesTeam();
        partialUpdatedSalesTeam.setId(salesTeam.getId());

        partialUpdatedSalesTeam.name(UPDATED_NAME);

        restSalesTeamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalesTeam.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSalesTeam))
            )
            .andExpect(status().isOk());

        // Validate the SalesTeam in the database
        List<SalesTeam> salesTeamList = salesTeamRepository.findAll();
        assertThat(salesTeamList).hasSize(databaseSizeBeforeUpdate);
        SalesTeam testSalesTeam = salesTeamList.get(salesTeamList.size() - 1);
        assertThat(testSalesTeam.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSalesTeam() throws Exception {
        int databaseSizeBeforeUpdate = salesTeamRepository.findAll().size();
        salesTeam.setId(count.incrementAndGet());

        // Create the SalesTeam
        SalesTeamDTO salesTeamDTO = salesTeamMapper.toDto(salesTeam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalesTeamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, salesTeamDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salesTeamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalesTeam in the database
        List<SalesTeam> salesTeamList = salesTeamRepository.findAll();
        assertThat(salesTeamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSalesTeam() throws Exception {
        int databaseSizeBeforeUpdate = salesTeamRepository.findAll().size();
        salesTeam.setId(count.incrementAndGet());

        // Create the SalesTeam
        SalesTeamDTO salesTeamDTO = salesTeamMapper.toDto(salesTeam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalesTeamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(salesTeamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SalesTeam in the database
        List<SalesTeam> salesTeamList = salesTeamRepository.findAll();
        assertThat(salesTeamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSalesTeam() throws Exception {
        int databaseSizeBeforeUpdate = salesTeamRepository.findAll().size();
        salesTeam.setId(count.incrementAndGet());

        // Create the SalesTeam
        SalesTeamDTO salesTeamDTO = salesTeamMapper.toDto(salesTeam);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalesTeamMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(salesTeamDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SalesTeam in the database
        List<SalesTeam> salesTeamList = salesTeamRepository.findAll();
        assertThat(salesTeamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSalesTeam() throws Exception {
        // Initialize the database
        salesTeamRepository.saveAndFlush(salesTeam);

        int databaseSizeBeforeDelete = salesTeamRepository.findAll().size();

        // Delete the salesTeam
        restSalesTeamMockMvc
            .perform(delete(ENTITY_API_URL_ID, salesTeam.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SalesTeam> salesTeamList = salesTeamRepository.findAll();
        assertThat(salesTeamList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
