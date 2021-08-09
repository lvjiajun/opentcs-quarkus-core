package org.youbai.opentcs.kernel.workingset.newObject;

import org.youbai.opentcs.data.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public interface KernelPool {
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
     * @param ref A reference to the object to return.
     * @return The referenced object, or <code>null</code>, if no such object exists in this pool.
     */
    @Nullable
    public TCSObject<?> getObjectOrNull(TCSObjectReference<?> ref);

    /**
     * Returns an object from the pool.
     *
     * @param ref A reference to the object to return.
     * @return The referenced object.
     * @throws ObjectUnknownException If the referenced object does not exist.
     */
    @Nonnull
    public TCSObject<?> getObject(TCSObjectReference<?> ref)
            throws ObjectUnknownException;

    /**
     * Returns an object from the pool.
     *
     * @param <T> The object's type.
     * @param clazz The class of the object to be returned.
     * @param ref A reference to the object to be returned.
     * @return The referenced object, or <code>null</code>, if no such object
     * exists in this pool or if an object exists but is not an instance of the
     * given class.
     */
    @Nullable
    public <T extends TCSObject<T>> T getObjectOrNull(Class<T> clazz, TCSObjectReference<T> ref);

    /**
     * Returns an object from the pool.
     *
     * @param <T> The object's type.
     * @param clazz The class of the object to be returned.
     * @param ref A reference to the object to be returned.
     * @return The referenced object.
     * @throws ObjectUnknownException If the referenced object does not exist, or if an object exists
     * but is not an instance of the given class.
     */
    @Nonnull
    public <T extends TCSObject<T>> T getObject(Class<T> clazz, TCSObjectReference<T> ref)
            throws ObjectUnknownException;

    /**
     * Returns an object from the pool.
     *
     * @param name The name of the object to return.
     * @return The object with the given name, or <code>null</code>, if no such
     * object exists in this pool.
     */
    @Nullable
    public TCSObject<?> getObjectOrNull(String name);

    /**
     * Returns an object from the pool.
     *
     * @param name The name of the object to return.
     * @return The object with the given name.
     * @throws ObjectUnknownException If the referenced object does not exist.
     */
    @Nonnull
    public TCSObject<?> getObject(String name)
            throws ObjectUnknownException;

    /**
     * Returns an object from the pool.
     *
     * @param <T> The object's type.
     * @param clazz The class of the object to be returned.
     * @param name The name of the object to be returned.
     * @return The named object, or <code>null</code>, if no such object
     * exists in this pool or if an object exists but is not an instance of the
     * given class.
     */
    @Nullable
    public <T extends TCSObject<T>> T getObjectOrNull(Class<T> clazz, String name);

    /**
     * Returns an object from the pool.
     *
     * @param <T> The object's type.
     * @param clazz The class of the object to be returned.
     * @param name The name of the object to be returned.
     * @return The named object.
     * @throws ObjectUnknownException If no object with the given name exists in this pool or if an
     * object exists but is not an instance of the given class.
     */
    @Nonnull
    public <T extends TCSObject<T>> T getObject(Class<T> clazz, String name)
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
    public Set<TCSObject<?>> getObjects(Pattern regexp);

    /**
     * Returns a set of objects belonging to the given class.
     *
     * @param <T> The objects' type.
     * @param clazz The class of the objects to be returned.
     * @return A set of objects belonging to the given class.
     */
    public <T extends TCSObject<T>> Set<T> getObjects(Class<T> clazz);

    /**
     * Returns a set of objects belonging to the given class whose names match the
     * given regular expression.
     *
     * @param <T> The objects' type.
     * @param clazz The class of the objects to be returned.
     * @param regexp The regular expression that the names of objects to return
     * must match. If <code>null</code>, all objects contained in this object pool
     * are returned.
     * @return A set of objects belonging to the given class whose names match the
     * given regular expression. If no such objects exist, the returned set is
     * empty.
     */
    public <T extends TCSObject<T>> Set<T> getObjects(Class<T> clazz, Pattern regexp);
    /**
     * Returns a set of objects of the given class for which the given predicate is true.
     *
     * @param <T> The objects' type.
     * @param clazz The class of the objects to be returned.
     * @param predicate The predicate that must be true for returned objects.
     * @return A set of objects of the given class for which the given predicate is true. If no such
     * objects exist, the returned set is empty.
     */
    public <T extends TCSObject<T>> Set<T> getObjects(@Nonnull Class<T> clazz,
                                                      @Nonnull Predicate<? super T> predicate);

    /**
     * Checks if this pool contains an object with the given name.
     *
     * @param objectName The name of the object whose existence in this pool is to
     * be checked.
     * @return <code>true</code> if, and only if, this pool contains an object
     * with the given name.
     */
    public boolean contains(String objectName);
    /**
     * Removes a referenced object from this pool.
     *
     * @param ref A reference to the object to be removed.
     * @return The object that was removed from the pool.
     * @throws ObjectUnknownException If the referenced object does not exist.
     */
    public TCSObject<?> removeObject(TCSObjectReference<?> ref)
            throws ObjectUnknownException;

    /**
     * Removes the objects with the given names from this pool.
     *
     * @param objectNames A set of names of objects to be removed.
     * @return The objects that were removed from the pool. If none were removed,
     * an empty set will be returned.
     */
    public Set<TCSObject<?>> removeObjects(Set<String> objectNames);

    /**
     * Sets a property for the referenced object.
     *
     * @param ref A reference to the object to be modified.
     * @param key The property's key/name.
     * @param value The property's value. If <code>null</code>, removes the
     * property from the object.
     * @throws ObjectUnknownException If the referenced object does not exist.
     */
    public void setObjectProperty(TCSObjectReference<?> ref, String key, String value)
            throws ObjectUnknownException;

    /**
     * Appends a history entry to the referenced object.
     *
     * @param ref A reference to the object to be modified.
     * @param entry The history entry to be appended.
     * @throws ObjectUnknownException If the referenced object does not exist.
     */
    public void appendObjectHistoryEntry(TCSObjectReference<?> ref, ObjectHistory.Entry entry)
            throws ObjectUnknownException;

    /**
     * Clears all of the referenced object's properties.
     *
     * @param ref A reference to the object to be modified.
     * @throws ObjectUnknownException If the referenced object does not exist.
     */
    public void clearObjectProperties(TCSObjectReference<?> ref)
            throws ObjectUnknownException;

    /**
     * Returns the number of objects kept in this pool.
     *
     * @return The number of objects kept in this pool.
     */
    public int size();

    /**
     * Checks if this pool does not contain any objects.
     *
     * @return {@code true} if, and only if, this pool does not contain any
     * objects.
     */
    public boolean isEmpty();
}
