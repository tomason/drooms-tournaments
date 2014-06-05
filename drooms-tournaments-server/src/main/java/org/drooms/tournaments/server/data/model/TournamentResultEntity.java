package org.drooms.tournaments.server.data.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.drooms.tournaments.domain.TournamentResult;
import org.drooms.tournaments.server.util.Convertible;
import org.drooms.tournaments.server.util.NullForbiddingSet;

@Entity
@Table(name = "TOURNAMENT_RESULT")
public class TournamentResultEntity implements Serializable, Convertible<TournamentResult> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    private Integer position;
    @ManyToOne(optional = false)
    private UserEntity player;
    @ManyToOne(optional = false)
    private TournamentEntity tournament;
    @OneToMany(mappedBy = "tournamentResult", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<TournamentResultPartialEntity> partialResults = new NullForbiddingSet<>();

    public TournamentResultEntity() {
    }

    public TournamentResultEntity(long id) {
        setId(id);
    }

    public TournamentResultEntity(UserEntity player, TournamentEntity tournament) {
        setPlayer(player);
        setTournament(tournament);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Id is already set");
        }
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public UserEntity getPlayer() {
        return player;
    }

    public void setPlayer(UserEntity player) {
        if (this.player != null) {
            throw new IllegalStateException("Player is already set");
        }
        if (player == null) {
            throw new IllegalArgumentException("Player must not be null");
        }
        this.player = player;
        player.addTournamentResult(this);
    }

    public TournamentEntity getTournament() {
        return tournament;
    }

    public void setTournament(TournamentEntity tournament) {
        if (this.tournament != null) {
            throw new IllegalStateException("Tournament is already set");
        }
        if (tournament == null) {
            throw new IllegalArgumentException("Tournament must not be null");
        }
        this.tournament = tournament;
        tournament.addResult(this);
    }

    public Collection<TournamentResultPartialEntity> getPartialResults() {
        return Collections.unmodifiableCollection(partialResults);
    }

    public void setPartialResults(Set<TournamentResultPartialEntity> partialResults) {
        if (partialResults == null) {
            throw new IllegalArgumentException("Partial results must not be null");
        }
        this.partialResults.clear();
        this.partialResults.addAll(partialResults);
    }

    public void addPartialResult(TournamentResultPartialEntity partialResult) {
        if (partialResult == null) {
            throw new IllegalArgumentException("Partial result must not be null");
        }
        if (!equals(partialResult.getTournamentResult())) {
            throw new IllegalArgumentException("Partial result is already assigned to another tournament");
        }
        partialResults.add(partialResult);
    }

    @Override
    public TournamentResult convert(int depth) {
        TournamentResult result = new TournamentResult();

        result.setPlayer(player.convert(depth - 1));
        result.setPosition(position == null ? 0 : position);

        return result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TournamentResultEntity other = (TournamentResultEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
