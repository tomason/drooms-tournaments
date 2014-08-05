package org.drooms.tournaments.client.widget.game.list;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.drooms.tournaments.client.page.GameDetailPage;
import org.drooms.tournaments.domain.Game;
import org.drooms.tournaments.domain.GameFilter;
import org.drooms.tournaments.domain.Tournament;
import org.drooms.tournaments.domain.User;
import org.drooms.tournaments.services.GameService;
import org.drooms.tournaments.services.TournamentService;
import org.drooms.tournaments.services.UserService;
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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

@Templated
public class GamesList extends Composite {
    @Inject
    @DataField
    private ListBox user;

    @DataField
    private ListBox tournament = new ListBox(false);

    @Inject
    @DataField
    private CheckBox finished;

    @Inject
    @DataField
    private Button resetFilter;

    @Inject
    @DataField
    private Button filter;

    @DataField
    private CellTable<Game> games = new CellTable<Game>();
    @DataField
    private SimplePager pager = new SimplePager(TextLocation.CENTER);

    @Inject
    private Caller<GameService> gameService;
    @Inject
    private Caller<UserService> userService;
    @Inject
    private Caller<TournamentService> tournamentService;
    @Inject
    private TransitionTo<GameDetailPage> gameDetailPage;

    private ListDataProvider<Game> gamesList = new ListDataProvider<Game>();

    @PostConstruct
    public void init() {
        gamesList.addDataDisplay(games);
        pager.setDisplay(games);

        tournament.setVisibleItemCount(1);

        resetFilter();

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
    }

    @EventHandler("resetFilter")
    public void resetFilterClicked(ClickEvent event) {
        resetFilter();
        reload();
    }

    @EventHandler("filter")
    public void onFilterClick(ClickEvent event) {
        reload();
    }

    public void reload() {
        GameFilter filter = new GameFilter();

        if (tournament.getSelectedIndex() > 0) {
            filter.setTournament(new Tournament(tournament.getValue(tournament.getSelectedIndex())));
        }

        if (user.getSelectedIndex() > 0) {
            filter.setPlayer(new User(user.getValue(user.getSelectedIndex())));
        }
        filter.setFinished(finished.getValue());

        gameService.call(new RemoteCallback<List<Game>>() {
            @Override
            public void callback(List<Game> response) {
                gamesList.setList(response);
            }
        }).getGames(filter);
    }

    private void resetFilter() {
        finished.setValue(true);

        tournament.clear();
        tournament.addItem("---");
        tournament.setSelectedIndex(0);
        tournamentService.call(new RemoteCallback<List<Tournament>>() {
            @Override
            public void callback(List<Tournament> response) {
                for (Tournament t : response) {
                    tournament.addItem(t.getName());
                }
            }
        }).getTournaments();

        user.clear();
        user.addItem("---");
        user.setSelectedIndex(0);
        userService.call(new RemoteCallback<List<User>>() {
            @Override
            public void callback(List<User> response) {
                for (User u : response) {
                    user.addItem(u.getName());
                }
            }
        }).getUsers();
    }
}
