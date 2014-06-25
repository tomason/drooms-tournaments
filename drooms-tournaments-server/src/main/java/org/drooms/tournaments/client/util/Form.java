package org.drooms.tournaments.client.util;

public interface Form<T> {
    void setMode(FormMode mode);

    void setValue(T value);
}
