package org.youbai.opentcs.drivers.adapter.netty.unetty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import static java.util.Objects.requireNonNull;

/**
 * Notifies a listener about connection state changes.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public class ClientConnectionDropNotifier
        extends ChannelDuplexHandler {

    private final ConnectionEventListener<?> connectionEventListener;

    public ClientConnectionDropNotifier(ConnectionEventListener<?> stateMessageHandler) {
        this.connectionEventListener = requireNonNull(stateMessageHandler, "stateMessageHandler");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        connectionEventListener.onDisconnect();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        if (evt instanceof IdleStateEvent) {
            if (((IdleStateEvent) evt).state() == IdleState.READER_IDLE) {
                connectionEventListener.onIdle();
            }
        }
        else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
