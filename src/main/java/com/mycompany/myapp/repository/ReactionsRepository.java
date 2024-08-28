package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Reactions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Reactions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReactionsRepository extends JpaRepository<Reactions, Long> {}
