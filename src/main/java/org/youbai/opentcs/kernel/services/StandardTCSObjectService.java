/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.services;

import java.util.HashSet;
import static java.util.Objects.requireNonNull;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.quarkus.runtime.Startup;
import org.youbai.opentcs.components.kernel.services.TCSObjectService;
import org.youbai.opentcs.data.ObjectHistory;
import org.youbai.opentcs.data.ObjectUnknownException;
import org.youbai.opentcs.data.TCSObject;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.kernel.GlobalSyncObject;
import org.youbai.opentcs.kernel.annotations.StandardTCSObjectAnnotations;
import org.youbai.opentcs.kernel.workingset.TCSObjectPool;

/**
 * This class is the standard implementation of the {@link TCSObjectService} interface.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */

@Singleton
@StandardTCSObjectAnnotations
public class StandardTCSObjectService
    implements TCSObjectService {

  /**
   * A global object to be used for synchronization within the kernel.
   */
  private final Object globalSyncObject;
  /**
   * The container of all course model and transport order objects.
   */
  private final TCSObjectPool globalObjectPool;

  /**
   * Creates a new instance.
   *
   * @param globalSyncObject The kernel threads' global synchronization object.
   * @param globalObjectPool The object pool to be used.
   */

  public StandardTCSObjectService(GlobalSyncObject globalSyncObject,
                                  TCSObjectPool globalObjectPool) {
    this.globalSyncObject = requireNonNull(globalSyncObject, "globalSyncObject");
    this.globalObjectPool = requireNonNull(globalObjectPool, "globalObjectPool");
  }

  @Override
  public <T extends TCSObject<T>> T fetchObject(Class<T> clazz, TCSObjectReference<T> ref) {
    synchronized (getGlobalSyncObject()) {
      return getGlobalObjectPool().getObjectOrNull(clazz, ref);
    }
  }

  @Override
  public <T extends TCSObject<T>> T fetchObject(Class<T> clazz, String name) {
    synchronized (getGlobalSyncObject()) {
      return getGlobalObjectPool().getObjectOrNull(clazz, name);
    }
  }

  @Override
  public <T extends TCSObject<T>> Set<T> fetchObjects(Class<T> clazz) {
    synchronized (getGlobalSyncObject()) {
      Set<T> objects = getGlobalObjectPool().getObjects(clazz);
      Set<T> copies = new HashSet<>();
      for (T object : objects) {
        copies.add(object);
      }
      return copies;
    }
  }

  @Override
  public <T extends TCSObject<T>> Set<T> fetchObjects(@Nonnull Class<T> clazz,
                                                      @Nonnull Predicate<? super T> predicate) {
    synchronized (getGlobalSyncObject()) {
      return getGlobalObjectPool().getObjects(clazz, predicate);
    }
  }

  @Override
  public void updateObjectProperty(TCSObjectReference<?> ref, String key, @Nullable String value)
      throws ObjectUnknownException {
    synchronized (getGlobalSyncObject()) {
      getGlobalObjectPool().setObjectProperty(ref, key, value);
    }
  }

  @Override
  public void appendObjectHistoryEntry(TCSObjectReference<?> ref, ObjectHistory.Entry entry)
      throws ObjectUnknownException {
    synchronized (getGlobalSyncObject()) {
      getGlobalObjectPool().appendObjectHistoryEntry(ref, entry);
    }
  }

  @Override
  public boolean contains(String objectName) {
    return getGlobalObjectPool().contains(objectName);
  }

  @Override
  public <T extends TCSObject<T>> T fetchObjectNotNull(Class<T> clazz, String name)throws ObjectUnknownException {
    return getGlobalObjectPool().getObject(clazz,name);
  }

  protected Object getGlobalSyncObject() {
    return globalSyncObject;
  }

  protected TCSObjectPool getGlobalObjectPool() {
    return globalObjectPool;
  }
}
