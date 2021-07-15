package org.youbai.opentcs.drivers.adapter;

import org.youbai.opentcs.common.extend.telegrams.RequestResponseMatcher;
import org.youbai.opentcs.common.extend.telegrams.StateRequesterTask;
import org.youbai.opentcs.common.extend.telegrams.TelegramSender;
import org.youbai.opentcs.data.model.Vehicle;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;

@ApplicationScoped
public class AdapterComponentsFactoryImp {


    @Inject
    OrderMapper orderMapper;
    @Inject
    AdapterComponentsFactory componentsFactory;
    @Inject@Named("ExecutorService")
    ExecutorService kernelExecutor;


    @Produces
    @ApplicationScoped
    public AdapterComponentsFactory adapterComponentsFactory(){
        return new AdapterComponentsFactory() {
            @Override
            public CommAdapter createCommAdapter(Vehicle vehicle) {
                return new CommAdapter(vehicle,orderMapper,componentsFactory,kernelExecutor);
            }

            @Override
            public RequestResponseMatcher createRequestResponseMatcher(TelegramSender telegramSender) {
                return new RequestResponseMatcher(telegramSender);
            }

            @Override
            public StateRequesterTask createStateRequesterTask(ActionListener stateRequestAction) {
                return new StateRequesterTask(stateRequestAction);
            }
        };
    }
}
