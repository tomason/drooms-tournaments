package org.drooms.tournaments.client.interactive.menu.playground;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.drooms.tournaments.client.interactive.menu.Choice;
import org.drooms.tournaments.client.interactive.menu.api.FormMenu;
import org.drooms.tournaments.client.interactive.menu.api.Menu;
import org.drooms.tournaments.client.interactive.util.OutputDevice;
import org.drooms.tournaments.client.services.TournamentsServerClient;
import org.drooms.tournaments.domain.Playground;
import org.slf4j.LoggerFactory;

class NewPlaygroundMenu extends FormMenu {
    private Playground playground = new Playground();

    protected NewPlaygroundMenu(OutputDevice console, TournamentsServerClient client) {
        super(console, client);
    }

    @Override
    protected String getHeadline() {
        return ("new playground");
    }

    @Override
    protected void printInstructions() {
        // TODO information about new playgrounds
    }

    @Override
    protected Menu execute(Choice choice) {
        String name;
        if (playground.getName() == null) {
            // new playground
            name = console.readLine("New playground name: ");
        } else {
            // editting playground
            name = console.readLine("New playground name(%s): ", playground.getName());
            if (name.length() == 0) {
                name = playground.getName();
            }
        }
        playground.setName(name);

        String source = null;
        try {
            source = readPlaygroundFromFile();
        } catch (Exception ex) {
            LoggerFactory.getLogger(getClass()).debug("Unable to read playground from file", ex);
        }

        if (source == null) {
            source = readFromCommandLine();
        }
        playground.setSource(source);

        return new ConfigurePlaygroundMenu(console, client, this, playground);
    }

    private String readPlaygroundFromFile() throws IOException {
        String playgroundLocation = console.readLine("Playground file: ");
        Path playgroundPath = Paths.get(playgroundLocation);
        if (Files.exists(playgroundPath) && Files.isRegularFile(playgroundPath) && Files.isReadable(playgroundPath)) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (String line : Files.readAllLines(playgroundPath, StandardCharsets.UTF_8)) {
                if (!first) {
                    sb.append("\n");
                }
                sb.append(line);
                first = false;
            }
            return sb.toString();
        }

        return null;
    }

    private String readFromCommandLine() {
        console.print("New layout (hit Ctrl+D to send EOF to stop reading):%n");
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = console.readLine()) != null) {
            sb.append(line).append("\n");
        }

        return sb.toString();
    }
}
