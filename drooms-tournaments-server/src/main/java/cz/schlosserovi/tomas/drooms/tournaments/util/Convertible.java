package cz.schlosserovi.tomas.drooms.tournaments.util;

/**
 * Classes implementing this interface can be converted into other classes. This
 * is done through the convert method. The conversion is recursive with
 * specified depth. If the class has Convertible attributes, it should call
 * their convert method with decremented depth. If the class contains Collection
 * of Convertible classes, it should convert the collection if and only if the
 * depth is more than 0. This class is intended for converting Entity methods
 * that are lazily retrieved from database and accessing nested collections
 * might cause problems.
 * 
 * @param <T>
 *            Class that is product of conversion.
 */
public interface Convertible<T> {

    /**
     * Converts this class to the other class.
     * 
     * @param depth
     *            Resursion depth.
     * @return Converted class.
     */
    public T convert(int depth);

}
