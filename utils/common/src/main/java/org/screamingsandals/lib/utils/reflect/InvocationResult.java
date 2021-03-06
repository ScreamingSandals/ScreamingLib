package org.screamingsandals.lib.utils.reflect;

import org.screamingsandals.lib.utils.BasicWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Optional;

public class InvocationResult extends BasicWrapper<Object> {
    public InvocationResult(Object wrappedObject) {
        super(wrappedObject);
    }

    public InstanceMethod getMethod(String names, Class<?>... params) {
        if (wrappedObject == null) {
            return new InstanceMethod(null, null);
        }
        return Reflect.getMethod(wrappedObject, names, params);
    }

    public InstanceMethod getMethod(String[] names, Class<?>... params) {
        if (wrappedObject == null) {
            return new InstanceMethod(null, null);
        }
        return Reflect.getMethod(wrappedObject, names, params);
    }

    public InstanceMethod getMethod(Method method) {
        if (wrappedObject == null) {
            return new InstanceMethod(null, null);
        }
        return Reflect.getMethod(wrappedObject, method);
    }

    public InvocationResult getFieldResulted(String names) {
        return new InvocationResult(getField(names));
    }

    public InvocationResult getFieldResulted(String[] names) {
        return new InvocationResult(getField(names));
    }

    public InvocationResult getFieldResulted(Field field) {
        return new InvocationResult(getField(field));
    }

    public Object getField(String names) {
        if (wrappedObject == null) {
            return null;
        }
        return Reflect.getField(wrappedObject, names);
    }

    public Object getField(String[] names) {
        if (wrappedObject == null) {
            return null;
        }
        return Reflect.getField(wrappedObject, names);
    }

    public Object getField(Field field) {
        if (wrappedObject == null) {
            return null;
        }
        return Reflect.getField(wrappedObject, field);
    }

    public Object setField(String names, Object set) {
        if (wrappedObject == null) {
            return null;
        }
        return Reflect.setField(wrappedObject, names, set);
    }

    public Object setField(String[] names, Object set) {
        if (wrappedObject == null) {
            return null;
        }
        return Reflect.setField(wrappedObject, names, set);
    }

    public Object setField(Field field, Object set) {
        if (wrappedObject == null) {
            return null;
        }
        return Reflect.setField(wrappedObject, field, set);
    }

    public Object fastInvoke(String names) {
        if (wrappedObject == null) {
            return null;
        }
        return Reflect.fastInvoke(wrappedObject, names);
    }

    public Object fastInvoke(String[] names) {
        if (wrappedObject == null) {
            return null;
        }
        return Reflect.fastInvoke(wrappedObject, names);
    }

    public Object fastInvoke(Method method, Object... params) {
        if (wrappedObject == null) {
            return null;
        }
        return Reflect.fastInvoke(wrappedObject, method, params);
    }

    public InvocationResult fastInvokeResulted(String names) {
        if (wrappedObject == null) {
            return new InvocationResult(null);
        }
        return Reflect.fastInvokeResulted(wrappedObject, names);
    }

    public InvocationResult fastInvokeResulted(String[] names) {
        if (wrappedObject == null) {
            return new InvocationResult(null);
        }
        return Reflect.fastInvokeResulted(wrappedObject, names);
    }

    public InvocationResult fastInvokeResulted(Method method, Object... params) {
        if (wrappedObject == null) {
            return new InvocationResult(null);
        }
        return Reflect.fastInvokeResulted(wrappedObject, method, params);
    }

    public Optional<InvocationHandler> asInvocationHandler() {
        if (wrappedObject != null && Proxy.isProxyClass(wrappedObject.getClass())) {
            return Optional.of(Proxy.getInvocationHandler(wrappedObject));
        } else {
            return Optional.empty();
        }
    }

    public boolean isInstance(String className) {
        if (wrappedObject == null) {
            return false;
        }
        return Reflect.isInstance(wrappedObject, className);
    }

    public boolean isInstance(Class<?> clazz) {
        if (wrappedObject == null) {
            return false;
        }
        return Reflect.isInstance(wrappedObject, clazz);
    }

    public boolean hasMethod(String methodNames, Class<?>... arguments) {
        if (wrappedObject == null) {
            return false;
        }
        return Reflect.hasMethod(wrappedObject, methodNames, arguments);
    }

    public boolean isPresent() {
        return wrappedObject != null;
    }

    public boolean isEmpty() {
        return wrappedObject == null;
    }

    @Override
    public String toString() {
        return String.valueOf(fastInvoke("toString"));
    }
}
