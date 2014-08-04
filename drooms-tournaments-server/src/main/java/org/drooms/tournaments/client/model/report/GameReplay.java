package org.drooms.tournaments.client.model.report;

import java.util.LinkedList;
import java.util.List;

import org.drooms.tournaments.client.model.Coordinates;
import org.drooms.tournaments.client.model.playground.PlaygroundModel;
import org.drooms.tournaments.client.model.report.Snake.SnakeNode;
import org.vectomatic.dom.svg.OMSVGElement;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGPolygonElement;
import org.vectomatic.dom.svg.OMSVGTransform;
import org.vectomatic.dom.svg.OMSVGUseElement;
import org.vectomatic.dom.svg.utils.SVGConstants;

public class GameReplay extends PlaygroundModel {
    private GameReport report;

    public GameReplay() {
        setHeight(0);
        setWidth(0);

        { // snake head
            OMSVGGElement g = document.createSVGGElement();
            g.setId("head");
            OMSVGPolygonElement head = document.createSVGPolygonElement();
            head.getPoints().appendItem(canvas.createSVGPoint(4, 2));
            head.getPoints().appendItem(canvas.createSVGPoint(0, 6));
            head.getPoints().appendItem(canvas.createSVGPoint(3, 10));
            head.getPoints().appendItem(canvas.createSVGPoint(7, 10));
            head.getPoints().appendItem(canvas.createSVGPoint(10, 6));
            head.getPoints().appendItem(canvas.createSVGPoint(6, 2));
            head.getPoints().appendItem(canvas.createSVGPoint(5, 0));
            g.appendChild(head);
            OMSVGPolygonElement tongue = document.createSVGPolygonElement();
            tongue.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_RED_VALUE);
            tongue.getPoints().appendItem(canvas.createSVGPoint(4, 2));
            tongue.getPoints().appendItem(canvas.createSVGPoint(6, 2));
            tongue.getPoints().appendItem(canvas.createSVGPoint(5, 0));
            g.appendChild(tongue);

            defs.appendChild(g);
        }

        { // snake body
            OMSVGGElement g = document.createSVGGElement();
            g.setId("body");
            OMSVGPolygonElement body = document.createSVGPolygonElement();
            body.getPoints().appendItem(canvas.createSVGPoint(3, 0));
            body.getPoints().appendItem(canvas.createSVGPoint(3, 10));
            body.getPoints().appendItem(canvas.createSVGPoint(7, 10));
            body.getPoints().appendItem(canvas.createSVGPoint(7, 0));
            g.appendChild(body);

            defs.appendChild(g);
        }

        { // snake bend
            OMSVGElement g = document.createSVGAElement();
            g.setId("bend");
            OMSVGPolygonElement bend = document.createSVGPolygonElement();
            bend.getPoints().appendItem(canvas.createSVGPoint(7, 10));
            bend.getPoints().appendItem(canvas.createSVGPoint(3, 10));
            bend.getPoints().appendItem(canvas.createSVGPoint(3, 6));
            bend.getPoints().appendItem(canvas.createSVGPoint(6, 3));
            bend.getPoints().appendItem(canvas.createSVGPoint(10, 3));
            bend.getPoints().appendItem(canvas.createSVGPoint(10, 7));
            bend.getPoints().appendItem(canvas.createSVGPoint(8, 7));
            bend.getPoints().appendItem(canvas.createSVGPoint(7, 8));
            g.appendChild(bend);

            defs.appendChild(g);
        }

        { // snake tail
            OMSVGGElement g = document.createSVGGElement();
            g.setId("tail");
            OMSVGPolygonElement tail = document.createSVGPolygonElement();
            tail.getPoints().appendItem(canvas.createSVGPoint(3, 0));
            tail.getPoints().appendItem(canvas.createSVGPoint(3, 8));
            tail.getPoints().appendItem(canvas.createSVGPoint(5, 10));
            tail.getPoints().appendItem(canvas.createSVGPoint(7, 8));
            tail.getPoints().appendItem(canvas.createSVGPoint(7, 0));
            g.appendChild(tail);

            defs.appendChild(g);
        }

        { // collectible
            OMSVGGElement g = document.createSVGGElement();
            g.setId("collectible");
            OMSVGPolygonElement strawberry = document.createSVGPolygonElement();
            strawberry.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_RED_VALUE);
            strawberry.getPoints().appendItem(canvas.createSVGPoint(0, 3));
            strawberry.getPoints().appendItem(canvas.createSVGPoint(2, 7));
            strawberry.getPoints().appendItem(canvas.createSVGPoint(5, 10));
            strawberry.getPoints().appendItem(canvas.createSVGPoint(8, 7));
            strawberry.getPoints().appendItem(canvas.createSVGPoint(10, 3));
            strawberry.getPoints().appendItem(canvas.createSVGPoint(5, 1));
            g.appendChild(strawberry);
            OMSVGPolygonElement leaf = document.createSVGPolygonElement();
            leaf.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_GREEN_VALUE);
            leaf.getPoints().appendItem(canvas.createSVGPoint(0, 0));
            leaf.getPoints().appendItem(canvas.createSVGPoint(5, 1));
            leaf.getPoints().appendItem(canvas.createSVGPoint(10, 0));
            leaf.getPoints().appendItem(canvas.createSVGPoint(10, 3));
            leaf.getPoints().appendItem(canvas.createSVGPoint(5, 1));
            leaf.getPoints().appendItem(canvas.createSVGPoint(0, 3));
            g.appendChild(leaf);

            defs.appendChild(g);
        }
    }

    @Override
    public void setSource(String source) {
        report = new GameReport(source);

        setWidth(report.getWidth());
        setHeight(report.getHeight());

        for (Coordinates wall : report.getWalls()) {
            setWall(wall);
        }

        turnCount = 0;
        redraw();
    }

    @Override
    public void addTempElement(OMSVGElement element) {
        super.addTempElement(element);
        tempNodes.add(element);
    }

    private List<OMSVGElement> tempNodes = new LinkedList<OMSVGElement>();
    private int turnCount = 0;

    public void nextTurn() {
        if (turnCount < report.getTurns().size() - 1) {
            turnCount++;
        } else {
            turnCount = 0;
        }
        redraw();
    }

    private void redraw() {
        for (OMSVGElement tempNode : tempNodes) {
            removeTempElement(tempNode);
        }
        Turn turn = report.getTurns().get(turnCount);
        for (Snake snake : turn.getSnakes()) {
            SnakeNode node = snake.getHead();
            { // head
                OMSVGGElement head = document.createSVGGElement();
                OMSVGUseElement use = new OMSVGUseElement();
                use.getHref().setBaseVal("#head");
                head.appendChild(use);
                head.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, snake.getPlayer().getColor());
                OMSVGTransform move = canvas.createSVGTransform();
                move.setTranslate(node.getCoordinates().getCol() * 10, node.getCoordinates().getRow() * 10);
                head.getTransform().getBaseVal().appendItem(move);
                OMSVGTransform rotate = canvas.createSVGTransform();
                rotate.setRotate(node.getRotation(), 5, 5);
                head.getTransform().getBaseVal().appendItem(rotate);
                addTempElement(head);
            }
            // move to next node
            node = node.getNext();
            if (node == null) {
                // in case we have 1 node snake (just the head)
                continue;
            }
            while (node.getNext() != null) {
                OMSVGGElement body = document.createSVGGElement();
                OMSVGUseElement use = new OMSVGUseElement();
                if (node.isBend()) {
                    use.getHref().setBaseVal("#bend");
                } else {
                    use.getHref().setBaseVal("#body");
                }
                body.appendChild(use);
                body.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, snake.getPlayer().getColor());
                OMSVGTransform transform = canvas.createSVGTransform();
                transform.setTranslate(node.getCoordinates().getCol() * 10, node.getCoordinates().getRow() * 10);
                body.getTransform().getBaseVal().appendItem(transform);
                OMSVGTransform rotate = canvas.createSVGTransform();
                rotate.setRotate(node.getRotation(), 5, 5);
                body.getTransform().getBaseVal().appendItem(rotate);
                addTempElement(body);

                node = node.getNext();
            }
            { // tail
                OMSVGGElement tail = document.createSVGGElement();
                OMSVGUseElement use = new OMSVGUseElement();
                use.getHref().setBaseVal("#tail");
                tail.appendChild(use);
                tail.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, snake.getPlayer().getColor());
                OMSVGTransform transform = canvas.createSVGTransform();
                transform.setTranslate(snake.getTail().getCoordinates().getCol() * 10, snake.getTail().getCoordinates()
                        .getRow() * 10);
                tail.getTransform().getBaseVal().appendItem(transform);
                OMSVGTransform rotate = canvas.createSVGTransform();
                rotate.setRotate(node.getRotation(), 5, 5);
                tail.getTransform().getBaseVal().appendItem(rotate);
                addTempElement(tail);
            }
        }
    }
}
