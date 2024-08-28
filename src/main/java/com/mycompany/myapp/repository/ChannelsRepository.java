package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Channels;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Channels entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChannelsRepository extends JpaRepository<Channels, Long> {}
