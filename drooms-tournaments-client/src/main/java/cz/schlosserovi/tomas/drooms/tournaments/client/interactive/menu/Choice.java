package cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu;

public enum Choice {
    // positions in the enum decides the order of choices in menu

    // main menu entries
    REGISTER("New user registration"),
    PLAYGROUNDS("Playgrounds menu"),
    STRATEGIES("Strategies menu"),
    TOURNAMENTS("Tournaments menu"),

    // submenu entries
    STRATEGY_NEW("New strategy"),
    STRATEGY_ACTIVATE("Set active strategy"),
    PLAYGROUND_NEW("New playground"),
    PLAYGROUND_DETAIL("Show playground detail"),
    TOURNAMENT_NEW("New tournament"),
    TOURNAMENT_DETAIL("Show tournament detail"),
    TOURNAMENT_JOIN("Join the tournament"),

    // wizard entries
    PLAYGROUND_ADD("Add playground"),
    PLAYGROUND_REMOVE("Remove playground"),

    PLAYGROUND_CREATE("Create playground"),
    PLAYGROUND_DISCARD("Discard playground"),
    TOURNAMENT_CREATE("Create tournament"),
    TOURNAMENT_DISCARD("Discard tournament"),
    CONFIGURATION_ADD("Add configuration entry"),
    CONFIGURATION_COLLECTIBLE("Add collectible"),
    CONFIGURATION_EDIT("Edit configuration value"),
    CONFIGURATION_REMOVE("Remove configuration entry"),

    DONE("Done"),

    // filter entries
    SHOW_ALL("Show all"),
    SHOW_MINE("Show mine"),

    // default entries
    BACK("Back"),
    MAIN_MENU("Return to main menu"),
    LOGIN("Login"),
    LOGOUT("Logout"),
    QUIT("Quit");

    private final String description;
    private Choice(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
