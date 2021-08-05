/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.extensions.rmi;

import static java.util.Objects.requireNonNull;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import javax.enterprise.context.ApplicationScoped;

import org.youbai.opentcs.access.rmi.services.RemoteTCSObjectService;
import org.youbai.opentcs.components.kernel.services.TCSObjectService;
import org.youbai.opentcs.customizations.kernel.KernelExecutor;
import org.youbai.opentcs.data.ObjectHistory;
import org.youbai.opentcs.data.TCSObject;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.kernel.annotations.ExecutorServiceAnnotations;
import org.youbai.opentcs.kernel.annotations.StandardTCSObjectAnnotations;

/**
 * This class is the standard implementation of the {@link RemoteTCSObjectService} interface.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public abstract class StandardRemoteTCSObjectService
    extends KernelRemoteService
    implements RemoteTCSObjectService {

  /**
   * The object service to invoke methods on.
   */
  private final TCSObjectService objectService;
  /**
   * Executes tasks modifying kernel data.
   */
  private final ExecutorService kernelExecutor;

  /**
   * Creates a new instance.
   *
   * @param objectService The object service.
   * @param kernelExecutor Executes tasks modifying kernel data.
   */
  public StandardRemoteTCSObjectService(TCSObjectService objectService,
                                        ExecutorService kernelExecutor) {
    this.objectService = requireNonNull(objectService, "objectService");
    this.kernelExecutor = requireNonNull(kernelExecutor, "kernelExecutor");
  }

  @Override
  public <T extends TCSObject<T>> T fetchObject(Class<T> clazz,
                                                TCSObjectReference<T> ref) {


    return objectService.fetchObject(clazz, ref);
  }

  @Override
  public <T extends TCSObject<T>> T fetchObject(Class<T> clazz, String name) {
    return objectService.fetchObject(clazz, name);
  }

  @Override
  public <T extends TCSObject<T>> Set<T> fetchObjects( Class<T> clazz) {
    return objectService.fetchObjects(clazz);
  }

  @Override
  public <T extends TCSObject<T>> Set<T> fetchObjects(Class<T> clazz,
                                                      Predicate<? super T> predicate) {
    return objectService.fetchObjects(clazz, predicate);
  }

  @Override
  public void updateObjectProperty(TCSObjectReference<?> ref,
                                   String key,
                                   @Nullable String value) {
    try {
      kernelExecutor.submit(() -> objectService.updateObjectProperty(ref, key, value)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public void appendObjectHistoryEntry(TCSObjectReference<?> ref,
                                       ObjectHistory.Entry entry) {
    try {
      kernelExecutor.submit(() -> objectService.appendObjectHistoryEntry(ref, entry)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

}
