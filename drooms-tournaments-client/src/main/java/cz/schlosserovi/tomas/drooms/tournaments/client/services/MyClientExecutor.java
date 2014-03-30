package cz.schlosserovi.tomas.drooms.tournaments.client.services;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

class MyClientExecutor extends ApacheHttpClient4Executor {
    private String credentials;
    private String username;

    public void setCredentials(String username, String password) {
        this.username = username;

        byte[] base = (username + ":" + password).getBytes(StandardCharsets.US_ASCII);
        this.credentials = new String(Base64.encodeBase64(base), StandardCharsets.US_ASCII);
    }

    public void setCredentials(String credentials) {
        if (credentials != null) {
            String decoded = new String(Base64.decodeBase64(credentials), StandardCharsets.US_ASCII);
            this.username = decoded.substring(0, decoded.indexOf(':'));
        } else {
            this.username = null;
        }
        this.credentials = credentials;
    }

    public boolean isAuthentication() {
        return credentials != null;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public ClientResponse<?> execute(ClientRequest request) throws Exception {
        if (credentials != null) {
            request.header("Authorization", "BASIC " + credentials);
        }

        return super.execute(request);
    }

}
