package org.drooms.tournaments.client.interactive.menu;

import org.drooms.tournaments.client.interactive.menu.api.Menu;

public class ExitMenu extends Menu {

    public ExitMenu() {
        super(null, null);
    }

    @Override
    protected String getHeadline() {
        throw new UnsupportedOperationException("Exit menu is not intended for any use");
    }

    @Override
    protected void printInstructions() {
        throw new UnsupportedOperationException("Exit menu is not intended for any use");
    }

    @Override
    protected Menu execute(Choice choice) {
        throw new UnsupportedOperationException("Exit menu is not intended for any use");
    }

}
