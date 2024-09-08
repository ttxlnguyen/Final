package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Messages;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Messages entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessagesRepository extends JpaRepository<Messages, Long> {
    @Query(
        value = "SELECT M.* FROM messages M " +
        "JOIN user_profile U ON M.user_profile_id = U.id " +
        "JOIN channels C ON M.channels_id = C.id " +
        "WHERE C.id = ?1 ORDER BY M.sent_at DESC",
        nativeQuery = true
    )
    List<Messages> findAllMessagesByChannelId(Long id);
    //    Optional<Messages> findByUsername(String username);
}
