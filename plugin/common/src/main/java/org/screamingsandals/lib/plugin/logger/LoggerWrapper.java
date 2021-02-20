package org.screamingsandals.lib.plugin.logger;

import org.screamingsandals.lib.utils.Wrapper;

public interface LoggerWrapper extends Wrapper {

    String getName();

    boolean isTraceEnabled();
    
    void trace(String msg);

    void trace(String format, Object argument, Throwable throwable);

    void trace(String format, Object... arguments);
    
    void trace(String msg, Throwable t);
    
    boolean isDebugEnabled();
    
    void debug(String msg);

    void debug(String format, Object argument, Throwable throwable);

    void debug(String format, Object... arguments);
    
    void debug(String msg, Throwable t);
    
    boolean isInfoEnabled();
    
    void info(String msg);
    
    void info(String format, Object argument, Throwable throwable);
    
    void info(String format, Object... arguments);
    
    void info(String msg, Throwable t);
    
    boolean isWarnEnabled();
    
    void warn(String msg);

    void warn(String format, Object... arguments);
    
    void warn(String format, Object argument, Throwable throwable);
    
    void warn(String msg, Throwable t);
    
    boolean isErrorEnabled();
    
    void error(String msg);

    void error(String format, Object argument, Throwable throwable);
    
    void error(String format, Object... arguments);
    
    void error(String msg, Throwable t);
}
