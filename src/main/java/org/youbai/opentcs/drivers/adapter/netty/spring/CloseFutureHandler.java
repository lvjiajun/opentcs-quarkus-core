package org.youbai.opentcs.drivers.adapter.netty.spring;

import org.youbai.opentcs.drivers.adapter.netty.annotations.CloseAnnotation;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@CloseAnnotation
public class CloseFutureHandler implements Handler {
    private Handler nextHandler;

    public Handler getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public String hander(String msg) {
        System.out.println(msg + "正在关闭。");
        if (nextHandler != null) {
            nextHandler.hander(msg);
        }
        return COMMONRET;
    }

}
