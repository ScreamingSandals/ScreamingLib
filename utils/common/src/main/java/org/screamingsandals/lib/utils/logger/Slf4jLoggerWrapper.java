package org.screamingsandals.lib.utils.logger;

import org.screamingsandals.lib.utils.BasicWrapper;
import org.slf4j.Logger;

public class Slf4jLoggerWrapper extends BasicWrapper<Logger> implements LoggerWrapper {

    public Slf4jLoggerWrapper(Logger wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String getName() {
        return wrappedObject.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return wrappedObject.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        wrappedObject.trace(msg);
    }

    @Override
    public void trace(String format, Object argument, Throwable throwable) {
        wrappedObject.trace(format, argument, throwable);
    }

    @Override
    public void trace(String format, Object... arguments) {
        wrappedObject.trace(format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        wrappedObject.trace(msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return wrappedObject.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        wrappedObject.debug(msg);
    }

    @Override
    public void debug(String format, Object argument, Throwable throwable) {
        wrappedObject.debug(format, argument, throwable);
    }

    @Override
    public void debug(String format, Object... arguments) {
        wrappedObject.debug(format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        wrappedObject.debug(msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return wrappedObject.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        wrappedObject.info(msg);
    }

    @Override
    public void info(String format, Object argument, Throwable throwable) {
        wrappedObject.info(format, argument, throwable);
    }

    @Override
    public void info(String format, Object... arguments) {
        wrappedObject.info(format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        wrappedObject.info(msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return wrappedObject.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        wrappedObject.warn(msg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        wrappedObject.warn(format, arguments);
    }

    @Override
    public void warn(String format, Object argument, Throwable throwable) {
        wrappedObject.warn(format, argument, throwable);
    }

    @Override
    public void warn(String msg, Throwable t) {
        wrappedObject.warn(msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return wrappedObject.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        wrappedObject.error(msg);
    }

    @Override
    public void error(String format, Object argument, Throwable throwable) {
        wrappedObject.error(format, argument, throwable);
    }

    @Override
    public void error(String format, Object... arguments) {
        wrappedObject.error(format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        wrappedObject.error(msg, t);
    }
}
