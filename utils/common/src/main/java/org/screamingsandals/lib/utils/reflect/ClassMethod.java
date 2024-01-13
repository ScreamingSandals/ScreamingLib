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

package org.screamingsandals.lib.utils.reflect;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Executable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.BiFunction;
import java.util.function.Function;

@RequiredArgsConstructor
@Getter
public class ClassMethod implements ReflectedExecutable<ClassMethod> {
    private final Method method;
    private final BiFunction<Class<?>[], Object[], Object[]> parameterTransformer;
    private final Function<Object, Object> resultTransformer;

    public ClassMethod(Method method) {
        this(method, (classes, objects) -> objects, o -> o);
    }

    public Object invokeStatic(Object... params) {
        return invokeInstance(null, params);
    }

    public InvocationResult invokeStaticResulted(Object...params) {
        return new InvocationResult(invokeStatic(params));
    }

    @SuppressWarnings("deprecation")
    public Object invokeInstance(Object instance, Object... params) {
        if (method == null) {
            return null;
        }

        if (!method.isAccessible()) {
            method.setAccessible(true);
        }

        var transformed = parameterTransformer.apply(method.getParameterTypes(), params);

        try {
            if (instance != null && Proxy.isProxyClass(instance.getClass())) {
                return resultTransformer.apply(Proxy.getInvocationHandler(instance).invoke(instance, method, transformed));
            } else {
                return resultTransformer.apply(method.invoke(instance, transformed));
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
            return resultTransformer.apply(invocationHandler.invoke(proxiedObject, method, parameterTransformer.apply(method.getParameterTypes(), params)));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public InvocationResult invokeInstanceResulted(InvocationHandler invocationHandler, Object proxiedObject,  Object...params) {
        return new InvocationResult(invokeInstance(invocationHandler, proxiedObject, params));
    }

    public ClassMethod withTransformer(BiFunction<Class<?>[], Object[], Object[]> parameterTransformer) {
        return new ClassMethod(method, parameterTransformer, resultTransformer);
    }

    public ClassMethod withTransformers(BiFunction<Class<?>[], Object[], Object[]> parameterTransformer, Function<Object, Object> resultTransformer) {
        return new ClassMethod(method, parameterTransformer, resultTransformer);
    }

    public ClassMethod withTransformer(Function<Object, Object> resultTransformer) {
        return new ClassMethod(method, parameterTransformer, resultTransformer);
    }

    @Override
    public ClassMethod self() {
        return this;
    }

    @Override
    public @Nullable Executable get() {
        return method;
    }
}