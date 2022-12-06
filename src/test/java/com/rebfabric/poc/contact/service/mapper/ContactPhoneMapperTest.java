package com.rebfabric.poc.contact.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContactPhoneMapperTest {

    private ContactPhoneMapper contactPhoneMapper;

    @BeforeEach
    public void setUp() {
        contactPhoneMapper = new ContactPhoneMapperImpl();
    }
}
