package cz.schlosserovi.tomas.drooms.tournaments.client;

import java.util.Collection;

import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.util.GenericType;

import cz.schlosserovi.tomas.drooms.tournaments.domain.GAV;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Strategy;
import cz.schlosserovi.tomas.drooms.tournaments.services.UserService;

public class UserServiceClient {
    private UserService service;
    private String authToken;
    private String userName;

    public UserServiceClient(String server) {
        StringBuilder sb = new StringBuilder();
        if (!server.startsWith("http")) {
            sb.append("https://");
        }
        sb.append(server);
        if (!server.endsWith("/services/users")) {
            if (!server.endsWith("/")) {
                sb.append("/");
            }
            sb.append("services/users");
        }
        service = ProxyFactory.create(UserService.class, sb.toString());
    }

    public void register(String userName, byte[] password) {
        if (authToken != null) {
            logout();
        }
        ClientResponse<?> response = null;
        try {
            response = (ClientResponse<?>) service.register(userName, password);
            if (response.getStatus() != Status.OK.getStatusCode()) {
                throw new RuntimeException(response.getEntity(String.class));
            }
        } finally {
            if (response != null) {
                response.releaseConnection();
            }
        }

        login(userName, password);
    }

    public boolean login(String userName, byte[] password) {
        if (authToken != null) {
            logout();
        }
        ClientResponse<?> response = null;
        try {
            response = (ClientResponse<?>) service.login(userName, password);
            switch (response.getStatus()) {
            case 200:
                this.userName = userName;
                authToken = response.getEntity(String.class);
                return true;
            case 401:
                return false;
            default:
                throw new RuntimeException(response.getEntity(String.class));
            }
        } finally {
            if (response != null) {
                response.releaseConnection();
            }
        }
    }

    public void logout() {
        if (authToken == null) {
            throw new IllegalStateException("Nobody is logged in");
        }
        ClientResponse<?> response = null;
        try {
            response = (ClientResponse<?>) service.logout(authToken);
            if (response.getStatus() != Status.OK.getStatusCode()) {
                throw new RuntimeException(response.getEntity(String.class));
            }
            authToken = null;
            userName = null;
        } finally {
            if (response != null) {
                response.releaseConnection();
            }
        }

    }

    public boolean isLoggedIn() {
        return authToken != null;
    }

    public Object getLoogedInUser() {
        if (isLoggedIn()) {
            return userName;
        } else {
            return "";
        }
    }

    public Collection<Strategy> getStrategies() {
        ClientResponse<?> response = null;
        try {
            response = (ClientResponse<?>) service.getStrategies(authToken);
            if (response.getStatus() != Status.OK.getStatusCode()) {
                throw new RuntimeException(response.getEntity(String.class));
            }
            return response.getEntity(new GenericType<Collection<Strategy>>() {
            });
        } finally {
            if (response != null) {
                response.releaseConnection();
            }
        }
    }

    public void setActiveStrategy(Strategy strategy) {
        ClientResponse<?> response = null;
        try {
            response = (ClientResponse<?>) service.activateStrategy(authToken, strategy.getGav());
            if (response.getStatus() != Status.OK.getStatusCode()) {
                throw new RuntimeException(response.getEntity(String.class));
            }
        } finally {
            if (response != null) {
                response.releaseConnection();
            }
        }
    }

    public void newStrategy(String groupId, String artifactId, String version) {
        ClientResponse<?> response = null;
        try {
            GAV gav = new GAV(groupId, artifactId, version);
            response = (ClientResponse<?>) service.newStrategy(authToken, gav);
            if (response.getStatus() != Status.OK.getStatusCode()) {
                throw new RuntimeException(response.getEntity(String.class));
            }
        } finally {
            if (response != null) {
                response.releaseConnection();
            }
        }
    }

    public Collection<Playground> getPlaygrounds() {
        ClientResponse<?> response = null;
        try {
            response = (ClientResponse<?>) service.getPlaygrounds(authToken);
            if (response.getStatus() != Status.OK.getStatusCode()) {
                throw new RuntimeException(response.getEntity(String.class));
            }
            return response.getEntity(new GenericType<Collection<Playground>>() {
            });
        } finally {
            if (response != null) {
                response.releaseConnection();
            }
        }
    }
}
