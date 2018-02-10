package com.riabchych.cloudstock.filter;

import com.riabchych.cloudstock.entity.User;
import com.riabchych.cloudstock.service.UserService;
import com.riabchych.cloudstock.util.TokenUtil;
import org.glassfish.jersey.server.ContainerRequest;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Key;
import java.util.Arrays;
import java.util.logging.Logger;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JWTSecurityFilter implements ContainerRequestFilter {

    final static Logger logger = Logger.getLogger(JWTSecurityFilter.class.getName());

    @Context
    UserService uService;

    @Context
    Key key;

    @Inject
    javax.inject.Provider<UriInfo> uriInfo;

    public static String extractJwtTokenFromAuthorizationHeader(String auth) {
        return auth.replaceFirst("[B|b][E|e][A|a][R|r][E|e][R|r] ", "").replace(" ", "");
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String method = requestContext.getMethod().toLowerCase();
        String path = ((ContainerRequest) requestContext).getPath(true).toLowerCase();

        if (("get".equals(method) && ("application.wadl".equals(path) || "application.wadl/xsd0.xsd".equals(path)))
                || ("post".equals(method) && "authentication".equals(path))) {
            requestContext.setSecurityContext(new SecurityContextAuthorizer(uriInfo, () -> "anonymous", new String[]{"anonymous"}));
            return;
        }

        String authorizationHeader = ((ContainerRequest) requestContext).getHeaderString("authorization");
        if (authorizationHeader == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        String strToken = extractJwtTokenFromAuthorizationHeader(authorizationHeader);
        if (TokenUtil.isValid(strToken, key)) {
            String name = TokenUtil.getName(strToken, key);
            String[] roles = TokenUtil.getRoles(strToken, key);
            int version = TokenUtil.getVersion(strToken, key);
            if (name != null && roles.length != 0 && version != -1) {
                User user = null;
                try {
                    user = uService.getUserByUsername(name);
                } catch (EntityNotFoundException e) {
                    logger.info("User not found " + name);
                }
                if (user != null) {
                    if (user.getVersion() == version && user.getRoles() != null &&
                            Arrays.asList(user.getRoles()).containsAll(Arrays.asList(roles))) {
                        requestContext.setSecurityContext(new SecurityContextAuthorizer(uriInfo, () -> name, roles));
                        return;
                    } else {
                        logger.info("Version or roles did not match the token");
                    }
                } else {
                    logger.info("User not found");
                }
            } else {
                logger.info("name, roles or version missing from token");
            }
        } else {
            logger.info("token is invalid");
        }
        throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
}