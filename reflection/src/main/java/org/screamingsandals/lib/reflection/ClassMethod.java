package org.screamingsandals.lib.reflection;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;

@AllArgsConstructor
@Getter
public class ClassMethod {
	private Method method;
	
	public Object invokeStatic(Object...params) {
		return invokeInstance(null, params);
	}
	
	public Object invokeInstance(Object instance, Object...params) {
		try {
			return method.invoke(instance, params);
		} catch (Throwable ignored) {
		}
		return null;
	}
}
