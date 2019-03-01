package com.romeroej.microservice.rest.data.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@AllArgsConstructor
@Entity
public class Rates implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private Double USD;
    private Double AUD;
    private Double CAD;
    private Double PLN;
    private Double MXN;

    public Rates() {

    }


}