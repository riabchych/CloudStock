package com.riabchych.cloudstock.filter;

import com.riabchych.cloudstock.annotation.Secured;
import com.riabchych.cloudstock.entity.User;
import com.riabchych.cloudstock.security.SecurityContextAuthorizer;
import com.riabchych.cloudstock.service.UserServiceImpl;
import com.riabchych.cloudstock.util.TokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.glassfish.jersey.server.ContainerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Secured
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private final TokenUtil tokenUtil;

    @Context
    private javax.inject.Provider<UriInfo> uriInfo;

    private final static Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());

    private final UserServiceImpl userService;

    @Autowired
    public AuthenticationFilter(UserServiceImpl userService, TokenUtil tokenUtil) {
        this.userService = userService;
        this.tokenUtil = tokenUtil;
    }

    private static String extractJwtTokenFromAuthorizationHeader(String auth) {
        return auth.replaceFirst("[B|b][E|e][A|a][R|r][E|e][R|r] ", "").replace(" ", "");
    }

    /**
     * Filter method called before a request has been dispatched to a resource.
     * <p>
     * <p>
     * Filters in the filter chain are ordered according to their {@code javax.annotation.Priority}
     * class-level annotation value.
     * If a request filter produces a response by calling {@link ContainerRequestContext#abortWith}
     * method, the execution of the (either pre-match or post-match) request filter
     * chain is stopped and the response is passed to the corresponding response
     * filter chain (either pre-match or post-match). For example, a pre-match
     * caching filter may produce a response in this way, which would effectively
     * skip any post-match request filters as well as post-match response filters.
     * Note however that a responses produced in this manner would still be processed
     * by the pre-match response filter chain.
     * </p>
     *
     * @param requestContext request context.
     * @see PreMatching
     */
    @Override
    public void filter(ContainerRequestContext requestContext) {

        String method = requestContext.getMethod().toLowerCase();
        String path = ((ContainerRequest) requestContext).getPath(true).toLowerCase();

        if (("get".equals(method) && ("application.wadl".equals(path) || "application.wadl/xsd0.xsd".equals(path)))
                || ("post".equals(method) && "authentication".equals(path))) {
            // pass through the filter.
            requestContext.setSecurityContext(new SecurityContextAuthorizer(uriInfo, () -> "anonymous", new String[]{"anonymous"}));
            return;
        }

        final String header = requestContext.getHeaderString("authorization");

        logger.log(Level.FINE, String.format("%s %s", requestContext.getMethod(), ((ContainerRequest) requestContext).getRequestUri()));
        if ((header == null) || !header.startsWith("Bearer ")) {
            logger.info("Missing or invalid Authorization header");
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        String username;
        String authToken = extractJwtTokenFromAuthorizationHeader(header);

        try {
            username = tokenUtil.getUsernameFromToken(authToken);

            logger.info("checking authentication for user " + username);
            if (username != null) {
                User user = null;
                try {
                    user = userService.getUserByUsername(username);

                    if (tokenUtil.validateToken(authToken, user)) {
                        try {
                            String[] roles = tokenUtil.getAudienceFromToken(authToken).split(",");
                            if (user.getRoles() != null && Arrays.asList(user.getRolesString()).containsAll(Arrays.asList(roles))) {
                                requestContext.setSecurityContext(new SecurityContextAuthorizer(uriInfo, () -> username, roles));
                                logger.info("authenticated user " + username + ", setting security context");
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (EntityNotFoundException e) {
                    logger.info("User not found " + user);
                }
            }
        } catch (SignatureException e) {
            logger.info("Invalid JWT signature.");
            logger.log(Level.FINEST, "Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {
            logger.info("Invalid JWT token.");
            logger.log(Level.FINEST, "Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {
            logger.info("Expired JWT token.");
            logger.log(Level.FINEST, "Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            logger.info("Unsupported JWT token.");
            logger.log(Level.FINEST, "Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            logger.info("JWT token compact of handler are invalid.");
            logger.log(Level.FINEST, "JWT token compact of handler are invalid trace: {}", e);
        }

        throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
}