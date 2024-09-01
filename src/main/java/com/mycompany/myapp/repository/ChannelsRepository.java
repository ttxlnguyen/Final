package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Channels;
import com.mycompany.myapp.domain.Messages;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Channels entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChannelsRepository extends JpaRepository<Channels, Long> {
    // List<Messages> getMessagesById(Long id);

    @Query("SELECT c FROM Channels c JOIN c.messages m WHERE c.id = ?1")
    List<Channels> findAllMessagesById(Long id);
}
