package org.screamingsandals.lib.utils.reflect;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@AllArgsConstructor
@Getter
public class ClassMethod {
    private final Method method;

    public Object invokeStatic(Object... params) {
        return invokeInstance(null, params);
    }

    public Object invokeInstance(Object instance, Object... params) {
        if (method == null) {
            return null;
        }

        try {
            if (instance != null && Proxy.isProxyClass(instance.getClass())) {
                return Proxy.getInvocationHandler(instance).invoke(instance, method, params);
            } else {
                return method.invoke(instance, params);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public Object invokeInstance(InvocationHandler invocationHandler, Object proxiedObject, Object... params) {
        if (method == null) {
            return null;
        }

        try {
            return invocationHandler.invoke(proxiedObject, method, params);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}