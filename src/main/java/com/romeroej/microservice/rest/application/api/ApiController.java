package com.romeroej.microservice.rest.application.api;


import com.romeroej.microservice.rest.domain.bussinesrules.EventAuditService;
import com.romeroej.microservice.rest.domain.bussinesrules.UserIdentityService;
import com.romeroej.microservice.rest.model.entities.CurrencyExchange;
import com.romeroej.microservice.rest.domain.bussinesrules.CurrencyExchangeService;
import io.swagger.annotations.*;
import org.jboss.logging.Logger;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/api/v1")
@Api(value = "/cex", description = "Generic Api Controller", tags = "login,weather,log")
@Produces(MediaType.APPLICATION_JSON)
@SwaggerDefinition(
        info = @Info(
                title = "Api Service",
                description = "Example Service for Consuming User Services/WeatherInformation/Audit",
                version = "1.0.0",
                contact = @Contact(
                        name = "Efrain Romero",
                        email = "ejromero2@gmail.com",
                        url = "https://github.com/romeroej2/thorntail-microservice"
                )
        ),
        host = "localhost",
        basePath = "/api/v1",
        schemes = {SwaggerDefinition.Scheme.HTTP}
)
public class ApiController {

    private static final Logger LOG = Logger.getLogger(ApiController.class);
    @Inject
    private CurrencyExchangeService currencyExchangeService;

    @Inject
    private EventAuditService eventAuditService;

    @Inject
    private UserIdentityService userIdentityService;

    /*
    @GET
    @Path("/rates")
    @ApiOperation(value = "Get the CEX exchange rate for EUR",
            notes = "Returns the CEX rates as a string",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Gets current CEX rates"),
            @ApiResponse(code = 500, message = "Generic Error")})
    public Response get() {

        LOG.info("CEX  rates [GET] executed");
        CurrencyExchange results = null;
        try {
            results = currencyExchangeService.getCEXRates("EUR");
            return Response.ok(results, MediaType.APPLICATION_JSON).build();

        } catch (Exception e) {
            return Response.status(500, "Problem Getting CEX Data: " + e.getMessage()).build();
        }

    }


    @GET
    @Path("convert/{fromCurrency}/{toCurrency}/{ammount}")
    @ApiOperation(value = "Get the CEX exchange date for a specified currency",
            notes = "Returns the CEX rates as a string",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Gets current CEX rates"),
            @ApiResponse(code = 500, message = "Generic Error")})
    public Response convertCurrency(@PathParam("fromCurrency") @ApiParam(value = "Currency To Convert", required = true) String fromCurrency,
                                    @PathParam("toCurrency") @ApiParam(value = "Currency To Convert", required = true) String toCurrency,
                                    @PathParam("ammount") @ApiParam(value = "Currency Ammount to Convert", required = true) double ammount) {

        LOG.info("CEX Convert [GET] executed");
        CurrencyExchange results = null;
        try {
            //results = currencyExchangeService.getCEXRates(currency);
            return Response.ok(String.format("{\"convertedAmmount\" : \"%s %s\"}", currencyExchangeService.getConvertedAmmount(fromCurrency.toUpperCase(), toCurrency.toUpperCase(), ammount), toCurrency.toUpperCase())
                    , MediaType.APPLICATION_JSON).build();

        } catch (Exception e) {
            return Response.status(500, "Problem Getting CEX Data: " + e.getMessage()).build();
        }

    }
    */

    @POST
    @Path("users/{username}")
    @ApiOperation(value = "Registers a new User to the DB",
            notes = "Return created user",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Gets current CEX rates"),
            @ApiResponse(code = 500, message = "Generic Error")})
    public Response createUser(@PathParam("username") @ApiParam(value = "Username", required = true) String username,
                               @ApiParam(value = "Full Name", required = true) String fullName,
                               @ApiParam(value = "E-Mail", required = true) String email,
                               @ApiParam(value = "Password", required = true) String password) {


        LOG.info("API createUser [POST] executed");

        try {

            return Response.ok(userIdentityService.createUser(username, fullName, email, password), MediaType.APPLICATION_JSON).build();

        } catch (Exception e) {
            return Response.status(500, "Problem Getting CEX Data: " + e.getMessage()).build();
        }
    }

    @POST
    @Path("users/{username}/login")
    @ApiOperation(value = "Validates Login to the DB",
            notes = "Return Valid user",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Gets current CEX rates"),
            @ApiResponse(code = 500, message = "Generic Error")})
    public Response validateUser(@PathParam("username") @ApiParam(value = "Username", required = true) String username,
                               @ApiParam(value = "Password", required = true) String password) {


        LOG.info("API validateUser [POST] executed");

        try {

            return Response.ok(userIdentityService.isValidLogin(username,password), MediaType.APPLICATION_JSON).build();

        } catch (Exception e) {
            return Response.status(500, "Problem Getting CEX Data: " + e.getMessage()).build();
        }
    }


    @GET
    @Path("/eventInformation")
    @ApiOperation(value = "Event Information Loggin all Past Events",
            notes = "Returns a string with event data",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Service Alive"),
            @ApiResponse(code = 500, message = "Generic Error")})
    public Response getEventInformation() {
        LOG.info("API getEventInformation [GET] executed");
        return Response.ok(eventAuditService.getEventsFromDB(), MediaType.APPLICATION_JSON).build();


    }


    @GET
    @Path("/status")
    @ApiOperation(value = "Service Alive Probe",
            notes = "Returns a string with bussinesrules ok & current time",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Service Alive"),
            @ApiResponse(code = 500, message = "Generic Error")})
    public Response getStatus() {
        LOG.info("API Status [GET] executed");
        try {
            eventAuditService.insertEvent("Audit", "Service Status Called");
            return Response.ok(String.format("{\"status\":\"bussinesrules ok\",\"value\" : \"The time is %s\"}", new DateTime()), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(500, e.getMessage()).build();
        }


    }

}
