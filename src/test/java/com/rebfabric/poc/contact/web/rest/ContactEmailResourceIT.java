package com.rebfabric.poc.contact.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rebfabric.poc.contact.IntegrationTest;
import com.rebfabric.poc.contact.domain.Contact;
import com.rebfabric.poc.contact.domain.ContactEmail;
import com.rebfabric.poc.contact.repository.ContactEmailRepository;
import com.rebfabric.poc.contact.service.dto.ContactEmailDTO;
import com.rebfabric.poc.contact.service.mapper.ContactEmailMapper;
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
 * Integration tests for the {@link ContactEmailResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactEmailResourceIT {

    private static final Integer DEFAULT_EMAIL_TYPE = 1;
    private static final Integer UPDATED_EMAIL_TYPE = 2;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contact-emails";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactEmailRepository contactEmailRepository;

    @Autowired
    private ContactEmailMapper contactEmailMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactEmailMockMvc;

    private ContactEmail contactEmail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactEmail createEntity(EntityManager em) {
        ContactEmail contactEmail = new ContactEmail().emailType(DEFAULT_EMAIL_TYPE).email(DEFAULT_EMAIL);
        // Add required entity
        Contact contact;
        if (TestUtil.findAll(em, Contact.class).isEmpty()) {
            contact = ContactResourceIT.createEntity(em);
            em.persist(contact);
            em.flush();
        } else {
            contact = TestUtil.findAll(em, Contact.class).get(0);
        }
        contactEmail.setContact(contact);
        return contactEmail;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactEmail createUpdatedEntity(EntityManager em) {
        ContactEmail contactEmail = new ContactEmail().emailType(UPDATED_EMAIL_TYPE).email(UPDATED_EMAIL);
        // Add required entity
        Contact contact;
        if (TestUtil.findAll(em, Contact.class).isEmpty()) {
            contact = ContactResourceIT.createUpdatedEntity(em);
            em.persist(contact);
            em.flush();
        } else {
            contact = TestUtil.findAll(em, Contact.class).get(0);
        }
        contactEmail.setContact(contact);
        return contactEmail;
    }

    @BeforeEach
    public void initTest() {
        contactEmail = createEntity(em);
    }

    @Test
    @Transactional
    void createContactEmail() throws Exception {
        int databaseSizeBeforeCreate = contactEmailRepository.findAll().size();
        // Create the ContactEmail
        ContactEmailDTO contactEmailDTO = contactEmailMapper.toDto(contactEmail);
        restContactEmailMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactEmailDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContactEmail in the database
        List<ContactEmail> contactEmailList = contactEmailRepository.findAll();
        assertThat(contactEmailList).hasSize(databaseSizeBeforeCreate + 1);
        ContactEmail testContactEmail = contactEmailList.get(contactEmailList.size() - 1);
        assertThat(testContactEmail.getEmailType()).isEqualTo(DEFAULT_EMAIL_TYPE);
        assertThat(testContactEmail.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createContactEmailWithExistingId() throws Exception {
        // Create the ContactEmail with an existing ID
        contactEmail.setId(1L);
        ContactEmailDTO contactEmailDTO = contactEmailMapper.toDto(contactEmail);

        int databaseSizeBeforeCreate = contactEmailRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactEmailMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactEmailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactEmail in the database
        List<ContactEmail> contactEmailList = contactEmailRepository.findAll();
        assertThat(contactEmailList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactEmailRepository.findAll().size();
        // set the field null
        contactEmail.setEmailType(null);

        // Create the ContactEmail, which fails.
        ContactEmailDTO contactEmailDTO = contactEmailMapper.toDto(contactEmail);

        restContactEmailMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactEmailDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContactEmail> contactEmailList = contactEmailRepository.findAll();
        assertThat(contactEmailList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactEmailRepository.findAll().size();
        // set the field null
        contactEmail.setEmail(null);

        // Create the ContactEmail, which fails.
        ContactEmailDTO contactEmailDTO = contactEmailMapper.toDto(contactEmail);

        restContactEmailMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactEmailDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContactEmail> contactEmailList = contactEmailRepository.findAll();
        assertThat(contactEmailList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContactEmails() throws Exception {
        // Initialize the database
        contactEmailRepository.saveAndFlush(contactEmail);

        // Get all the contactEmailList
        restContactEmailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactEmail.getId().intValue())))
            .andExpect(jsonPath("$.[*].emailType").value(hasItem(DEFAULT_EMAIL_TYPE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getContactEmail() throws Exception {
        // Initialize the database
        contactEmailRepository.saveAndFlush(contactEmail);

        // Get the contactEmail
        restContactEmailMockMvc
            .perform(get(ENTITY_API_URL_ID, contactEmail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactEmail.getId().intValue()))
            .andExpect(jsonPath("$.emailType").value(DEFAULT_EMAIL_TYPE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingContactEmail() throws Exception {
        // Get the contactEmail
        restContactEmailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContactEmail() throws Exception {
        // Initialize the database
        contactEmailRepository.saveAndFlush(contactEmail);

        int databaseSizeBeforeUpdate = contactEmailRepository.findAll().size();

        // Update the contactEmail
        ContactEmail updatedContactEmail = contactEmailRepository.findById(contactEmail.getId()).get();
        // Disconnect from session so that the updates on updatedContactEmail are not directly saved in db
        em.detach(updatedContactEmail);
        updatedContactEmail.emailType(UPDATED_EMAIL_TYPE).email(UPDATED_EMAIL);
        ContactEmailDTO contactEmailDTO = contactEmailMapper.toDto(updatedContactEmail);

        restContactEmailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactEmailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactEmailDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContactEmail in the database
        List<ContactEmail> contactEmailList = contactEmailRepository.findAll();
        assertThat(contactEmailList).hasSize(databaseSizeBeforeUpdate);
        ContactEmail testContactEmail = contactEmailList.get(contactEmailList.size() - 1);
        assertThat(testContactEmail.getEmailType()).isEqualTo(UPDATED_EMAIL_TYPE);
        assertThat(testContactEmail.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingContactEmail() throws Exception {
        int databaseSizeBeforeUpdate = contactEmailRepository.findAll().size();
        contactEmail.setId(count.incrementAndGet());

        // Create the ContactEmail
        ContactEmailDTO contactEmailDTO = contactEmailMapper.toDto(contactEmail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactEmailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactEmailDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactEmailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactEmail in the database
        List<ContactEmail> contactEmailList = contactEmailRepository.findAll();
        assertThat(contactEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactEmail() throws Exception {
        int databaseSizeBeforeUpdate = contactEmailRepository.findAll().size();
        contactEmail.setId(count.incrementAndGet());

        // Create the ContactEmail
        ContactEmailDTO contactEmailDTO = contactEmailMapper.toDto(contactEmail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactEmailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactEmailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactEmail in the database
        List<ContactEmail> contactEmailList = contactEmailRepository.findAll();
        assertThat(contactEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactEmail() throws Exception {
        int databaseSizeBeforeUpdate = contactEmailRepository.findAll().size();
        contactEmail.setId(count.incrementAndGet());

        // Create the ContactEmail
        ContactEmailDTO contactEmailDTO = contactEmailMapper.toDto(contactEmail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactEmailMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactEmailDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactEmail in the database
        List<ContactEmail> contactEmailList = contactEmailRepository.findAll();
        assertThat(contactEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactEmailWithPatch() throws Exception {
        // Initialize the database
        contactEmailRepository.saveAndFlush(contactEmail);

        int databaseSizeBeforeUpdate = contactEmailRepository.findAll().size();

        // Update the contactEmail using partial update
        ContactEmail partialUpdatedContactEmail = new ContactEmail();
        partialUpdatedContactEmail.setId(contactEmail.getId());

        partialUpdatedContactEmail.emailType(UPDATED_EMAIL_TYPE).email(UPDATED_EMAIL);

        restContactEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactEmail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactEmail))
            )
            .andExpect(status().isOk());

        // Validate the ContactEmail in the database
        List<ContactEmail> contactEmailList = contactEmailRepository.findAll();
        assertThat(contactEmailList).hasSize(databaseSizeBeforeUpdate);
        ContactEmail testContactEmail = contactEmailList.get(contactEmailList.size() - 1);
        assertThat(testContactEmail.getEmailType()).isEqualTo(UPDATED_EMAIL_TYPE);
        assertThat(testContactEmail.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateContactEmailWithPatch() throws Exception {
        // Initialize the database
        contactEmailRepository.saveAndFlush(contactEmail);

        int databaseSizeBeforeUpdate = contactEmailRepository.findAll().size();

        // Update the contactEmail using partial update
        ContactEmail partialUpdatedContactEmail = new ContactEmail();
        partialUpdatedContactEmail.setId(contactEmail.getId());

        partialUpdatedContactEmail.emailType(UPDATED_EMAIL_TYPE).email(UPDATED_EMAIL);

        restContactEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactEmail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactEmail))
            )
            .andExpect(status().isOk());

        // Validate the ContactEmail in the database
        List<ContactEmail> contactEmailList = contactEmailRepository.findAll();
        assertThat(contactEmailList).hasSize(databaseSizeBeforeUpdate);
        ContactEmail testContactEmail = contactEmailList.get(contactEmailList.size() - 1);
        assertThat(testContactEmail.getEmailType()).isEqualTo(UPDATED_EMAIL_TYPE);
        assertThat(testContactEmail.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingContactEmail() throws Exception {
        int databaseSizeBeforeUpdate = contactEmailRepository.findAll().size();
        contactEmail.setId(count.incrementAndGet());

        // Create the ContactEmail
        ContactEmailDTO contactEmailDTO = contactEmailMapper.toDto(contactEmail);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactEmailDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactEmailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactEmail in the database
        List<ContactEmail> contactEmailList = contactEmailRepository.findAll();
        assertThat(contactEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactEmail() throws Exception {
        int databaseSizeBeforeUpdate = contactEmailRepository.findAll().size();
        contactEmail.setId(count.incrementAndGet());

        // Create the ContactEmail
        ContactEmailDTO contactEmailDTO = contactEmailMapper.toDto(contactEmail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactEmailDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactEmail in the database
        List<ContactEmail> contactEmailList = contactEmailRepository.findAll();
        assertThat(contactEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactEmail() throws Exception {
        int databaseSizeBeforeUpdate = contactEmailRepository.findAll().size();
        contactEmail.setId(count.incrementAndGet());

        // Create the ContactEmail
        ContactEmailDTO contactEmailDTO = contactEmailMapper.toDto(contactEmail);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactEmailMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactEmailDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactEmail in the database
        List<ContactEmail> contactEmailList = contactEmailRepository.findAll();
        assertThat(contactEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContactEmail() throws Exception {
        // Initialize the database
        contactEmailRepository.saveAndFlush(contactEmail);

        int databaseSizeBeforeDelete = contactEmailRepository.findAll().size();

        // Delete the contactEmail
        restContactEmailMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactEmail.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactEmail> contactEmailList = contactEmailRepository.findAll();
        assertThat(contactEmailList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
