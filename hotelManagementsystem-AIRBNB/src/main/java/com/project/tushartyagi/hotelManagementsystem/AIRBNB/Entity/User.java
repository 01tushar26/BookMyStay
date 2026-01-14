package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity;


import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Entity.Enums.Roles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@RequiredArgsConstructor

//By default, post gress didnot form a table named user because it already have it
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false ,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;// should be encrypted

    private String name;

    //this will create a new table(name is app_user_roles that store user id and there roles only)
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Roles> roles;


}
