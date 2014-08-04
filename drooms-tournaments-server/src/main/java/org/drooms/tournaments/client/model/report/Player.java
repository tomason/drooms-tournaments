package org.drooms.tournaments.client.model.report;

class Player {
    private final String name;
    private final String color;

    public Player(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

}
