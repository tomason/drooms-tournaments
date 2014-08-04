package org.drooms.tournaments.client.model.playground;

import org.drooms.tournaments.client.model.Coordinates;
import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.utils.SVGConstants;

class WallCell extends PlaygroundCell<OMSVGRectElement> {
    static WallCell newWall(OMSVGDocument document, Coordinates coordinates) {
        OMSVGRectElement element = document.createSVGRectElement(coordinates.getCol() * CELL_SIZE, coordinates.getRow()
                * CELL_SIZE, CELL_SIZE, CELL_SIZE, 0, 0);
        element.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "url(#brick)");

        return new WallCell(coordinates, element);
    }

    public WallCell(Coordinates coordinates, OMSVGRectElement element) {
        super(coordinates, element);
    }

    @Override
    public char getSign() {
        return '#';
    }
}
