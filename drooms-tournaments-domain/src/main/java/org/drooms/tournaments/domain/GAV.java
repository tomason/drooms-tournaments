package org.drooms.tournaments.domain;

import java.io.Serializable;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class GAV implements Serializable, Comparable<GAV> {
    private static final long serialVersionUID = 1L;

    private String groupId;
    private String artifactId;
    private String version;

    public GAV() {
    }

    public GAV(String gav) {
        if (gav == null) {
            throw new IllegalArgumentException("gav must not be null");
        }
        String[] components = gav.split(":");
        if (components.length != 3) {
            throw new IllegalArgumentException("gav must be in format groupId:artifactId:version");
        }
        setGroupId(components[0]);
        setArtifactId(components[1]);
        setVersion(components[2]);
    }

    public GAV(String groupId, String artifactId, String version) {
        setGroupId(groupId);
        setArtifactId(artifactId);
        setVersion(version);
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        if (groupId == null) {
            throw new IllegalArgumentException("groupId must not be null");
        }
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        if (artifactId == null) {
            throw new IllegalArgumentException("artifactId must not be null");
        }
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        if (version == null) {
            throw new IllegalArgumentException("version must not be null");
        }
        this.version = version;
    }

    public static GAV fromString(String gav) {
        GAV result = new GAV();

        final String[] gavParts = gav.split("\\Q:\\E");
        if (gavParts.length != 3) {
            throw new IllegalArgumentException("Wrong Maven GAV " + gav);
        }
        result.groupId = gavParts[0];
        result.artifactId = gavParts[1];
        result.version = gavParts[2];

        return result;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(groupId).append(':').append(artifactId).append(':').append(version).toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((artifactId == null) ? 0 : artifactId.hashCode());
        result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public int compareTo(GAV o) {
        int result = 0;
        result = groupId.compareTo(o.groupId);
        if (result == 0) {
            result = artifactId.compareTo(o.artifactId);
        }
        if (result == 0) {
            result = version.compareTo(o.version);
        }

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
        GAV other = (GAV) obj;
        if (artifactId == null) {
            if (other.artifactId != null)
                return false;
        } else if (!artifactId.equals(other.artifactId))
            return false;
        if (groupId == null) {
            if (other.groupId != null)
                return false;
        } else if (!groupId.equals(other.groupId))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }
}
