package com.rebfabric.poc.contact.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rebfabric.poc.contact.IntegrationTest;
import com.rebfabric.poc.contact.domain.Contact;
import com.rebfabric.poc.contact.repository.ContactRepository;
import com.rebfabric.poc.contact.service.dto.ContactDTO;
import com.rebfabric.poc.contact.service.mapper.ContactMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ContactResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SUFFIX = "AAAAAAAAAA";
    private static final String UPDATED_SUFFIX = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DEPT = "AAAAAAAAAA";
    private static final String UPDATED_DEPT = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_MANAGER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MANAGER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ASSISTANT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ASSISTANT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REFERRED_BY = "AAAAAAAAAA";
    private static final String UPDATED_REFERRED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_CONTEXT = "AAAAAAAAAA";
    private static final String UPDATED_CONTEXT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PRIMARY = false;
    private static final Boolean UPDATED_IS_PRIMARY = true;

    private static final Boolean DEFAULT_IS_DELETE = false;
    private static final Boolean UPDATED_IS_DELETE = true;

    private static final String DEFAULT_IMAGE_P_ATH = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_P_ATH = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REF_NO = "AAAAAAAAAA";
    private static final String UPDATED_REF_NO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_GLOBAL_CONTACT = false;
    private static final Boolean UPDATED_IS_GLOBAL_CONTACT = true;

    private static final Boolean DEFAULT_IS_BOUNCE = false;
    private static final Boolean UPDATED_IS_BOUNCE = true;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_SYNC = false;
    private static final Boolean UPDATED_IS_SYNC = true;

    private static final String ENTITY_API_URL = "/api/contacts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactMapper contactMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactMockMvc;

    private Contact contact;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contact createEntity(EntityManager em) {
        Contact contact = new Contact()
            .title(DEFAULT_TITLE)
            .firstName(DEFAULT_FIRST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .suffix(DEFAULT_SUFFIX)
            .fullName(DEFAULT_FULL_NAME)
            .jobTitle(DEFAULT_JOB_TITLE)
            .dept(DEFAULT_DEPT)
            .location(DEFAULT_LOCATION)
            .managerName(DEFAULT_MANAGER_NAME)
            .assistantName(DEFAULT_ASSISTANT_NAME)
            .referredBy(DEFAULT_REFERRED_BY)
            .context(DEFAULT_CONTEXT)
            .isPrimary(DEFAULT_IS_PRIMARY)
            .isDelete(DEFAULT_IS_DELETE)
            .imagePAth(DEFAULT_IMAGE_P_ATH)
            .notes(DEFAULT_NOTES)
            .createDate(DEFAULT_CREATE_DATE)
            .updateDate(DEFAULT_UPDATE_DATE)
            .refNo(DEFAULT_REF_NO)
            .isGlobalContact(DEFAULT_IS_GLOBAL_CONTACT)
            .isBounce(DEFAULT_IS_BOUNCE)
            .isActive(DEFAULT_IS_ACTIVE)
            .isSync(DEFAULT_IS_SYNC);
        return contact;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contact createUpdatedEntity(EntityManager em) {
        Contact contact = new Contact()
            .title(UPDATED_TITLE)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .suffix(UPDATED_SUFFIX)
            .fullName(UPDATED_FULL_NAME)
            .jobTitle(UPDATED_JOB_TITLE)
            .dept(UPDATED_DEPT)
            .location(UPDATED_LOCATION)
            .managerName(UPDATED_MANAGER_NAME)
            .assistantName(UPDATED_ASSISTANT_NAME)
            .referredBy(UPDATED_REFERRED_BY)
            .context(UPDATED_CONTEXT)
            .isPrimary(UPDATED_IS_PRIMARY)
            .isDelete(UPDATED_IS_DELETE)
            .imagePAth(UPDATED_IMAGE_P_ATH)
            .notes(UPDATED_NOTES)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .refNo(UPDATED_REF_NO)
            .isGlobalContact(UPDATED_IS_GLOBAL_CONTACT)
            .isBounce(UPDATED_IS_BOUNCE)
            .isActive(UPDATED_IS_ACTIVE)
            .isSync(UPDATED_IS_SYNC);
        return contact;
    }

    @BeforeEach
    public void initTest() {
        contact = createEntity(em);
    }

    @Test
    @Transactional
    void createContact() throws Exception {
        int databaseSizeBeforeCreate = contactRepository.findAll().size();
        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);
        restContactMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isCreated());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeCreate + 1);
        Contact testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testContact.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testContact.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testContact.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testContact.getSuffix()).isEqualTo(DEFAULT_SUFFIX);
        assertThat(testContact.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testContact.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testContact.getDept()).isEqualTo(DEFAULT_DEPT);
        assertThat(testContact.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testContact.getManagerName()).isEqualTo(DEFAULT_MANAGER_NAME);
        assertThat(testContact.getAssistantName()).isEqualTo(DEFAULT_ASSISTANT_NAME);
        assertThat(testContact.getReferredBy()).isEqualTo(DEFAULT_REFERRED_BY);
        assertThat(testContact.getContext()).isEqualTo(DEFAULT_CONTEXT);
        assertThat(testContact.getIsPrimary()).isEqualTo(DEFAULT_IS_PRIMARY);
        assertThat(testContact.getIsDelete()).isEqualTo(DEFAULT_IS_DELETE);
        assertThat(testContact.getImagePAth()).isEqualTo(DEFAULT_IMAGE_P_ATH);
        assertThat(testContact.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testContact.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testContact.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testContact.getRefNo()).isEqualTo(DEFAULT_REF_NO);
        assertThat(testContact.getIsGlobalContact()).isEqualTo(DEFAULT_IS_GLOBAL_CONTACT);
        assertThat(testContact.getIsBounce()).isEqualTo(DEFAULT_IS_BOUNCE);
        assertThat(testContact.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testContact.getIsSync()).isEqualTo(DEFAULT_IS_SYNC);
    }

    @Test
    @Transactional
    void createContactWithExistingId() throws Exception {
        // Create the Contact with an existing ID
        contact.setId(1L);
        ContactDTO contactDTO = contactMapper.toDto(contact);

        int databaseSizeBeforeCreate = contactRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContacts() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList
        restContactMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contact.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].suffix").value(hasItem(DEFAULT_SUFFIX)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].dept").value(hasItem(DEFAULT_DEPT)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].managerName").value(hasItem(DEFAULT_MANAGER_NAME)))
            .andExpect(jsonPath("$.[*].assistantName").value(hasItem(DEFAULT_ASSISTANT_NAME)))
            .andExpect(jsonPath("$.[*].referredBy").value(hasItem(DEFAULT_REFERRED_BY)))
            .andExpect(jsonPath("$.[*].context").value(hasItem(DEFAULT_CONTEXT)))
            .andExpect(jsonPath("$.[*].isPrimary").value(hasItem(DEFAULT_IS_PRIMARY.booleanValue())))
            .andExpect(jsonPath("$.[*].isDelete").value(hasItem(DEFAULT_IS_DELETE.booleanValue())))
            .andExpect(jsonPath("$.[*].imagePAth").value(hasItem(DEFAULT_IMAGE_P_ATH)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].refNo").value(hasItem(DEFAULT_REF_NO)))
            .andExpect(jsonPath("$.[*].isGlobalContact").value(hasItem(DEFAULT_IS_GLOBAL_CONTACT.booleanValue())))
            .andExpect(jsonPath("$.[*].isBounce").value(hasItem(DEFAULT_IS_BOUNCE.booleanValue())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isSync").value(hasItem(DEFAULT_IS_SYNC.booleanValue())));
    }

    @Test
    @Transactional
    void getContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get the contact
        restContactMockMvc
            .perform(get(ENTITY_API_URL_ID, contact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contact.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.suffix").value(DEFAULT_SUFFIX))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE))
            .andExpect(jsonPath("$.dept").value(DEFAULT_DEPT))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.managerName").value(DEFAULT_MANAGER_NAME))
            .andExpect(jsonPath("$.assistantName").value(DEFAULT_ASSISTANT_NAME))
            .andExpect(jsonPath("$.referredBy").value(DEFAULT_REFERRED_BY))
            .andExpect(jsonPath("$.context").value(DEFAULT_CONTEXT))
            .andExpect(jsonPath("$.isPrimary").value(DEFAULT_IS_PRIMARY.booleanValue()))
            .andExpect(jsonPath("$.isDelete").value(DEFAULT_IS_DELETE.booleanValue()))
            .andExpect(jsonPath("$.imagePAth").value(DEFAULT_IMAGE_P_ATH))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_UPDATE_DATE.toString()))
            .andExpect(jsonPath("$.refNo").value(DEFAULT_REF_NO))
            .andExpect(jsonPath("$.isGlobalContact").value(DEFAULT_IS_GLOBAL_CONTACT.booleanValue()))
            .andExpect(jsonPath("$.isBounce").value(DEFAULT_IS_BOUNCE.booleanValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isSync").value(DEFAULT_IS_SYNC.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingContact() throws Exception {
        // Get the contact
        restContactMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        int databaseSizeBeforeUpdate = contactRepository.findAll().size();

        // Update the contact
        Contact updatedContact = contactRepository.findById(contact.getId()).get();
        // Disconnect from session so that the updates on updatedContact are not directly saved in db
        em.detach(updatedContact);
        updatedContact
            .title(UPDATED_TITLE)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .suffix(UPDATED_SUFFIX)
            .fullName(UPDATED_FULL_NAME)
            .jobTitle(UPDATED_JOB_TITLE)
            .dept(UPDATED_DEPT)
            .location(UPDATED_LOCATION)
            .managerName(UPDATED_MANAGER_NAME)
            .assistantName(UPDATED_ASSISTANT_NAME)
            .referredBy(UPDATED_REFERRED_BY)
            .context(UPDATED_CONTEXT)
            .isPrimary(UPDATED_IS_PRIMARY)
            .isDelete(UPDATED_IS_DELETE)
            .imagePAth(UPDATED_IMAGE_P_ATH)
            .notes(UPDATED_NOTES)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .refNo(UPDATED_REF_NO)
            .isGlobalContact(UPDATED_IS_GLOBAL_CONTACT)
            .isBounce(UPDATED_IS_BOUNCE)
            .isActive(UPDATED_IS_ACTIVE)
            .isSync(UPDATED_IS_SYNC);
        ContactDTO contactDTO = contactMapper.toDto(updatedContact);

        restContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactDTO))
            )
            .andExpect(status().isOk());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
        Contact testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContact.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testContact.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testContact.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testContact.getSuffix()).isEqualTo(UPDATED_SUFFIX);
        assertThat(testContact.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testContact.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testContact.getDept()).isEqualTo(UPDATED_DEPT);
        assertThat(testContact.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testContact.getManagerName()).isEqualTo(UPDATED_MANAGER_NAME);
        assertThat(testContact.getAssistantName()).isEqualTo(UPDATED_ASSISTANT_NAME);
        assertThat(testContact.getReferredBy()).isEqualTo(UPDATED_REFERRED_BY);
        assertThat(testContact.getContext()).isEqualTo(UPDATED_CONTEXT);
        assertThat(testContact.getIsPrimary()).isEqualTo(UPDATED_IS_PRIMARY);
        assertThat(testContact.getIsDelete()).isEqualTo(UPDATED_IS_DELETE);
        assertThat(testContact.getImagePAth()).isEqualTo(UPDATED_IMAGE_P_ATH);
        assertThat(testContact.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testContact.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testContact.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testContact.getRefNo()).isEqualTo(UPDATED_REF_NO);
        assertThat(testContact.getIsGlobalContact()).isEqualTo(UPDATED_IS_GLOBAL_CONTACT);
        assertThat(testContact.getIsBounce()).isEqualTo(UPDATED_IS_BOUNCE);
        assertThat(testContact.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testContact.getIsSync()).isEqualTo(UPDATED_IS_SYNC);
    }

    @Test
    @Transactional
    void putNonExistingContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();
        contact.setId(count.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();
        contact.setId(count.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();
        contact.setId(count.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactWithPatch() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        int databaseSizeBeforeUpdate = contactRepository.findAll().size();

        // Update the contact using partial update
        Contact partialUpdatedContact = new Contact();
        partialUpdatedContact.setId(contact.getId());

        partialUpdatedContact
            .title(UPDATED_TITLE)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .jobTitle(UPDATED_JOB_TITLE)
            .dept(UPDATED_DEPT)
            .location(UPDATED_LOCATION)
            .managerName(UPDATED_MANAGER_NAME)
            .isDelete(UPDATED_IS_DELETE)
            .updateDate(UPDATED_UPDATE_DATE);

        restContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContact.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContact))
            )
            .andExpect(status().isOk());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
        Contact testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContact.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testContact.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testContact.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testContact.getSuffix()).isEqualTo(DEFAULT_SUFFIX);
        assertThat(testContact.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testContact.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testContact.getDept()).isEqualTo(UPDATED_DEPT);
        assertThat(testContact.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testContact.getManagerName()).isEqualTo(UPDATED_MANAGER_NAME);
        assertThat(testContact.getAssistantName()).isEqualTo(DEFAULT_ASSISTANT_NAME);
        assertThat(testContact.getReferredBy()).isEqualTo(DEFAULT_REFERRED_BY);
        assertThat(testContact.getContext()).isEqualTo(DEFAULT_CONTEXT);
        assertThat(testContact.getIsPrimary()).isEqualTo(DEFAULT_IS_PRIMARY);
        assertThat(testContact.getIsDelete()).isEqualTo(UPDATED_IS_DELETE);
        assertThat(testContact.getImagePAth()).isEqualTo(DEFAULT_IMAGE_P_ATH);
        assertThat(testContact.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testContact.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testContact.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testContact.getRefNo()).isEqualTo(DEFAULT_REF_NO);
        assertThat(testContact.getIsGlobalContact()).isEqualTo(DEFAULT_IS_GLOBAL_CONTACT);
        assertThat(testContact.getIsBounce()).isEqualTo(DEFAULT_IS_BOUNCE);
        assertThat(testContact.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testContact.getIsSync()).isEqualTo(DEFAULT_IS_SYNC);
    }

    @Test
    @Transactional
    void fullUpdateContactWithPatch() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        int databaseSizeBeforeUpdate = contactRepository.findAll().size();

        // Update the contact using partial update
        Contact partialUpdatedContact = new Contact();
        partialUpdatedContact.setId(contact.getId());

        partialUpdatedContact
            .title(UPDATED_TITLE)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .suffix(UPDATED_SUFFIX)
            .fullName(UPDATED_FULL_NAME)
            .jobTitle(UPDATED_JOB_TITLE)
            .dept(UPDATED_DEPT)
            .location(UPDATED_LOCATION)
            .managerName(UPDATED_MANAGER_NAME)
            .assistantName(UPDATED_ASSISTANT_NAME)
            .referredBy(UPDATED_REFERRED_BY)
            .context(UPDATED_CONTEXT)
            .isPrimary(UPDATED_IS_PRIMARY)
            .isDelete(UPDATED_IS_DELETE)
            .imagePAth(UPDATED_IMAGE_P_ATH)
            .notes(UPDATED_NOTES)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .refNo(UPDATED_REF_NO)
            .isGlobalContact(UPDATED_IS_GLOBAL_CONTACT)
            .isBounce(UPDATED_IS_BOUNCE)
            .isActive(UPDATED_IS_ACTIVE)
            .isSync(UPDATED_IS_SYNC);

        restContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContact.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContact))
            )
            .andExpect(status().isOk());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
        Contact testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testContact.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testContact.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testContact.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testContact.getSuffix()).isEqualTo(UPDATED_SUFFIX);
        assertThat(testContact.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testContact.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testContact.getDept()).isEqualTo(UPDATED_DEPT);
        assertThat(testContact.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testContact.getManagerName()).isEqualTo(UPDATED_MANAGER_NAME);
        assertThat(testContact.getAssistantName()).isEqualTo(UPDATED_ASSISTANT_NAME);
        assertThat(testContact.getReferredBy()).isEqualTo(UPDATED_REFERRED_BY);
        assertThat(testContact.getContext()).isEqualTo(UPDATED_CONTEXT);
        assertThat(testContact.getIsPrimary()).isEqualTo(UPDATED_IS_PRIMARY);
        assertThat(testContact.getIsDelete()).isEqualTo(UPDATED_IS_DELETE);
        assertThat(testContact.getImagePAth()).isEqualTo(UPDATED_IMAGE_P_ATH);
        assertThat(testContact.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testContact.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testContact.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testContact.getRefNo()).isEqualTo(UPDATED_REF_NO);
        assertThat(testContact.getIsGlobalContact()).isEqualTo(UPDATED_IS_GLOBAL_CONTACT);
        assertThat(testContact.getIsBounce()).isEqualTo(UPDATED_IS_BOUNCE);
        assertThat(testContact.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testContact.getIsSync()).isEqualTo(UPDATED_IS_SYNC);
    }

    @Test
    @Transactional
    void patchNonExistingContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();
        contact.setId(count.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();
        contact.setId(count.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();
        contact.setId(count.incrementAndGet());

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contactDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        int databaseSizeBeforeDelete = contactRepository.findAll().size();

        // Delete the contact
        restContactMockMvc
            .perform(delete(ENTITY_API_URL_ID, contact.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
