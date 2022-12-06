package com.rebfabric.poc.contact.repository;

import com.rebfabric.poc.contact.domain.ContactPhone;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContactPhone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactPhoneRepository extends JpaRepository<ContactPhone, Long> {}
