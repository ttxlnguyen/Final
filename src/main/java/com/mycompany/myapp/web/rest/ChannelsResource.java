package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Channels;
import com.mycompany.myapp.domain.Messages;
import com.mycompany.myapp.repository.ChannelsRepository;
import com.mycompany.myapp.service.ChannelsService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.mail.Service;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Channels}.
 */
@RestController
@RequestMapping("/api/channels")
@Transactional
public class ChannelsResource {

    private ChannelsService service;

    private static final Logger log = LoggerFactory.getLogger(ChannelsResource.class);

    private static final String ENTITY_NAME = "channels";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChannelsRepository channelsRepository;

    public ChannelsResource(ChannelsRepository channelsRepository, @Autowired ChannelsService service) {
        this.channelsRepository = channelsRepository;
        this.service = service;
    }

    /**
     * {@code POST  /channels} : Create a new channels.
     *
     * @param channels the channels to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new channels, or with status {@code 400 (Bad Request)} if the channels has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Channels> createChannels(@RequestBody Channels channels) throws URISyntaxException {
        log.debug("REST request to save Channels : {}", channels);
        if (channels.getId() != null) {
            throw new BadRequestAlertException("A new channels cannot already have an ID", ENTITY_NAME, "idexists");
        }
        channels = channelsRepository.save(channels);
        return ResponseEntity.created(new URI("/api/channels/" + channels.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, channels.getId().toString()))
            .body(channels);
    }

    @PostMapping("/username/{username}")
    public ResponseEntity<Channels> postChannelsByUsername(@PathVariable("username") String username, @RequestBody Channels channels)
        throws URISyntaxException {
        log.debug("REST request to save Channels : {}", channels);
        if (channels.getId() != null) {
            throw new BadRequestAlertException("A new channels cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Channels channel = service.createChannelByUsername(username, channels);

        return ResponseEntity.created(new URI("/api/channels/username" + username))
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, channel.getId().toString()))
            .body(channel);
    }

    @PostMapping("/username/public")
    public ResponseEntity<Channels> postPublicChannelsByUsername(@RequestBody Channels channels) throws URISyntaxException {
        log.debug("REST request to save Channels : {}", channels);
        if (channels.getId() != null) {
            throw new BadRequestAlertException("A new channels cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Boolean privacy = false;
        Channels channel = service.createPublicChannelByUsername(channels);

        return ResponseEntity.created(new URI("/api/channels/username"))
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, channel.getId().toString()))
            .body(channel);
    }

    //    @PostMapping("/username/{username}/public")
    //    public ResponseEntity<Channels> postPublicChannelsByUsername(@PathVariable("username") String username, @RequestBody Channels channels)
    //        throws URISyntaxException {
    //        log.debug("REST request to save Channels : {}", channels);
    //        if (channels.getId() != null) {
    //            throw new BadRequestAlertException("A new channels cannot already have an ID", ENTITY_NAME, "idexists");
    //        }
    //        Boolean privacy = false;
    //        Channels channel = service.createPublicChannelByUsername(username, channels);
    //
    //        return ResponseEntity.created(new URI("/api/channels/username" + username))
    //            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, channel.getId().toString()))
    //            .body(channel);
    //    }

    /**
     * {@code PUT  /channels/:id} : Updates an existing channels.
     *
     * @param id the id of the channels to save.
     * @param channels the channels to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated channels,
     * or with status {@code 400 (Bad Request)} if the channels is not valid,
     * or with status {@code 500 (Internal Server Error)} if the channels couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Channels> updateChannels(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Channels channels
    ) throws URISyntaxException {
        log.debug("REST request to update Channels : {}, {}", id, channels);
        if (channels.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, channels.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!channelsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        channels = channelsRepository.save(channels);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, channels.getId().toString()))
            .body(channels);
    }

    /**
     * {@code PATCH  /channels/:id} : Partial updates given fields of an existing channels, field will ignore if it is null
     *
     * @param id the id of the channels to save.
     * @param channels the channels to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated channels,
     * or with status {@code 400 (Bad Request)} if the channels is not valid,
     * or with status {@code 404 (Not Found)} if the channels is not found,
     * or with status {@code 500 (Internal Server Error)} if the channels couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Channels> partialUpdateChannels(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Channels channels
    ) throws URISyntaxException {
        log.debug("REST request to partial update Channels partially : {}, {}", id, channels);
        if (channels.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, channels.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!channelsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Channels> result = channelsRepository
            .findById(channels.getId())
            .map(existingChannels -> {
                if (channels.getName() != null) {
                    existingChannels.setName(channels.getName());
                }

                return existingChannels;
            })
            .map(channelsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, channels.getId().toString())
        );
    }

    /**
     * {@code GET  /channels} : get all the channels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of channels in body.
     */
    @GetMapping("")
    public List<Channels> getAllChannels() {
        log.debug("REST request to get all Channels");
        return channelsRepository.findAll();
    }

    @GetMapping("/public")
    public List<Channels> getAlPubliclChannels() {
        log.debug("REST request to get all Channels");
        return service.getAllPublicChannels();
    }

    /**
     * {@code GET  /channels/:id} : get the "id" channels.
     *
     * @param id the id of the channels to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the channels, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Channels> getChannels(@PathVariable("id") Long id) {
        log.debug("REST request to get Channels : {}", id);
        Optional<Channels> channels = channelsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(channels);
    }

    /**
     * {@code DELETE  /channels/:id} : delete the "id" channels.
     *
     * @param id the id of the channels to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChannels(@PathVariable("id") Long id) {
        log.debug("REST request to delete Channels : {}", id);
        channelsRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    //    @RequestMapping("channels/{id}")
    //    public List<Messages> getAllMessages(@PathVariable("id") Long id){
    //
    //        return service.getAllMessagesByChannel(id) ;
    //    }

    @GetMapping("/messages-by-id/{id}")
    public ResponseEntity<List<Channels>> getAllMessagesById(@PathVariable("id") Long id) {
        List<Channels> channels = service.findAllChannelMessagesByID(id);

        return ResponseEntity.ok(channels);
        //    try {
        //        List<Channels> channels = channelsRepository.findAllMessagesById(id);
        //        if (channels.isEmpty()) {
        //            return ResponseEntity.noContent().build();
        //        }
        //        return ResponseEntity.ok(channels);
        //    } catch (Exception e) {
        //        // Log the exception
        //        e.printStackTrace();
        //        // Return a 500 response with an error message
        //        System.out.println("error");
        //
        //    }
        //    return null;
    }

    @GetMapping("/user-profile/{username}")
    public ResponseEntity<List<Channels>> getAllChannelsByUsernames(@PathVariable("username") String username) {
        List<Channels> channels = service.findChannelsByUsername(username);

        return ResponseEntity.ok(channels);
    }

    @GetMapping("/userdms/{username}")
    public ResponseEntity<List<Channels>> getAllPrivateChannelsByUsernames(@PathVariable("username") String username) {
        List<Channels> channels = service.findPrivateChannelsByUsername(username);

        return ResponseEntity.ok(channels);
    }
}
