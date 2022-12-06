package com.rebfabric.poc.contact.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RFUserMapperTest {

    private RFUserMapper rFUserMapper;

    @BeforeEach
    public void setUp() {
        rFUserMapper = new RFUserMapperImpl();
    }
}
