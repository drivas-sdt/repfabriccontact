package com.rebfabric.poc.contact.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rebfabric.poc.contact.IntegrationTest;
import com.rebfabric.poc.contact.domain.CompanyRegion;
import com.rebfabric.poc.contact.repository.CompanyRegionRepository;
import com.rebfabric.poc.contact.service.dto.CompanyRegionDTO;
import com.rebfabric.poc.contact.service.mapper.CompanyRegionMapper;
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
 * Integration tests for the {@link CompanyRegionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompanyRegionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/company-regions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompanyRegionRepository companyRegionRepository;

    @Autowired
    private CompanyRegionMapper companyRegionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyRegionMockMvc;

    private CompanyRegion companyRegion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyRegion createEntity(EntityManager em) {
        CompanyRegion companyRegion = new CompanyRegion().name(DEFAULT_NAME);
        return companyRegion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyRegion createUpdatedEntity(EntityManager em) {
        CompanyRegion companyRegion = new CompanyRegion().name(UPDATED_NAME);
        return companyRegion;
    }

    @BeforeEach
    public void initTest() {
        companyRegion = createEntity(em);
    }

    @Test
    @Transactional
    void createCompanyRegion() throws Exception {
        int databaseSizeBeforeCreate = companyRegionRepository.findAll().size();
        // Create the CompanyRegion
        CompanyRegionDTO companyRegionDTO = companyRegionMapper.toDto(companyRegion);
        restCompanyRegionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyRegionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CompanyRegion in the database
        List<CompanyRegion> companyRegionList = companyRegionRepository.findAll();
        assertThat(companyRegionList).hasSize(databaseSizeBeforeCreate + 1);
        CompanyRegion testCompanyRegion = companyRegionList.get(companyRegionList.size() - 1);
        assertThat(testCompanyRegion.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createCompanyRegionWithExistingId() throws Exception {
        // Create the CompanyRegion with an existing ID
        companyRegion.setId(1L);
        CompanyRegionDTO companyRegionDTO = companyRegionMapper.toDto(companyRegion);

        int databaseSizeBeforeCreate = companyRegionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyRegionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyRegionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyRegion in the database
        List<CompanyRegion> companyRegionList = companyRegionRepository.findAll();
        assertThat(companyRegionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompanyRegions() throws Exception {
        // Initialize the database
        companyRegionRepository.saveAndFlush(companyRegion);

        // Get all the companyRegionList
        restCompanyRegionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyRegion.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCompanyRegion() throws Exception {
        // Initialize the database
        companyRegionRepository.saveAndFlush(companyRegion);

        // Get the companyRegion
        restCompanyRegionMockMvc
            .perform(get(ENTITY_API_URL_ID, companyRegion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(companyRegion.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCompanyRegion() throws Exception {
        // Get the companyRegion
        restCompanyRegionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompanyRegion() throws Exception {
        // Initialize the database
        companyRegionRepository.saveAndFlush(companyRegion);

        int databaseSizeBeforeUpdate = companyRegionRepository.findAll().size();

        // Update the companyRegion
        CompanyRegion updatedCompanyRegion = companyRegionRepository.findById(companyRegion.getId()).get();
        // Disconnect from session so that the updates on updatedCompanyRegion are not directly saved in db
        em.detach(updatedCompanyRegion);
        updatedCompanyRegion.name(UPDATED_NAME);
        CompanyRegionDTO companyRegionDTO = companyRegionMapper.toDto(updatedCompanyRegion);

        restCompanyRegionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companyRegionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyRegionDTO))
            )
            .andExpect(status().isOk());

        // Validate the CompanyRegion in the database
        List<CompanyRegion> companyRegionList = companyRegionRepository.findAll();
        assertThat(companyRegionList).hasSize(databaseSizeBeforeUpdate);
        CompanyRegion testCompanyRegion = companyRegionList.get(companyRegionList.size() - 1);
        assertThat(testCompanyRegion.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCompanyRegion() throws Exception {
        int databaseSizeBeforeUpdate = companyRegionRepository.findAll().size();
        companyRegion.setId(count.incrementAndGet());

        // Create the CompanyRegion
        CompanyRegionDTO companyRegionDTO = companyRegionMapper.toDto(companyRegion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyRegionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companyRegionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyRegionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyRegion in the database
        List<CompanyRegion> companyRegionList = companyRegionRepository.findAll();
        assertThat(companyRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompanyRegion() throws Exception {
        int databaseSizeBeforeUpdate = companyRegionRepository.findAll().size();
        companyRegion.setId(count.incrementAndGet());

        // Create the CompanyRegion
        CompanyRegionDTO companyRegionDTO = companyRegionMapper.toDto(companyRegion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyRegionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyRegionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyRegion in the database
        List<CompanyRegion> companyRegionList = companyRegionRepository.findAll();
        assertThat(companyRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompanyRegion() throws Exception {
        int databaseSizeBeforeUpdate = companyRegionRepository.findAll().size();
        companyRegion.setId(count.incrementAndGet());

        // Create the CompanyRegion
        CompanyRegionDTO companyRegionDTO = companyRegionMapper.toDto(companyRegion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyRegionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyRegionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyRegion in the database
        List<CompanyRegion> companyRegionList = companyRegionRepository.findAll();
        assertThat(companyRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompanyRegionWithPatch() throws Exception {
        // Initialize the database
        companyRegionRepository.saveAndFlush(companyRegion);

        int databaseSizeBeforeUpdate = companyRegionRepository.findAll().size();

        // Update the companyRegion using partial update
        CompanyRegion partialUpdatedCompanyRegion = new CompanyRegion();
        partialUpdatedCompanyRegion.setId(companyRegion.getId());

        restCompanyRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyRegion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanyRegion))
            )
            .andExpect(status().isOk());

        // Validate the CompanyRegion in the database
        List<CompanyRegion> companyRegionList = companyRegionRepository.findAll();
        assertThat(companyRegionList).hasSize(databaseSizeBeforeUpdate);
        CompanyRegion testCompanyRegion = companyRegionList.get(companyRegionList.size() - 1);
        assertThat(testCompanyRegion.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCompanyRegionWithPatch() throws Exception {
        // Initialize the database
        companyRegionRepository.saveAndFlush(companyRegion);

        int databaseSizeBeforeUpdate = companyRegionRepository.findAll().size();

        // Update the companyRegion using partial update
        CompanyRegion partialUpdatedCompanyRegion = new CompanyRegion();
        partialUpdatedCompanyRegion.setId(companyRegion.getId());

        partialUpdatedCompanyRegion.name(UPDATED_NAME);

        restCompanyRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyRegion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanyRegion))
            )
            .andExpect(status().isOk());

        // Validate the CompanyRegion in the database
        List<CompanyRegion> companyRegionList = companyRegionRepository.findAll();
        assertThat(companyRegionList).hasSize(databaseSizeBeforeUpdate);
        CompanyRegion testCompanyRegion = companyRegionList.get(companyRegionList.size() - 1);
        assertThat(testCompanyRegion.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCompanyRegion() throws Exception {
        int databaseSizeBeforeUpdate = companyRegionRepository.findAll().size();
        companyRegion.setId(count.incrementAndGet());

        // Create the CompanyRegion
        CompanyRegionDTO companyRegionDTO = companyRegionMapper.toDto(companyRegion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, companyRegionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyRegionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyRegion in the database
        List<CompanyRegion> companyRegionList = companyRegionRepository.findAll();
        assertThat(companyRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompanyRegion() throws Exception {
        int databaseSizeBeforeUpdate = companyRegionRepository.findAll().size();
        companyRegion.setId(count.incrementAndGet());

        // Create the CompanyRegion
        CompanyRegionDTO companyRegionDTO = companyRegionMapper.toDto(companyRegion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyRegionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyRegionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyRegion in the database
        List<CompanyRegion> companyRegionList = companyRegionRepository.findAll();
        assertThat(companyRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompanyRegion() throws Exception {
        int databaseSizeBeforeUpdate = companyRegionRepository.findAll().size();
        companyRegion.setId(count.incrementAndGet());

        // Create the CompanyRegion
        CompanyRegionDTO companyRegionDTO = companyRegionMapper.toDto(companyRegion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyRegionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyRegionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyRegion in the database
        List<CompanyRegion> companyRegionList = companyRegionRepository.findAll();
        assertThat(companyRegionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompanyRegion() throws Exception {
        // Initialize the database
        companyRegionRepository.saveAndFlush(companyRegion);

        int databaseSizeBeforeDelete = companyRegionRepository.findAll().size();

        // Delete the companyRegion
        restCompanyRegionMockMvc
            .perform(delete(ENTITY_API_URL_ID, companyRegion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CompanyRegion> companyRegionList = companyRegionRepository.findAll();
        assertThat(companyRegionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
