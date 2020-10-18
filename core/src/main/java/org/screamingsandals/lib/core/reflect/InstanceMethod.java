package org.screamingsandals.lib.core.reflect;

import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public class InstanceMethod extends ClassMethod {
	private final Object instance;
	
	public InstanceMethod(Object instance, Method method) {
		super(method);
		this.instance = instance;
	}
	
	public Object invoke(Object...params) {
		return invokeInstance(instance, params);
	}
}
