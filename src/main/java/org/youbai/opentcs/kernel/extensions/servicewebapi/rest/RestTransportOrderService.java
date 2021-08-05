package org.youbai.opentcs.kernel.extensions.servicewebapi.rest;

import org.youbai.opentcs.access.to.order.OrderSequenceCreationTO;
import org.youbai.opentcs.access.to.order.TransportOrderCreationTO;
import org.youbai.opentcs.data.order.OrderSequence;
import org.youbai.opentcs.data.order.TransportOrder;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

@Path("TransportOrderService")
public interface RestTransportOrderService {
    @POST
    @Path("createOrderSequence")
    AppResult<OrderSequence> createOrderSequence(OrderSequenceCreationTO to);
    @POST
    @Path("createTransportOrder")
    AppResult<TransportOrder> createTransportOrder(TransportOrderCreationTO to);
    @PUT
    @Path("markOrderSequenceComplete")
    AppResult<String> markOrderSequenceComplete(String ref);
}
