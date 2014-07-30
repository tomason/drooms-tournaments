package org.drooms.tournaments.client.model;

import org.vectomatic.dom.svg.OMSVGElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;

abstract class PlaygroundCell<T extends OMSVGElement> {
    protected static final int CELL_SIZE = 10;

    private final T element;
    private final Coordinates coordinates;

    protected PlaygroundCell(Coordinates coordinates, T element) {
        this.coordinates = coordinates;
        this.element = element;
    }

    public void addToCanvas(OMSVGSVGElement canvas) {
        canvas.appendChild(element);
    }

    public void removeFromCanvas(OMSVGElement canvas) {
        canvas.removeChild(element);
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public abstract char getSign();
}
