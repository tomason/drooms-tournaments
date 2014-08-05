package org.drooms.tournaments.client.widget.game.replay;

import javax.annotation.PostConstruct;

import org.drooms.tournaments.client.model.report.GameReplay;
import org.drooms.tournaments.client.model.report.Score;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;

@Templated
public class GameReplayPlayer extends Composite {
    private static final ScheduledCommand NO_COMMAND = new ScheduledCommand() {
        @Override
        public void execute() {
            // intentionaly left blank
        }
    };
    private final GameReplay model = new GameReplay();
    private final PopupPanel popup = new PopupPanel(false, true);
    private final Timer player = new Timer() {
        @Override
        public void run() {
            model.nextTurn();
            afterTurn();
            if (model.getCurrentTurn() == model.getTurnCount() - 1) {
                pause();
            }
        }
    };

    private int speed = 500;
    private boolean playing = false;
    private MenuItem turnStatus;

    @DataField
    private MenuBar menuBar = new MenuBar();

    @DataField
    private Element canvas = model.getSvgElement();

    @DataField
    private TableElement scoreboard = Document.get().createTableElement();

    @DataField
    private MenuBar statusBar = new MenuBar();

    @PostConstruct
    public void init() {
        menuBar.addItem("Pause", new ScheduledCommand() {
            @Override
            public void execute() {
                pause();
            }
        });
        menuBar.addItem("Play", new ScheduledCommand() {
            @Override
            public void execute() {
                play();
            }
        });
        menuBar.addItem("Stop", new ScheduledCommand() {
            @Override
            public void execute() {
                stop();
            }
        });
        menuBar.addSeparator();
        menuBar.addItem("Speed up", new ScheduledCommand() {
            @Override
            public void execute() {
                if (speed > 100) {
                    pause();
                    speed -= 100;
                    play();
                }
            }
        });
        menuBar.addItem("Slow down", new ScheduledCommand() {
            @Override
            public void execute() {
                if (speed < 5000) {
                    pause();
                    speed += 100;
                    play();
                }
            }
        });
        menuBar.addSeparator();
        menuBar.addItem("Exit", new ScheduledCommand() {
            @Override
            public void execute() {
                hide();
            }
        });

        statusBar.addItem("Turn: ", NO_COMMAND);
        turnStatus = statusBar.addItem("0/0", NO_COMMAND);
        popup.setGlassEnabled(true);
        popup.setWidget(this);
    }

    public void setGameReport(String gameReport) {
        model.setSource(gameReport);
        afterTurn();
    }

    public void show() {
        popup.center();
        popup.show();
        play();
    }

    public void hide() {
        popup.hide();
        stop();
    }

    private void play() {
        if (!playing) {
            player.scheduleRepeating(speed);
            playing = true;
        }
    }

    private void pause() {
        if (playing) {
            player.cancel();
            playing = false;
        }
    }

    private void stop() {
        if (playing) {
            player.cancel();
            playing = false;
        }
        model.setCurrentTurn(0);
        afterTurn();
    }

    private void afterTurn() {
        turnStatus.setText((model.getCurrentTurn() + 1) + "/" + model.getTurnCount());

        while ( scoreboard.getChildCount() > 0) {
            scoreboard.removeChild(scoreboard.getChild(0));
        }

        for (Score score : model.getCurrentScore()) {
            TableRowElement row = Document.get().createTRElement();
            scoreboard.appendChild(row);

            TableCellElement color = Document.get().createTDElement();
            color.getStyle().setBackgroundColor(score.getPlayerColor());
            color.getStyle().setWidth(10, Unit.PX);
            row.appendChild(color);

            TableCellElement name = Document.get().createTDElement();
            name.setInnerText(score.getPlayerName());
            name.getStyle().setWidth(100, Unit.PX);
            row.appendChild(name);

            TableCellElement points = Document.get().createTDElement();
            points.setInnerText(Integer.toString(score.getScore()));
            points.getStyle().setTextAlign(TextAlign.RIGHT);
            points.getStyle().setWidth(30, Unit.PX);
            row.appendChild(points);
        }
    }
}
