package com.romeroej.microservice.rest.application.api;


import com.romeroej.microservice.rest.data.model.CurrencyExchange;
import com.romeroej.microservice.rest.domain.service.CurrencyExchangeService;
import io.swagger.annotations.*;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/api/v1/cex")
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
    @ApiOperation(value = "Get the CEX exchange date",
            notes = "Returns the CEX rates as a string",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Gets current CEX"),
            @ApiResponse(code = 500, message = "Generic Error")})
    public CurrencyExchange get() {

        LOG.info("CEX [GET] executed");
        CurrencyExchange results = currencyExchangeService.find();
        return results;
    }

}
