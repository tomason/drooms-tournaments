package org.drooms.tournaments.client.model.report;

import org.drooms.tournaments.client.model.Coordinates;

public class Collectible {
    private final Coordinates position;
    private final int value;

    public Collectible(Coordinates position, int value) {
        this.position = position;
        this.value = value;
    }

    public Coordinates getPosition() {
        return position;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((position == null) ? 0 : position.hashCode());
        result = prime * result + value;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Collectible other = (Collectible) obj;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        if (value != other.value)
            return false;
        return true;
    }

}
