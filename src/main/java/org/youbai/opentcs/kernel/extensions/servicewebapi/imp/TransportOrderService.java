package org.youbai.opentcs.kernel.extensions.servicewebapi.imp;

import org.youbai.opentcs.access.rmi.services.RemoteTCSObjectService;
import org.youbai.opentcs.access.rmi.services.RemoteTransportOrderService;
import org.youbai.opentcs.access.to.order.OrderSequenceCreationTO;
import org.youbai.opentcs.access.to.order.TransportOrderCreationTO;
import org.youbai.opentcs.data.order.OrderSequence;
import org.youbai.opentcs.data.order.TransportOrder;
import org.youbai.opentcs.kernel.annotations.StandardTransportOrderServiceAnnotations;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResultBuilder;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.ResultCode;
import org.youbai.opentcs.kernel.extensions.servicewebapi.rest.RestTransportOrderService;
import org.youbai.opentcs.kernel.services.StandardTCSObjectService;
import org.youbai.opentcs.kernel.services.StandardTransportOrderService;

import javax.inject.Inject;

public class TransportOrderService implements RestTransportOrderService {

    @Inject
    RemoteTransportOrderService remoteTransportOrderService;

    @Inject@StandardTransportOrderServiceAnnotations
    StandardTransportOrderService standardTransportOrderService;

    @Override
    public AppResult<OrderSequence> createOrderSequence(OrderSequenceCreationTO to) {
        OrderSequence orderSequence =  remoteTransportOrderService.createOrderSequence(to);
        return AppResultBuilder.success(orderSequence, ResultCode.SUCCESS);
    }

    @Override
    public AppResult<TransportOrder> createTransportOrder(TransportOrderCreationTO to) {
        TransportOrder transportOrder = remoteTransportOrderService.createTransportOrder(to);
        return AppResultBuilder.success(transportOrder,ResultCode.SUCCESS);
    }

    @Override
    public AppResult<String> markOrderSequenceComplete(String ref) {
        OrderSequence orderSequence = standardTransportOrderService.fetchObjectNotNull(OrderSequence.class,ref);
        remoteTransportOrderService.markOrderSequenceComplete(orderSequence.getReference());
        return AppResultBuilder.success(ResultCode.SUCCESS);
    }
}
