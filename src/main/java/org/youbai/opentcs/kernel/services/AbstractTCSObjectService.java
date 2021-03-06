/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.services;

import static java.util.Objects.requireNonNull;
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.youbai.opentcs.access.CredentialsException;
import org.youbai.opentcs.access.KernelRuntimeException;
import org.youbai.opentcs.components.kernel.services.TCSObjectService;
import org.youbai.opentcs.data.ObjectHistory;
import org.youbai.opentcs.data.ObjectUnknownException;
import org.youbai.opentcs.data.TCSObject;
import org.youbai.opentcs.data.TCSObjectReference;

/**
 * Delegate method calls to the {@link TCSObjectService} implementation.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public abstract class AbstractTCSObjectService
    implements TCSObjectService {

  /**
   * The tcs object service to delegate method calls to.
   */
  private final TCSObjectService objectService;

  /**
   * Creates a new instance.
   *
   * @param objectService The service to delegate method calls to.
   */
  public AbstractTCSObjectService(TCSObjectService objectService) {
    this.objectService = requireNonNull(objectService, "objectService");
  }

  @Override
  public <T extends TCSObject<T>> T fetchObject(Class<T> clazz, TCSObjectReference<T> ref)
      throws CredentialsException {
    return getObjectService().fetchObject(clazz, ref);
  }

  @Override
  public <T extends TCSObject<T>> T fetchObject(Class<T> clazz, String name)
      throws CredentialsException {
    return getObjectService().fetchObject(clazz, name);
  }

  @Override
  public <T extends TCSObject<T>> Set<T> fetchObjects(Class<T> clazz)
      throws CredentialsException {
    return getObjectService().fetchObjects(clazz);
  }

  @Override
  public <T extends TCSObject<T>> Set<T> fetchObjects(@Nonnull Class<T> clazz,
                                                      @Nonnull Predicate<? super T> predicate)
      throws CredentialsException {
    return getObjectService().fetchObjects(clazz, predicate);
  }

  @Override
  public void updateObjectProperty(TCSObjectReference<?> ref,
                                   String key,
                                   @Nullable String value)
      throws ObjectUnknownException, CredentialsException {
    getObjectService().updateObjectProperty(ref, key, value);
  }

  @Override
  public void appendObjectHistoryEntry(TCSObjectReference<?> ref, ObjectHistory.Entry entry)
      throws ObjectUnknownException, KernelRuntimeException {
    getObjectService().appendObjectHistoryEntry(ref, entry);
  }
  @Override
  public boolean contains(String objectName) {
    return getObjectService().contains(objectName);
  }

  @Override
  public <T extends TCSObject<T>> T fetchObjectNotNull(Class<T> clazz, String name)throws ObjectUnknownException{
    return getObjectService().fetchObject(clazz,name);
  }
  /**
   * Retruns the {@link TCSObjectService} implementation being used.
   *
   * @return The {@link TCSObjectService} implementation being used.
   */
  public TCSObjectService getObjectService() {
    return objectService;
  }

}
