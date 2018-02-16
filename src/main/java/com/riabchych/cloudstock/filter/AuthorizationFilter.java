package com.riabchych.cloudstock.filter;

import com.riabchych.cloudstock.annotation.Secured;
import com.riabchych.cloudstock.entity.EnumRole;
import com.riabchych.cloudstock.entity.Role;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) {

        Class<?> resourceClass = resourceInfo.getResourceClass();
        List<Role> classRoles = extractRoles(resourceClass);

        Method resourceMethod = resourceInfo.getResourceMethod();
        List<Role> methodRoles = extractRoles(resourceMethod);

        try {
            if (methodRoles.isEmpty()) {
                checkPermissions(classRoles);
            } else {
                checkPermissions(methodRoles);
            }

        } catch (Exception e) {
            requestContext.abortWith(
                    Response.status(Response.Status.FORBIDDEN).build());
        }
    }

    private List<Role> extractRoles(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            return new ArrayList<>();
        } else {
            Secured secured = annotatedElement.getAnnotation(Secured.class);
            if (secured == null) {
                return new ArrayList<>();
            } else {
                return Arrays.stream(secured.value())
                        .map(Role::new)
                        .collect(Collectors.toList());
            }
        }
    }

    private void checkPermissions(List<Role> allowedRoles) throws Exception {
        if (!allowedRoles.isEmpty()) {
            if (hasRole(allowedRoles, EnumRole.ROLE_ADMIN))
                throw new Exception("Do not have user permission");
            if (hasRole(allowedRoles, EnumRole.ROLE_USER))
                throw new Exception("Do not have admin permission");
            if (hasRole(allowedRoles, EnumRole.ROLE_OWNER))
                throw new Exception("Do not have admin permission");
        }

    }

    private boolean hasRole(List<Role> allowedRoles, EnumRole role) {
        for (Role irole : allowedRoles) {
            if (irole.getName() == role)
                return false;
        }
        return true;
    }
}
