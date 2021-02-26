package org.screamingsandals.lib.utils.logger;

import org.screamingsandals.lib.utils.Pair;

import java.util.Optional;

public class DualLoggerWrapper extends Pair<LoggerWrapper, LoggerWrapper> implements LoggerWrapper {

    public DualLoggerWrapper(LoggerWrapper first, LoggerWrapper second) {
        super(first, second);
    }

    @Override
    public String getName() {
        return getFirstNonNull().getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return getFirstNonNull().isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        getFirstNonNull().trace(msg);
    }

    @Override
    public void trace(String format, Object argument, Throwable throwable) {
        getFirstNonNull().trace(format, argument, throwable);
    }

    @Override
    public void trace(String format, Object... arguments) {
        getFirstNonNull().trace(format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        getFirstNonNull().trace(msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return getFirstNonNull().isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        getFirstNonNull().debug(msg);
    }

    @Override
    public void debug(String format, Object argument, Throwable throwable) {
        getFirstNonNull().debug(format, argument, throwable);
    }

    @Override
    public void debug(String format, Object... arguments) {
        getFirstNonNull().debug(format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        getFirstNonNull().debug(msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return getFirstNonNull().isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        getFirstNonNull().info(msg);
    }

    @Override
    public void info(String format, Object argument, Throwable throwable) {
        getFirstNonNull().info(format, argument, throwable);
    }

    @Override
    public void info(String format, Object... arguments) {
        getFirstNonNull().info(format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        getFirstNonNull().info(msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return getFirstNonNull().isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        getFirstNonNull().warn(msg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        getFirstNonNull().warn(format, arguments);
    }

    @Override
    public void warn(String format, Object argument, Throwable throwable) {
        getFirstNonNull().warn(format, argument, throwable);
    }

    @Override
    public void warn(String msg, Throwable t) {
        getFirstNonNull().warn(msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return getFirstNonNull().isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        getFirstNonNull().error(msg);
    }

    @Override
    public void error(String format, Object argument, Throwable throwable) {
        getFirstNonNull().error(format, argument, throwable);
    }

    @Override
    public void error(String format, Object... arguments) {
        getFirstNonNull().error(format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        getFirstNonNull().error(msg, t);
    }

    @Override
    public <T> T as(Class<T> type) {
        try {
            return getFirst().as(type);
        } catch (Throwable throwable) {
            return getSecond().as(type);
        }
    }

    private LoggerWrapper getFirstNonNull() {
        return Optional.ofNullable(getFirst()).orElseGet(this::getSecond);
    }

}
