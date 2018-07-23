package com.meksula.chat.client.logic.web.exchange;

import com.meksula.chat.client.logic.web.ApiPath;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @Author
 * Karol Meksuła
 * 23-07-2018
 * */

//TODO zrób singletona !!!

public class HttpServerConnectorImpl<T> implements HttpServerConnector<T> {
    private Class<T> type;
    private ClientConfig clientConfig = new ClientConfig();

    public HttpServerConnectorImpl(Class<T> entityType) {
        this.type = entityType;
    }

    @Override
    public String login(String username, String password) {
        this.clientConfig = buildConfiguration(username, password);

        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget webTarget = client.target(ApiPath.LOGIN.getPath());

        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder
                .cookie("param1","value1")
                .cookie(new Cookie("param2", "value2"))
                .get();

        return response.getStatusInfo().toString();
    }

    @Override
    public T post(Object entity, ApiPath apiPath) {
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget webTarget = client.target(apiPath.getPath());

        Invocation.Builder inv = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = inv.post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE));

        return response.readEntity(type);
    }

    @Override
    public T put() {
        return null;
    }

    @Override
    public T get(ApiPath apiPath) {
        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget webTarget = client.target(apiPath.getPath());

        Invocation.Builder inv = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = inv.get();

        return response.readEntity(type);
    }

    private ClientConfig buildConfiguration(String username, String password) {
        ClientConfig clientConfig = new ClientConfig();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
        clientConfig.register(feature);
        clientConfig.register(JacksonFeature.class);
        return clientConfig;
    }

}
