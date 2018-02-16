package com.riabchych.cloudstock.endpoint;

import com.riabchych.cloudstock.annotation.CrossOrigin;
import com.riabchych.cloudstock.annotation.Secured;
import com.riabchych.cloudstock.entity.EnumRole;
import com.riabchych.cloudstock.entity.User;
import com.riabchych.cloudstock.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.util.List;

@Component
@Path("/user")
public class UserEndpoint {
    private static final Logger logger = LoggerFactory.getLogger(UserEndpoint.class);
    private final UserService userService;

    @Autowired
    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @GET
    @CrossOrigin
    @Path("/")
    @Secured({
            EnumRole.ROLE_ADMIN,
            EnumRole.ROLE_USER,
            EnumRole.ROLE_OWNER
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserDetails() {
        List<User> list = userService.getAllUsers();
        return Response.ok(list).build();
    }

    @GET
    @CrossOrigin
    @Path("/{login}")
    @Secured({
            EnumRole.ROLE_ADMIN,
            EnumRole.ROLE_USER,
            EnumRole.ROLE_OWNER
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("login") String login) {
        long num = extractNumber(login);
        User user = num > 0 ? userService.getUserById(num) : userService.getUserByUsername(login);
        return Response.ok(user).build();
    }

    @POST
    @CrossOrigin
    @Path("/")
    @Secured({EnumRole.ROLE_ADMIN})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(User user) {
        boolean isAdded = userService.addUser(user);
        if (!isAdded) {
            logger.info("User already exits.");
            return Response.status(Status.CONFLICT).build();
        }
        return Response.created(URI.create("/api/user/" + user.getId())).build();
    }

    @PUT
    @CrossOrigin
    @Path("/{login}")
    @Secured({
            EnumRole.ROLE_ADMIN,
            EnumRole.ROLE_USER,
            EnumRole.ROLE_OWNER
    })
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(User user) {
        userService.updateUser(user);
        return Response.ok(user).build();
    }

    @DELETE
    @CrossOrigin
    @Path("/{id}")
    @Secured(EnumRole.ROLE_ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") Long id) {
        userService.deleteUser(id);
        return Response.noContent().build();
    }

    private long extractNumber(String s) {
        long number;
        try {
            number = Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return 0;
        }
        return number;
    }
}