package com.incubyte.sweetshopsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @Column(name = "category_id")
    private Integer category_id;

    @Column(name = "name", nullable = false)
    private String name;

    public Category() {
    }

    public Category(Integer category_id, String name) {
        this.category_id = category_id;
        this.name = name;
    }

    // Getters and Setters
    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
