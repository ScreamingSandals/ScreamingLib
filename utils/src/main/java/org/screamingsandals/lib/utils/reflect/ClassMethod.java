package org.screamingsandals.lib.utils.reflect;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;

@AllArgsConstructor
@Getter
public class ClassMethod {
	private final Method method;
	
	public Object invokeStatic(Object...params) {
		return invokeInstance(null, params);
	}
	
	public Object invokeInstance(Object instance, Object...params) {
		if (method == null) {
			return null;
		}

		try {
			return method.invoke(instance, params);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
}