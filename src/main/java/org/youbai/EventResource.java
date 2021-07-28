package org.youbai;
import com.esotericsoftware.kryo.Kryo;
import com.sun.xml.bind.api.impl.NameConverter;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.youbai.opentcs.components.kernel.services.TCSObjectService;
import org.youbai.opentcs.data.TCSObject;
import org.youbai.opentcs.data.model.Point;
import org.youbai.opentcs.kernel.KernelStarter;
import org.youbai.opentcs.kernel.annotations.StandardTCSObjectAnnotations;
import org.youbai.opentcs.kernel.workingset.TCSObjectPool;


import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Set;

@Path("/async")
public class EventResource {

    @Inject
    EventBus bus;


    @Inject
    KernelStarter kernelStarter;
    @Inject
    TCSObjectPool tcsObjectPool;

    @Inject@StandardTCSObjectAnnotations
    TCSObjectService tcsObjectService;
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{name}")
    public String greeting(@PathParam String name) {
        return tcsObjectService.fetchObject(Point.class,name).getPosition().toString();
    }

}
