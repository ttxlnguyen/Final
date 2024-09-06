package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Messages.
 */
@Entity
@Table(name = "messages")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Messages implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "edited_at")
    private Instant editedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = { "messages", "channels" }, allowSetters = true)
    private UserProfile userProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "messages", "userProfiles" }, allowSetters = true)
    private Channels channels;

    @PrePersist
    protected void onCreate() {
        if (this.sentAt == null) {
            this.sentAt = Instant.now();
        }
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Messages id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public Messages content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getSentAt() {
        return this.sentAt;
    }

    public Messages sentAt(Instant sentAt) {
        this.setSentAt(sentAt);
        return this;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    public Instant getEditedAt() {
        return this.editedAt;
    }

    public Messages editedAt(Instant editedAt) {
        this.setEditedAt(editedAt);
        return this;
    }

    public void setEditedAt(Instant editedAt) {
        this.editedAt = editedAt;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public Messages isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public Messages userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    public Channels getChannels() {
        return this.channels;
    }

    public void setChannels(Channels channels) {
        this.channels = channels;
    }

    public Messages channels(Channels channels) {
        this.setChannels(channels);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Messages)) {
            return false;
        }
        return getId() != null && getId().equals(((Messages) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Messages{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", sentAt='" + getSentAt() + "'" +
            ", editedAt='" + getEditedAt() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            "}";
    }
}
