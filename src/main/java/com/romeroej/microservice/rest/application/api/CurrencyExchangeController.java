package com.romeroej.microservice.rest.application.api;


import com.romeroej.microservice.rest.data.model.CurrencyExchange;
import com.romeroej.microservice.rest.domain.service.CurrencyExchangeService;
import io.swagger.annotations.*;
import org.jboss.logging.Logger;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/api/cex/v1")
@Api(value = "/cex", description = "Currency Exchange", tags = "cex")
@Produces(MediaType.APPLICATION_JSON)
@SwaggerDefinition(
        info = @Info(
                title = "CEX Service",
                description = "Example Service for Consuming CEX trade info, and cache it in BD",
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
public class CurrencyExchangeController {

    private static final Logger LOG = Logger.getLogger(CurrencyExchangeController.class);
    @Inject
    private CurrencyExchangeService currencyExchangeService;

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
            return Response.ok(String.format("{\"convertedAmmount\" : \"%s %s\"}", currencyExchangeService.getConvertedAmmount(fromCurrency, toCurrency, ammount), toCurrency)
                    , MediaType.APPLICATION_JSON).build();

        } catch (Exception e) {
            return Response.status(500, "Problem Getting CEX Data: " + e.getMessage()).build();
        }

    }


    @GET
    @Path("/status")
    @ApiOperation(value = "Service Alive Probe",
            notes = "Returns a string with service ok & current time",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Service Alive"),
            @ApiResponse(code = 500, message = "Generic Error")})
    public Response getStatus() {
        LOG.info("CEX Status [GET] executed");
        return Response.ok(String.format("{\"status\":\"service ok\",\"value\" : \"The time is %s\"}", new DateTime()), MediaType.APPLICATION_JSON).build();


    }

}
