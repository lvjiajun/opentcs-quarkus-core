package org.youbai.opentcs.virtualvehicle;

import org.youbai.opentcs.customizations.kernel.KernelExecutor;
import org.youbai.opentcs.data.model.Vehicle;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.ExecutorService;
@ApplicationScoped
public class LoopbackCommunicationAdapterImp {

    @Inject
    LoopbackAdapterComponentsFactory componentsFactory;
    @Inject
    VirtualVehicleConfiguration configuration;
    @Inject@Named("ExecutorService")
    ExecutorService kernelExecutor;
    @Produces
    public LoopbackAdapterComponentsFactory loopbackAdapterComponentsFactory(){
        return new LoopbackAdapterComponentsFactory() {
            @Override
            public LoopbackCommunicationAdapter createLoopbackCommAdapter(Vehicle vehicle) {
                return new LoopbackCommunicationAdapter(componentsFactory,configuration,vehicle,kernelExecutor);
            }
        };
    }
}
