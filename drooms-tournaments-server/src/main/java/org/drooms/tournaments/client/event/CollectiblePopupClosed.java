package org.drooms.tournaments.client.event;

import org.drooms.tournaments.client.util.Collectible;

public class CollectiblePopupClosed {
    private final Collectible collectible;

    public CollectiblePopupClosed(Collectible collectible) {
        this.collectible = collectible;
    }

    public Collectible getCollectible() {
        return collectible;
    }
}
