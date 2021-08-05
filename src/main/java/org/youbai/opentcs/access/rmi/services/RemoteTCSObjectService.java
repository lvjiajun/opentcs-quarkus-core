/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.access.rmi.services;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.function.Predicate;

import org.youbai.opentcs.components.kernel.services.TCSObjectService;
import org.youbai.opentcs.data.ObjectHistory;
import org.youbai.opentcs.data.TCSObject;
import org.youbai.opentcs.data.TCSObjectReference;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Declares the methods provided by the {@link TCSObjectService} via RMI.
 *
 * <p>
 * The majority of the methods declared here have signatures analogous to their counterparts in
 * {@link TCSObjectService}, with an additional {@link ClientID} parameter which serves the purpose
 * of identifying the calling client and determining its permissions.
 * </p>
 * <p>
 * To avoid redundancy, the semantics of methods that only pass through their arguments are not
 * explicitly documented here again. See the corresponding API documentation in
 * {@link TCSObjectService} for these, instead.
 * </p>
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */

public interface RemoteTCSObjectService{

  <T extends TCSObject<T>> T fetchObject(Class<T> clazz,
                                         TCSObjectReference<T> ref);

  <T extends TCSObject<T>> T fetchObject(Class<T> clazz, String name);

  <T extends TCSObject<T>> Set<T> fetchObjects(Class<T> clazz);

  <T extends TCSObject<T>> Set<T> fetchObjects(
                                               Class<T> clazz,
                                               Predicate<? super T> predicate);

  void updateObjectProperty(TCSObjectReference<?> ref,
                            String key,
                            String value);

  void appendObjectHistoryEntry(TCSObjectReference<?> ref,
                                ObjectHistory.Entry entry);
}
