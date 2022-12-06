package com.rebfabric.poc.contact.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rebfabric.poc.contact.IntegrationTest;
import com.rebfabric.poc.contact.domain.Contact;
import com.rebfabric.poc.contact.domain.ContactAddress;
import com.rebfabric.poc.contact.repository.ContactAddressRepository;
import com.rebfabric.poc.contact.service.dto.ContactAddressDTO;
import com.rebfabric.poc.contact.service.mapper.ContactAddressMapper;
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
 * Integration tests for the {@link ContactAddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactAddressResourceIT {

    private static final Integer DEFAULT_ADDRESS_TYPE = 1;
    private static final Integer UPDATED_ADDRESS_TYPE = 2;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_FORMATTED_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_FORMATTED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PO_BOX = "AAAAAAAAAA";
    private static final String UPDATED_PO_BOX = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contact-addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContactAddressRepository contactAddressRepository;

    @Autowired
    private ContactAddressMapper contactAddressMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContactAddressMockMvc;

    private ContactAddress contactAddress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactAddress createEntity(EntityManager em) {
        ContactAddress contactAddress = new ContactAddress()
            .addressType(DEFAULT_ADDRESS_TYPE)
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .zipCode(DEFAULT_ZIP_CODE)
            .country(DEFAULT_COUNTRY)
            .formattedAddress(DEFAULT_FORMATTED_ADDRESS)
            .poBox(DEFAULT_PO_BOX);
        // Add required entity
        Contact contact;
        if (TestUtil.findAll(em, Contact.class).isEmpty()) {
            contact = ContactResourceIT.createEntity(em);
            em.persist(contact);
            em.flush();
        } else {
            contact = TestUtil.findAll(em, Contact.class).get(0);
        }
        contactAddress.setContact(contact);
        return contactAddress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactAddress createUpdatedEntity(EntityManager em) {
        ContactAddress contactAddress = new ContactAddress()
            .addressType(UPDATED_ADDRESS_TYPE)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY)
            .formattedAddress(UPDATED_FORMATTED_ADDRESS)
            .poBox(UPDATED_PO_BOX);
        // Add required entity
        Contact contact;
        if (TestUtil.findAll(em, Contact.class).isEmpty()) {
            contact = ContactResourceIT.createUpdatedEntity(em);
            em.persist(contact);
            em.flush();
        } else {
            contact = TestUtil.findAll(em, Contact.class).get(0);
        }
        contactAddress.setContact(contact);
        return contactAddress;
    }

    @BeforeEach
    public void initTest() {
        contactAddress = createEntity(em);
    }

    @Test
    @Transactional
    void createContactAddress() throws Exception {
        int databaseSizeBeforeCreate = contactAddressRepository.findAll().size();
        // Create the ContactAddress
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(contactAddress);
        restContactAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeCreate + 1);
        ContactAddress testContactAddress = contactAddressList.get(contactAddressList.size() - 1);
        assertThat(testContactAddress.getAddressType()).isEqualTo(DEFAULT_ADDRESS_TYPE);
        assertThat(testContactAddress.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testContactAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testContactAddress.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testContactAddress.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testContactAddress.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testContactAddress.getFormattedAddress()).isEqualTo(DEFAULT_FORMATTED_ADDRESS);
        assertThat(testContactAddress.getPoBox()).isEqualTo(DEFAULT_PO_BOX);
    }

    @Test
    @Transactional
    void createContactAddressWithExistingId() throws Exception {
        // Create the ContactAddress with an existing ID
        contactAddress.setId(1L);
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(contactAddress);

        int databaseSizeBeforeCreate = contactAddressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAddressTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactAddressRepository.findAll().size();
        // set the field null
        contactAddress.setAddressType(null);

        // Create the ContactAddress, which fails.
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(contactAddress);

        restContactAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContactAddresses() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get all the contactAddressList
        restContactAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].addressType").value(hasItem(DEFAULT_ADDRESS_TYPE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].formattedAddress").value(hasItem(DEFAULT_FORMATTED_ADDRESS)))
            .andExpect(jsonPath("$.[*].poBox").value(hasItem(DEFAULT_PO_BOX)));
    }

    @Test
    @Transactional
    void getContactAddress() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        // Get the contactAddress
        restContactAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, contactAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactAddress.getId().intValue()))
            .andExpect(jsonPath("$.addressType").value(DEFAULT_ADDRESS_TYPE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.formattedAddress").value(DEFAULT_FORMATTED_ADDRESS))
            .andExpect(jsonPath("$.poBox").value(DEFAULT_PO_BOX));
    }

    @Test
    @Transactional
    void getNonExistingContactAddress() throws Exception {
        // Get the contactAddress
        restContactAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContactAddress() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        int databaseSizeBeforeUpdate = contactAddressRepository.findAll().size();

        // Update the contactAddress
        ContactAddress updatedContactAddress = contactAddressRepository.findById(contactAddress.getId()).get();
        // Disconnect from session so that the updates on updatedContactAddress are not directly saved in db
        em.detach(updatedContactAddress);
        updatedContactAddress
            .addressType(UPDATED_ADDRESS_TYPE)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY)
            .formattedAddress(UPDATED_FORMATTED_ADDRESS)
            .poBox(UPDATED_PO_BOX);
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(updatedContactAddress);

        restContactAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactAddressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeUpdate);
        ContactAddress testContactAddress = contactAddressList.get(contactAddressList.size() - 1);
        assertThat(testContactAddress.getAddressType()).isEqualTo(UPDATED_ADDRESS_TYPE);
        assertThat(testContactAddress.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testContactAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testContactAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testContactAddress.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testContactAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testContactAddress.getFormattedAddress()).isEqualTo(UPDATED_FORMATTED_ADDRESS);
        assertThat(testContactAddress.getPoBox()).isEqualTo(UPDATED_PO_BOX);
    }

    @Test
    @Transactional
    void putNonExistingContactAddress() throws Exception {
        int databaseSizeBeforeUpdate = contactAddressRepository.findAll().size();
        contactAddress.setId(count.incrementAndGet());

        // Create the ContactAddress
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(contactAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactAddressDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContactAddress() throws Exception {
        int databaseSizeBeforeUpdate = contactAddressRepository.findAll().size();
        contactAddress.setId(count.incrementAndGet());

        // Create the ContactAddress
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(contactAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContactAddress() throws Exception {
        int databaseSizeBeforeUpdate = contactAddressRepository.findAll().size();
        contactAddress.setId(count.incrementAndGet());

        // Create the ContactAddress
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(contactAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactAddressMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContactAddressWithPatch() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        int databaseSizeBeforeUpdate = contactAddressRepository.findAll().size();

        // Update the contactAddress using partial update
        ContactAddress partialUpdatedContactAddress = new ContactAddress();
        partialUpdatedContactAddress.setId(contactAddress.getId());

        partialUpdatedContactAddress.addressType(UPDATED_ADDRESS_TYPE).state(UPDATED_STATE).country(UPDATED_COUNTRY).poBox(UPDATED_PO_BOX);

        restContactAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactAddress))
            )
            .andExpect(status().isOk());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeUpdate);
        ContactAddress testContactAddress = contactAddressList.get(contactAddressList.size() - 1);
        assertThat(testContactAddress.getAddressType()).isEqualTo(UPDATED_ADDRESS_TYPE);
        assertThat(testContactAddress.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testContactAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testContactAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testContactAddress.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testContactAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testContactAddress.getFormattedAddress()).isEqualTo(DEFAULT_FORMATTED_ADDRESS);
        assertThat(testContactAddress.getPoBox()).isEqualTo(UPDATED_PO_BOX);
    }

    @Test
    @Transactional
    void fullUpdateContactAddressWithPatch() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        int databaseSizeBeforeUpdate = contactAddressRepository.findAll().size();

        // Update the contactAddress using partial update
        ContactAddress partialUpdatedContactAddress = new ContactAddress();
        partialUpdatedContactAddress.setId(contactAddress.getId());

        partialUpdatedContactAddress
            .addressType(UPDATED_ADDRESS_TYPE)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipCode(UPDATED_ZIP_CODE)
            .country(UPDATED_COUNTRY)
            .formattedAddress(UPDATED_FORMATTED_ADDRESS)
            .poBox(UPDATED_PO_BOX);

        restContactAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactAddress))
            )
            .andExpect(status().isOk());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeUpdate);
        ContactAddress testContactAddress = contactAddressList.get(contactAddressList.size() - 1);
        assertThat(testContactAddress.getAddressType()).isEqualTo(UPDATED_ADDRESS_TYPE);
        assertThat(testContactAddress.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testContactAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testContactAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testContactAddress.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testContactAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testContactAddress.getFormattedAddress()).isEqualTo(UPDATED_FORMATTED_ADDRESS);
        assertThat(testContactAddress.getPoBox()).isEqualTo(UPDATED_PO_BOX);
    }

    @Test
    @Transactional
    void patchNonExistingContactAddress() throws Exception {
        int databaseSizeBeforeUpdate = contactAddressRepository.findAll().size();
        contactAddress.setId(count.incrementAndGet());

        // Create the ContactAddress
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(contactAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactAddressDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContactAddress() throws Exception {
        int databaseSizeBeforeUpdate = contactAddressRepository.findAll().size();
        contactAddress.setId(count.incrementAndGet());

        // Create the ContactAddress
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(contactAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContactAddress() throws Exception {
        int databaseSizeBeforeUpdate = contactAddressRepository.findAll().size();
        contactAddress.setId(count.incrementAndGet());

        // Create the ContactAddress
        ContactAddressDTO contactAddressDTO = contactAddressMapper.toDto(contactAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactAddressMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactAddressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactAddress in the database
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContactAddress() throws Exception {
        // Initialize the database
        contactAddressRepository.saveAndFlush(contactAddress);

        int databaseSizeBeforeDelete = contactAddressRepository.findAll().size();

        // Delete the contactAddress
        restContactAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactAddress.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactAddress> contactAddressList = contactAddressRepository.findAll();
        assertThat(contactAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
