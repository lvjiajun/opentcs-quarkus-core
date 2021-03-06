/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.workingset;

import io.quarkus.runtime.Startup;
import io.vertx.core.eventbus.EventBus;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.youbai.opentcs.data.*;
import org.youbai.opentcs.kernel.annotations.SimpleEventBusAnnotation;
import org.youbai.opentcs.util.UniqueStringGenerator;
import org.youbai.opentcs.util.event.EventHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.youbai.opentcs.util.Assertions.checkArgument;

/**
 * A container for <code>TCSObject</code>s belonging together.
 * It keeps all basic data objects (model data, transport order data and system
 * messages) and ensures these objects have unique IDs and names.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
@Singleton
public class TCSObjectPool {

  /**
   * This class's Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(TCSObjectPool.class);
  /**
   * The objects contained in this pool, mapped by their names.
   */
//  private final Map<String, TCSObject<?>> objectsByName = new ConcurrentHashMap<>();
  /**
   * The generator providing unique names for objects in this pool.
   */
  private final UniqueStringGenerator<?> objectNameGenerator = new UniqueStringGenerator<>();


  private final EventHandler eventHandler;


  RMap<String, TCSObject<?>> objectsByName;

  /**
   * Creates a new instance that uses the given event handler.
   *
   * @param eventHandler
   */

  public TCSObjectPool(@SimpleEventBusAnnotation EventHandler eventHandler,
                       RedissonClient redisson) {
    this.eventHandler = eventHandler;
    objectsByName = redisson.getMap("map");
  }

  /**
   * Adds a new object to the pool.
   *
   * @param newObject The object to be added to the pool.
   * @throws ObjectExistsException If an object with the same ID or the same
   * name as the new one already exists in this pool.
   */
  public void addObject(TCSObject<?> newObject)
      throws ObjectExistsException {
    requireNonNull(newObject, "newObject");

    if (objectsByName.containsKey(newObject.getName())) {
      throw new ObjectExistsException("Object name " + newObject.getName() + " already exists.");
    }
    objectsByName.put(newObject.getName(), newObject);
    objectNameGenerator.addString(newObject.getName());
  }

  public <E extends TCSObject<E>> E replaceObject(E object) {
    requireNonNull(object, "object");
    checkArgument(objectsByName.containsKey(object.getName()),
                  "Object named '%s' does not exist",
                  object.getName());

    objectsByName.put(object.getName(), object);
    return object;
  }

  /**
   * Returns an object from the pool.
   *
   * @param ref A reference to the object to return.
   * @return The referenced object, or <code>null</code>, if no such object exists in this pool.
   */
  @Nullable
  public TCSObject<?> getObjectOrNull(TCSObjectReference<?> ref) {
    requireNonNull(ref);

    return objectsByName.get(ref.getName());
  }

  /**
   * Returns an object from the pool.
   *
   * @param ref A reference to the object to return.
   * @return The referenced object.
   * @throws ObjectUnknownException If the referenced object does not exist.
   */
  @Nonnull
  public TCSObject<?> getObject(TCSObjectReference<?> ref)
      throws ObjectUnknownException {
    TCSObject<?> result = getObjectOrNull(ref);
    if (result == null) {
      throw new ObjectUnknownException(ref);
    }
    return result;
  }

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
  public <T extends TCSObject<T>> T getObjectOrNull(Class<T> clazz, TCSObjectReference<T> ref) {
    requireNonNull(clazz, "clazz");
    requireNonNull(ref, "ref");

    TCSObject<?> result = objectsByName.get(ref.getName());
    if (clazz.isInstance(result)) {
      return clazz.cast(result);
    }
    else {
      return null;
    }
  }

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
      throws ObjectUnknownException {
    T result = getObjectOrNull(clazz, ref);
    if (result == null) {
      throw new ObjectUnknownException(ref);
    }
    return result;
  }

  /**
   * Returns an object from the pool.
   *
   * @param name The name of the object to return.
   * @return The object with the given name, or <code>null</code>, if no such
   * object exists in this pool.
   */
  @Nullable
  public TCSObject<?> getObjectOrNull(String name) {
    requireNonNull(name, "name");

    return objectsByName.get(name);
  }

  /**
   * Returns an object from the pool.
   *
   * @param name The name of the object to return.
   * @return The object with the given name.
   * @throws ObjectUnknownException If the referenced object does not exist.
   */
  @Nonnull
  public TCSObject<?> getObject(String name)
      throws ObjectUnknownException {
    TCSObject<?> result = getObjectOrNull(name);
    if (result == null) {
      throw new ObjectUnknownException(name);
    }
    return result;
  }

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
  public <T extends TCSObject<T>> T getObjectOrNull(Class<T> clazz, String name) {
    requireNonNull(clazz, "clazz");
    requireNonNull(name, "name");

    TCSObject<?> result = objectsByName.get(name);
    if (clazz.isInstance(result)) {
      return clazz.cast(result);
    }
    else {
      return null;
    }
  }

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
      throws ObjectUnknownException {
    T result = getObjectOrNull(clazz, name);
    if (result == null) {
      throw new ObjectUnknownException(name);
    }
    return result;
  }

  /**
   * Returns a set of objects whose names match the given regular expression.
   *
   * @param regexp The regular expression that the names of objects to return
   * must match. If <code>null</code>, all objects contained in this object pool
   * are returned.
   * @return A set of objects whose names match the given regular expression.
   * If no such objects exist, the returned set is empty.
   */
  public Set<TCSObject<?>> getObjects(Pattern regexp) {
    Set<TCSObject<?>> result = new HashSet<>();
    if (regexp == null) {
      result.addAll(objectsByName.values());
    }
    else {
      for (TCSObject<?> curObject : objectsByName.values()) {
        if (regexp.matcher(curObject.getName()).matches()) {
          result.add(curObject);
        }
      }
    }
    return result;
  }

  /**
   * Returns a set of objects belonging to the given class.
   *
   * @param <T> The objects' type.
   * @param clazz The class of the objects to be returned.
   * @return A set of objects belonging to the given class.
   */
  public <T extends TCSObject<T>> Set<T> getObjects(Class<T> clazz) {
    return getObjects(clazz, (Pattern) null);
  }

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
  public <T extends TCSObject<T>> Set<T> getObjects(Class<T> clazz, Pattern regexp) {
    requireNonNull(clazz, "clazz");

    Set<T> result = new HashSet<>();
    for (TCSObject<?> curObject : objectsByName.values()) {
      if (clazz.isInstance(curObject)
          && (regexp == null
              || regexp.matcher(curObject.getName()).matches())) {
        result.add(clazz.cast(curObject));
      }
    }
    return result;
  }
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
                                                    @Nonnull Predicate<? super T> predicate) {
    requireNonNull(clazz, "clazz");
    requireNonNull(predicate, "predicate");

    return objectsByName.values().stream()
        .filter(obj -> clazz.isInstance(obj))
        .map(obj -> clazz.cast(obj))
        .filter(predicate)
        .collect(Collectors.toSet());
  }

  /**
   * Checks if this pool contains an object with the given name.
   *
   * @param objectName The name of the object whose existence in this pool is to
   * be checked.
   * @return <code>true</code> if, and only if, this pool contains an object
   * with the given name.
   */
  public boolean contains(String objectName) {
    requireNonNull(objectName, "objectName");

    return objectsByName.containsKey(objectName);
  }
  /**
   * Removes a referenced object from this pool.
   *
   * @param ref A reference to the object to be removed.
   * @return The object that was removed from the pool.
   * @throws ObjectUnknownException If the referenced object does not exist.
   */
  public TCSObject<?> removeObject(TCSObjectReference<?> ref)
      throws ObjectUnknownException {
    requireNonNull(ref, "ref");

    TCSObject<?> rmObject = objectsByName.remove(ref.getName());
    if (rmObject == null) {
      throw new ObjectUnknownException(ref);
    }
    objectNameGenerator.removeString(rmObject.getName());
    return rmObject;
  }

  /**
   * Removes the objects with the given names from this pool.
   *
   * @param objectNames A set of names of objects to be removed.
   * @return The objects that were removed from the pool. If none were removed,
   * an empty set will be returned.
   */
  public Set<TCSObject<?>> removeObjects(Set<String> objectNames) {
    requireNonNull(objectNames, "objectNames");

    Set<TCSObject<?>> result = new HashSet<>();
    for (String curName : objectNames) {
      TCSObject<?> removedObject = objectsByName.remove(curName);
      if (removedObject != null) {
        result.add(removedObject);
        objectNameGenerator.removeString(removedObject.getName());
      }
    }
    return result;
  }

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
      throws ObjectUnknownException {
    requireNonNull(ref, "ref");

    TCSObject<?> object = objectsByName.get(ref.getName());
    if (object == null) {
      throw new ObjectUnknownException("No object with name " + ref.getName());
    }
    TCSObject<?> previousState = object;
    LOG.debug("Setting property on object named '{}': key='{}', value='{}'",
              ref.getName(),
              key,
              value);
    object = object.withProperty(key, value);
    objectsByName.put(object.getName(), object);
    emitObjectEvent(object, previousState, TCSObjectEvent.Type.OBJECT_MODIFIED);
  }

  /**
   * Appends a history entry to the referenced object.
   *
   * @param ref A reference to the object to be modified.
   * @param entry The history entry to be appended.
   * @throws ObjectUnknownException If the referenced object does not exist.
   */
  public void appendObjectHistoryEntry(TCSObjectReference<?> ref, ObjectHistory.Entry entry)
      throws ObjectUnknownException {
    requireNonNull(ref, "ref");

    TCSObject<?> object = objectsByName.get(ref.getName());
    if (object == null) {
      throw new ObjectUnknownException("No object with name " + ref.getName());
    }
    TCSObject<?> previousState = object;
    LOG.debug("Appending history entry to object named '{}': {}", ref.getName(), entry);
    object = object.withHistoryEntry(entry);
    objectsByName.put(object.getName(), object);
    emitObjectEvent(object, previousState, TCSObjectEvent.Type.OBJECT_MODIFIED);
  }

  /**
   * Clears all of the referenced object's properties.
   *
   * @param ref A reference to the object to be modified.
   * @throws ObjectUnknownException If the referenced object does not exist.
   */
  public void clearObjectProperties(TCSObjectReference<?> ref)
      throws ObjectUnknownException {
    requireNonNull(ref, "ref");

    TCSObject<?> object = objectsByName.get(ref.getName());
    if (object == null) {
      throw new ObjectUnknownException("No object with name " + ref.getName());
    }
    TCSObject<?> previousState = object;
    object = object.withProperties(new HashMap<>());
    objectsByName.put(object.getName(), object);
    emitObjectEvent(object, previousState, TCSObjectEvent.Type.OBJECT_MODIFIED);
  }

  /**
   * Returns the number of objects kept in this pool.
   *
   * @return The number of objects kept in this pool.
   */
  public int size() {
    return objectsByName.size();
  }

  /**
   * Checks if this pool does not contain any objects.
   *
   * @return {@code true} if, and only if, this pool does not contain any
   * objects.
   */
  public boolean isEmpty() {
    return objectsByName.isEmpty();
  }

  /**
   * Emits an event for the given object with the given type.
   *
   * @param currentObjectState The current state of the object to emit an event
   * for.
   * @param previousObjectState The previous state of the object to emit an
   * event for.
   * @param evtType The type of event to emit.
   */
  public void emitObjectEvent(TCSObject<?> currentObjectState,
                              TCSObject<?> previousObjectState,
                              TCSObjectEvent.Type evtType) {

    eventHandler.onEvent(new TCSObjectEvent(currentObjectState, previousObjectState, evtType));
  }
}
