package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Messages;
import java.util.List;
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
        "WHERE C.id = ?1",
        nativeQuery = true
    )
    List<Messages> findAllMessagesByChannelId(Long id);
    //    @Query("SELECT m FROM Messages m " +
    //        "JOIN m.userProfile u " +
    //        "JOIN m.channels c " +
    //        "WHERE c.id = :channelId")
    //    List<Messages> findAllMessagesByChannelId(@Param("channelId") Long channelId);

    //@Query("SELECT new com.mycompany.myapp.dto.MessageDTO(m.id, m.content, m.sentAt, m.editedAt, m.isDeleted, u.id, u.username, u.email, c.id, c.name) " +
    //    "FROM Messages m " +
    //    "JOIN m.userProfile u " +
    //    "JOIN m.channels c " +
    //    "WHERE c.id = :channelId")
    //List<MessageDTO> findMessagesByChannelId(@Param("channelId") Long channelId);
}
