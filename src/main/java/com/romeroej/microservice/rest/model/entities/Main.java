package com.romeroej.microservice.rest.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


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
public class Main
{
    public double temp ;
    public int pressure ;
    public int humidity ;
    public double temp_min ;
    public double temp_max ;
}

