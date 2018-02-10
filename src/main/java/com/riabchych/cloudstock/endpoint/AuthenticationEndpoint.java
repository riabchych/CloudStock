package com.riabchych.cloudstock.endpoint;

import com.riabchych.cloudstock.entity.Token;
import com.riabchych.cloudstock.entity.User;
import com.riabchych.cloudstock.service.UserService;
import com.riabchych.cloudstock.util.TokenUtil;

import javax.annotation.security.PermitAll;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

@PermitAll
@Path("/authentication")
public class AuthenticationEndpoint {

    private final static Logger logger = Logger.getLogger(AuthenticationEndpoint.class.getName());

    @Context
    UserService uService;

    @Context
    Key key;

    @POST
    @Produces("application/json")
    @Consumes("application/x-www-form-urlencoded")
    public Response authenticateUser(@FormParam("username") String username,
                                     @FormParam("password") String password) {

        Date expiry = getExpiryDate(15);
        User user = authenticate(username, password);

        String jwtString = TokenUtil.getJWTString(username, user.getRolesString(), user.getVersion(), expiry, key);
        Token token = new Token();
        token.setAuthToken(jwtString);
        token.setExpires(expiry);

        return Response.ok(token).build();
    }

    private Date getExpiryDate(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    private User authenticate(String username, String password) throws NotAuthorizedException {
        User user = null;
        try {
            user = uService.getUserByUsername(username);
        } catch (EntityNotFoundException e) {
            logger.info("Invalid username '" + username + "' ");
            throw new NotAuthorizedException("Invalid username '" + username + "' ");
        }

        if (user.getPassword().equals(password)) {
            logger.info("USER AUTHENTICATED");
        } else {
            logger.info("USER NOT AUTHENTICATED");
            throw new NotAuthorizedException("Invalid username or password");
        }
        return user;
    }


}