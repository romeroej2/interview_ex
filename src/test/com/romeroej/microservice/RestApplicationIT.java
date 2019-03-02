package com.romeroej.microservice;


import com.google.gson.GsonBuilder;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.MalformedJsonException;
import com.romeroej.microservice.rest.application.ApplicationActivator;
import com.romeroej.microservice.rest.application.api.ApiController;
import com.romeroej.microservice.rest.application.api.filter.CORSFilter;

import com.romeroej.microservice.rest.domain.bussinesrules.EventAuditService;
import com.romeroej.microservice.rest.model.entities.User;
import com.romeroej.microservice.rest.model.entities.Weather;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import static org.fest.assertions.Assertions.assertThat;


@RunWith(Arquillian.class)
public class RestApplicationIT {

    @Drone
    WebDriver browser;


    @Deployment
    public static Archive createDeployment() {


        MavenResolverSystem resolver = Maven.resolver();
        resolver.loadPomFromFile("pom.xml");


        return ShrinkWrap.create(WebArchive.class)

                .addPackage(ApiController.class.getPackage())
                .addPackage(CORSFilter.class.getPackage())
                .addPackage(EventAuditService.class.getPackage())
                .addPackage(Weather.class.getPackage())
                .addClass(ApplicationActivator.class)
                .addPackage(GsonBuilder.class.getPackage())
                .addAsLibraries(resolver.resolve("joda-time:joda-time:2.7").withTransitivity().as(JavaArchive.class))
                .addPackage(Excluder.class.getPackage())
                .addPackage(JsonTreeReader.class.getPackage())
                .addPackage(TypeToken.class.getPackage())
                .addPackage(IOUtils.class.getPackage())
                .addPackage(StringBuilderWriter.class.getPackage())
                .addPackage(JsonAdapter.class.getPackage())


                .addPackage(MalformedJsonException.class.getPackage())
                .addAsResource("project-defaults.yml", "project-defaults.yml")
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/load.sql", "META-INF/load.sql")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

    }




    //Validate JSON SPEC deplioyed
    @RunAsClient
    @Test
    public void testSwaggerJson() {
        browser.navigate().to("http://localhost:8080/swagger.json");
        assertThat(browser.getPageSource()).contains("swagger");
    }



    //Validate Heartbeat endpoint alive
    @RunAsClient
    @Test
    public void testServiceStatus() {
        browser.navigate().to("http://localhost:8080/api/v1/status");
        assertThat(browser.getPageSource()).contains("bussinesrules ok");
    }


    //Service returning weather info for Bogota
    @RunAsClient
    @Test
    public void testGetWeather() {
        browser.navigate().to("http://localhost:8080/api/v1/weather/bogota");
        assertThat(browser.getPageSource()).contains("\"Bogota\"");
    }



    //After Weather Info consumed, Logged in Events?
    @RunAsClient
    @Test
    public void testAudit() {

        //generate an event
        browser.navigate().to("http://localhost:8080/api/v1/weather/bogota");
        //check that event exists.
        browser.navigate().to("http://localhost:8080/api/v1/eventInformation");
        assertThat(browser.getPageSource()).contains("Consulted Weather");
    }


    //Register New User
    @RunAsClient
    @Test
    @InSequence(1)
    public void testRegistration() {


        final String urlStr =
                "http://localhost:8080/api/v1/users/register";
        String response =
                ClientBuilder.newClient().target(urlStr).request()
                        .post( Entity.json("{\"username\": \"string\",\"fullName\": \"string\",\"address\": \"string\",\"email\": \"string\",\"password\": \"string\"}"), String.class);


        assertThat(response.contains("romeroej"));


    }



    //Successfull Login
    @RunAsClient
    @Test
    @InSequence(2)
    public void testLogin(  final WebTarget webTarget ) {



        String urlStr =
                "http://localhost:8080/api/v1/users/romeroej/login";
        String response =
                ClientBuilder.newClient().target(urlStr).request()
                        .post( Entity.json("\"string\""), String.class);


        assertThat(response.contains("romeroej"));


    }


    //Failed Login
    @RunAsClient
    @Test
    @InSequence(3)
    public void testLoginFail(  final WebTarget webTarget ) {



        String urlStr =
                "http://localhost:8080/api/v1/users/romeroej/login";

        try {
            String response =
                    ClientBuilder.newClient().target(urlStr).request()
                            .post(Entity.json("bad"), String.class);
            assertThat(false);
        }catch (Exception ex)
        {
            if(ex.getMessage().contains("401"))
                assertThat(true);
        }






    }

}

