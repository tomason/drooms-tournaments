package org.drooms.tournaments.client.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.drooms.tournaments.client.event.AuthEvent;
import org.drooms.tournaments.client.event.AuthEvent.AuthEventType;
import org.drooms.tournaments.services.StrategyService;
import org.jboss.errai.common.client.util.Base64Util;
import org.jboss.errai.enterprise.client.jaxrs.api.ResponseCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.enterprise.client.jaxrs.api.RestErrorCallback;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.DateTimeFormat;

@ApplicationScoped
public class ApplicationContext {
    public static DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd");

    private String userName;
    private String digest;

    @Inject
    private Event<AuthEvent> authEvents;

    public void login(String username, String password) {
        this.userName = username;
        byte[] data = (username + ":" + password).getBytes();
        digest = Base64Util.encode(data, 0, data.length);

        RestClient.create(StrategyService.class, new ResponseCallback() {
            @Override
            public void callback(Response response) {
                authEvents.fire(new AuthEvent(AuthEventType.LOGGED_IN));
            }
        }, new RestErrorCallback() {
            @Override
            public boolean error(Request message, Throwable throwable) {
                digest = null;
                userName = null;

                authEvents.fire(new AuthEvent(AuthEventType.FAILED_AUTH));
                return false;
            }
        }).getUserStrategies();
    }

    public void logout() {
        userName = null;
        digest = null;

        authEvents.fire(new AuthEvent(AuthEventType.LOGGED_OUT));
    }

    public boolean isLoggedIn() {
        return userName != null && digest != null;
    }

    String getDigest() {
        return digest;
    }

    public String getUsername() {
        return userName;
    }
}
