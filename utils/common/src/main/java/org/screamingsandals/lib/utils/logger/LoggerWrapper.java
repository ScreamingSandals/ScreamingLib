/*
 * Copyright 2024 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.utils.logger;

import org.screamingsandals.lib.api.Wrapper;

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
