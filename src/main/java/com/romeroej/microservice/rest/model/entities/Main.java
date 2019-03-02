package com.romeroej.microservice.rest.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
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
public class Main implements Serializable {



    @Id
    private String id;

    private String temp;
    private String temp_min;
    private String temp_max;



    public Main() {
    }




}

