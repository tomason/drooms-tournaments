package cz.schlosserovi.tomas.drooms.tournaments.client.services;

import org.jboss.resteasy.client.ProxyFactory;

class AbstractServiceClient<T> {
    protected final T service;
    private final MyClientExecutor clientExecutor;

    public AbstractServiceClient(Class<T> clazz, String server) {
        clientExecutor = new MyClientExecutor();
        service = ProxyFactory.create(clazz, server, clientExecutor);
    }

    public void login(String username, String password) {
        clientExecutor.setCredentials(username, password);
    }

    public void login(String credentials) {
        clientExecutor.setCredentials(credentials);
    }

    public boolean isLoggedIn() {
        return clientExecutor.isAuthentication();
    }

    
}
