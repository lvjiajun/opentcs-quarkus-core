package org.youbai.opentcs.kernel.workingset.newObject;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.youbai.opentcs.data.ObjectUnknownException;
import org.youbai.opentcs.data.TCSObject;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Block;
import org.youbai.opentcs.util.event.EventHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class BlockPool
        extends TCSResourcePool
        implements ResourcePool{

    /**
     * Creates a new instance that uses the given event handler.
     *
     * @param eventHandler
     * @param objectsByName
     */
    public BlockPool(EventHandler eventHandler, RMap<String, TCSObject<?>> objectsByName) {
        super(eventHandler, objectsByName);
    }

    @Nullable
    @Override
    public <T extends TCSObject<T>> T getOrNull(TCSObjectReference<T> ref) {
        return null;
    }

    @Nonnull
    @Override
    public <T extends TCSObject<T>> T get(TCSObjectReference<T> ref) throws ObjectUnknownException {
        return null;
    }

    @Nullable
    @Override
    public TCSObject<?> getTCSObjectOrNull(String name) {
        return null;
    }

    @Nonnull
    @Override
    public TCSObject<?> getTCSObject(String name) throws ObjectUnknownException {
        return null;
    }

    @Override
    public Set<TCSObject<?>> getTCSObjects(Pattern regexp) {
        return null;
    }

    @Override
    public <T extends TCSObject<T>> Set<T> getObjects() {
        return null;
    }

    @Override
    public <T extends TCSObject<T>> Set<T> getObjects(@Nonnull Predicate<? super T> predicate) {
        return null;
    }
}
