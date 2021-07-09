package org.youbai.opentcs.drivers.adapter.netty.spring;

import org.youbai.opentcs.drivers.adapter.netty.annotations.BussinessAnnotation;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@BussinessAnnotation
public class BussinessFutureHandler implements Handler {
    private Handler nextHandler;

    public Handler getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public String hander(String msg) {
        System.out.println("接收到信息：" + msg);
        if (nextHandler != null) {
            nextHandler.hander(msg);
        }
        return msg;
    }

}
