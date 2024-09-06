package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Channels.
 */
@Entity
@Table(name = "channels")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Channels implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "privacy")
    private Boolean privacy = false;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "channels") //I changed the fetch tpe to Eager to get whole json body
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userProfile", "channels" }, allowSetters = true)
    private Set<Messages> messages = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "channels") //I changed the fetch type to eager to get whole json body
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "messages", "channels" }, allowSetters = true)
    private Set<UserProfile> userProfiles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Channels id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Channels name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Messages> getMessages() {
        return this.messages;
    }

    public void setMessages(Set<Messages> messages) {
        if (this.messages != null) {
            this.messages.forEach(i -> i.setChannels(null));
        }
        if (messages != null) {
            messages.forEach(i -> i.setChannels(this));
        }
        this.messages = messages;
    }

    public Channels messages(Set<Messages> messages) {
        this.setMessages(messages);
        return this;
    }

    public Channels addMessages(Messages messages) {
        this.messages.add(messages);
        messages.setChannels(this);
        return this;
    }

    public Channels removeMessages(Messages messages) {
        this.messages.remove(messages);
        messages.setChannels(null);
        return this;
    }

    public Set<UserProfile> getUserProfiles() {
        return this.userProfiles;
    }

    public void setUserProfiles(Set<UserProfile> userProfiles) {
        if (this.userProfiles != null) {
            this.userProfiles.forEach(i -> i.removeChannels(this));
        }
        if (userProfiles != null) {
            userProfiles.forEach(i -> i.addChannels(this));
        }
        this.userProfiles = userProfiles;
    }

    public Channels userProfiles(Set<UserProfile> userProfiles) {
        this.setUserProfiles(userProfiles);
        return this;
    }

    public Channels addUserProfile(UserProfile userProfile) {
        this.userProfiles.add(userProfile);
        userProfile.getChannels().add(this);
        return this;
    }

    public Channels removeUserProfile(UserProfile userProfile) {
        this.userProfiles.remove(userProfile);
        userProfile.getChannels().remove(this);
        return this;
    }

    public Boolean getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Boolean privacy) {
        this.privacy = privacy;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Channels)) {
            return false;
        }
        return getId() != null && getId().equals(((Channels) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Channels{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
