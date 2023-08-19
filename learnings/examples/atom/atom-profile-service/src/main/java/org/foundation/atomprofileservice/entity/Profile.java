package org.foundation.atomprofileservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    private UUID id;
    @Column(unique = true)
    private String url;
    @Column(unique = true)
    private String email;
    private String firstName;
    private String lastName;
    private String picture;

    @PrePersist
    private synchronized void generateId() {
        if (id == null) {
            this.id = UUID.randomUUID();
        }
    }
}
