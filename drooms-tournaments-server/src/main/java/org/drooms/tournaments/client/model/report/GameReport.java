package org.drooms.tournaments.client.model.report;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.drooms.tournaments.client.model.Coordinates;
import org.drooms.tournaments.client.model.report.Snake.SnakeNode;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class GameReport {
    private final Map<String, Player> players = new HashMap<String, Player>();
    private final List<Coordinates> walls = new LinkedList<Coordinates>();
    private final List<Turn> turns = new LinkedList<Turn>();
    private int width = 0;
    private int height = 0;

    public GameReport(String gameReport) {
        Document report = XMLParser.parse(gameReport);

        Element root = (Element) report.getElementsByTagName("game").item(0);
        Element players = (Element) root.getElementsByTagName("players").item(0);
        Element playground = (Element) root.getElementsByTagName("playground").item(0);
        Element turns = (Element) root.getElementsByTagName("turns").item(0);

        parsePlayers(players.getElementsByTagName("player"));
        parsePlayground(playground.getElementsByTagName("node"));
        parseTurns(turns.getElementsByTagName("turn"));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Coordinates> getWalls() {
        return walls;
    }

    public List<Turn> getTurns() {
        // TODO replace with multiple methods with lesser access
        return turns;
    }

    private void parsePlayers(NodeList playerNodes) {
        int index = 0;
        while (index < playerNodes.getLength()) {
            Element player = (Element) playerNodes.item(index);
            players.put(player.getAttribute("name"), new Player(player.getAttribute("name"), playerColors[index]));

            index++;
        }
    }

    private void parsePlayground(NodeList nodeNodes) {
        int index = 0;
        List<Coordinates> emptySpace = new LinkedList<Coordinates>();

        while (index < nodeNodes.getLength()) {
            Element node = (Element) nodeNodes.item(index);
            int row = Integer.parseInt(node.getAttribute("y"));
            int col = Integer.parseInt(node.getAttribute("x"));
            width = Math.max(width, col + 1);
            height = Math.max(height, row + 1);
            emptySpace.add(new Coordinates(row, col));

            index++;
        }

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (!emptySpace.contains(new Coordinates(height - row - 1, col))) {
                    walls.add(new Coordinates(row, col));
                }
            }
        }
    }

    private void parseTurns(NodeList turnNodes) {
        int index = 0;
        while (index < turnNodes.getLength()) {
            Element turnNode = (Element) turnNodes.item(index);

            Turn turn = new Turn(Integer.parseInt(turnNode.getAttribute("number")));
            parseTurnPlayerPositions(turn, turnNode.getElementsByTagName("playerPosition"));
            parseTurnNewCollectibles(turn, turnNode.getElementsByTagName("newCollectible"));

            turns.add(turn);
            index++;
        }
    }

    private void parseTurnPlayerPositions(Turn turn, NodeList playerPositions) {
        int index = 0;
        while (index < playerPositions.getLength()) {
            Element playerPosition = (Element) playerPositions.item(index);
            Element playerNode = (Element) playerPosition.getElementsByTagName("player").item(0);
            Player player = players.get(playerNode.getAttribute("name"));

            turn.addSnake(parsePlayerSnake(player, playerPosition.getElementsByTagName("node")));

            index++;
        }
    }

    private Snake parsePlayerSnake(Player player, NodeList nodes) {
        Snake result = new Snake(player);
        int index = 0;
        while (index < nodes.getLength()) {
            Element node = (Element) nodes.item(index);
            int row = height - Integer.parseInt(node.getAttribute("y")) - 1;
            int col = Integer.parseInt(node.getAttribute("x"));

            SnakeNode snake = new SnakeNode();
            snake.setCoordinates(new Coordinates(row, col));

            result.addNode(snake);
            index++;
        }

        return result;
    }

    private void parseTurnNewCollectibles(Turn turn, NodeList newCollectibles) {

    }

    private static final String[] playerColors = new String[] { SVGConstants.CSS_AQUA_VALUE, SVGConstants.CSS_BLACK_VALUE,
            SVGConstants.CSS_BLUE_VALUE, SVGConstants.CSS_FUCHSIA_VALUE, SVGConstants.CSS_GREY_VALUE,
            SVGConstants.CSS_GREEN_VALUE, SVGConstants.CSS_LIME_VALUE, SVGConstants.CSS_MAROON_VALUE,
            SVGConstants.CSS_NAVY_VALUE, SVGConstants.CSS_OLIVE_VALUE, SVGConstants.CSS_ORANGE_VALUE,
            SVGConstants.CSS_PURPLE_VALUE, SVGConstants.CSS_SILVER_VALUE, SVGConstants.CSS_TEAL_VALUE,
            SVGConstants.CSS_YELLOW_VALUE, SVGConstants.CSS_RED_VALUE };
}
