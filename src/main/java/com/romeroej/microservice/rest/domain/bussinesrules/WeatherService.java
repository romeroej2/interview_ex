package com.romeroej.microservice.rest.domain.bussinesrules;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.romeroej.microservice.rest.model.entities.WeatherData;
import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;


/**
 * Service Solution that contains Application Logic to resolve the RATES date
 * and allow the information to be kept in sync. The class should cache the information
 * in the Database for 10min if the data is older it will remove it and refresh it.
 *
 * @author RomeroEJ
 * @version 1.0
 * @since 2019-02-28
 */
@Stateless
public class WeatherService {


    private static final Logger LOG = Logger.getLogger(WeatherService.class);


    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    @ConfigurationValue("microservice.weather-url")
    private String weather_url;


    @Inject
    @ConfigurationValue("microservice.weather-token")
    private String weather_token;



    @PostConstruct
    public void WeatherServicePostConstruct() {

    }



    public String queryRestEndpoint4Weather(String city) throws Exception {


        String urlStr = String.format(weather_url,city,weather_token);


        try {

            URL url = new URL(urlStr);

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();


            //Read REST info and Parse it to POJO to Persist.
            String response =IOUtils.toString(url, "UTF-8"); //gson.fromJson(IOUtils.toString(url, "UTF-8"), WeatherData.class);



            return response;


        } catch (MalformedURLException e) {

            LOG.errorf("Malformed Endpoint %s", urlStr);
            throw new Exception("Problem Fetching new Weather Data");

        } catch (IOException e) {

            LOG.errorf("Error consuming Endpoint %s, exception: %s", urlStr, e.getMessage());
            throw new Exception("Problem Fetching new Weather Data");

        } catch (Exception e) {
            e.printStackTrace();
            LOG.errorf("Exception: %s", urlStr, e.getMessage());
            throw new Exception("Problem Fetching new Weather Data");
        }


    }

}
    /*

    public CurrencyExchange getCEXRates(String currency) throws Exception {


        validateCurrency(currency);

        currency = "EUR"; // Service only support EUR.

        CurrencyExchange currencyExchange = null;

        try {
            //Fetch Information from DB.
            currencyExchange = entityManager.find(CurrencyExchange.class, currency);
        } catch (Exception ex) {
            LOG.infof("Database doesnt contain a RATE for %s", currency);
            ex.printStackTrace();
        }


        if (currencyExchange != null) {

            LOG.infof("CEX RATE found on DB: %s", currencyExchange);


            //Fetch Timestamps from DB & Current Time and Evaluate if data should be refreshed.
            Instant now = Instant.now();
            Instant cexTimeStamp = Instant.ofEpochMilli(currencyExchange.getTimestampRest());

            long timeElapsed = Duration.between(cexTimeStamp, now).toMinutes();

            LOG.infof("Elapsed time for CEX data: %s minutes", timeElapsed);

            if (timeElapsed > 10) {

                //Refresh CEX data as its STALE.

                try {
                    entityManager.remove(currencyExchange);
                } catch (Exception ex) {
                    LOG.errorf("Problem Removing DB Data. Ex: %s", ex.getMessage());
                    //Should be able to continue even if this fails, as next time data will be refreshed.
                }

                currencyExchange = queryRestEndpoint4CEX(currency);

                try {
                    entityManager.persist(currencyExchange);

                } catch (Exception ex) {
                    LOG.errorf("Problem Persisting DB Data. Ex: %s", ex.getMessage());
                    //Should be able to continue even if this fails, as next time data will be refreshed.
                }

            }


        } else {

            //Refresh CEX data as its not on cache.

            currencyExchange = queryRestEndpoint4CEX(currency);

            if (currencyExchange != null) {
                currencyExchange.setTimestampRest(Instant.now().toEpochMilli());
                try {
                    entityManager.persist(currencyExchange);
                } catch (Exception ex) {
                    LOG.errorf("Problem Persisting DB Data. Ex: %s", ex.getMessage());
                    //Should be able to continue even if this fails, as next time data will be refreshed.
                }
            }
        }


        return currencyExchange;


    }


    private CurrencyExchange queryRestEndpoint4CEX(String currency) throws Exception {





        try {

            URL url = new URL(urlStr);

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();


            //Read REST info and Parse it to POJO to Persist.
            CurrencyExchange response = gson.fromJson(IOUtils.toString(url, "UTF-8"), CurrencyExchange.class);

            //Since Free Api only Works for EUR if the currency is different we need to convert it :(
            response.getRates().setEUR(1.0);


            return response;


        } catch (MalformedURLException e) {

            LOG.errorf("Malformed Endpoint %s", urlStr);
            throw new Exception("Problem Fetching new CEX Data");

        } catch (IOException e) {

            LOG.errorf("Error consuming Endpoint %s, exception: %s", urlStr, e.getMessage());
            throw new Exception("Problem Fetching new CEX Data");

        } catch (Exception e) {
            e.printStackTrace();
            LOG.errorf("Exception: %s", urlStr, e.getMessage());
            throw new Exception("Problem Fetching new CEX Data");
        }


    }


    public Double getConvertedAmmount(String fromCurrency, String toCurrency, Double ammount) throws Exception {

        validateCurrency(toCurrency);
        validateCurrency(fromCurrency);

        CurrencyExchange cex = getCEXRates(fromCurrency);

        if (fromCurrency.equals(cex.getBase())) {
            //if BASE just convert using rate
            ammount = ammount * cex.getRates().getRate(toCurrency);

        } else {
            //Convert to Base

            ammount = ammount / cex.getRates().getRate(fromCurrency);

            //Convert to Source
            ammount = ammount * cex.getRates().getRate(toCurrency);


        }

        return ammount;
    }

    private void validateCurrency(String currency) throws Exception {
        if (Stream.of("USD", "AUD", "CAD", "PLN", "MXN", "COP", "EUR").filter(c -> c.equals(currency)).count() == 0) {
            throw new Exception("Invalid Currency");
        }

    }
*/

