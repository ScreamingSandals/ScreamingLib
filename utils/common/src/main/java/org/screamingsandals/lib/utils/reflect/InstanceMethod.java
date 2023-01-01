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

import lombok.Getter;

import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
public class InstanceMethod extends ClassMethod /*implements ReflectedExecutable<InstanceMethod>*/ {
	private final Object instance;

	public InstanceMethod(Object instance, Method method) {
		super(method);
		this.instance = instance;
	}

	public InstanceMethod(Object instance, Method method, BiFunction<Class<?>[], Object[], Object[]> parameterTransformer, Function<Object, Object> resultTransformer) {
		super(method, parameterTransformer, resultTransformer);
		this.instance = instance;
	}

	public Object invoke(Object... params) {
		return invokeInstance(instance, params);
	}

	public InvocationResult invokeResulted(Object... params) {
		return new InvocationResult(invoke(params));
	}

	public InstanceMethod withTransformer(BiFunction<Class<?>[], Object[], Object[]> parameterTransformer) {
		return new InstanceMethod(instance, getMethod(), parameterTransformer, getResultTransformer());
	}

	public InstanceMethod withTransformers(BiFunction<Class<?>[], Object[], Object[]> parameterTransformer, Function<Object, Object> resultTransformer) {
		return new InstanceMethod(instance, getMethod(), parameterTransformer, resultTransformer);
	}

	public InstanceMethod withTransformer(Function<Object, Object> resultTransformer) {
		return new InstanceMethod(instance, getMethod(), getParameterTransformer(), resultTransformer);
	}
}