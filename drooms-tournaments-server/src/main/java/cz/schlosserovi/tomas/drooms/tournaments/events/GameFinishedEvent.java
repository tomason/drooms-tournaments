package cz.schlosserovi.tomas.drooms.tournaments.events;

import cz.schlosserovi.tomas.drooms.tournaments.model.GameEntity;

public class GameFinishedEvent {
    private final GameEntity game;

    public GameFinishedEvent(GameEntity game) {
        this.game = game;
    }

    public GameEntity getGame() {
        return game;
    }

}
