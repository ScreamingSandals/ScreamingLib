package org.screamingsandals.lib.utils.reflect;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Executable;
import java.util.function.BiFunction;

@RequiredArgsConstructor
public class Constructor implements ReflectedExecutable<Constructor> {
    private final java.lang.reflect.Constructor<?> constructor;
    private final BiFunction<Class<?>[], Object[], Object[]> parameterTransformer;

    public Constructor(java.lang.reflect.Constructor<?> constructor) {
        this(constructor, (classes, objects) -> objects);
    }

    public Object construct(Object...parameters) {
        try {
            return constructor.newInstance(parameterTransformer.apply(constructor.getParameterTypes(), parameters));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    public InvocationResult constructResulted(Object...parameters) {
        return new InvocationResult(construct(parameters));
    }

    public Constructor withTransformer(BiFunction<Class<?>[], Object[], Object[]> parameterTransformer) {
        return new Constructor(constructor, parameterTransformer);
    }

    @Override
    public Constructor self() {
        return this;
    }

    @Override
    @Nullable
    public Executable get() {
        return constructor;
    }
}
