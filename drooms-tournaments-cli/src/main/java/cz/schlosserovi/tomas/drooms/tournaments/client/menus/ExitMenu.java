package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

public class ExitMenu extends Menu {

    public ExitMenu() {
        super(null, null);
    }
    
    @Override
    protected String getHeadline() {
        return null;
    }

    @Override
    protected void printMenu() {
    }

    @Override
    protected Menu execute(int choice) {
        return null;
    }

    @Override
    protected boolean allowMainMenu() {
        return false;
    }

}
