package org.drooms.tournaments.client.widget.game.detail;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.drooms.tournaments.client.model.report.GameReplay;
import org.drooms.tournaments.client.page.PlaygroundDetailPage;
import org.drooms.tournaments.client.page.TournamentDetailPage;
import org.drooms.tournaments.client.page.UserDetailPage;
import org.drooms.tournaments.client.util.Form;
import org.drooms.tournaments.client.util.FormMode;
import org.drooms.tournaments.domain.Game;
import org.drooms.tournaments.domain.GameResult;
import org.jboss.errai.ui.nav.client.local.Navigation;
import org.jboss.errai.ui.nav.client.local.TransitionAnchor;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.common.collect.Multimaps;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.widget.client.TextButton;

@Templated
public class GameDetail extends Composite implements Form<Game> {
    private Game value;
    private GameReplay reportModel = new GameReplay();

    @Inject
    @DataField
    private TextBox id;

    @Inject
    @DataField
    private TransitionAnchor<TournamentDetailPage> tournament;

    @Inject
    @DataField
    private TextButton playground;

    @DataField
    private CellTable<GameResult> results = new CellTable<GameResult>();

    @DataField
    private Element report = reportModel.getSvgElement();

    @Inject
    @DataField
    private Button next;

    @Inject
    private Navigation navigation;

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

        id.setText(value.getId());
        tournament.setText(value.getTournament().getName());
        tournament.setHref("#TournamentDetailPage;tournamentName=" + value.getTournament().getName());

        playground.setText(value.getPlayground().getName());

        Collections.sort(value.getResults());
        results.setRowData(value.getResults());

        if (value.getGameReport() != null && value.getGameReport().length() > 0) {
            reportModel.setSource(value.getGameReport());
        }
    }

    @EventHandler("playground")
    public void playgroundClicked(ClickEvent event) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("playgroundName", value.getPlayground().getName());
        navigation.goTo(PlaygroundDetailPage.class, Multimaps.forMap(map));
    }

    @EventHandler("next")
    public void nextClicked(ClickEvent event) {
        reportModel.nextTurn();
    }
}
