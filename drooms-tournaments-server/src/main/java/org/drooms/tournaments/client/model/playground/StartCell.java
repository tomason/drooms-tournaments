package org.drooms.tournaments.client.model.playground;

import org.drooms.tournaments.client.model.Coordinates;
import org.vectomatic.dom.svg.OMSVGCircleElement;
import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.utils.SVGConstants;

public class StartCell extends PlaygroundCell<OMSVGCircleElement> {
    static StartCell newStartPosition(OMSVGDocument document, Coordinates coordinates) {
        OMSVGCircleElement element = document.createSVGCircleElement(coordinates.getCol() * CELL_SIZE + CELL_SIZE / 2,
                coordinates.getRow() * CELL_SIZE + CELL_SIZE / 2, CELL_SIZE / 2);
        element.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_GREEN_VALUE);

        return new StartCell(coordinates, element);
    }

    public StartCell(Coordinates coordinates, OMSVGCircleElement element) {
        super(coordinates, element);
    }

    @Override
    public char getSign() {
        return '@';
    }
}
