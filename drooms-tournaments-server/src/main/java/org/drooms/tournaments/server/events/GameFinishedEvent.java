package org.drooms.tournaments.server.events;

import org.drooms.tournaments.server.data.model.GameEntity;

public class GameFinishedEvent {
    private final GameEntity game;

    public GameFinishedEvent(GameEntity game) {
        this.game = game;
    }

    public GameEntity getGame() {
        return game;
    }

}
