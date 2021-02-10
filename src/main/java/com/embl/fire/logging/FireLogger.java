package com.embl.fire.logging;

import lombok.experimental.Delegate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class FireLogger implements ILogger {

    @Delegate(types = Logger.class)
    private final Logger logger;


    public FireLogger(Class clazz) {
        logger = LogManager.getLogger(clazz);
    }

    public FireLogger(String name) {
        logger = LogManager.getLogger(name);
    }

    @Override
    public void debug(Supplier<String> messageSupplier) {
        if(this.isDebugEnabled()) {
            this.debug(messageSupplier.get());
        }
    }

    @Override
    public void debug(Supplier<String> messageSupplier, Throwable throwable) {
        if(this.isDebugEnabled()) {
            this.debug(messageSupplier.get(), throwable);
        }
    }
}
