package com.bumsoap.store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "employee_id")
public class Employee extends BsUser{
    private long id;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
              orphanRemoval = true)
    @JoinColumn(name = "photo_id")
    private Photo photo;
}
