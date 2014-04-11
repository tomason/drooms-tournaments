package org.drooms.tournaments.server.util;

import java.util.Collection;
import java.util.HashSet;

public class NullForbiddingSet<E> extends HashSet<E> {
    private static final long serialVersionUID = 1L;

    public NullForbiddingSet() {
        super();
    }

    public NullForbiddingSet(Collection<? extends E> c) {
        super(c);
    }

    public NullForbiddingSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public NullForbiddingSet(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new IllegalArgumentException("This collection does not allow null elements");
        }

        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = false;
        for (E e : c) {
            result |= add(e);
        }
        return result;
    }
}
