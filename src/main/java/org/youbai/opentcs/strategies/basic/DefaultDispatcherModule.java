package org.youbai.opentcs.strategies.basic;

import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.data.order.TransportOrder;
import org.youbai.opentcs.strategies.basic.dispatching.AssignmentCandidate;
import org.youbai.opentcs.strategies.basic.dispatching.priorization.candidate.*;
import org.youbai.opentcs.strategies.basic.dispatching.priorization.transportorder.TransportOrderComparatorByAge;
import org.youbai.opentcs.strategies.basic.dispatching.priorization.transportorder.TransportOrderComparatorByDeadline;
import org.youbai.opentcs.strategies.basic.dispatching.priorization.transportorder.TransportOrderComparatorByName;
import org.youbai.opentcs.strategies.basic.dispatching.priorization.vehicle.VehicleComparatorByEnergyLevel;
import org.youbai.opentcs.strategies.basic.dispatching.priorization.vehicle.VehicleComparatorByName;
import org.youbai.opentcs.strategies.basic.dispatching.priorization.vehicle.VehicleComparatorIdleFirst;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.Comparator;
import java.util.Currency;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@ApplicationScoped
public class DefaultDispatcherModule {


    @Inject
    VehicleComparatorByEnergyLevel vehicleComparatorByEnergyLevel;
    @Inject
    VehicleComparatorByName vehicleComparatorByName;
    @Inject
    VehicleComparatorIdleFirst vehicleComparatorIdleFirst;

    @Inject
    TransportOrderComparatorByAge transportOrderComparatorByAge;
    @Inject
    TransportOrderComparatorByDeadline transportOrderComparatorByDeadline;
    @Inject
    TransportOrderComparatorByName transportOrderComparatorByName;


    @Inject
    CandidateComparatorByCompleteRoutingCosts candidateComparatorByCompleteRoutingCosts;
    @Inject
    CandidateComparatorByDeadline candidateComparatorByDeadline;
    @Inject
    CandidateComparatorByEnergyLevel candidateComparatorByEnergyLevel;
    @Inject
    CandidateComparatorByInitialRoutingCosts candidateComparatorByInitialRoutingCosts;
    @Inject
    CandidateComparatorByOrderAge candidateComparatorByOrderAge;
    @Inject
    CandidateComparatorByOrderName candidateComparatorByOrderName;
    @Inject
    CandidateComparatorByVehicleName candidateComparatorByVehicleName;
    @Inject
    CandidateComparatorIdleFirst candidateComparatorIdleFirst;

    @Produces
    Map<String, Comparator<Vehicle>> vehicleComparatorBinder(){
        ConcurrentHashMap<String, Comparator<Vehicle>> comparatorConcurrentHashMap = new ConcurrentHashMap<>();
        comparatorConcurrentHashMap.put(VehicleComparatorByEnergyLevel.CONFIGURATION_KEY,vehicleComparatorByEnergyLevel);
        comparatorConcurrentHashMap.put(VehicleComparatorByName.CONFIGURATION_KEY,vehicleComparatorByName);
        comparatorConcurrentHashMap.put(VehicleComparatorIdleFirst.CONFIGURATION_KEY,vehicleComparatorIdleFirst);
        return comparatorConcurrentHashMap;
    }
    @Produces
    Map<String,Comparator<TransportOrder>> orderComparatorBinder(){
        ConcurrentHashMap<String, Comparator<TransportOrder>> comparatorConcurrentHashMap = new ConcurrentHashMap<>();
        comparatorConcurrentHashMap.put(TransportOrderComparatorByAge.CONFIGURATION_KEY,transportOrderComparatorByAge);
        comparatorConcurrentHashMap.put(TransportOrderComparatorByDeadline.CONFIGURATION_KEY,transportOrderComparatorByDeadline);
        comparatorConcurrentHashMap.put(TransportOrderComparatorByName.CONFIGURATION_KEY,transportOrderComparatorByName);
        return comparatorConcurrentHashMap;
    }
    @Produces
    Map<String,Comparator<AssignmentCandidate>> candidateComparatorBinder(){
        ConcurrentHashMap<String, Comparator<AssignmentCandidate>> comparatorConcurrentHashMap = new ConcurrentHashMap<>();
        comparatorConcurrentHashMap.put(CandidateComparatorByCompleteRoutingCosts.CONFIGURATION_KEY,candidateComparatorByCompleteRoutingCosts);
        comparatorConcurrentHashMap.put(CandidateComparatorByDeadline.CONFIGURATION_KEY,candidateComparatorByDeadline);
        comparatorConcurrentHashMap.put(CandidateComparatorByEnergyLevel.CONFIGURATION_KEY,candidateComparatorByEnergyLevel);
        comparatorConcurrentHashMap.put(CandidateComparatorByInitialRoutingCosts.CONFIGURATION_KEY,candidateComparatorByInitialRoutingCosts);
        comparatorConcurrentHashMap.put(CandidateComparatorByOrderAge.CONFIGURATION_KEY,candidateComparatorByOrderAge);
        comparatorConcurrentHashMap.put(CandidateComparatorByOrderName.CONFIGURATION_KEY,candidateComparatorByOrderName);
        comparatorConcurrentHashMap.put(CandidateComparatorByVehicleName.CONFIGURATION_KEY,candidateComparatorByVehicleName);
        comparatorConcurrentHashMap.put(CandidateComparatorIdleFirst.CONFIGURATION_KEY,candidateComparatorIdleFirst);
        return comparatorConcurrentHashMap;
    }

}
