package org.youbai.opentcs.commadapter.peripheral.loopback;

import org.youbai.opentcs.customizations.ApplicationEventBus;
import org.youbai.opentcs.customizations.kernel.KernelExecutor;
import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.data.model.TCSResourceReference;
import org.youbai.opentcs.kernel.annotations.SimpleEventBusAnnotation;
import org.youbai.opentcs.util.event.EventHandler;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import java.util.concurrent.ScheduledExecutorService;

@ApplicationScoped
public class LoopbackPeripheralAdapterComponentsFactoryImp {

    @SimpleEventBusAnnotation
    EventHandler eventHandler;

    ScheduledExecutorService kernelExecutor;

    @Dependent
    LoopbackPeripheralAdapterComponentsFactory loopbackPeripheralAdapterComponentsFactory(){
        return new LoopbackPeripheralAdapterComponentsFactory() {
            @Override
            public LoopbackPeripheralCommAdapter createLoopbackCommAdapter(TCSResourceReference<Location> location) {
                return new LoopbackPeripheralCommAdapter(location,eventHandler,kernelExecutor);
            }
        };
    }
}
