package org.drooms.tournaments.client.model.playground;

import org.drooms.tournaments.client.model.Coordinates;
import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGLineElement;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.utils.SVGConstants;

public class PortalCell extends PlaygroundCell<OMSVGRectElement> {
    private PortalCell other;
    private OMSVGLineElement line;
    private final char sign;

    public static OMSVGRectElement newPortalElement(OMSVGDocument document, Coordinates coordinates) {
        OMSVGRectElement element = document.createSVGRectElement(coordinates.getCol() * CELL_SIZE, coordinates.getRow()
                * CELL_SIZE, CELL_SIZE, CELL_SIZE, 0, 0);
        element.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_LIGHTBLUE_VALUE);

        return element;
    }

    public static OMSVGLineElement newPortalLine(OMSVGDocument document, Coordinates start, Coordinates end) {
        OMSVGLineElement element = document
                .createSVGLineElement(start.getCol() * CELL_SIZE + CELL_SIZE / 2, start.getRow() * CELL_SIZE + CELL_SIZE / 2,
                        end.getCol() * CELL_SIZE + CELL_SIZE / 2, end.getRow() * CELL_SIZE + CELL_SIZE / 2);
        element.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_NONE_VALUE);
        element.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, SVGConstants.CSS_LIGHTBLUE_VALUE);
        element.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "3");

        return element;
    }

    static PortalCell newPortal(OMSVGDocument document, Coordinates start, Coordinates end, char sign) {
        PortalCell result = new PortalCell(start, newPortalElement(document, start), sign);
        result.setOther(new PortalCell(end, newPortalElement(document, end), sign));
        result.setLine(newPortalLine(document, start, end));
        result.getOther().setLine(result.getLine());

        return result;
    }

    private PortalCell(Coordinates coordinates, OMSVGRectElement element, char sign) {
        super(coordinates, element);
        this.sign = sign;
    }

    @Override
    public char getSign() {
        return sign;
    }

    public PortalCell getOther() {
        return other;
    }

    public OMSVGLineElement getLine() {
        return line;
    }

    protected void setOther(PortalCell other) {
        this.other = other;
        other.other = this;

        if (this.line != null) {
            other.line = line;
        }
    }

    public void setLine(OMSVGLineElement line) {
        this.line = line;
        if (other != null) {
            other.line = line;
        }
    }

}
