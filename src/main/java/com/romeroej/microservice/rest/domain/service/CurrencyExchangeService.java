package com.romeroej.microservice.rest.domain.service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.romeroej.microservice.rest.data.model.CurrencyExchange;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;


/**
 * @author Efrain Romero  ejromero2@gmail.com
 */
@Stateless
public class CurrencyExchangeService {


    private static final Logger LOG = Logger.getLogger(CurrencyExchangeService.class);


    @PersistenceContext
    private EntityManager entityManager;


    public CurrencyExchange find() {

        //System.out.println("SERVICE INVOKED");

        CurrencyExchange currencyExchange = null;

        try {
            currencyExchange = entityManager.find(CurrencyExchange.class, "EUR");
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        if (currencyExchange != null) {

            //System.out.println("DB OBJ: " + currencyExchange);

            Instant now = Instant.now();

            //System.out.println("InstantNow: "+ now.toString());

            Instant cexTimeStamp = Instant.ofEpochMilli(currencyExchange.getTimestampRest());

            //System.out.println("InstantRest: "+ cexTimeStamp.toString());


            long timeElapsed = Duration.between(cexTimeStamp, now).toMinutes();

            System.out.println("Elapsed time: " + timeElapsed);
            if (timeElapsed > 10) {
                entityManager.remove(currencyExchange);
                currencyExchange = queryRestEndpoint4CEX();
                entityManager.persist(currencyExchange);

            }


        } else {
            currencyExchange = queryRestEndpoint4CEX();

            if (currencyExchange != null) {
                //System.out.println("new DB OBJ: " + currencyExchange);
                currencyExchange.setTimestampRest(Instant.now().toEpochMilli());
                entityManager.persist(currencyExchange);
            }
        }


        return currencyExchange;


    }


    private CurrencyExchange queryRestEndpoint4CEX() {


        String urlStr = "http://data.fixer.io/api/latest?access_key=98389804f272658862bec77fd0af4f17&symbols=USD,AUD,CAD,PLN,MXN&format=1";

        LOG.infof("Getting CEX data from Endpoint {}", urlStr);


        try {

            URL url = new URL(urlStr);

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            return gson.fromJson(url.toString(), CurrencyExchange.class);


        } catch (MalformedURLException e) {

            LOG.errorf("Malformed Endpoint {}", urlStr);

            e.printStackTrace();

        } catch (IOException e) {

            LOG.errorf("Error consuming Endpoint {}, exception: {}", urlStr, e.getMessage());

            e.printStackTrace();

        }

        return null;

    }
}
