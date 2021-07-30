package org.youbai.opentcs.virtualvehicle;

import org.youbai.opentcs.customizations.kernel.KernelExecutor;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.kernel.annotations.ExecutorServiceAnnotations;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.ExecutorService;
@ApplicationScoped
public class LoopbackCommunicationAdapterImp {

    @Inject
    VirtualVehicleConfiguration configuration;
    @Inject@ExecutorServiceAnnotations
    ExecutorService kernelExecutor;
    @Produces
    public LoopbackAdapterComponentsFactory loopbackAdapterComponentsFactory(){
        return new LoopbackAdapterComponentsFactory() {
            @Override
            public LoopbackCommunicationAdapter createLoopbackCommAdapter(Vehicle vehicle) {
                return new LoopbackCommunicationAdapter(configuration,vehicle,kernelExecutor);
            }
        };
    }
}
