package com.rebfabric.poc.contact.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rebfabric.poc.contact.IntegrationTest;
import com.rebfabric.poc.contact.domain.CompanyType;
import com.rebfabric.poc.contact.repository.CompanyTypeRepository;
import com.rebfabric.poc.contact.service.dto.CompanyTypeDTO;
import com.rebfabric.poc.contact.service.mapper.CompanyTypeMapper;
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
 * Integration tests for the {@link CompanyTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompanyTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/company-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompanyTypeRepository companyTypeRepository;

    @Autowired
    private CompanyTypeMapper companyTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyTypeMockMvc;

    private CompanyType companyType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyType createEntity(EntityManager em) {
        CompanyType companyType = new CompanyType().name(DEFAULT_NAME);
        return companyType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyType createUpdatedEntity(EntityManager em) {
        CompanyType companyType = new CompanyType().name(UPDATED_NAME);
        return companyType;
    }

    @BeforeEach
    public void initTest() {
        companyType = createEntity(em);
    }

    @Test
    @Transactional
    void createCompanyType() throws Exception {
        int databaseSizeBeforeCreate = companyTypeRepository.findAll().size();
        // Create the CompanyType
        CompanyTypeDTO companyTypeDTO = companyTypeMapper.toDto(companyType);
        restCompanyTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CompanyType in the database
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CompanyType testCompanyType = companyTypeList.get(companyTypeList.size() - 1);
        assertThat(testCompanyType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createCompanyTypeWithExistingId() throws Exception {
        // Create the CompanyType with an existing ID
        companyType.setId(1L);
        CompanyTypeDTO companyTypeDTO = companyTypeMapper.toDto(companyType);

        int databaseSizeBeforeCreate = companyTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyType in the database
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompanyTypes() throws Exception {
        // Initialize the database
        companyTypeRepository.saveAndFlush(companyType);

        // Get all the companyTypeList
        restCompanyTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCompanyType() throws Exception {
        // Initialize the database
        companyTypeRepository.saveAndFlush(companyType);

        // Get the companyType
        restCompanyTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, companyType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(companyType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCompanyType() throws Exception {
        // Get the companyType
        restCompanyTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompanyType() throws Exception {
        // Initialize the database
        companyTypeRepository.saveAndFlush(companyType);

        int databaseSizeBeforeUpdate = companyTypeRepository.findAll().size();

        // Update the companyType
        CompanyType updatedCompanyType = companyTypeRepository.findById(companyType.getId()).get();
        // Disconnect from session so that the updates on updatedCompanyType are not directly saved in db
        em.detach(updatedCompanyType);
        updatedCompanyType.name(UPDATED_NAME);
        CompanyTypeDTO companyTypeDTO = companyTypeMapper.toDto(updatedCompanyType);

        restCompanyTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companyTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CompanyType in the database
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeUpdate);
        CompanyType testCompanyType = companyTypeList.get(companyTypeList.size() - 1);
        assertThat(testCompanyType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCompanyType() throws Exception {
        int databaseSizeBeforeUpdate = companyTypeRepository.findAll().size();
        companyType.setId(count.incrementAndGet());

        // Create the CompanyType
        CompanyTypeDTO companyTypeDTO = companyTypeMapper.toDto(companyType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companyTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyType in the database
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompanyType() throws Exception {
        int databaseSizeBeforeUpdate = companyTypeRepository.findAll().size();
        companyType.setId(count.incrementAndGet());

        // Create the CompanyType
        CompanyTypeDTO companyTypeDTO = companyTypeMapper.toDto(companyType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companyTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyType in the database
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompanyType() throws Exception {
        int databaseSizeBeforeUpdate = companyTypeRepository.findAll().size();
        companyType.setId(count.incrementAndGet());

        // Create the CompanyType
        CompanyTypeDTO companyTypeDTO = companyTypeMapper.toDto(companyType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companyTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyType in the database
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompanyTypeWithPatch() throws Exception {
        // Initialize the database
        companyTypeRepository.saveAndFlush(companyType);

        int databaseSizeBeforeUpdate = companyTypeRepository.findAll().size();

        // Update the companyType using partial update
        CompanyType partialUpdatedCompanyType = new CompanyType();
        partialUpdatedCompanyType.setId(companyType.getId());

        restCompanyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanyType))
            )
            .andExpect(status().isOk());

        // Validate the CompanyType in the database
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeUpdate);
        CompanyType testCompanyType = companyTypeList.get(companyTypeList.size() - 1);
        assertThat(testCompanyType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCompanyTypeWithPatch() throws Exception {
        // Initialize the database
        companyTypeRepository.saveAndFlush(companyType);

        int databaseSizeBeforeUpdate = companyTypeRepository.findAll().size();

        // Update the companyType using partial update
        CompanyType partialUpdatedCompanyType = new CompanyType();
        partialUpdatedCompanyType.setId(companyType.getId());

        partialUpdatedCompanyType.name(UPDATED_NAME);

        restCompanyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanyType))
            )
            .andExpect(status().isOk());

        // Validate the CompanyType in the database
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeUpdate);
        CompanyType testCompanyType = companyTypeList.get(companyTypeList.size() - 1);
        assertThat(testCompanyType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCompanyType() throws Exception {
        int databaseSizeBeforeUpdate = companyTypeRepository.findAll().size();
        companyType.setId(count.incrementAndGet());

        // Create the CompanyType
        CompanyTypeDTO companyTypeDTO = companyTypeMapper.toDto(companyType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, companyTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyType in the database
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompanyType() throws Exception {
        int databaseSizeBeforeUpdate = companyTypeRepository.findAll().size();
        companyType.setId(count.incrementAndGet());

        // Create the CompanyType
        CompanyTypeDTO companyTypeDTO = companyTypeMapper.toDto(companyType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companyTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyType in the database
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompanyType() throws Exception {
        int databaseSizeBeforeUpdate = companyTypeRepository.findAll().size();
        companyType.setId(count.incrementAndGet());

        // Create the CompanyType
        CompanyTypeDTO companyTypeDTO = companyTypeMapper.toDto(companyType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(companyTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyType in the database
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompanyType() throws Exception {
        // Initialize the database
        companyTypeRepository.saveAndFlush(companyType);

        int databaseSizeBeforeDelete = companyTypeRepository.findAll().size();

        // Delete the companyType
        restCompanyTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, companyType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CompanyType> companyTypeList = companyTypeRepository.findAll();
        assertThat(companyTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
