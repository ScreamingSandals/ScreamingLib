package org.screamingsandals.lib.utils.reflect;

import sun.reflect.ReflectionFactory;

import java.lang.reflect.*;
import java.util.*;

public class Reflect {
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassSafe(String... classNames) {
        return (Class<T>) getClassSafe(Map.of(), classNames);
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassSafe(ClassLoader loader, String... classNames) {
        return (Class<T>) getClassSafe(loader, Map.of(), classNames);
    }

    public static Class<?> getClassSafe(Map<String, String> replaceRules, String... classNames) {
        for (var className : classNames) {
            try {
                for (var rule : replaceRules.entrySet()) {
                    className = className.replace(rule.getKey(), rule.getValue());
                }
                return Class.forName(className);
            } catch (ClassNotFoundException ignored) {
            }
        }
        return null;
    }

    public static Class<?> getClassSafe(ClassLoader loader, Map<String, String> replaceRules, String... classNames) {
        for (var className : classNames) {
            try {
                for (var rule : replaceRules.entrySet()) {
                    className = className.replace(rule.getKey(), rule.getValue());
                }
                return Class.forName(className, true, loader);
            } catch (ClassNotFoundException ignored) {
            }
        }
        return null;
    }

    public static ClassMethod getMethod(String className, String names, Class<?>... params) {
        return getMethod(getClassSafe(className), names.split(","), params);
    }

    public static ClassMethod getMethod(Class<?> clazz, String names, Class<?>... params) {
        return getMethod(clazz, names.split(","), params);
    }

    public static ClassMethod getMethod(String className, String[] names, Class<?>... params) {
        return getMethod(getClassSafe(className), names, params);
    }

    public static ClassMethod getMethod(Class<?> clazz, String[] names, Class<?>... params) {
        return getMethod(Collections.singletonList(clazz), names, params);
    }

    public static ClassMethod getMethod(List<Class<?>> classes, String[] names, Class<?>... params) {
        for (var clazz : classes) {
            if (clazz == null) {
                continue;
            }

            for (var name : names) {
                try {
                    var method = clazz.getMethod(name.trim(), params);
                    return new ClassMethod(method);
                } catch (Throwable ignored) {
                    var clazz2 = clazz;
                    do {
                        try {
                            var method = clazz2.getDeclaredMethod(name.trim(), params);
                            method.setAccessible(true);
                            return new ClassMethod(method);
                        } catch (Throwable ignored2) {
                        }
                    } while ((clazz2 = clazz2.getSuperclass()) != null && clazz2 != Object.class);
                }
            }
        }
        return new ClassMethod(null);
    }

    public static ClassMethod getMethod(Method method) {
        return new ClassMethod(method);
    }

    public static InstanceMethod getMethod(Object instance, String names, Class<?>...params) {
        var method = getMethod(retrieveClasses(instance), names.split(","), params);
        return new InstanceMethod(instance, method.getMethod());
    }

    public static InstanceMethod getMethod(Object instance, String[] names, Class<?>...params) {
        var method = getMethod(retrieveClasses(instance), names, params);
        return new InstanceMethod(instance, method.getMethod());
    }

    public static InstanceMethod getMethod(Object instance, Method method) {
        return new InstanceMethod(instance, method);
    }

    public static InvocationResult getFieldResulted(Object instance, String names) {
        return new InvocationResult(getField(instance, names));
    }

    public static InvocationResult getFieldResulted(Object instance, String[] names) {
        return new InvocationResult(getField(instance, names));
    }

    public static InvocationResult getFieldResulted(Class<?> clazz, String names) {
        return new InvocationResult(getField(clazz, names));
    }

    public static InvocationResult getFieldResulted(Field field) {
        return new InvocationResult(getField(field));
    }

    public static InvocationResult getFieldResulted(Object instance, Field field) {
        return new InvocationResult(getField(instance, field));
    }


    public static InvocationResult getFieldResulted(Class<?> clazz, String[] names) {
        return new InvocationResult(getField(clazz, names));
    }

    public static InvocationResult getFieldResulted(Class<?> clazz, String names, Object instance) {
        return new InvocationResult(getField(clazz, names, instance));
    }

    public static InvocationResult getFieldResulted(Class<?> clazz, String[] names, Object instance) {
        return new InvocationResult(getField(clazz, names, instance));
    }

    public static Object getField(Object instance, String names) {
        return getField(instance.getClass(), names.split(","), instance);
    }

    public static Object getField(Object instance, String[] names) {
        return getField(instance.getClass(), names, instance);
    }

    public static Object getField(Class<?> clazz, String names) {
        return getField(clazz, names.split(","), null);
    }

    public static Object getField(Class<?> clazz, String[] names) {
        return getField(clazz, names, null);
    }

    public static Object getField(Class<?> clazz, String names, Object instance) {
        return getField(clazz, names.split(","), instance);
    }

    public static Object getField(Class<?> clazz, String[] names, Object instance) {
        for (var name : names) {
            try {
                var field = clazz.getField(name.trim());
                return field.get(instance);
            } catch (Throwable ignored) {
                var clazz1 = clazz;
                do {
                    try {
                        var field = clazz1.getDeclaredField(name.trim());
                        field.setAccessible(true);
                        return field.get(instance);
                    } catch (Throwable ignored2) {
                    }
                } while ((clazz1 = clazz1.getSuperclass()) != null && clazz1 != Object.class);
            }
        }
        return null;
    }

    public static Object getField(Field field) {
        return getField(null, field);
    }

    public static Object getField(Object instance, Field field) {
        try {
            field.setAccessible(true);
            return field.get(instance);
        } catch (Throwable ignored) {}
        return null;
    }

    public static Object setField(Object instance, String names, Object set) {
        return setField(instance.getClass(), names.split(","), instance, set);
    }

    public static Object setField(Object instance, String[] names, Object set) {
        return setField(instance.getClass(), names, instance, set);
    }

    public static Object setField(Class<?> clazz, String names, Object set) {
        return setField(clazz, names.split(","), null, set);
    }

    public static Object setField(Class<?> clazz, String[] names, Object set) {
        return setField(clazz, names, null, set);
    }

    public static Object setField(Class<?> clazz, String names, Object instance, Object set) {
        return setField(clazz, names.split(","), instance, set);
    }

    public static Object setField(Class<?> clazz, String[] names, Object instance, Object set) {
        for (var name : names) {
            try {
                var field = clazz.getField(name.trim());
                field.set(instance, set);
                return field.get(instance);
            } catch (Throwable ignored) {
                var clazz1 = clazz;
                do {
                    try {
                        var field = clazz1.getDeclaredField(name.trim());
                        field.setAccessible(true);
                        Field modifiersField = Field.class.getDeclaredField("modifiers");
                        modifiersField.setAccessible(true);
                        int modifiers = modifiersField.getInt(field);
                        modifiers &= ~Modifier.FINAL;
                        modifiersField.setInt(field, modifiers);
                        field.set(instance, set);
                        return field.get(instance);
                    } catch (Throwable ignored2) {
                    }
                } while ((clazz1 = clazz1.getSuperclass()) != null && clazz1 != Object.class);
            }
        }
        return null;
    }

    public static Object setField(Field field, Object value) {
        return setField(null, field, value);
    }

    public static Object setField(Object instance, Field field, Object value) {
        try {
            field.setAccessible(true);
            try {
                var modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                int modifiers = modifiersField.getInt(field);
                modifiers &= ~Modifier.FINAL;
                modifiersField.setInt(field, modifiers);
            } catch (Throwable ignored) {}
            field.set(instance, value);
            return field.get(instance);
        } catch (Throwable ignored) {}
        return null;
    }

    public static Object findEnumConstant(Class<?> enumClass, String constantNames) {
        return findEnumConstant(enumClass, constantNames.split(","));
    }

    public static Object findEnumConstant(Class<?> enumClass, String[] constantNames) {
        var enums = enumClass.getEnumConstants();
        if (enums != null) {
            for (var enumeration : enums) {
                var name = getMethod(enumeration, "name").invoke();
                for (var constant : constantNames) {
                    if (constant.equals(name)) {
                        return enumeration;
                    }
                }
            }
        }
        return null;
    }

    /* fastInvoke = getMethod().invoke() */

    public static Object fastInvoke(Object instance, String names) {
        return fastInvoke(instance, names.split(","));
    }

    public static Object fastInvoke(Object instance, String[] names) {
        return fastInvoke(instance.getClass(), names, instance);
    }

    public static Object fastInvoke(Class<?> className, String names) {
        return fastInvoke(className, names.split(","));
    }

    public static Object fastInvoke(Class<?> className, String[] names) {
        return fastInvoke(className, names, null);
    }

    public static Object fastInvoke(Class<?> className, String names, Object instance) {
        return fastInvoke(className, names.split(","), instance);
    }

    public static Object fastInvoke(Class<?> className, String[] names, Object instance) {
        ClassMethod method = getMethod(className, names);
        return method.invokeInstance(instance);
    }

    public static Object fastInvoke(Method method) {
        return getMethod(method).invokeStatic();
    }

    public static Object fastInvoke(Object instance, Method method) {
        return getMethod(instance, method).invoke();
    }

    public static Object fastInvoke(Method method, Object... params) {
        return fastInvoke(null, method, params);
    }

    public static Object fastInvoke(Object instance, Method method, Object... params) {
        return getMethod(instance, method).invoke(params);
    }

    public static InvocationResult fastInvokeResulted(Object instance, String names) {
        return fastInvokeResulted(instance, names.split(","));
    }

    public static InvocationResult fastInvokeResulted(Object instance, String[] names) {
        return fastInvokeResulted(instance.getClass(), names, instance);
    }

    public static InvocationResult fastInvokeResulted(Class<?> className, String names) {
        return fastInvokeResulted(className, names.split(","));
    }

    public static InvocationResult fastInvokeResulted(Class<?> className, String[] names) {
        return fastInvokeResulted(className, names, null);
    }

    public static InvocationResult fastInvokeResulted(Class<?> className, String names, Object instance) {
        return fastInvokeResulted(className, names.split(","), instance);
    }

    public static InvocationResult fastInvokeResulted(Class<?> className, String[] names, Object instance) {
        ClassMethod method = getMethod(className, names);
        return method.invokeInstanceResulted(instance);
    }

    public static InvocationResult fastInvokeResulted(Method method) {
        return getMethod(method).invokeStaticResulted();
    }

    public static InvocationResult fastInvokeResulted(Object instance, Method method) {
        return getMethod(instance, method).invokeResulted();
    }

    public static InvocationResult fastInvokeResulted(Method method, Object... params) {
        return fastInvokeResulted(null, method, params);
    }

    public static InvocationResult fastInvokeResulted(Object instance, Method method, Object... params) {
        return getMethod(instance, method).invokeResulted(params);
    }

    public static List<Class<?>> retrieveClasses(Object instance) {
        if (Proxy.isProxyClass(instance.getClass())) {
            return Arrays.asList(instance.getClass().getInterfaces());
        } else {
            return Collections.singletonList(instance.getClass());
        }
    }

    public static Optional<InvocationHandler> asInvocationHandler(Object instance) {
        if (instance != null && Proxy.isProxyClass(instance.getClass())) {
            return Optional.of(Proxy.getInvocationHandler(instance));
        } else {
            return Optional.empty();
        }
    }

    public static boolean isInstance(Object instance, String className) {
        return isInstance(instance, getClassSafe(className));
    }

    public static boolean isInstance(Object instance, Class<?> clazz) {
        if (clazz == null) {
            return false;
        }

        return clazz.isInstance(instance);
    }

    public static boolean has(String className) {
        return getClassSafe(className) != null;
    }

    public static boolean has(Class<?> clazz) {
        return clazz != null;
    }

    public static boolean hasMethod(String className, String methodNames, Class<?>... arguments) {
        return getMethod(className, methodNames, arguments).getMethod() != null;
    }

    public static boolean hasMethod(Object instance, String methodNames, Class<?>... arguments) {
        return hasMethod(instance.getClass(), methodNames, arguments);
    }

    public static boolean hasMethod(Class<?> type, String methodNames, Class<?>... arguments) {
        return getMethod(type, methodNames, arguments).getMethod() != null;
    }

    public static Constructor constructor(String className, Class<?>... arguments) {
        return constructor(getClassSafe(className), arguments);
    }

    public static Constructor constructor(Class<?> type, Class<?>... arguments) {
        try {
            return new Constructor(type.getConstructor(arguments));
        } catch (Exception ignored) { }
        return new Constructor(null);
    }

    public static Constructor constructor(java.lang.reflect.Constructor<?> constructor) {
        return new Constructor(constructor);
    }

    public static Object construct(String className) {
        return construct(getClassSafe(className));
    }

    public static Object construct(Class<?> type) {
        return constructor(type).construct();
    }

    public static Object construct(java.lang.reflect.Constructor<?> constructor, Object... params) {
        return constructor(constructor).construct(params);
    }

    public static InvocationResult constructResulted(String className) {
        return constructResulted(getClassSafe(className));
    }

    public static InvocationResult constructResulted(Class<?> type) {
        return constructor(type).constructResulted();
    }

    public static InvocationResult constructResulted(java.lang.reflect.Constructor<?> constructor, Object... params) {
        return constructor(constructor).constructResulted(params);
    }

    // create objects without constructor
    public static <T> T forceConstruct(Class<T> clazz) {
        try {
            ReflectionFactory rf = ReflectionFactory.getReflectionFactory();
            java.lang.reflect.Constructor<?> objDef = Object.class.getDeclaredConstructor();
            java.lang.reflect.Constructor<?> intConstr = rf.newConstructorForSerialization(
                    clazz, objDef
            );
            return clazz.cast(intConstr.newInstance());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Cannot create object", e);
        }
    }
}