package cz.schlosserovi.tomas.drooms.tournaments.client;

public class ResponseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ResponseException() {
        super();
    }

    public ResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResponseException(String message) {
        super(message);
    }

    public ResponseException(Throwable cause) {
        super(cause);
    }

}
