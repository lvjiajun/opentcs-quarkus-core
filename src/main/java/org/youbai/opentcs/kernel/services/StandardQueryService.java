/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.services;

import java.util.HashMap;
import java.util.Map;
import static java.util.Objects.requireNonNull;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.quarkus.runtime.Startup;
import org.youbai.opentcs.components.kernel.Query;
import org.youbai.opentcs.components.kernel.QueryResponder;
import org.youbai.opentcs.components.kernel.services.InternalQueryService;
import org.youbai.opentcs.customizations.kernel.GlobalSyncObject;
import static org.youbai.opentcs.util.Assertions.checkArgument;

/**
 * The default implementation of the {@link InternalQueryService} interface.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
@Singleton
public class StandardQueryService
    implements InternalQueryService {

  /**
   * A global object to be used for synchronization within the kernel.
   */
  private final Object globalSyncObject;
  /**
   * The responders, by query type.
   */
  private final Map<Class<? extends Query<?>>, QueryResponder> respondersByQueryType
      = new HashMap<>();

  /**
   * Creates a new instance.
   *
   * @param globalSyncObject The kernel threads' global synchronization object.
   */

  public StandardQueryService(Object globalSyncObject) {
    this.globalSyncObject = requireNonNull(globalSyncObject, "globalSyncObject");
  }

  @Override
  public <T> T query(Query<T> query) {
    requireNonNull(query, "query");

    synchronized (globalSyncObject) {
      QueryResponder responder = respondersByQueryType.get(query.getClass());

      checkArgument(responder != null, "Query class not taken: %s", query.getClass().getName());
      return responder.query(query);
    }
  }

  @Override
  public void registerResponder(@Nonnull Class<? extends Query<?>> clazz,
                                @Nonnull QueryResponder responder) {
    requireNonNull(clazz, "clazz");
    requireNonNull(responder, "responder");

    synchronized (globalSyncObject) {
      checkArgument(!respondersByQueryType.containsKey(clazz),
                    "Query class already taken: %s",
                    clazz.getName());

      respondersByQueryType.put(clazz, responder);
    }
  }

  @Override
  public void unregisterResponder(@Nonnull Class<? extends Query<?>> clazz) {
    requireNonNull(clazz, "clazz");

    synchronized (globalSyncObject) {
      respondersByQueryType.remove(clazz);
    }
  }
}
