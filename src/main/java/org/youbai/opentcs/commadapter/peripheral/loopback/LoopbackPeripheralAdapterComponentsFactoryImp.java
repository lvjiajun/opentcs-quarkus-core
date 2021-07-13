package org.youbai.opentcs.commadapter.peripheral.loopback;

import org.youbai.opentcs.customizations.ApplicationEventBus;
import org.youbai.opentcs.customizations.kernel.KernelExecutor;
import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.data.model.TCSResourceReference;
import org.youbai.opentcs.kernel.annotations.SimpleEventBusAnnotation;
import org.youbai.opentcs.util.event.EventHandler;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.concurrent.ScheduledExecutorService;


public class LoopbackPeripheralAdapterComponentsFactoryImp {

    @SimpleEventBusAnnotation
    EventHandler eventHandler;

    @Inject
    ScheduledExecutorService kernelExecutor;

    @Produces
    LoopbackPeripheralAdapterComponentsFactory loopbackPeripheralAdapterComponentsFactory(){
        return new LoopbackPeripheralAdapterComponentsFactory() {
            @Override
            public LoopbackPeripheralCommAdapter createLoopbackCommAdapter(TCSResourceReference<Location> location) {
                return new LoopbackPeripheralCommAdapter(location,eventHandler,kernelExecutor);
            }
        };
    }
}
