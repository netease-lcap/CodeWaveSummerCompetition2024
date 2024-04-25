package com.fdddf.shorturl.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "link")
public class Link implements Serializable {

    @Id
    @GeneratedValue
    public Long id;

    @Column(name = "long_url")
    public String longUrl;

    @Column(name = "short_url")
    public String shortUrl;

    @Column(name = "expiration_time")
    public String expirationTime;

    @Column(name = "max_access_count")
    public Long maxAccessCount;

    @Column(name = "access_count")
    public Long accessCount;
}