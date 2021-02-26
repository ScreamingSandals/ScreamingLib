package org.screamingsandals.lib.utils.logger;

import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.Pair;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JULLoggerWrapper extends BasicWrapper<Logger> implements LoggerWrapper {
    public JULLoggerWrapper(Logger wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String getName() {
        return wrappedObject.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return wrappedObject.isLoggable(Level.FINEST);
    }

    @Override
    public void trace(String msg) {
        wrappedObject.log(Level.FINEST, msg);
    }

    @Override
    public void trace(String format, Object argument, Throwable throwable) {
        var msg = getMsg(format, new Object[]{argument}, throwable);
        wrappedObject.log(Level.FINEST, msg.getFirst(), msg.getSecond());
    }

    @Override
    public void trace(String format, Object... arguments) {
        var msg = getMsg(format, Arrays.asList(arguments));
        wrappedObject.log(Level.FINEST, msg.getFirst(), msg.getSecond());
    }

    @Override
    public void trace(String msg, Throwable t) {
        wrappedObject.log(Level.FINEST, msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return wrappedObject.isLoggable(Level.FINE);
    }

    @Override
    public void debug(String msg) {
        wrappedObject.log(Level.FINE, msg);
    }

    @Override
    public void debug(String format, Object argument, Throwable throwable) {
        var msg = getMsg(format, new Object[]{argument}, throwable);
        wrappedObject.log(Level.FINE, msg.getFirst(), msg.getSecond());
    }

    @Override
    public void debug(String format, Object... arguments) {
        var msg = getMsg(format, Arrays.asList(arguments));
        wrappedObject.log(Level.FINE, msg.getFirst(), msg.getSecond());
    }

    @Override
    public void debug(String msg, Throwable t) {
        wrappedObject.log(Level.FINE, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return wrappedObject.isLoggable(Level.INFO);
    }

    @Override
    public void info(String msg) {
        wrappedObject.log(Level.INFO, msg);
    }

    @Override
    public void info(String format, Object argument, Throwable throwable) {
        var msg = getMsg(format, new Object[]{argument}, throwable);
        wrappedObject.log(Level.INFO, msg.getFirst(), msg.getSecond());
    }

    @Override
    public void info(String format, Object... arguments) {
        var msg = getMsg(format, Arrays.asList(arguments));
        wrappedObject.log(Level.INFO, msg.getFirst(), msg.getSecond());
    }

    @Override
    public void info(String msg, Throwable t) {
        wrappedObject.log(Level.INFO, msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return wrappedObject.isLoggable(Level.WARNING);
    }

    @Override
    public void warn(String msg) {
        wrappedObject.log(Level.WARNING, msg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        var msg = getMsg(format, Arrays.asList(arguments));
        wrappedObject.log(Level.WARNING, msg.getFirst(), msg.getSecond());
    }

    @Override
    public void warn(String format, Object argument, Throwable throwable) {
        var msg = getMsg(format, new Object[]{argument}, throwable);
        wrappedObject.log(Level.WARNING, msg.getFirst(), msg.getSecond());
    }

    @Override
    public void warn(String msg, Throwable t) {
        wrappedObject.log(Level.WARNING, msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return wrappedObject.isLoggable(Level.SEVERE);
    }

    @Override
    public void error(String msg) {
        wrappedObject.log(Level.SEVERE, msg);
    }

    @Override
    public void error(String format, Object argument, Throwable throwable) {
        var msg = getMsg(format, new Object[]{argument}, throwable);
        wrappedObject.log(Level.SEVERE, msg.getFirst(), msg.getSecond());
    }

    @Override
    public void error(String format, Object... arguments) {
        var msg = getMsg(format, Arrays.asList(arguments));
        wrappedObject.log(Level.SEVERE, msg.getFirst(), msg.getSecond());
    }

    @Override
    public void error(String msg, Throwable t) {
        wrappedObject.log(Level.SEVERE, msg, t);
    }


    private Pair<String, Throwable> getMsg(String msg, List<Object> args) {
        var throwableCandidate = args.stream().filter(o -> o instanceof Throwable).findFirst().map(o -> (Throwable) o);
        throwableCandidate.ifPresent(args::remove);
        return getMsg(msg, args.toArray(), throwableCandidate.orElse(null));
    }

    private Pair<String, Throwable> getMsg(String msg, Object[] args, Throwable throwable) {
        if (msg == null || args == null || args.length == 0) {
            return Pair.of(msg, throwable);
        }

        var format = new MessageFormat(msg);
        return Pair.of(format.format(args), throwable);
    }
}
