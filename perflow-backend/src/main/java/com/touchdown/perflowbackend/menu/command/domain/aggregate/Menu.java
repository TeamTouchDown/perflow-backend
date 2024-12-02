package com.touchdown.perflowbackend.menu.command.domain.aggregate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Menu {

    @Id
    @GeneratedValue
    private Long menuId;

    private String name;

    private String url;
}
