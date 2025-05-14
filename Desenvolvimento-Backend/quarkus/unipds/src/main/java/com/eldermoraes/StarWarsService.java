package com.eldermoraes;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.temporal.ChronoUnit;

@RegisterRestClient(baseUri = "https://swapi.info/api/")
public interface StarWarsService {

    public static final String MSG_ERROR = "Fallback ";

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("starships")
    @Timeout(value = 3000L)
    @Fallback(
            fallbackMethod = "getStarshipsFallback"
    )
    @CircuitBreaker(
            requestVolumeThreshold = 2,
            failureRatio = .5,
            delay = 3000L,
            successThreshold = 2
    )
    public String getStarships();

    default String getStarshipsFallback(){
        return MSG_ERROR;
    }
}
