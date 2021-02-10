package com.embl.fire.logging;

import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public interface ILogger extends Logger {

    void debug(Supplier<String> messageSupplier);

    void debug(Supplier<String> messageSupplier, Throwable throwable);

}
