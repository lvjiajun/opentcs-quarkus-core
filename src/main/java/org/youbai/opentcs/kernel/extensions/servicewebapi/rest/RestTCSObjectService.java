package org.youbai.opentcs.kernel.extensions.servicewebapi.rest;

import org.youbai.opentcs.data.ObjectHistory;
import org.youbai.opentcs.data.TCSObject;
import org.youbai.opentcs.data.TCSObjectReference;

import java.util.Set;
import java.util.function.Predicate;

public interface RestTCSObjectService {
    <T extends TCSObject<T>> T fetchObject(Class<T> clazz,
                                           TCSObjectReference<T> ref);

    <T extends TCSObject<T>> T fetchObject(Class<T> clazz, String name);

    <T extends TCSObject<T>> Set<T> fetchObjects(Class<T> clazz);

    <T extends TCSObject<T>> Set<T> fetchObjects(
            Class<T> clazz,
            Predicate<? super T> predicate);

    void updateObjectProperty(TCSObjectReference<?> ref,
                              String key,
                              String value);

    void appendObjectHistoryEntry(TCSObjectReference<?> ref,
                                  ObjectHistory.Entry entry);
}
