package org.youbai;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;


public class GreetingService {

    @ConsumeEvent("greeting")
    public String consume(String name) {
        return name.toUpperCase();
    }

}
