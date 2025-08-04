package com.amit.SmartcontactManager.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="CONTACT")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cId;

    private String name;
    private String secondName;
    private String work;
    private String email;
    private String phone;
    private String image;

    @Column(length = 6000)
    private String description;

    @ManyToOne
    private User user;

}
