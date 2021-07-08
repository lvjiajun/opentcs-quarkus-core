package org.youbai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.youbai.opentcs.kernel.SslConfiguration;
import org.youbai.opentcs.kernel.workingset.TCSObjectPool;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class ExampleResource {


    @Inject
    SslConfiguration sslConfiguration;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        return sslConfiguration.keystoreFile();
    }
}