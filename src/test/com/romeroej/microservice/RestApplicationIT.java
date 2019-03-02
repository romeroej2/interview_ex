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
import com.romeroej.microservice.rest.model.entities.CurrencyExchange;
import com.romeroej.microservice.rest.domain.bussinesrules.CurrencyExchangeService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
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
                .addPackage(CurrencyExchange.class.getPackage())
                .addPackage(ApiController.class.getPackage())
                .addPackage(CORSFilter.class.getPackage())
                .addPackage(CurrencyExchangeService.class.getPackage())
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


    @RunAsClient
    @Test
    public void testSwaggerJson() {
        browser.navigate().to("http://localhost:8080/swagger.json");
        assertThat(browser.getPageSource()).contains("swagger");
    }


    @RunAsClient
    @Test
    public void testServiceStatus() {
        browser.navigate().to("http://localhost:8080/api/cex/v1/status");
        assertThat(browser.getPageSource()).contains("bussinesrules ok");
    }


    @RunAsClient
    @Test
    public void testGetCex() {
        browser.navigate().to("http://localhost:8080/api/cex/v1/rates");
        assertThat(browser.getPageSource()).contains("\"eur\":1.0");
    }

    @RunAsClient
    @Test
    public void testConvert() {
        browser.navigate().to("http://localhost:8080/api/cex/v1/convert/EUR/EUR/1");
        assertThat(browser.getPageSource()).contains("1.0 EUR");
    }


}

