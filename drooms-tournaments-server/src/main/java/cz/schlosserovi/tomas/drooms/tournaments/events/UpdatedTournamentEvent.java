package cz.schlosserovi.tomas.drooms.tournaments.events;

import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentEntity;

public class UpdatedTournamentEvent extends TournamentEvent {

    public UpdatedTournamentEvent(TournamentEntity tournament) {
        super(tournament);
    }

}
