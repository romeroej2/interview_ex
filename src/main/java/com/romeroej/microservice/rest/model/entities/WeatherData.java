package com.romeroej.microservice.rest.model.entities;

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
public class WeatherData implements Serializable {


    @Id
    private String name;
    private Long timestampRest;

    public WeatherData() {
    }




}

