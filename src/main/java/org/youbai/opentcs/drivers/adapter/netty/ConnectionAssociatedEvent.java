package org.youbai.opentcs.drivers.adapter.netty;

public class ConnectionAssociatedEvent {

    /**
     * The key a connection was associated to.
     */
    private final Object key;

    public ConnectionAssociatedEvent(Object key) {
        this.key = key;
    }

    public Object getKey() {
        return key;
    }
}
