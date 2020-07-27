package com.tech.challenge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tech.challenge.model.licensetype.LicenseType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
public class License {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    private LicenseType type;
    private Long contentId;


    public License(Customer customer, LicenseType licenseType, Long contentId) {
        this.customer = customer;
        this.type = licenseType;
        this.contentId = contentId;
    }
}
