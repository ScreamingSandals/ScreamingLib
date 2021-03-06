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