package com.fdddf.shorturl.model;

import com.netease.lowcode.core.annotation.NaslStructure;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "links")
@NaslStructure
public class Link implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String longUrl;

    @Column(nullable = false, unique = true)
    public String shortCode;

    @Column
    public String expirationTime;

    @Column
    public Long maxAccessCount;

    @Column
    public Long accessCount;
}