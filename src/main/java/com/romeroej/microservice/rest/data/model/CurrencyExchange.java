package com.romeroej.microservice.rest.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@Entity
public class CurrencyExchange implements Serializable {
    private Boolean success;
    private Integer timestamp;
    private Long timestampRest;
    private String date;

    @Id
    private String base;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "rate_id")
    private Rates rates;


    public CurrencyExchange() {
    }


}

