package com.romeroej.microservice.rest.application.api;

/*

@Path("/api/v1/time")
@Api(value = "/time", description = "Get the time", tags = "time")
@Produces(MediaType.APPLICATION_JSON)
@SwaggerDefinition(
        info = @Info(
                title = "Time Service",
                description = "Example Service for Getting the Current Time",
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
public class TimeController {

    private static final Logger LOG = Logger.getLogger(CurrencyExchangeService.class);


    @GET
    @Path("/now")
    @ApiOperation(value = "Get the current time",
            notes = "Returns the time as a string",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Current Time"),
            @ApiResponse(code = 500, message = "Generic Error")})
    public String get() {
        return String.format("{\"value\" : \"The time is %s\"}", new DateTime());
    }
}

*/