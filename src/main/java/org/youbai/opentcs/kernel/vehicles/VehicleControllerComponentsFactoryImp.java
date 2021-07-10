package org.youbai.opentcs.kernel.vehicles;

import org.youbai.opentcs.components.kernel.services.PeripheralDispatcherService;
import org.youbai.opentcs.components.kernel.services.PeripheralJobService;
import org.youbai.opentcs.customizations.ApplicationEventBus;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.kernel.annotations.SimpleEventBusAnnotation;
import org.youbai.opentcs.kernel.annotations.StandardPeripheralJobServiceAnnotations;
import org.youbai.opentcs.util.event.EventSource;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
@ApplicationScoped
public class VehicleControllerComponentsFactoryImp {

    @StandardPeripheralJobServiceAnnotations
    PeripheralJobService peripheralJobService;
    @Inject
    PeripheralDispatcherService peripheralDispatcherService;
    @SimpleEventBusAnnotation
    EventSource eventSource;

    @Produces
    public VehicleControllerComponentsFactory vehicleControllerComponentsFactory(){
        return new VehicleControllerComponentsFactory() {
            @Override
            public PeripheralInteractor createPeripheralInteractor(TCSObjectReference<Vehicle> vehicleRef) {
                return new PeripheralInteractor(vehicleRef,peripheralJobService,peripheralDispatcherService,eventSource);
            }
        };
    }
}
