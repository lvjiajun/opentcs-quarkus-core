package org.youbai.opentcs.kernel.vehicles;

import org.youbai.opentcs.components.kernel.Scheduler;
import org.youbai.opentcs.components.kernel.services.DispatcherService;
import org.youbai.opentcs.components.kernel.services.InternalVehicleService;
import org.youbai.opentcs.components.kernel.services.NotificationService;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapter;
import org.youbai.opentcs.kernel.annotations.SimpleEventBusAnnotation;
import org.youbai.opentcs.kernel.annotations.StandardDispatcherServiceAnnotations;
import org.youbai.opentcs.kernel.annotations.StandardNotificationServiceAnnotation;
import org.youbai.opentcs.util.event.EventBus;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
public class VehicleControllerFactoryImp {


    @Inject
    InternalVehicleService vehicleService;
    @Inject@StandardNotificationServiceAnnotation
    NotificationService notificationService;
    @Inject@StandardDispatcherServiceAnnotations
    DispatcherService dispatcherService;
    @Inject@Named("bindScheduler")
    Scheduler scheduler;
    @Inject@SimpleEventBusAnnotation
    EventBus eventBus;
    @Inject
    VehicleControllerComponentsFactory componentsFactory;

    @Produces
    @ApplicationScoped
    public VehicleControllerFactory vehicleControllerFactory(){


        return new VehicleControllerFactory() {
            @Override
            public DefaultVehicleController createVehicleController(Vehicle vehicle, VehicleCommAdapter commAdapter) {
                return new DefaultVehicleController(vehicle,commAdapter,
                        vehicleService,notificationService,dispatcherService,scheduler,eventBus,componentsFactory);
            }
        };
    }
}
