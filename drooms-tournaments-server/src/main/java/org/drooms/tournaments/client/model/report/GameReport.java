package org.drooms.tournaments.client.model.report;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.drooms.tournaments.client.model.Coordinates;
import org.drooms.tournaments.client.model.report.Snake.SnakeNode;
import org.vectomatic.dom.svg.utils.SVGConstants;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
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
        return turns;
    }

    private void parsePlayers(NodeList playerNodes) {
        int index = 0;
        while (index < playerNodes.getLength()) {
            Element playerNode = (Element) playerNodes.item(index);
            Player player = parsePlayer(playerNode);
            currentScore.put(player, 0);

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

    // tmp variables
    private final Set<Collectible> activeCollectibles = new HashSet<Collectible>();
    private final Map<Player, Integer> currentScore = new HashMap<Player, Integer>();

    private void parseTurns(NodeList turnNodes) {
        int index = 0;
        while (index < turnNodes.getLength()) {
            Element turnNode = (Element) turnNodes.item(index);

            Turn turn = new Turn(Integer.parseInt(turnNode.getAttribute("number")));
            // parse positions
            parseTurnPlayerPositions(turn, turnNode.getElementsByTagName("playerPosition"));

            // parse collectible-related events
            parseTurnNewCollectibles(turnNode.getElementsByTagName("newCollectible"));
            parseTurnRemoveCollectibles(turnNode.getElementsByTagName("removedCollectible"));
            parseTurnCollectedCollectibles(turnNode.getElementsByTagName("collectedCollectible"));

            // reward the survivors
            parseTurnSurvivedPlayer(turnNode.getElementsByTagName("survivedPlayer"));

            // set collectibles
            for (Collectible collectible : activeCollectibles) {
                turn.getCollectibles().add(collectible);
            }

            // set scores
            for (Entry<Player, Integer> entry : currentScore.entrySet()) {
                turn.setScore(entry.getKey(), entry.getValue());
            }

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

    private void parseTurnNewCollectibles(NodeList newCollectibles) {
        int index = 0;
        while (index < newCollectibles.getLength()) {
            Element newCollectible = (Element) newCollectibles.item(index);
            activeCollectibles.add(parseCollectible(newCollectible.getElementsByTagName("collectible").item(0), newCollectible
                    .getElementsByTagName("node").item(0)));
            index++;
        }
    }

    private void parseTurnRemoveCollectibles(NodeList removedCollectibles) {
        int index = 0;
        while (index < removedCollectibles.getLength()) {
            Element removedCollectible = (Element) removedCollectibles.item(index);
            activeCollectibles.remove(parseCollectible(removedCollectible.getElementsByTagName("collectible").item(0),
                    removedCollectible.getElementsByTagName("node").item(0)));
            index++;
        }
    }

    private void parseTurnCollectedCollectibles(NodeList collectedCollectibles) {
        int index = 0;
        while (index < collectedCollectibles.getLength()) {
            Element collectedCollectible = (Element) collectedCollectibles.item(index);
            Collectible collectible = parseCollectible(collectedCollectible.getElementsByTagName("collectible").item(0),
                    collectedCollectible.getElementsByTagName("node").item(0));
            Player player = parsePlayer(collectedCollectible.getElementsByTagName("player").item(0));

            currentScore.put(player, currentScore.get(player) + collectible.getValue());
            activeCollectibles.remove(collectible);

            index++;
        }
    }

    private void parseTurnSurvivedPlayer(NodeList survivedPlayers) {
        int index = 0;
        while (index < survivedPlayers.getLength()) {
            Element survivedPlayer = (Element) survivedPlayers.item(index);

            int points = Integer.parseInt(survivedPlayer.getAttribute("points"));
            Player player = parsePlayer(survivedPlayer.getElementsByTagName("player").item(0));

            currentScore.put(player, currentScore.get(player) + points);
            index++;
        }
    }

    private Collectible parseCollectible(Node collectibleNode, Node node) {
        int points = Integer.parseInt(((Element) collectibleNode).getAttribute("points"));
        return new Collectible(parseNode(node), points);
    }

    /**
     * Parses the node containing 'x' and 'y' attributes. WARNING: requires
     * already parsed playground (to convert 'y' to row number requires height
     * of the playground).
     * 
     * @param node
     *            Element with 'x' and 'y' attributes.
     * @return Parsed coordinates.
     */
    private Coordinates parseNode(Node node) {
        Element element = (Element) node;
        int row = height - Integer.parseInt(element.getAttribute("y")) - 1;
        int col = Integer.parseInt(element.getAttribute("x"));

        return new Coordinates(row, col);
    }

    private Player parsePlayer(Node playerNode) {
        Element element = (Element) playerNode;
        String name = element.getAttribute("name");

        if (!players.containsKey(name)) {
            players.put(name, new Player(name, playerColors[players.size()]));
        }

        return players.get(element.getAttribute("name"));
    }

    // TODO add more colors (in case of huge maps, current maximum is 16
    // players)
    private static final String[] playerColors = new String[] { SVGConstants.CSS_AQUA_VALUE, SVGConstants.CSS_BLACK_VALUE,
            SVGConstants.CSS_BLUE_VALUE, SVGConstants.CSS_FUCHSIA_VALUE, SVGConstants.CSS_GREY_VALUE,
            SVGConstants.CSS_GREEN_VALUE, SVGConstants.CSS_LIME_VALUE, SVGConstants.CSS_MAROON_VALUE,
            SVGConstants.CSS_NAVY_VALUE, SVGConstants.CSS_OLIVE_VALUE, SVGConstants.CSS_ORANGE_VALUE,
            SVGConstants.CSS_PURPLE_VALUE, SVGConstants.CSS_SILVER_VALUE, SVGConstants.CSS_TEAL_VALUE,
            SVGConstants.CSS_YELLOW_VALUE, SVGConstants.CSS_RED_VALUE };
}
