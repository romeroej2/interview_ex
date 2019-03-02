package com.romeroej.microservice.rest.model.entities;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.reflect.Method;


/**
 * Base entity that describes the RATES obtanined from the External CEX Endpoint
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
public class Rates implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private Double USD;
    private Double AUD;
    private Double CAD;
    private Double PLN;
    private Double MXN;
    private Double EUR;
    private Double COP;

    public Rates() {

    }


    @Transient
    public Double getRate(String currency) throws Exception {

        //Get Currency Value using Reflection
        Class<?> c = Class.forName("com.romeroej.microservice.rest.model.entities.Rates");
        Method method = c.getMethod("get" + currency);
        return (Double) method.invoke(this);
    }


}