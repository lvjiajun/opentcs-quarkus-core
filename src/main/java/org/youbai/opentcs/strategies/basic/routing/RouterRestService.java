package org.youbai.opentcs.strategies.basic.routing;

import org.youbai.opentcs.components.kernel.Router;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Point;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.data.order.DriveOrder;
import org.youbai.opentcs.data.order.Route;
import org.youbai.opentcs.data.order.TransportOrder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Path("Router")
public class RouterRestService {

    @DELETE
    @Path("topologyChanged")
    public void topologyChanged() {

    }
    @POST
    @Path("checkRoutability")
    public Set<Vehicle> checkRoutability(@Nonnull TransportOrder order) {
        return null;
    }

    @POST
    @Path("getRoute")
    public Optional<List<DriveOrder>> getRoute(@Nonnull Vehicle vehicle,
                                               @Nonnull Point sourcePoint,
                                               @Nonnull TransportOrder transportOrder) {
        return Optional.empty();
    }

    @POST
    @Path("getRoute")
    public Optional<Route> getRoute(@Nonnull Vehicle vehicle,
                                    @Nonnull Point sourcePoint,
                                    @Nonnull Point destinationPoint) {
        return Optional.empty();
    }
    @POST
    @Path(" getCosts")
    public long getCosts(@Nonnull Vehicle vehicle,
                         @Nonnull Point sourcePoint,
                         @Nonnull Point destinationPoint) {
        return 0;
    }
    @POST
    @Path("getCostsByPointRef")
    public long getCostsByPointRef(@Nonnull Vehicle vehicle,
                                   @Nonnull TCSObjectReference<Point> srcPointRef,
                                   @Nonnull TCSObjectReference<Point> dstPointRef) {
        return 0;
    }
    @POST
    @Path("selectRoute")
    public void selectRoute(@Nonnull Vehicle vehicle,
                            @Nullable List<DriveOrder> driveOrders) {

    }
    @POST
    @Path("getSelectedRoutes")
    public Map<Vehicle, List<DriveOrder>> getSelectedRoutes() {
        return null;
    }


    public Set<Point> getTargetedPoints() {
        return null;
    }
}
