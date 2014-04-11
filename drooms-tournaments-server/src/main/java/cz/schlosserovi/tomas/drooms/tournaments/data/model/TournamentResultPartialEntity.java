package cz.schlosserovi.tomas.drooms.tournaments.data.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TOURNAMENT_RESULT_PARTIAL")
public class TournamentResultPartialEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private TournamentResultEntity tournamentResult;
    @ManyToOne(optional = false)
    @JoinColumn(name = "PLAYGROUND_NAME")
    private PlaygroundEntity playground;

    private Integer position;

    public TournamentResultPartialEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TournamentResultEntity getTournamentResult() {
        return tournamentResult;
    }

    public void setTournamentResult(TournamentResultEntity tournamentResult) {
        if (this.tournamentResult != null) {
            throw new IllegalStateException("Tournament result is already set");
        }
        if (tournamentResult == null) {
            throw new IllegalArgumentException("Tournament result must not be null");
        }
        this.tournamentResult = tournamentResult;
        tournamentResult.addPartialResult(this);
    }

    public PlaygroundEntity getPlayground() {
        return playground;
    }

    public void setPlayground(PlaygroundEntity playground) {
        if (this.playground != null) {
            throw new IllegalStateException("Playground is already set");
        }
        if (playground == null) {
            throw new IllegalArgumentException("Playground must not be null");
        }
        this.playground = playground;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
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
        TournamentResultPartialEntity other = (TournamentResultPartialEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
