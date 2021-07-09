package org.youbai.opentcs.drivers.adapter.netty.spring;


/**
 * A user event triggered when a channel is being associated to a key.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
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
