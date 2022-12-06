package com.rebfabric.poc.contact.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SalesTeamMapperTest {

    private SalesTeamMapper salesTeamMapper;

    @BeforeEach
    public void setUp() {
        salesTeamMapper = new SalesTeamMapperImpl();
    }
}
