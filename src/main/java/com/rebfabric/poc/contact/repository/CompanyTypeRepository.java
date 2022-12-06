package com.rebfabric.poc.contact.repository;

import com.rebfabric.poc.contact.domain.CompanyType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CompanyType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyTypeRepository extends JpaRepository<CompanyType, Long> {}
