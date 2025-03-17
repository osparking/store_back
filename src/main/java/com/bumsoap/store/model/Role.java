package com.bumsoap.store.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.Collection;
import java.util.HashSet;

@Entity
@NoArgsConstructor
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NaturalId
    private String name;
    @ManyToMany(mappedBy = "roles")
    private Collection<BsUser> users = new HashSet<>();

    public Role(String name) {
        this.name = name;
    }
    public String getName() {
        return name == null ? "" : name;
    }
    @Override
    public String toString() {
        return name;
    }
}
