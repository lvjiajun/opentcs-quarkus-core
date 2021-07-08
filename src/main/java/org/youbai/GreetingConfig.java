package org.youbai;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;


public interface GreetingConfig {


    String message();

}