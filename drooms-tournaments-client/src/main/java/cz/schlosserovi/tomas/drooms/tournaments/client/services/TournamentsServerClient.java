package cz.schlosserovi.tomas.drooms.tournaments.client.services;

import java.util.HashMap;
import java.util.Map;

import org.jboss.resteasy.client.ProxyFactory;

public class TournamentsServerClient {
    private Map<Class<?>, Object> services = new HashMap<>();
    private String server;
    private MyClientExecutor executor = new MyClientExecutor();

    public TournamentsServerClient(String server) {
        if (!server.startsWith("http")) {
            server = "https://" + server;
        }
        this.server = server;
    }

    public <T> T getService(Class<T> clazz) {
        if (!services.containsKey(clazz)) {
            services.put(clazz, ProxyFactory.create(clazz, server, executor));
        }

        return clazz.cast(services.get(clazz));
    }

    public void login(String credentials) {
        executor.setCredentials(credentials);
    }

    public void login(String username, String password) {
        executor.setCredentials(username, password);
    }

    public void logout() {
        executor.setCredentials(null);
    }

    public boolean isLoggedIn() {
        return executor.isAuthentication();
    }

    public String getLoogedInUser() {
        return executor.getUsername();
    }
}
