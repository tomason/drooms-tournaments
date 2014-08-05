package org.drooms.tournaments.client.widget.game.list;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.drooms.tournaments.client.page.GameDetailPage;
import org.drooms.tournaments.domain.Game;
import org.drooms.tournaments.domain.GameFilter;
import org.drooms.tournaments.domain.User;
import org.drooms.tournaments.services.GameService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ui.nav.client.local.TransitionTo;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.common.collect.Multimaps;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

@Templated
public class GamesList extends Composite {
    @Inject
    @DataField
    private TextBox user;

    @Inject
    @DataField
    private CheckBox finished;

    @Inject
    @DataField
    private Button filter;
    
    @DataField
    private CellTable<Game> games = new CellTable<Game>();
    @DataField
    private SimplePager pager = new SimplePager(TextLocation.CENTER);

    @Inject
    private Caller<GameService> service;
    @Inject
    private TransitionTo<GameDetailPage> gameDetailPage;

    private ListDataProvider<Game> gamesList = new ListDataProvider<Game>();

    @PostConstruct
    public void init() {
        gamesList.addDataDisplay(games);
        pager.setDisplay(games);

        games.addColumn(new Column<Game, String>(new TextCell()) {
            @Override
            public String getValue(Game object) {
                return object.getId();
            }
        }, "Id");
        games.addColumn(new Column<Game, String>(new TextCell()) {
            @Override
            public String getValue(Game object) {
                return object.getTournament().getName();
            }
        }, "Tournament");
        games.addColumn(new Column<Game, String>(new TextCell()) {
            @Override
            public String getValue(Game object) {
                return object.getPlayground().getName();
            }
        }, "Playground");
        games.addColumn(new Column<Game, String>(new TextCell()) {
            @Override
            public String getValue(Game object) {
                return object.isFinished() ? "Yes" : "No";
            }
        }, "Finished");

        final NoSelectionModel<Game> selection = new NoSelectionModel<Game>();
        selection.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("gameId", selection.getLastSelectedObject().getId());
                gameDetailPage.go(Multimaps.forMap(map));
            }
        });
        games.setSelectionModel(selection);

        finished.setValue(true);
    }

    @EventHandler("filter")
    public void onFilterClick(ClickEvent event) {
        reload();
    }

    public void reload() {
        GameFilter filter = new GameFilter();

        filter.setPlayer(new User(user.getValue()));
        filter.setFinished(finished.getValue());

        service.call(new RemoteCallback<List<Game>>() {
            @Override
            public void callback(List<Game> response) {
                gamesList.setList(response);
            }
        }).getGames(filter);
    }
}
