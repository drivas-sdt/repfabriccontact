package com.rebfabric.poc.contact.repository;

import com.rebfabric.poc.contact.domain.ContactAddress;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContactAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactAddressRepository extends JpaRepository<ContactAddress, Long> {}
