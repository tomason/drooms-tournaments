package cz.schlosserovi.tomas.drooms.tournaments.util;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Utility class for converting entity classes to domain ones.
 * 
 * @param <S>
 *            Source class.
 * @param <T>
 *            Target class.
 */
public class Converter<S extends Convertible<T>, T> {

    /**
     * Creates converter for a given class. The class must implement
     * {@link Convertible} interface.
     * 
     * @param clazz
     *            Class to create convertor for.
     * @return Convertor for a given class.
     */
    public static <S extends Convertible<T>, T> Converter<S, T> forClass(Class<S> clazz) {
        return new Converter<S, T>();
    }

    private int depth = 0;

    private Converter() {
    }

    /**
     * Converts entity class to domain class.
     * 
     * @param source
     *            Entity class to convert.
     * @return Converted domain class.
     */
    public T convert(S source) {
        return source.convert(depth);
    }

    /**
     * Converts given {@link Collection} of entity classes to a {@link List} of
     * domain classes. The {@link List} was chosen due to its usage in domain
     * classes.
     * 
     * @param sources
     *            Collection of entity classes to convert.
     * @return List of converted domain classes.
     */
    public List<T> convert(Collection<S> sources) {
        return convertCollection(sources, new LinkedList<T>());
    }

    /**
     * Converts given {@link List} of entity classes to a {@link List} of domain
     * classes.
     * 
     * @param sources
     *            List of entity classes to convert.
     * @return List of converted domain classes.
     */
    public List<T> convert(List<S> sources) {
        return convertCollection(sources, new LinkedList<T>());
    }

    /**
     * Converts given {@link Set} of entity classes to a {@link Set} of domain
     * classes.
     * 
     * @param sources
     *            Set of entity classes to convert.
     * @return Set of converted domain classes.
     */
    public Set<T> convert(Set<S> sources) {
        return convertCollection(sources, new LinkedHashSet<T>());
    }

    /**
     * Sets the dept of recursion on converted object.
     * @param depth
     *            Depth of recursion.
     * @return Converter with set recursion depth.
     * @see Convertible
     */
    public Converter<S, T> setRecurseDepth(int depth) {
        this.depth = depth;

        return this;
    }

    private <R extends Collection<T>> R convertCollection(Collection<S> sources, R target) {
        target.clear();
        for (S source : sources) {
            target.add(convert(source));
        }

        return target;
    }
}
