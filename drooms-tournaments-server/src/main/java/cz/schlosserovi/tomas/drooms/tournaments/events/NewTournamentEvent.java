package cz.schlosserovi.tomas.drooms.tournaments.events;

import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentEntity;

public class NewTournamentEvent extends TournamentEvent {

    public NewTournamentEvent(TournamentEntity tournament) {
        super(tournament);
    }
}
