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

    @Query(
        value = "SELECT c.* FROM channels c JOIN REL_USER_PROFILE__CHANNELS upc ON c.id = upc.CHANNELS_ID JOIN user_profile p ON p.id = upc.USER_PROFILE_ID WHERE c.privacy is false AND p.username = :username",
        nativeQuery = true
    )
    List<Channels> findAllChannelsByUsername(String username);

    Channels findChannelsById(Long id);

    @Query(
        value = "SELECT c.* FROM channels c JOIN REL_USER_PROFILE__CHANNELS upc ON c.id = upc.CHANNELS_ID JOIN user_profile p ON p.id = upc.USER_PROFILE_ID WHERE c.privacy is true AND p.username = :username",
        nativeQuery = true
    )
    List<Channels> findAllPrivateChannelsByUsername(String username);

    @Query("SELECT c FROM Channels c WHERE c.privacy = false")
    List<Channels> listAllPublicChannels();
}
