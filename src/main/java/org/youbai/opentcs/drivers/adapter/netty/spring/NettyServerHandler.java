package org.youbai.opentcs.drivers.adapter.netty.spring;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.youbai.opentcs.drivers.adapter.netty.annotations.BussinessAnnotation;
import org.youbai.opentcs.drivers.adapter.netty.annotations.CloseAnnotation;
import org.youbai.opentcs.drivers.adapter.netty.annotations.ExceptionAnnotation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@Sharable
@ApplicationScoped
public class NettyServerHandler extends SimpleChannelInboundHandler<String>{


    private final Handler closeFutureHandler;


    private final Handler exceptionFutureHandler;


    private final Handler bussinessFutureHandler;

    @Inject
    public NettyServerHandler(
            @CloseAnnotation Handler closeFutureHandler,
            @ExceptionAnnotation Handler exceptionFutureHandler,
            @BussinessAnnotation Handler bussinessFutureHandler) {
        this.closeFutureHandler = closeFutureHandler;
        this.exceptionFutureHandler = exceptionFutureHandler;
        this.bussinessFutureHandler = bussinessFutureHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//        System.out.println(((HandlerServiceImp) exportServiceMap.get("helloWorldService")).test());
//        // 返回客户端消息 - 我已经接收到了你的消息
        System.out.println(Thread.currentThread().getName()+"----位置6");
//        handlerService.handle(msg);
        String retMsg = bussinessFutureHandler.hander(msg);
        ctx.writeAndFlush(retMsg);

    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " channelRegistered " );
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " channelUnregistered " );
        super.channelUnregistered(ctx);
        if(closeFutureHandler !=null){
            closeFutureHandler.hander(ctx.name());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " channelActive " );
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " channelInactive " );
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " exceptionCaught :" + cause.getMessage() );
        super.exceptionCaught(ctx, cause);
        if(exceptionFutureHandler !=null){
            exceptionFutureHandler.hander(cause.getMessage());
        }
    }

}