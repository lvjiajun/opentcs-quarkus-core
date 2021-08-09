package org.youbai.opentcs.kernel.workingset.newObject;

import org.youbai.opentcs.data.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public interface ResourcePool {
    /**
     * Adds a new object to the pool.
     *
     * @param newObject The object to be added to the pool.
     * @throws ObjectExistsException If an object with the same ID or the same
     * name as the new one already exists in this pool.
     */
    public void addObject(TCSObject<?> newObject)
            throws ObjectExistsException;

    public <E extends TCSObject<E>> E replaceObject(E object);


    /**
     * Returns an object from the pool.
     *
     * @param <T> The object's type.
     * @param ref A reference to the object to be returned.
     * @return The referenced object, or <code>null</code>, if no such object
     * exists in this pool or if an object exists but is not an instance of the
     * given class.
     */
    @Nullable
    public <T extends TCSObject<T>> T getOrNull(TCSObjectReference<T> ref);

    /**
     * Returns an object from the pool.
     *
     * @param <T> The object's type.
     * @param ref A reference to the object to be returned.
     * @return The referenced object.
     * @throws ObjectUnknownException If the referenced object does not exist, or if an object exists
     * but is not an instance of the given class.
     */
    @Nonnull
    public <T extends TCSObject<T>> T get(TCSObjectReference<T> ref)
            throws ObjectUnknownException;

    /**
     * Returns an object from the pool.
     *
     * @param name The name of the object to return.
     * @return The object with the given name, or <code>null</code>, if no such
     * object exists in this pool.
     */
    @Nullable
    public TCSObject<?> getTCSObjectOrNull(String name);

    /**
     * Returns an object from the pool.
     *
     * @param name The name of the object to return.
     * @return The object with the given name.
     * @throws ObjectUnknownException If the referenced object does not exist.
     */
    @Nonnull
    public TCSObject<?> getTCSObject(String name)
            throws ObjectUnknownException;

    /**
     * Returns an object from the pool.
     *
     * @param <T> The object's type.
     * @param name The name of the object to be returned.
     * @return The named object, or <code>null</code>, if no such object
     * exists in this pool or if an object exists but is not an instance of the
     * given class.
     */
    @Nullable
    public <T extends TCSObject<T>> T getObjectOrNull(String name);
    /**
     * Returns an object from the pool.
     *
     * @param <T> The object's type.
     * @param name The name of the object to be returned.
     * @return The named object.
     * @throws ObjectUnknownException If no object with the given name exists in this pool or if an
     * object exists but is not an instance of the given class.
     */
    @Nonnull
    public <T extends TCSObject<T>> T getObject(String name)
            throws ObjectUnknownException;

    /**
     * Returns a set of objects whose names match the given regular expression.
     *
     * @param regexp The regular expression that the names of objects to return
     * must match. If <code>null</code>, all objects contained in this object pool
     * are returned.
     * @return A set of objects whose names match the given regular expression.
     * If no such objects exist, the returned set is empty.
     */
    public Set<TCSObject<?>> getTCSObjects(Pattern regexp);
    /**
     * Returns a set of objects belonging to the given class.
     *
     * @param <T> The objects' type.
     * @return A set of objects belonging to the given class.
     */
    public <T extends TCSObject<T>> Set<T> getObjects();

    /**
     * Returns a set of objects belonging to the given class whose names match the
     * given regular expression.
     *
     * @param <T> The objects' type.
     * @param regexp The regular expression that the names of objects to return
     * must match. If <code>null</code>, all objects contained in this object pool
     * are returned.
     * @return A set of objects belonging to the given class whose names match the
     * given regular expression. If no such objects exist, the returned set is
     * empty.
     */
    public <T extends TCSObject<T>> Set<T> getObjects(Pattern regexp);
    /**
     * Returns a set of objects of the given class for which the given predicate is true.
     *
     * @param <T> The objects' type.
     * @param predicate The predicate that must be true for returned objects.
     * @return A set of objects of the given class for which the given predicate is true. If no such
     * objects exist, the returned set is empty.
     */
    public <T extends TCSObject<T>> Set<T> getObjects(@Nonnull Predicate<? super T> predicate);
}
