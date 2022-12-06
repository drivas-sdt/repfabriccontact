package com.rebfabric.poc.contact.repository;

import com.rebfabric.poc.contact.domain.ContactEmail;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContactEmail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactEmailRepository extends JpaRepository<ContactEmail, Long> {}
