package com.bluemangoose.client.logic.web.exchange;

import com.bluemangoose.client.controller.cache.SessionCache;
import com.bluemangoose.client.logic.web.ApiPath;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.image.Image;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import javax.imageio.ImageIO;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * @Author
 * Karol Meksuła
 * 23-07-2018
 * */

public class HttpServerConnectorImpl<T> implements HttpServerConnector<T> {
    private Class<T> type;
    public static ClientConfig clientConfig;
    public static String session;

    public HttpServerConnectorImpl(Class<T> entityType) {
        this.type = entityType;
    }

    @Override
    public String login(String username, String password) {
        clientConfig = buildConfiguration(username, password);

        Response response = clientPrepare(ApiPath.LOGIN)
                .cookie("param1","value1")
                .cookie(new Cookie("param2", "value2"))
                .get();

        session = response.getCookies().get("JSESSIONID").toCookie().getValue();

        return response.readEntity(String.class);
    }

    @Override
    public T post(Object entity, ApiPath apiPath) {
        Response response = clientPrepare(apiPath).post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE));
        return response.readEntity(type);
    }

    @Override
    public T put() {
        return null;
    }

    @Override
    public T get(ApiPath apiPath) {
        Response response = clientPrepare(apiPath).get();
        return response.readEntity(type);
    }

    @Override
    public T putFile(ApiPath apiPath, File picture) {
        final String contentType = "multipart/mixed";

        Client client = ClientBuilder.newBuilder().
                register(MultiPartFeature.class).withConfig(HttpServerConnectorImpl.clientConfig).build();
        WebTarget target = client.target(apiPath.getPath());
        target.register(MultiPartFeature.class);

        MultiPart multiPart = new MultiPart();

        FileDataBodyPart imgBodyPart = new FileDataBodyPart("pic", picture,
                MediaType.APPLICATION_OCTET_STREAM_TYPE);

        multiPart.bodyPart(imgBodyPart);

        Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(multiPart, contentType));

        return response.readEntity(type);
    }

    @Override
    public Image getImage(ApiPath avatarGet) {
        Client client = ClientBuilder.newBuilder().withConfig(clientConfig).build();
        WebTarget webTarget = client.target(avatarGet.getPath());
        Response response = webTarget.request(MediaType.APPLICATION_OCTET_STREAM).get();
        byte[] bytes = response.readEntity(byte[].class);

        InputStream inputStream = new ByteArrayInputStream(bytes);
        return new Image(inputStream);
    }

    private Invocation.Builder clientPrepare(ApiPath apiPath) {
        if (clientConfig == null) {
            clientConfig = new ClientConfig();
        }

        Client client = ClientBuilder.newBuilder().withConfig(clientConfig).build();
        WebTarget webTarget = client.target(apiPath.getPath());

        return webTarget.request(MediaType.APPLICATION_JSON_TYPE);
    }

    private ClientConfig buildConfiguration(String username, String password) {
        ClientConfig clientConfig = new ClientConfig();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
        clientConfig.register(feature);
        clientConfig.register(JacksonFeature.class);
        return clientConfig;
    }

}
