package cz.schlosserovi.tomas.drooms.tournaments.client.batch;

import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.InteractiveClient;

/**
 * Client in batch mode allows running the same commands as an
 * {@link InteractiveClient} from the command line (e.g. adding playgrounds).
 * 
 * This is not yet implemented, so caling it will only throw {@link UnsupportedOperationException}.
 */
public class BatchClient {

    public BatchClient(String[] args) {

    }

    public void execute() {
        throw new UnsupportedOperationException("Batch client mode is not yet implemented.");
    }
}
