/*
 * Copyright 2023 ScreamingSandals
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
    public @Nullable Executable get() {
        return constructor;
    }
}
