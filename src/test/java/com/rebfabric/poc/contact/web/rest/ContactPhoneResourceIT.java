package com.rebfabric.poc.contact.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rebfabric.poc.contact.IntegrationTest;
import com.rebfabric.poc.contact.domain.Contact;
import com.rebfabric.poc.contact.domain.ContactPhone;
import com.rebfabric.poc.contact.repository.ContactPhoneRepository;
import com.rebfabric.poc.contact.service.dto.ContactPhoneDTO;
import com.rebfabric.poc.contact.service.mapper.ContactPhoneMapper;
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
 * Integration tests for the {@link ContactPhoneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactPhoneResourceIT {

    private static final Integer DEFAULT_PHONE_TYPE = 1;
    private static final Integer UPDATED_PHONE_TYPE = 2;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contact-phones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactPhoneRepository contactPhoneRepository;

    @Autowired
    private ContactPhoneMapper contactPhoneMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactPhoneMockMvc;

    private ContactPhone contactPhone;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactPhone createEntity(EntityManager em) {
        ContactPhone contactPhone = new ContactPhone().phoneType(DEFAULT_PHONE_TYPE).phone(DEFAULT_PHONE);
        // Add required entity
        Contact contact;
        if (TestUtil.findAll(em, Contact.class).isEmpty()) {
            contact = ContactResourceIT.createEntity(em);
            em.persist(contact);
            em.flush();
        } else {
            contact = TestUtil.findAll(em, Contact.class).get(0);
        }
        contactPhone.setContact(contact);
        return contactPhone;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactPhone createUpdatedEntity(EntityManager em) {
        ContactPhone contactPhone = new ContactPhone().phoneType(UPDATED_PHONE_TYPE).phone(UPDATED_PHONE);
        // Add required entity
        Contact contact;
        if (TestUtil.findAll(em, Contact.class).isEmpty()) {
            contact = ContactResourceIT.createUpdatedEntity(em);
            em.persist(contact);
            em.flush();
        } else {
            contact = TestUtil.findAll(em, Contact.class).get(0);
        }
        contactPhone.setContact(contact);
        return contactPhone;
    }

    @BeforeEach
    public void initTest() {
        contactPhone = createEntity(em);
    }

    @Test
    @Transactional
    void createContactPhone() throws Exception {
        int databaseSizeBeforeCreate = contactPhoneRepository.findAll().size();
        // Create the ContactPhone
        ContactPhoneDTO contactPhoneDTO = contactPhoneMapper.toDto(contactPhone);
        restContactPhoneMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactPhoneDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContactPhone in the database
        List<ContactPhone> contactPhoneList = contactPhoneRepository.findAll();
        assertThat(contactPhoneList).hasSize(databaseSizeBeforeCreate + 1);
        ContactPhone testContactPhone = contactPhoneList.get(contactPhoneList.size() - 1);
        assertThat(testContactPhone.getPhoneType()).isEqualTo(DEFAULT_PHONE_TYPE);
        assertThat(testContactPhone.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void createContactPhoneWithExistingId() throws Exception {
        // Create the ContactPhone with an existing ID
        contactPhone.setId(1L);
        ContactPhoneDTO contactPhoneDTO = contactPhoneMapper.toDto(contactPhone);

        int databaseSizeBeforeCreate = contactPhoneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactPhoneMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactPhoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactPhone in the database
        List<ContactPhone> contactPhoneList = contactPhoneRepository.findAll();
        assertThat(contactPhoneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPhoneTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactPhoneRepository.findAll().size();
        // set the field null
        contactPhone.setPhoneType(null);

        // Create the ContactPhone, which fails.
        ContactPhoneDTO contactPhoneDTO = contactPhoneMapper.toDto(contactPhone);

        restContactPhoneMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactPhoneDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContactPhone> contactPhoneList = contactPhoneRepository.findAll();
        assertThat(contactPhoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactPhoneRepository.findAll().size();
        // set the field null
        contactPhone.setPhone(null);

        // Create the ContactPhone, which fails.
        ContactPhoneDTO contactPhoneDTO = contactPhoneMapper.toDto(contactPhone);

        restContactPhoneMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactPhoneDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContactPhone> contactPhoneList = contactPhoneRepository.findAll();
        assertThat(contactPhoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContactPhones() throws Exception {
        // Initialize the database
        contactPhoneRepository.saveAndFlush(contactPhone);

        // Get all the contactPhoneList
        restContactPhoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactPhone.getId().intValue())))
            .andExpect(jsonPath("$.[*].phoneType").value(hasItem(DEFAULT_PHONE_TYPE)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }

    @Test
    @Transactional
    void getContactPhone() throws Exception {
        // Initialize the database
        contactPhoneRepository.saveAndFlush(contactPhone);

        // Get the contactPhone
        restContactPhoneMockMvc
            .perform(get(ENTITY_API_URL_ID, contactPhone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactPhone.getId().intValue()))
            .andExpect(jsonPath("$.phoneType").value(DEFAULT_PHONE_TYPE))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }

    @Test
    @Transactional
    void getNonExistingContactPhone() throws Exception {
        // Get the contactPhone
        restContactPhoneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContactPhone() throws Exception {
        // Initialize the database
        contactPhoneRepository.saveAndFlush(contactPhone);

        int databaseSizeBeforeUpdate = contactPhoneRepository.findAll().size();

        // Update the contactPhone
        ContactPhone updatedContactPhone = contactPhoneRepository.findById(contactPhone.getId()).get();
        // Disconnect from session so that the updates on updatedContactPhone are not directly saved in db
        em.detach(updatedContactPhone);
        updatedContactPhone.phoneType(UPDATED_PHONE_TYPE).phone(UPDATED_PHONE);
        ContactPhoneDTO contactPhoneDTO = contactPhoneMapper.toDto(updatedContactPhone);

        restContactPhoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactPhoneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactPhoneDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContactPhone in the database
        List<ContactPhone> contactPhoneList = contactPhoneRepository.findAll();
        assertThat(contactPhoneList).hasSize(databaseSizeBeforeUpdate);
        ContactPhone testContactPhone = contactPhoneList.get(contactPhoneList.size() - 1);
        assertThat(testContactPhone.getPhoneType()).isEqualTo(UPDATED_PHONE_TYPE);
        assertThat(testContactPhone.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void putNonExistingContactPhone() throws Exception {
        int databaseSizeBeforeUpdate = contactPhoneRepository.findAll().size();
        contactPhone.setId(count.incrementAndGet());

        // Create the ContactPhone
        ContactPhoneDTO contactPhoneDTO = contactPhoneMapper.toDto(contactPhone);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactPhoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactPhoneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactPhoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactPhone in the database
        List<ContactPhone> contactPhoneList = contactPhoneRepository.findAll();
        assertThat(contactPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactPhone() throws Exception {
        int databaseSizeBeforeUpdate = contactPhoneRepository.findAll().size();
        contactPhone.setId(count.incrementAndGet());

        // Create the ContactPhone
        ContactPhoneDTO contactPhoneDTO = contactPhoneMapper.toDto(contactPhone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactPhoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactPhoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactPhone in the database
        List<ContactPhone> contactPhoneList = contactPhoneRepository.findAll();
        assertThat(contactPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactPhone() throws Exception {
        int databaseSizeBeforeUpdate = contactPhoneRepository.findAll().size();
        contactPhone.setId(count.incrementAndGet());

        // Create the ContactPhone
        ContactPhoneDTO contactPhoneDTO = contactPhoneMapper.toDto(contactPhone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactPhoneMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactPhoneDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactPhone in the database
        List<ContactPhone> contactPhoneList = contactPhoneRepository.findAll();
        assertThat(contactPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactPhoneWithPatch() throws Exception {
        // Initialize the database
        contactPhoneRepository.saveAndFlush(contactPhone);

        int databaseSizeBeforeUpdate = contactPhoneRepository.findAll().size();

        // Update the contactPhone using partial update
        ContactPhone partialUpdatedContactPhone = new ContactPhone();
        partialUpdatedContactPhone.setId(contactPhone.getId());

        restContactPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactPhone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactPhone))
            )
            .andExpect(status().isOk());

        // Validate the ContactPhone in the database
        List<ContactPhone> contactPhoneList = contactPhoneRepository.findAll();
        assertThat(contactPhoneList).hasSize(databaseSizeBeforeUpdate);
        ContactPhone testContactPhone = contactPhoneList.get(contactPhoneList.size() - 1);
        assertThat(testContactPhone.getPhoneType()).isEqualTo(DEFAULT_PHONE_TYPE);
        assertThat(testContactPhone.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void fullUpdateContactPhoneWithPatch() throws Exception {
        // Initialize the database
        contactPhoneRepository.saveAndFlush(contactPhone);

        int databaseSizeBeforeUpdate = contactPhoneRepository.findAll().size();

        // Update the contactPhone using partial update
        ContactPhone partialUpdatedContactPhone = new ContactPhone();
        partialUpdatedContactPhone.setId(contactPhone.getId());

        partialUpdatedContactPhone.phoneType(UPDATED_PHONE_TYPE).phone(UPDATED_PHONE);

        restContactPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactPhone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactPhone))
            )
            .andExpect(status().isOk());

        // Validate the ContactPhone in the database
        List<ContactPhone> contactPhoneList = contactPhoneRepository.findAll();
        assertThat(contactPhoneList).hasSize(databaseSizeBeforeUpdate);
        ContactPhone testContactPhone = contactPhoneList.get(contactPhoneList.size() - 1);
        assertThat(testContactPhone.getPhoneType()).isEqualTo(UPDATED_PHONE_TYPE);
        assertThat(testContactPhone.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void patchNonExistingContactPhone() throws Exception {
        int databaseSizeBeforeUpdate = contactPhoneRepository.findAll().size();
        contactPhone.setId(count.incrementAndGet());

        // Create the ContactPhone
        ContactPhoneDTO contactPhoneDTO = contactPhoneMapper.toDto(contactPhone);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactPhoneDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactPhoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactPhone in the database
        List<ContactPhone> contactPhoneList = contactPhoneRepository.findAll();
        assertThat(contactPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactPhone() throws Exception {
        int databaseSizeBeforeUpdate = contactPhoneRepository.findAll().size();
        contactPhone.setId(count.incrementAndGet());

        // Create the ContactPhone
        ContactPhoneDTO contactPhoneDTO = contactPhoneMapper.toDto(contactPhone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactPhoneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactPhone in the database
        List<ContactPhone> contactPhoneList = contactPhoneRepository.findAll();
        assertThat(contactPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactPhone() throws Exception {
        int databaseSizeBeforeUpdate = contactPhoneRepository.findAll().size();
        contactPhone.setId(count.incrementAndGet());

        // Create the ContactPhone
        ContactPhoneDTO contactPhoneDTO = contactPhoneMapper.toDto(contactPhone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactPhoneDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactPhone in the database
        List<ContactPhone> contactPhoneList = contactPhoneRepository.findAll();
        assertThat(contactPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContactPhone() throws Exception {
        // Initialize the database
        contactPhoneRepository.saveAndFlush(contactPhone);

        int databaseSizeBeforeDelete = contactPhoneRepository.findAll().size();

        // Delete the contactPhone
        restContactPhoneMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactPhone.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactPhone> contactPhoneList = contactPhoneRepository.findAll();
        assertThat(contactPhoneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
