package org.youbai.opentcs.kernel.peripherals;

import org.youbai.opentcs.components.kernel.services.InternalPeripheralService;
import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.data.model.TCSResourceReference;
import org.youbai.opentcs.drivers.peripherals.PeripheralCommAdapter;
import org.youbai.opentcs.kernel.annotations.SimpleEventBusAnnotation;
import org.youbai.opentcs.util.event.EventBus;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
@ApplicationScoped
public class PeripheralControllerFactoryImp {


    @Inject
    InternalPeripheralService peripheralService;
    @Inject @SimpleEventBusAnnotation
    EventBus eventBus;

    @Produces
    PeripheralControllerFactory peripheralControllerFactory(){
        return new PeripheralControllerFactory() {
            @Override
            public DefaultPeripheralController createVehicleController(TCSResourceReference<Location> location, PeripheralCommAdapter commAdapter) {
                return new DefaultPeripheralController(location,commAdapter,peripheralService,eventBus);
            }
        };
    }
}
