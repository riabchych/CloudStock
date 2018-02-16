package com.riabchych.cloudstock.filter;


import com.riabchych.cloudstock.annotation.CrossOrigin;
import com.riabchych.cloudstock.security.CrossOriginConfiguration;
import com.riabchych.cloudstock.security.CrossOriginHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
@CrossOrigin
public class CrossOriginResponseFilter implements ContainerResponseFilter {

    private final CrossOriginConfiguration configuration;

    public CrossOriginResponseFilter(CrossOriginConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if(!isPreflightRequest(requestContext)) {
            handleSimpleRequest(requestContext, responseContext);
        }
    }

    private boolean isPreflightRequest(ContainerRequestContext requestContext)
    {
        String method = requestContext.getMethod();

        // A Preflight Request uses the OPTIONS Method to Query Resources:
        if (!"OPTIONS".equalsIgnoreCase(method)) {
            return false;
        }

        // If no Access Control Request Header is given, we should not interpret this as a Preflight Request:
        return requestContext.getHeaders().containsKey(CrossOriginHeaders.ACCESS_CONTROL_REQUEST_METHOD_HEADER);
    }

    private void handleSimpleRequest(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        responseContext.getHeaders().add(CrossOriginHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, configuration.getAllowedOrigins());
        responseContext.getHeaders().add(CrossOriginHeaders.ACCESS_CONTROL_ALLOW_HEADERS, configuration.getAllowedHeaders());
        responseContext.getHeaders().add(CrossOriginHeaders.ACCESS_CONTROL_ALLOW_METHODS, configuration.getAllowedMethods());
        responseContext.getHeaders().add(CrossOriginHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, configuration.getExposedHeaders());
        responseContext.getHeaders().add(CrossOriginHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, configuration.getAllowCredentials());
    }

}