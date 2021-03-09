package org.screamingsandals.lib.utils.reflect;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Executable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.BiFunction;

@RequiredArgsConstructor
@Getter
public class ClassMethod implements ReflectedExecutable<ClassMethod> {
    private final Method method;
    private final BiFunction<Class<?>[], Object[], Object[]> parameterTransformer;

    public ClassMethod(Method method) {
        this(method, (classes, objects) -> objects);
    }

    public Object invokeStatic(Object... params) {
        return invokeInstance(null, params);
    }

    public InvocationResult invokeStaticResulted(Object...params) {
        return new InvocationResult(invokeStatic(params));
    }

    public Object invokeInstance(Object instance, Object... params) {
        if (method == null) {
            return null;
        }

        var transformed = parameterTransformer.apply(method.getParameterTypes(), params);

        try {
            if (instance != null && Proxy.isProxyClass(instance.getClass())) {
                return Proxy.getInvocationHandler(instance).invoke(instance, method, transformed);
            } else {
                return method.invoke(instance, transformed);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public InvocationResult invokeInstanceResulted(Object instance, Object...params) {
        return new InvocationResult(invokeInstance(instance, params));
    }

    public Object invokeInstance(InvocationHandler invocationHandler, Object proxiedObject, Object... params) {
        if (method == null) {
            return null;
        }

        try {
            return invocationHandler.invoke(proxiedObject, method, parameterTransformer.apply(method.getParameterTypes(), params));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public InvocationResult invokeInstanceResulted(InvocationHandler invocationHandler, Object proxiedObject,  Object...params) {
        return new InvocationResult(invokeInstance(invocationHandler, proxiedObject, params));
    }

    public ClassMethod withTransformer(BiFunction<Class<?>[], Object[], Object[]> parameterTransformer) {
        return new ClassMethod(method, parameterTransformer);
    }

    @Override
    public ClassMethod self() {
        return this;
    }

    @Nullable
    @Override
    public Executable get() {
        return method;
    }
}