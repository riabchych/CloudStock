package com.riabchych.cloudstock.security;


import com.riabchych.cloudstock.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CrossOriginConfiguration {

    private final String allowedOrigins;

    private final String allowedMethods;

    private final String allowedHeaders;

    private final String exposedHeaders;

    private final String allowCredentials;

    public CrossOriginConfiguration(String allowedOrigins, String allowedMethods, String allowedHeaders, String exposedHeaders, String allowCredentials) {
        this.allowedOrigins = allowedOrigins;
        this.allowedMethods = allowedMethods;
        this.allowedHeaders = allowedHeaders;
        this.exposedHeaders = exposedHeaders;
        this.allowCredentials = allowCredentials;
    }

    public String getAllowedOrigins() {
        return allowedOrigins;
    }

    public String getAllowedMethods() {
        return allowedMethods;
    }

    public String getAllowedHeaders() {
        return allowedHeaders;
    }

    public String getExposedHeaders() {
        return exposedHeaders;
    }

    public String getAllowCredentials() {
        return allowCredentials;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private List<String> allowedOrigins = new ArrayList<>();
        private List<String> allowedMethods = new ArrayList<>();
        private List<String> allowedHeaders = new ArrayList<>();
        private List<String> exposedHeaders = new ArrayList<>();
        private String allowCredentials;

        Builder() {
            // Set Default Values:
            allowedOrigins.addAll(Collections.singletonList("*"));
            allowedMethods.addAll(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
            exposedHeaders.addAll(Arrays.asList("Cache-Control", "Content-Language", "Content-Type", "Expires", "Last-Modified", "Pragma"));
            allowCredentials = "true";
        }

        public Builder allowedOrigins(List<String> origins) {
            this.allowedOrigins = origins;

            return this;
        }

        public Builder allowedOrigin(String origin) {
            allowedOrigins.add(origin);

            return this;
        }

        public Builder allowedMethods(List<String> methods) {
            this.allowedMethods = methods;

            return this;
        }

        public Builder allowedMethod(String method) {
            this.allowedMethods.add(method);

            return this;
        }

        public Builder allowedHeaders(List<String> headers) {
            this.allowedHeaders = headers;

            return this;
        }

        public Builder allowedHeader(String header) {
            this.allowedHeaders.add(header);

            return this;
        }

        public Builder allowCredentials(boolean allowCredentials) {
            this.allowCredentials = allowCredentials ? "true" : "false";

            return this;
        }


        public Builder exposedHeaders(List<String> headers) {
            this.exposedHeaders = headers;

            return this;
        }

        public Builder exposedHeader(String header) {
            this.exposedHeaders.add(header);

            return this;
        }

        public CrossOriginConfiguration build() {
            return new CrossOriginConfiguration(
                    getCommaSeparatedString(allowedOrigins),
                    getCommaSeparatedString(allowedMethods),
                    getCommaSeparatedString(allowedHeaders),
                    getCommaSeparatedString(exposedHeaders),
                    allowCredentials
            );
        }

        private static String getCommaSeparatedString(List<String> source) {
            return StringUtils.getDelimitedString(source, ", ");
        }
    }

}