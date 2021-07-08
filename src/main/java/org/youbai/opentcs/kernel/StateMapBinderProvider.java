package org.youbai.opentcs.kernel;

import org.youbai.opentcs.access.Kernel;
import org.youbai.opentcs.kernel.annotations.StateMapBinderProviderAnnotations;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@StateMapBinderProviderAnnotations
public class StateMapBinderProvider implements Map {

    private final KernelStateShutdown kernelStateShutdown;
    private final KernelStateModelling kernelStateModelling;
    private final KernelStateOperating kernelStateOperating;
    private final Map stateMapBinderProvider = new ConcurrentHashMap();

    @Inject
    public StateMapBinderProvider(KernelStateShutdown kernelStateShutdown,
                                  KernelStateModelling kernelStateModelling,
                                  KernelStateOperating kernelStateOperating) {
        this.kernelStateShutdown = kernelStateShutdown;
        this.kernelStateModelling = kernelStateModelling;
        this.kernelStateOperating = kernelStateOperating;
        stateMapBinderProvider.put(Kernel.State.SHUTDOWN,this.kernelStateShutdown);
        stateMapBinderProvider.put(Kernel.State.MODELLING,this.kernelStateModelling);
        stateMapBinderProvider.put(Kernel.State.OPERATING,this.kernelStateOperating);
    }


    @Override
    public int size() {
        return stateMapBinderProvider.size();
    }

    @Override
    public boolean isEmpty() {
        return stateMapBinderProvider.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return stateMapBinderProvider.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return stateMapBinderProvider.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return stateMapBinderProvider.get(key);
    }

    @Override
    public Object put(Object key, Object value) {
        return stateMapBinderProvider.put(key,value);
    }

    @Override
    public Object remove(Object key) {
        return stateMapBinderProvider.remove(key);
    }

    @Override
    public void putAll(Map m) {
        stateMapBinderProvider.putAll(m);
    }

    @Override
    public void clear() {
        stateMapBinderProvider.clear();
    }

    @Override
    public Set keySet() {
        return stateMapBinderProvider.keySet();
    }

    @Override
    public Collection values() {
        return stateMapBinderProvider.values();
    }

    @Override
    public Set<Entry> entrySet() {
        return stateMapBinderProvider.entrySet();
    }
}
