package org.youbai;
import com.sun.xml.bind.api.impl.NameConverter;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.youbai.opentcs.kernel.KernelStarter;
import org.youbai.opentcs.kernel.workingset.TCSObjectPool;


import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/async")
public class EventResource {

    @Inject
    EventBus bus;


    @Inject
    KernelStarter kernelStarter;
    @Inject
    TCSObjectPool tcsObjectPool;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{name}")
    public String greeting(@PathParam String name) {
        return tcsObjectPool.getObjectOrNull(name).getName();
    }
}
