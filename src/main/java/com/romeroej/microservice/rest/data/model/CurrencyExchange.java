package com.romeroej.microservice.rest.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Base entity that CurrencyExchange Response with the rates and timestamp from when it was created.
 * <p>
 * Getters,Setters and constructors auto generated via Lombok
 *
 * @author RomeroEJ
 * @version 1.0
 * @since 2019-02-28
 */
@Data
@AllArgsConstructor
@Entity
public class CurrencyExchange implements Serializable {


    @Id
    private String base;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "rate_id")
    private Rates rates;


    public CurrencyExchange() {
    }


    private Boolean success;
    private Integer timestamp;
    private Long timestampRest;
    private String date;


}

