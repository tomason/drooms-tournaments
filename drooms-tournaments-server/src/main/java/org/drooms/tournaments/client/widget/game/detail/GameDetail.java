package org.drooms.tournaments.client.widget.game.detail;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.drooms.tournaments.client.page.PlaygroundDetailPage;
import org.drooms.tournaments.client.page.UserDetailPage;
import org.drooms.tournaments.client.util.Form;
import org.drooms.tournaments.client.util.FormMode;
import org.drooms.tournaments.client.widget.game.replay.GameReplayPlayer;
import org.drooms.tournaments.domain.Game;
import org.drooms.tournaments.domain.GameResult;
import org.jboss.errai.ui.nav.client.local.Navigation;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.common.collect.Multimaps;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

@Templated
public class GameDetail extends Composite implements Form<Game> {
    private Game value;

    @DataField
    private LabelElement id = Document.get().createLabelElement();

    @DataField
    private AnchorElement tournament = Document.get().createAnchorElement();

    @DataField
    private AnchorElement playground = Document.get().createAnchorElement();

    @DataField
    private CellTable<GameResult> results = new CellTable<GameResult>();

    @Inject
    @DataField
    private Button replay;

    @Inject
    @DataField
    private Button download;

    @Inject
    private Navigation navigation;

    @Inject
    private GameReplayPlayer player;

    @Override
    public void setMode(FormMode mode) {
        // intentionaly left blank
    }

    @PostConstruct
    public void init() {
        results.addColumn(new Column<GameResult, Number>(new NumberCell()) {
            @Override
            public Number getValue(GameResult object) {
                return object.getPoints();
            }
        }, "Points");
        results.addColumn(new Column<GameResult, String>(new TextCell()) {
            @Override
            public String getValue(GameResult object) {
                return object.getStrategy().getPlayer().getName();
            }
        }, "Player");
        results.addColumn(new Column<GameResult, String>(new TextCell()) {
            @Override
            public String getValue(GameResult object) {
                return object.getStrategy().getGav().toString();
            }
        }, "Strategy");

        final NoSelectionModel<GameResult> selection = new NoSelectionModel<GameResult>();
        selection.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("userName", selection.getLastSelectedObject().getStrategy().getPlayer().getName());
                navigation.goTo(UserDetailPage.class, Multimaps.forMap(map));
            }
        });
        results.setSelectionModel(selection);
    }

    @Override
    public void setValue(Game value) {
        this.value = value;

        id.setInnerText(value.getId());
        tournament.setInnerText(value.getTournament().getName());
        tournament.setHref("#TournamentDetailPage;tournamentName=" + value.getTournament().getName());

        playground.setInnerText(value.getPlayground().getName());
        playground.setHref("#PlaygroundDetailPage;playgroundName=" + value.getPlayground().getName());

        Collections.sort(value.getResults(), Collections.reverseOrder());
        results.setRowData(value.getResults());
    }

    @EventHandler("playground")
    public void playgroundClicked(ClickEvent event) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("playgroundName", value.getPlayground().getName());
        navigation.goTo(PlaygroundDetailPage.class, Multimaps.forMap(map));
    }

    @EventHandler("replay")
    public void replayClicked(ClickEvent event) {
        player.setGameReport(value.getGameReport());
        player.show();
    }

    @EventHandler("download")
    public void downloadClicked(ClickEvent event) {
        Window.open("/services/games/" + value.getId() + "/report", "_self", "");
    }
}
