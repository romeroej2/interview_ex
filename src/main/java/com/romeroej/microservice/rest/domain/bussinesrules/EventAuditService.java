package com.romeroej.microservice.rest.domain.bussinesrules;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.romeroej.microservice.rest.model.entities.Event;
import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;


/**
 *
 * @author RomeroEJ
 * @version 1.0
 * @since 2019-02-28
 */
@Stateless
public class EventAuditService {


    private static final Logger LOG = Logger.getLogger(EventAuditService.class);


    @PersistenceContext
    private EntityManager entityManager;


    @PostConstruct
    public void EventAuditServicePostConstruct()
    {

    }


    public void insertEvent(String eventType, String eventDescription) throws Exception {

        Event newEvent = new Event(eventType,eventDescription);

        try {
            entityManager.persist(newEvent);
        }catch (Exception ex)
        {
            throw  new Exception(String.format("Problem Persistin New Event %s %s",eventType,eventDescription));
        }

    }


    public List<Event> getEventsFromDB()
    {

        TypedQuery<Event> q2 =
                entityManager.createQuery("SELECT c FROM Event c", Event.class);


        List<Event> eventList = q2.getResultList();


        return eventList;

    }


}
