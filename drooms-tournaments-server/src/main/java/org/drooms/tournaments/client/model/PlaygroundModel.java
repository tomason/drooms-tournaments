package org.drooms.tournaments.client.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;

import org.vectomatic.dom.svg.OMSVGDefsElement;
import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGElement;
import org.vectomatic.dom.svg.OMSVGLineElement;
import org.vectomatic.dom.svg.OMSVGMatrix;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGPatternElement;
import org.vectomatic.dom.svg.OMSVGPoint;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.utils.OMSVGParser;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;

public class PlaygroundModel {
    private final OMSVGDocument document = OMSVGParser.createDocument();
    private final OMSVGSVGElement canvas = document.createSVGSVGElement();
    private final OMSVGRectElement grid;
    private final Queue<Character> portalCharacters = new LinkedList<Character>();
    private final List<CanvasEventHandler> eventHandlers = new LinkedList<CanvasEventHandler>();
    private final SortedMap<Coordinates, PlaygroundCell<?>> source = new TreeMap<Coordinates, PlaygroundCell<?>>();
    private final List<OMSVGLineElement> portalLines = new LinkedList<OMSVGLineElement>();

    private int width = 30;
    private int height = 20;
    private boolean showPortalLines = false;

    public PlaygroundModel() {
        // TODO move definitions to file?
        OMSVGDefsElement defs = document.createSVGDefsElement();

        { // define grid pattern
            OMSVGPatternElement pattern = document.createSVGPatternElement();
            pattern.setId("grid");
            pattern.setAttribute("width", "10");
            pattern.setAttribute("height", "10");
            pattern.setAttribute("patternUnits", "userSpaceOnUse");

            OMSVGPathElement path = document.createSVGPathElement();
            path.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_NONE_VALUE);
            path.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, SVGConstants.CSS_GRAY_VALUE);
            path.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "0.5");
            path.getPathSegList().appendItem(path.createSVGPathSegMovetoAbs(10, 0));
            path.getPathSegList().appendItem(path.createSVGPathSegLinetoAbs(0, 0));
            path.getPathSegList().appendItem(path.createSVGPathSegLinetoAbs(0, 10));
            pattern.appendChild(path);

            defs.appendChild(pattern);
        }

        { // define brick pattern
            OMSVGPatternElement pattern = document.createSVGPatternElement();
            pattern.setId("brick");
            pattern.setAttribute("width", "10");
            pattern.setAttribute("height", "10");
            pattern.setAttribute("patternUnits", "userSpaceOnUse");

            OMSVGRectElement background = document.createSVGRectElement();
            background.setAttribute("width", "100%");
            background.setAttribute("height", "100%");
            background.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "rgb(150,22,11)");
            pattern.appendChild(background);

            OMSVGPathElement path = document.createSVGPathElement();
            path.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_NONE_VALUE);
            path.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, SVGConstants.CSS_YELLOW_VALUE);
            path.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_WIDTH_PROPERTY, "1");
            path.getPathSegList().appendItem(path.createSVGPathSegMovetoAbs(0, 0));
            path.getPathSegList().appendItem(path.createSVGPathSegLinetoAbs(10, 0));
            path.getPathSegList().appendItem(path.createSVGPathSegMovetoAbs(0, 5));
            path.getPathSegList().appendItem(path.createSVGPathSegLinetoAbs(0, 10));
            path.getPathSegList().appendItem(path.createSVGPathSegLinetoAbs(10, 10));
            path.getPathSegList().appendItem(path.createSVGPathSegLinetoAbs(10, 5));
            path.getPathSegList().appendItem(path.createSVGPathSegLinetoAbs(0, 5));
            path.getPathSegList().appendItem(path.createSVGPathSegMovetoAbs(5, 5));
            path.getPathSegList().appendItem(path.createSVGPathSegLinetoAbs(5, 0));
            pattern.appendChild(path);

            defs.appendChild(pattern);
        }

        { // create grid
            grid = document.createSVGRectElement();
            grid.setAttribute("width", "100%");
            grid.setAttribute("height", "100%");
            grid.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "url(#grid)");
        }

        { // initialize portal characters
            for (Character ch : "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ~!$%^&*()_+{}:|<>?/*-;[]',."
                    .toCharArray()) {
                portalCharacters.add(ch);
            }
        }

        canvas.appendChild(defs);
        canvas.appendChild(grid);
        resize();

        { // register handlers
            canvas.addMouseDownHandler(new MouseDownHandler() {
                @Override
                public void onMouseDown(MouseDownEvent event) {
                    Coordinates coordinates = getCoordinatesForEvent(event);

                    for (CanvasEventHandler handler : eventHandlers) {
                        handler.onCanvasMouseDown(event, coordinates);
                    }

                    event.preventDefault();
                    event.stopPropagation();
                }
            });

            canvas.addMouseUpHandler(new MouseUpHandler() {
                @Override
                public void onMouseUp(MouseUpEvent event) {
                    Coordinates coordinates = getCoordinatesForEvent(event);

                    for (CanvasEventHandler handler : eventHandlers) {
                        handler.onCanvasMouseUp(event, coordinates);
                    }

                    event.preventDefault();
                    event.stopPropagation();
                }
            });

            canvas.addMouseMoveHandler(new MouseMoveHandler() {
                @Override
                public void onMouseMove(MouseMoveEvent event) {
                    Coordinates coordinates = getCoordinatesForEvent(event);

                    for (CanvasEventHandler handler : eventHandlers) {
                        handler.onCanvasMouseMove(event, coordinates);
                    }

                    event.preventDefault();
                    event.stopPropagation();
                }
            });
        }
    }

    public Element getSvgElement() {
        return canvas.getElement();
    }

    public OMSVGDocument getDocument() {
        return document;
    }

    public void setSource(String source) {
        this.source.clear();

        String[] lines = source.split("\n");
        Map<Character, Coordinates> portals = new HashMap<Character, Coordinates>();

        height = lines.length;
        int row = 0;
        for (String line : lines) {
            int col = 0;
            width = Math.max(width, line.length());

            for (char sign : line.toCharArray()) {
                Coordinates coordinates = new Coordinates(row, col);
                switch (sign) {
                case ' ':
                    setEmpty(coordinates);
                    break;
                case '#':
                    setWall(coordinates);
                    break;
                case '@':
                    setStart(coordinates);
                    break;
                default:
                    if (portalCharacters.contains(sign)) {
                        if (portals.get(sign) == null) {
                            portals.put(sign, coordinates);
                        } else {
                            Coordinates start = portals.remove(sign);
                            setPortal(start, coordinates, sign);
                            portalCharacters.remove(sign);
                        }
                    }
                    break;
                }
                col++;
            }
            row++;
        }

        resize();
    }

    public String getSource() {
        StringBuilder sb = new StringBuilder();

        for (int row = 0; row < height; row++) {
            if (sb.length() > 0) {
                sb.append("\n");
            }

            for (int col = 0; col < width; col++) {
                PlaygroundCell<?> cell = source.get(new Coordinates(row, col));
                if (cell != null) {
                    sb.append(cell.getSign());
                } else {
                    sb.append(' ');
                }
            }
        }

        return sb.toString();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
        resize();
    }

    public void setHeight(int height) {
        this.height = height;
        resize();
    }

    public void toggleGridVisibility() {
        if (SVGConstants.CSS_NONE_VALUE.equals(grid.getStyle().getSVGProperty(SVGConstants.CSS_FILL_PROPERTY))) {
            grid.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, "url(#grid)");
        } else {
            grid.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_NONE_VALUE);
        }
    }

    public void togglePortalLinesVisibility() {
        showPortalLines = !showPortalLines;

        for (OMSVGLineElement element : portalLines) {
            if (showPortalLines) {
                canvas.appendChild(element);
            } else {
                canvas.removeChild(element);
            }
        }
    }

    public void setEmpty(Coordinates coordinates) {
        unsetCell(coordinates);
    }

    public void setWall(Coordinates coordinates) {
        setCell(WallCell.newWall(document, coordinates));
    }

    public void setStart(Coordinates coordinates) {
        setCell(StartCell.newStartPosition(document, coordinates));
    }

    public void setPortal(Coordinates start, Coordinates end) {
        setPortal(start, end, portalCharacters.remove());
    }

    public void setPortal(Coordinates start, Coordinates end, char sign) {
        PortalCell portal = PortalCell.newPortal(document, start, end, sign);
        setCell(portal);
        setCell(portal.getOther());

        portalLines.add(portal.getLine());
        if (showPortalLines) {
            canvas.appendChild(portal.getLine());
        }
    }

    public void addTempElement(OMSVGElement element) {
        canvas.appendChild(element);
    }

    public void removeTempElement(OMSVGElement element) {
        try {
            canvas.removeChild(element);
        } catch (Exception ex) {
            // tried removing non-existent element... please don't do that
        }
    }

    public void registerEventHandler(CanvasEventHandler handler) {
        eventHandlers.add(handler);
    }

    public void removeEventHandler(CanvasEventHandler handler) {
        eventHandlers.remove(handler);
    }

    private void setCell(PlaygroundCell<?> newCell) {
        unsetCell(newCell.getCoordinates());

        newCell.addToCanvas(canvas);
        source.put(newCell.getCoordinates(), newCell);
    }

    private void unsetCell(Coordinates coordinates) {
        PlaygroundCell<?> cell = source.get(coordinates);
        if (cell != null) {
            if (cell instanceof PortalCell) {
                PortalCell other = ((PortalCell) cell).getOther();

                other.removeFromCanvas(canvas);
                source.remove(other.getCoordinates());

                portalLines.remove(other.getLine());
                if (showPortalLines) {
                    canvas.removeChild(other.getLine());
                }
                // reuse the portal character
                portalCharacters.add(cell.getSign());
            }

            cell.removeFromCanvas(canvas);
            source.remove(coordinates);
        }
    }

    private Coordinates getCoordinatesForEvent(MouseEvent<?> event) {
        OMSVGPoint p = canvas.createSVGPoint(event.getClientX(), event.getClientY());
        OMSVGMatrix m = canvas.getScreenCTM().inverse();
        OMSVGPoint result = p.matrixTransform(m);

        return new Coordinates((int) result.getY() / 10, (int) result.getX() / 10);
    }

    private void resize() {
        canvas.setHeight(Unit.PX, height * PlaygroundCell.CELL_SIZE);
        canvas.setWidth(Unit.PX, width * PlaygroundCell.CELL_SIZE);
    }
}
