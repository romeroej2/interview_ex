package com.romeroej.microservice.rest.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


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
public class Event implements Serializable {


    @Id
    @GeneratedValue
    private Long id;

    public Event() {
    }

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
    private String eventType;
    private String eventDescription;

    public Event(String eventType, String eventDescription) {
        this.eventType = eventType;
        this.eventDescription = eventDescription;
        this.timestamp = new Date();
    }
}

