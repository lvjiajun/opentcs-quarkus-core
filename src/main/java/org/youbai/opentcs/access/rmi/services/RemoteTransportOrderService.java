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

import org.youbai.opentcs.access.to.order.OrderSequenceCreationTO;
import org.youbai.opentcs.access.to.order.TransportOrderCreationTO;
import org.youbai.opentcs.components.kernel.services.TransportOrderService;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.order.OrderSequence;
import org.youbai.opentcs.data.order.TransportOrder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Declares the methods provided by the {@link TransportOrderService} via RMI.
 *
 * <p>
 * The majority of the methods declared here have signatures analogous to their counterparts in
 * {@link TransportOrderService}, with an additional {@link ClientID} parameter which serves the
 * purpose of identifying the calling client and determining its permissions.
 * </p>
 * <p>
 * To avoid redundancy, the semantics of methods that only pass through their arguments are not
 * explicitly documented here again. See the corresponding API documentation in
 * {@link TransportOrderService} for these, instead.
 * </p>
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */

public interface RemoteTransportOrderService
    extends RemoteTCSObjectService{

  OrderSequence createOrderSequence(OrderSequenceCreationTO to);

  TransportOrder createTransportOrder(TransportOrderCreationTO to);

  void markOrderSequenceComplete(TCSObjectReference<OrderSequence> ref);
}
