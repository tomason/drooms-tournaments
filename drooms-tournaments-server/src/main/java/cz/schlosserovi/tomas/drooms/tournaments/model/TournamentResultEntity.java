package cz.schlosserovi.tomas.drooms.tournaments.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TOURNAMENT_RESULT")
public class TournamentResultEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    private Integer position;
    @ManyToOne(optional = false, cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
    private UserEntity player;
    @ManyToOne(optional = false, cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
    private TournamentEntity tournament;

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
