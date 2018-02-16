package com.riabchych.cloudstock.endpoint;

import com.riabchych.cloudstock.annotation.CrossOrigin;
import com.riabchych.cloudstock.entity.Credentials;
import com.riabchych.cloudstock.entity.Token;
import com.riabchych.cloudstock.entity.User;
import com.riabchych.cloudstock.service.UserService;
import com.riabchych.cloudstock.util.PasswordUtils;
import com.riabchych.cloudstock.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@PermitAll
@Path("/authentication")
@Component
public class AuthenticationEndpoint {

    private final PasswordUtils passwordUtils;
    private final UserService userService;
    private final TokenUtil tokenUtil;

    private final static Logger logger = Logger.getLogger(AuthenticationEndpoint.class.getName());


    @Autowired
    public AuthenticationEndpoint(UserService userService, PasswordUtils passwordUtils, TokenUtil tokenUtil) {
        this.userService = userService;
        this.passwordUtils = passwordUtils;
        this.tokenUtil = tokenUtil;
    }

    @POST
    @CrossOrigin
    @Produces("application/json")
    @Consumes("application/json")
    public Response authenticateUser(Credentials creds) {
        User user = authenticate(creds);
        Token token = tokenUtil.getJWTToken(user.getUsername(), user.getRolesString());
        return Response.ok(token).build();
    }

    private User authenticate(Credentials creds) throws NotAuthorizedException {
        User user;
        try {
            user = userService.getUserByUsername(creds.getUsername());
        } catch (EntityNotFoundException e) {
            logger.info("Invalid username '" + creds.getUsername() + "' ");
            throw new NotAuthorizedException("Invalid username '" + creds.getUsername() + "' ");
        }

        if (passwordUtils.verify(creds.getPassword(), user.getPassword(), user.getSalt())) {
            logger.info("USER AUTHENTICATED");
        } else {
            logger.info("USER NOT AUTHENTICATED");
            throw new NotAuthorizedException("Invalid username or password");
        }
        return user;
    }


}