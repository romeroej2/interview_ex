package com.romeroej.microservice.rest.application.api;


import com.romeroej.microservice.rest.domain.bussinesrules.EventAuditService;
import com.romeroej.microservice.rest.domain.bussinesrules.UserIdentityService;

import com.romeroej.microservice.rest.domain.bussinesrules.WeatherService;
import com.romeroej.microservice.rest.model.entities.User;
import io.swagger.annotations.*;
import org.jboss.logging.Logger;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/api/v1")
@Api(value = "/", description = "Generic Api Controller", tags = "login,weather,log")
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
 private WeatherService weatherService;

    @Inject
    private EventAuditService eventAuditService;

    @Inject
    private UserIdentityService userIdentityService;


    @GET
    @Path("/weather/{city}")
    @ApiOperation(value = "Gets the current weather for city",
            notes = "Returns the current weather for city",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Gets current Weather"),
            @ApiResponse(code = 500, message = "Generic Error")})
    public Response getWeather(@PathParam("city") @ApiParam(value = "City", required = true) String city) {

        LOG.info("API  getWeather [GET] executed");

        try {

            eventAuditService.insertEvent("WeatherQuery",String.format("Consulted Weather for %s",city));
            return Response.ok(weatherService.queryRestEndpoint4Weather(city), MediaType.APPLICATION_JSON).build();

        } catch (Exception e) {
            return Response.status(500, "Problem Getting Weather Data: " + e.getMessage()).build();
        }

    }



    @POST
    @Path("users/register")
    @ApiOperation(value = "Registers a new User to the DB",
            notes = "Return created user",
            response = String.class
    )
    //@Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Gets created user"),
            @ApiResponse(code = 409, message = "User Exists"),
            @ApiResponse(code = 500, message = "Generic Error")})
    public Response createUser( @ApiParam(value = "Full Name", required = true) User user
                               ) {


        LOG.info("API createUser [POST] executed");

        try {

            return Response.ok(userIdentityService.createUser(user), MediaType.APPLICATION_JSON).build();

        } catch (Exception e) {
            if(e.getMessage().contains("User Already Exists"))
            return Response.status(409,  e.getMessage()).build();
                else
            return Response.status(500, "Problem Creating User: " + e.getMessage()).build();
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
            @ApiResponse(code = 200, message = "Creates Login Request"),
            @ApiResponse(code = 401, message = "User doesnt exist or invalid password")})
    public Response validateUser(@PathParam("username") @ApiParam(value = "Username", required = true) String username,
                               @ApiParam(value = "Password", required = true, example = "string") String password) {




        LOG.info("API validateUser [POST] executed");

        try {

            return Response.ok(userIdentityService.isValidLogin(username,password), MediaType.APPLICATION_JSON).build();

        } catch (Exception e) {
            return Response.status(401, "Unauthorized: " + e.getMessage()).build();
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
            //eventAuditService.insertEvent("Audit", "Service Status Called");
            return Response.ok(String.format("{\"status\":\"bussinesrules ok\",\"value\" : \"The time is %s\"}", new DateTime()), MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.status(500, e.getMessage()).build();
        }


    }

}
