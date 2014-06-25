package org.drooms.tournaments.client.event;

public class AuthEvent {
    public enum AuthEventType {
        LOGGED_IN, LOGGED_OUT, FAILED_AUTH
    }

    private final AuthEventType type;

    public AuthEvent(AuthEventType type) {
        this.type = type;
    }

    public AuthEventType getType() {
        return type;
    }
}
