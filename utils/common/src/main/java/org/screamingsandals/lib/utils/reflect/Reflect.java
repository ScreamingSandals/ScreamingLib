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

import lombok.experimental.UtilityClass;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.misc.Unsafe;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
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

    public static ClassMethod getMethod(@Language(value = "JAVA", prefix = "class X { void m() { ", suffix = ".class; } }") String className, String names, Class<?>... params) {
        return getMethod(getClassSafe(className), names.split(","), params);
    }

    public static ClassMethod getMethod(Class<?> clazz, String names, Class<?>... params) {
        return getMethod(clazz, names.split(","), params);
    }

    public static ClassMethod getMethod(@Language(value = "JAVA", prefix = "class X { void m() { ", suffix = ".class; } }") String className, String[] names, Class<?>... params) {
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

    public static List<InstanceMethod> getMethodsCalled(Object instance, String names, int parametersCount) {
        var namesS = Arrays.asList(names.split(","));
        var classes = retrieveClasses(instance);
        return classes.stream().flatMap(aClass -> Arrays.stream(aClass.getMethods()))
                .filter(method -> namesS.contains(method.getName()) && method.getParameterCount() == parametersCount)
                .map(method -> new InstanceMethod(instance, method))
                .collect(Collectors.toList());
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

    public static Object getField(@Language(value = "JAVA", prefix = "class X { void m() { ", suffix = ".class; } }") String className, String names) {
        var clazz = getClassSafe(className);
        if (clazz == null) {
            return null;
        }
        return getField(clazz, names.split(","));
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
                        if ((field.getModifiers() & Modifier.FINAL) == Modifier.FINAL) {
                            try {
                                var modifiersField = Field.class.getDeclaredField("modifiers");
                                modifiersField.setAccessible(true);
                                int modifiers = modifiersField.getInt(field);
                                modifiers &= ~Modifier.FINAL;
                                modifiersField.setInt(field, modifiers);
                            } catch (Throwable ignored2) {
                            }
                        }
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
            if ((field.getModifiers() & Modifier.FINAL) == Modifier.FINAL) {
                try {
                    var modifiersField = Field.class.getDeclaredField("modifiers");
                    modifiersField.setAccessible(true);
                    int modifiers = modifiersField.getInt(field);
                    modifiers &= ~Modifier.FINAL;
                    modifiersField.setInt(field, modifiers);
                } catch (Throwable ignored) {
                }
            }
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

    public static boolean isInstance(Object instance, @Language(value = "JAVA", prefix = "class X { void m() { ", suffix = ".class; } }") String className) {
        return isInstance(instance, getClassSafe(className));
    }

    public static boolean isInstance(Object instance, Class<?> clazz) {
        if (clazz == null) {
            return false;
        }

        return clazz.isInstance(instance);
    }

    public static boolean has(@Language(value = "JAVA", prefix = "class X { void m() { ", suffix = ".class; } }") String className) {
        return getClassSafe(className) != null;
    }

    public static boolean has(Class<?> clazz) {
        return clazz != null;
    }

    public static boolean hasMethod(@Language(value = "JAVA", prefix = "class X { void m() { ", suffix = ".class; } }") String className, String methodNames, Class<?>... arguments) {
        return getMethod(className, methodNames, arguments).getMethod() != null;
    }

    public static boolean hasMethod(Object instance, String methodNames, Class<?>... arguments) {
        return hasMethod(instance.getClass(), methodNames, arguments);
    }

    public static boolean hasMethod(Class<?> type, String methodNames, Class<?>... arguments) {
        return getMethod(type, methodNames, arguments).getMethod() != null;
    }

    public static Constructor constructor(@Language(value = "JAVA", prefix = "class X { void m() { ", suffix = ".class; } }") String className, Class<?>... arguments) {
        return constructor(getClassSafe(className), arguments);
    }

    public static Constructor constructor(Class<?> type, Class<?>... arguments) {
        try {
            return new Constructor(type.getConstructor(arguments));
        } catch (Exception ignored) { }
        return new Constructor(null);
    }

    public static <T extends Enum<T>> @Nullable T newEnumValue(@NotNull Class<T> type, @NotNull String name, int ordinal) {
        try {
            var constructor = Unsafe.class.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            var unsafe = (Unsafe) constructor.newInstance();
            var newValue = unsafe.allocateInstance(type);
            var ordinalField = Enum.class.getDeclaredField("ordinal");
            unsafe.putInt(newValue, unsafe.objectFieldOffset(ordinalField), ordinal);
            var nameField = Enum.class.getDeclaredField("name");
            unsafe.putObject(newValue, unsafe.objectFieldOffset(nameField), name);
            return (T) newValue;
        } catch (Exception ignored) { }
        return null;
    }

    public static Constructor constructor(java.lang.reflect.Constructor<?> constructor) {
        return new Constructor(constructor);
    }

    public static Object construct(@Language(value = "JAVA", prefix = "class X { void m() { ", suffix = ".class; } }") String className) {
        return construct(getClassSafe(className));
    }

    public static Object construct(Class<?> type) {
        return constructor(type).construct();
    }

    public static Object construct(java.lang.reflect.Constructor<?> constructor, Object... params) {
        return constructor(constructor).construct(params);
    }

    public static InvocationResult constructResulted(@Language(value = "JAVA", prefix = "class X { void m() { ", suffix = ".class; } }") String className) {
        return constructResulted(getClassSafe(className));
    }

    public static InvocationResult constructResulted(Class<?> type) {
        return constructor(type).constructResulted();
    }

    public static InvocationResult constructResulted(java.lang.reflect.Constructor<?> constructor, Object... params) {
        return constructor(constructor).constructResulted(params);
    }
}