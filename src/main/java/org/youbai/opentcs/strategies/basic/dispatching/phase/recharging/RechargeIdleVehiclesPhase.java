/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.dispatching.phase.recharging;

import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.youbai.opentcs.access.to.order.DestinationCreationTO;
import org.youbai.opentcs.access.to.order.TransportOrderCreationTO;
import org.youbai.opentcs.components.kernel.Router;
import org.youbai.opentcs.components.kernel.services.InternalTransportOrderService;
import org.youbai.opentcs.data.model.Point;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.data.order.DriveOrder;
import org.youbai.opentcs.data.order.TransportOrder;
import org.youbai.opentcs.kernel.annotations.StandardTransportOrderServiceAnnotations;
import org.youbai.opentcs.strategies.basic.dispatching.AssignmentCandidate;
import org.youbai.opentcs.strategies.basic.dispatching.DefaultDispatcherConfiguration;
import org.youbai.opentcs.strategies.basic.dispatching.Phase;
import org.youbai.opentcs.strategies.basic.dispatching.TransportOrderUtil;
import org.youbai.opentcs.strategies.basic.dispatching.selection.candidates.CompositeAssignmentCandidateSelectionFilter;
import org.youbai.opentcs.strategies.basic.dispatching.selection.vehicles.CompositeRechargeVehicleSelectionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates recharging orders for any vehicles with a degraded energy level.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
@ApplicationScoped
public class RechargeIdleVehiclesPhase
    implements Phase {

  /**
   * This class's Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(RechargeIdleVehiclesPhase.class);
  /**
   * The transport order service.
   */
  @Inject@StandardTransportOrderServiceAnnotations
  InternalTransportOrderService orderService;
  /**
   * The strategy used for finding suitable recharge locations.
   */
  @Inject
  RechargePositionSupplier rechargePosSupplier;
  /**
   * The Router instance calculating route costs.
   */
  @Inject
  Router router;
  /**
   * A collection of predicates for filtering assignment candidates.
   */
  @Inject
  CompositeAssignmentCandidateSelectionFilter assignmentCandidateSelectionFilter;
  @Inject
  CompositeRechargeVehicleSelectionFilter vehicleSelectionFilter;
  @Inject
  TransportOrderUtil transportOrderUtil;
  /**
   * The dispatcher configuration.
   */
  private final DefaultDispatcherConfiguration configuration;
  /**
   * Indicates whether this component is initialized.
   */
  private boolean initialized;


  public RechargeIdleVehiclesPhase(
      DefaultDispatcherConfiguration configuration) {
    this.configuration = requireNonNull(configuration, "configuration");
  }

  @Override
  public void initialize() {
    if (isInitialized()) {
      return;
    }

    rechargePosSupplier.initialize();

    initialized = true;
  }

  @Override
  public boolean isInitialized() {
    return initialized;
  }

  @Override
  public void terminate() {
    if (!isInitialized()) {
      return;
    }

    rechargePosSupplier.terminate();

    initialized = false;
  }

  @Override
  public void run() {
    if (!configuration.rechargeIdleVehicles()) {
      return;
    }

    orderService.fetchObjects(Vehicle.class).stream()
        .filter(vehicle -> vehicleSelectionFilter.apply(vehicle).isEmpty())
        .forEach(vehicle -> createRechargeOrder(vehicle));
  }

  private void createRechargeOrder(Vehicle vehicle) {
    List<DriveOrder.Destination> rechargeDests = rechargePosSupplier.findRechargeSequence(vehicle);
    LOG.debug("Recharge sequence for {}: {}", vehicle, rechargeDests);

    if (rechargeDests.isEmpty()) {
      LOG.info("{}: Did not find a suitable recharge sequence.", vehicle.getName());
      return;
    }

    List<DestinationCreationTO> chargeDests = new ArrayList<>(rechargeDests.size());
    for (DriveOrder.Destination dest : rechargeDests) {
      chargeDests.add(
          new DestinationCreationTO(dest.getDestination().getName(), dest.getOperation())
              .withProperties(dest.getProperties())
      );
    }
    // Create a transport order for recharging and verify its processability.
    // The recharge order may be withdrawn unless its energy level is critical.
    TransportOrder rechargeOrder = orderService.createTransportOrder(
        new TransportOrderCreationTO("Recharge-", chargeDests)
            .withIncompleteName(true)
            .withIntendedVehicleName(vehicle.getName())
            .withDispensable(!vehicle.isEnergyLevelCritical())
    );

    Point vehiclePosition = orderService.fetchObject(Point.class, vehicle.getCurrentPosition());
    Optional<AssignmentCandidate> candidate = computeCandidate(vehicle,
                                                               vehiclePosition,
                                                               rechargeOrder)
        .filter(c -> assignmentCandidateSelectionFilter.apply(c).isEmpty());
    // XXX Change this to Optional.ifPresentOrElse() once we're at Java 9+.
    if (candidate.isPresent()) {
      transportOrderUtil.assignTransportOrder(candidate.get().getVehicle(),
                                              candidate.get().getTransportOrder(),
                                              candidate.get().getDriveOrders());
    }
    else {
      // Mark the order as failed, since the vehicle cannot execute it.
      orderService.updateTransportOrderState(rechargeOrder.getReference(),
                                             TransportOrder.State.FAILED);
    }
  }

  private Optional<AssignmentCandidate> computeCandidate(Vehicle vehicle,
                                                         Point vehiclePosition,
                                                         TransportOrder order) {
    return router.getRoute(vehicle, vehiclePosition, order)
        .map(driveOrders -> new AssignmentCandidate(vehicle, order, driveOrders));
  }
}
