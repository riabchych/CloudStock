package com.riabchych.cloudstock.config;

import com.riabchych.cloudstock.endpoint.AuthenticationEndpoint;
import com.riabchych.cloudstock.endpoint.BarcodeEndpoint;
import com.riabchych.cloudstock.endpoint.UserEndpoint;
import com.riabchych.cloudstock.filter.AuthenticationFilter;
import com.riabchych.cloudstock.filter.AuthorizationFilter;
import com.riabchych.cloudstock.filter.CrossOriginResponseFilter;
import com.riabchych.cloudstock.security.CrossOriginConfiguration;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {

    @Autowired
    public JerseyConfig(CrossOriginConfiguration crossOriginConfiguration) {
        register(new CrossOriginResponseFilter(crossOriginConfiguration), 1);
        register(RequestContextFilter.class);
        register(BarcodeEndpoint.class);
        register(UserEndpoint.class);
        register(AuthenticationFilter.class);
        register(AuthorizationFilter.class);
        register(AuthenticationEndpoint.class);
    }
}