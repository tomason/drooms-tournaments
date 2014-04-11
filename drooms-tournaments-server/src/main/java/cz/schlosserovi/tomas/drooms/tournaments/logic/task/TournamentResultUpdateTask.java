package cz.schlosserovi.tomas.drooms.tournaments.logic.task;

import java.math.BigInteger;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import cz.schlosserovi.tomas.drooms.tournaments.data.TournamentResultDAO;
import cz.schlosserovi.tomas.drooms.tournaments.data.model.TournamentEntity;
import cz.schlosserovi.tomas.drooms.tournaments.data.model.TournamentResultEntity;
import cz.schlosserovi.tomas.drooms.tournaments.data.model.TournamentResultPartialEntity;

public class TournamentResultUpdateTask implements Runnable {
    private static final BigInteger TWO = new BigInteger("2");
    private TournamentResultDAO tournamentResults;
    private TournamentEntity tournament;

    public TournamentResultUpdateTask(TournamentResultDAO tournamentResults, TournamentEntity tournament) {
        this.tournamentResults = tournamentResults;
        this.tournament = tournament;
    }

    @Override
    public void run() {
        Collection<TournamentResultEntity> results = tournamentResults.getTournamentResults(tournament);
        SortedMap<BigInteger, Collection<TournamentResultEntity>> groupedResults = new TreeMap<>();
        for (TournamentResultEntity tournamentResult : results) {
            BigInteger sum = new BigInteger("0");
            for (TournamentResultPartialEntity partialResult : tournamentResult.getPartialResults()) {
                sum = sum.add(TWO.pow(partialResult.getPosition()));
            }
            if (!groupedResults.containsKey(sum)) {
                groupedResults.put(sum, new LinkedList<TournamentResultEntity>());
            }
            groupedResults.get(sum).add(tournamentResult);
        }

        Collection<TournamentResultEntity> toBeUpdated = new LinkedList<>();
        int position = 1;
        for (Entry<BigInteger, Collection<TournamentResultEntity>> resultEntry : groupedResults.entrySet()) {
            for (TournamentResultEntity resultEntity : resultEntry.getValue()) {
                // update position only if necessary
                if (!Integer.valueOf(position).equals(resultEntity.getPosition())) {
                    resultEntity.setPosition(position);
                    toBeUpdated.add(resultEntity);
                }
            }
            position += resultEntry.getValue().size();
        }

        tournamentResults.updateTournamentresults(toBeUpdated);
    }

}
