package cz.schlosserovi.tomas.drooms.tournaments.client;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;
import org.jboss.resteasy.util.GenericType;

import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Strategy;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Tournament;
import cz.schlosserovi.tomas.drooms.tournaments.domain.User;
import cz.schlosserovi.tomas.drooms.tournaments.services.PlaygroundService;
import cz.schlosserovi.tomas.drooms.tournaments.services.RegistrationService;
import cz.schlosserovi.tomas.drooms.tournaments.services.UserService;

public class TournamentsServerClient {
    private UserService userService;
    private RegistrationService registrationService;
    private PlaygroundService playgroundService;

    private String userName;
    private String hash;

    public TournamentsServerClient(String server) {
        StringBuilder sb = new StringBuilder();
        if (!server.startsWith("http")) {
            sb.append("https://");
        }
        sb.append(server);

        HttpClient client = new DefaultHttpClient();

        ClientExecutor executor = new ApacheHttpClient4Executor(client) {
            @Override
            public ClientResponse<?> execute(ClientRequest request) throws Exception {
                if (hash != null) {
                    request.header("Authorization", "BASIC " + hash);
                }

                return super.execute(request);
            }
        };

        userService = ProxyFactory.create(UserService.class, sb.toString(), executor);
        registrationService = ProxyFactory.create(RegistrationService.class, sb.toString(), executor);
        playgroundService = ProxyFactory.create(PlaygroundService.class, sb.toString(), executor);
    }

    public void register(String userName, String password) {
        if (isLoggedIn()) {
            logout();
        }
        ClientResponse<?> response = null;
        try {
            response = (ClientResponse<?>) registrationService.register(new User(userName, password));
            if (response.getStatus() != Status.OK.getStatusCode()) {
                throw new ResponseException(response.getEntity(String.class));
            }
        } finally {
            if (response != null) {
                response.releaseConnection();
            }
        }

        login(userName, password);
    }

    public boolean login(String userName, String password) {
        this.userName = userName;
        this.hash = new String(Base64.encodeBase64((userName + ":" + password).getBytes(StandardCharsets.US_ASCII)));

        boolean result = false;

        ClientResponse<?> response = null;
        try {
            response = (ClientResponse<?>) userService.getPlaygrounds();
            if (response.getStatus() == 200) {
                result = true;
            } else {
                this.userName = null;
                this.hash = null;
            }
        } finally {
            if (response != null) {
                response.releaseConnection();
            }
        }

        return result;
    }

    public void logout() {
        userName = null;
        hash = null;
    }

    public boolean isLoggedIn() {
        return hash != null;
    }

    public Object getLoogedInUser() {
        if (isLoggedIn()) {
            return userName;
        } else {
            return "not logged in";
        }
    }

    public List<Strategy> getStrategies() {
        ClientResponse<?> response = null;
        try {
            response = (ClientResponse<?>) userService.getStrategies();
            if (response.getStatus() != Status.OK.getStatusCode()) {
                throw new ResponseException(response.getEntity(String.class));
            }
            return response.getEntity(new GenericType<List<Strategy>>() {
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
            response = (ClientResponse<?>) userService.activateStrategy(strategy);
            if (response.getStatus() != Status.OK.getStatusCode()) {
                throw new ResponseException(response.getEntity(String.class));
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
            response = (ClientResponse<?>) userService.newStrategy(new Strategy(groupId, artifactId, version));
            if (response.getStatus() != Status.OK.getStatusCode()) {
                throw new ResponseException(response.getEntity(String.class));
            }
        } finally {
            if (response != null) {
                response.releaseConnection();
            }
        }
    }

    public List<Playground> getPlaygrounds() {
        ClientResponse<?> response = null;
        try {
            response = (ClientResponse<?>) userService.getPlaygrounds();
            if (response.getStatus() != Status.OK.getStatusCode()) {
                throw new ResponseException(response.getEntity(String.class));
            }
            return response.getEntity(new GenericType<List<Playground>>() {
            });
        } finally {
            if (response != null) {
                response.releaseConnection();
            }
        }
    }
    
//    public List<Playground> getPlaygrounds(Tournament tournament) {
//        ClientResponse<?> response = null;
//        try {
//            response = (ClientResponse<?>) playgroundService.getPlaygrounds(tournament);
//            if (response.getStatus() != Status.OK.getStatusCode()) {
//                throw new ResponseException(response.getEntity(String.class));
//            }
//            return response.getEntity(new GenericType<List<Playground>>() {
//            });
//        } finally {
//            if (response != null) {
//                response.releaseConnection();
//            }
//        }
//    }

    public void newPlayground(Playground playground) {
        ClientResponse<?> response = null;
        try {
            response = (ClientResponse<?>) userService.newPlayground(playground);
            if (response.getStatus() != Status.OK.getStatusCode()) {
                throw new ResponseException(response.getEntity(String.class));
            }
        } finally {
            if (response != null) {
                response.releaseConnection();
            }
        }
    }

    public void configurePlayground(Playground playground) {
        ClientResponse<?> response = null;
        try {
            response = (ClientResponse<?>) userService.configurePlayground(playground);
            if (response.getStatus() != Status.OK.getStatusCode()) {
                throw new ResponseException(response.getEntity(String.class));
            }
        } finally {
            if (response != null) {
                response.releaseConnection();
            }
        }
    }

    public void newTournament(Tournament tournament) {
        ClientResponse<?> response = null;
        try {
            response = (ClientResponse<?>) userService.newTournament(tournament);
            if (response.getStatus() != Status.OK.getStatusCode()) {
                throw new ResponseException(response.getEntity(String.class));
            }
        } finally {
            if (response != null) {
                response.releaseConnection();
            }
        }
    }
    
    public List<Tournament> getTournaments() {
        ClientResponse<?> response = null;
        try {
            response = (ClientResponse<?>) userService.getTournaments();
            if (response.getStatus() != Status.OK.getStatusCode()) {
                throw new ResponseException(response.getEntity(String.class));
            }

            return response.getEntity(new GenericType<List<Tournament>>() {
            });
        } finally {
            if (response != null) {
                response.releaseConnection();
            }
        }
    }

    public void joinTournament(Tournament tournament) {
        ClientResponse<?> response = null;
        try {
            response = (ClientResponse<?>) userService.joinTournament(tournament);
            if (response.getStatus() != Status.OK.getStatusCode()) {
                throw new ResponseException(response.getEntity(String.class));
            }
        } finally {
            if (response != null) {
                response.releaseConnection();
            }
        }
    }

    public List<Playground> getAllPlaygrounds() {
        ClientResponse<?> response = null;
        try {
            response = (ClientResponse<?>) playgroundService.getPlaygrounds();
            if (response.getStatus() != Status.OK.getStatusCode()) {
                throw new ResponseException(response.getEntity(String.class));
            }
            return response.getEntity(new GenericType<List<Playground>>() {
            });
        } finally {
            if (response != null) {
                response.releaseConnection();
            }
        }
    }
}
