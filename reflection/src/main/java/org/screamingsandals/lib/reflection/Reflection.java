package org.screamingsandals.lib.reflection;

import java.util.Map;

public class Reflection {
    public static Class<?> getClassSafe(String... classNames) {
        return getClassSafe(Map.of(), classNames);
    }

    public static Class<?> getClassSafe(Map<String, String> replaceRules, String... classNames) {
        for (var className : classNames) {
            try {
                for (var rule : replaceRules.entrySet()) {
                    className = className.replaceAll(rule.getKey(), rule.getValue());
                }
                return Class.forName(className);
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
        return new ClassMethod(null);
    }
    public static InstanceMethod getMethod(Object instance, String names, Class<?>...params) {
        var method = getMethod(instance.getClass(), names.split(","), params);
        return new InstanceMethod(instance, method.getMethod());
    }

    public static InstanceMethod getMethod(Object instance, String[] names, Class<?>...params) {
        var method = getMethod(instance.getClass(), names, params);
        return new InstanceMethod(instance, method.getMethod());
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
                        field.set(instance, set);
                        return field.get(instance);
                    } catch (Throwable ignored2) {
                    }
                } while ((clazz1 = clazz1.getSuperclass()) != null && clazz1 != Object.class);
            }
        }
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
}
