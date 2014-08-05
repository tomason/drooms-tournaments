package org.drooms.tournaments.client.model.report;

import java.util.LinkedList;
import java.util.List;

import org.drooms.tournaments.client.model.Coordinates;

public class Snake {
    private final Player player;
    private SnakeNode head;
    private SnakeNode tail;
    private List<SnakeNode> body = new LinkedList<SnakeNode>();

    public Snake(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public SnakeNode getHead() {
        return head;
    }

    public SnakeNode getTail() {
        return tail;
    }

    void addNode(SnakeNode node) {
        if (body.size() == 0) {
            head = node;
        } else {
            tail = node;
            node.previous = body.get(body.size() - 1);
            node.previous.next = node;

            // recount the rotation for previous node
            recountRotation(node.previous);
        }
        recountRotation(node);

        body.add(node);
    }

    private void recountRotation(SnakeNode node) {
        int rotation = 0;
        if (node.next == null) {
            if (node.previous != null) {
                // tail
                rotation = countRotation(node.getCoordinates(), node.getPrevious().getCoordinates());
            }
        } else {
            if (node.previous == null) {
                // head
                rotation = countRotation(node.getNext().getCoordinates(), node.getCoordinates());
            } else {
                // body
                int rotation1 = countRotation(node.getNext().getCoordinates(), node.getCoordinates());
                int rotation2 = countRotation(node.getCoordinates(), node.getPrevious().getCoordinates());

                if (rotation1 != rotation2) {
                    node.setBend(true);
                    if ((rotation1 == 0 && rotation2 == 270) || (rotation1 == 90 && rotation2 == 0)
                            || (rotation1 == 180 && rotation2 == 90) || (rotation1 == 270 && rotation2 == 180)) {
                        rotation = rotation1 + 90;
                    } else {
                        rotation = rotation1;
                    }
                } else {
                    node.setBend(false);
                    rotation = rotation1;
                }
            }
        }

        node.rotation = rotation;
    }

    private int countRotation(Coordinates source, Coordinates target) {
        int deltaRow = source.getRow() - target.getRow();
        int deltaCol = source.getCol() - target.getCol();

        int rotation = 0;
        if (deltaRow == 1) {
            // UP
            rotation = 0;
        } else if (deltaCol == -1) {
            // RIGHT
            rotation = 90;
        } else if (deltaRow == -1) {
            // DOWN
            rotation = 180;
        } else if (deltaCol == 1) {
            // LEFT
            rotation = 270;
        }

        return rotation;
    }

    public static class SnakeNode {
        private int rotation = 0;
        private boolean bend = false;
        private Coordinates coordinates;
        private SnakeNode previous;
        private SnakeNode next;

        public int getRotation() {
            return rotation;
        }

        public boolean isBend() {
            return bend;
        }

        public void setBend(boolean bend) {
            this.bend = bend;
        }

        public Coordinates getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(Coordinates coordinates) {
            this.coordinates = coordinates;
        }

        public SnakeNode getNext() {
            return next;
        }

        public SnakeNode getPrevious() {
            return previous;
        }
    }
}
