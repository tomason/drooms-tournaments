package cz.schlosserovi.tomas.drooms.tournaments;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ResourceProvider {

    @Produces
    @PersistenceContext(name = "tournamentDS")
    private EntityManager entityManager;

}
