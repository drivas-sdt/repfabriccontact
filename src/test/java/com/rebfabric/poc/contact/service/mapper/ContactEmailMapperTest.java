package com.rebfabric.poc.contact.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContactEmailMapperTest {

    private ContactEmailMapper contactEmailMapper;

    @BeforeEach
    public void setUp() {
        contactEmailMapper = new ContactEmailMapperImpl();
    }
}
