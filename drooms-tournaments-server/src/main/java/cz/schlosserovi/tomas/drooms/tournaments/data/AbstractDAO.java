package cz.schlosserovi.tomas.drooms.tournaments.data;

import javax.inject.Inject;
import javax.persistence.EntityManager;

abstract class AbstractDAO {
    @Inject
    protected EntityManager em;
}
