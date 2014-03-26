package cz.schlosserovi.tomas.drooms.tournaments;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceProvider.class);
    private static final Path artifactStorage;

    static {
        String dataDir = System.getenv("OPENSHIFT_DATA_DIR");
        if (dataDir == null) {
            dataDir = System.getProperty("user.home");
        }
        artifactStorage = Paths.get(dataDir, "artifacts");
        if (!Files.exists(artifactStorage)) {
            try {
                Files.createDirectory(artifactStorage);
            } catch (IOException ex) {
                LOGGER.error("Unable to create artifact storage", ex);
            }
        }
    }

    @Produces
    @PersistenceContext(name = "tournamentDS")
    private EntityManager entityManager;

    @Produces
    public Path getArtifactStorage() {
        return artifactStorage;
    }
}
