package org.youbai.opentcs.kernel.extensions.rmi;

import org.youbai.opentcs.access.rmi.factories.NullSocketFactoryProvider;
import org.youbai.opentcs.access.rmi.factories.SecureSocketFactoryProvider;
import org.youbai.opentcs.access.rmi.factories.SocketFactoryProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@ApplicationScoped
public class RmiServicesModule {

    @Inject
    RmiKernelInterfaceConfiguration rmiKernelInterfaceConfiguration;

    @Inject
    SecureSocketFactoryProvider secureSocketFactoryProvider;

    @Inject
    NullSocketFactoryProvider nullSocketFactoryProvider;

    @Produces
    @Singleton
    @Named("socketFactoryProvider")
    SocketFactoryProvider socketFactoryProvider(){
        if (rmiKernelInterfaceConfiguration.useSsl()){
            return secureSocketFactoryProvider;
        }else{
            return nullSocketFactoryProvider;
        }
    }

}
