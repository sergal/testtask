package ru.sergal.testtask.restapi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

public class Application extends ResourceConfig {
    public Application() {
        packages(ru.sergal.testtask.restapi.Application.class.getPackage().getName());
        property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, Boolean.TRUE);

        JacksonJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider();
        jacksonJsonProvider.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        register(jacksonJsonProvider);
    }
}
