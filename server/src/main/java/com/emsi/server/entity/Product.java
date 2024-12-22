package com.emsi.server.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name="product")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_product")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name = "unit_price")
    private double unit_price;

    @Column(name = "available")
    private boolean available;

    @Column(name = "image_url")
    private String imgUrl;


    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;
}