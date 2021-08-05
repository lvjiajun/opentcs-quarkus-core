package org.youbai.opentcs.kernel.extensions.servicewebapi.rest;

import org.youbai.opentcs.components.kernel.Query;

public interface RestQueryService {
    <T> T query(Query<T> query);
}
