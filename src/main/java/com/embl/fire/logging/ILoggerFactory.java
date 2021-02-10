package com.embl.fire.logging;

public class ILoggerFactory {

    private ILoggerFactory() {

    }

    public static ILogger getLogger(Class clazz) {
        return new FireLogger(clazz);
    }

    public static ILogger getLogger(String name) {
        return new FireLogger(name);
    }

}
