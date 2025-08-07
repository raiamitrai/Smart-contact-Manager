package com.amit.SmartcontactManager.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="USER")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private String password;


    private String email;

    @Column(length = 500)
    private String about;

    private String imageName;
    private String role;
    private boolean enabled;

    @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY, mappedBy = "user")
    private List<Contact> contacts = new ArrayList<>();
}
