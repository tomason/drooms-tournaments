package cz.schlosserovi.tomas.drooms.tournaments.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PLAYGROUND_CONFIGURATION")
public class PlaygroundConfigEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    private String key;
    private String value;

    public PlaygroundConfigEntity() {
    }

    public PlaygroundConfigEntity(long id) {
        setId(id);
    }

    public PlaygroundConfigEntity(String key, String value) {
        setKey(key);
        setValue(value);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Can't change Id in persisted PlaygroundConfig");
        }
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        if (this.key != null) {
            throw new IllegalStateException("Key is already set");
        }
        if (key == null || key.length() == 0) {
            throw new IllegalArgumentException("Key must not be null nor empty");
        }
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (value == null || value.length() == 0) {
            throw new IllegalArgumentException("Key must not be null nor empty");
        }
        this.value = value;
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
        PlaygroundConfigEntity other = (PlaygroundConfigEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
