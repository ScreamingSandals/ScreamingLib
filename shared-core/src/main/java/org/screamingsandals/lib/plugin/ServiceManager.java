package org.screamingsandals.lib.plugin;

import org.jetbrains.annotations.ApiStatus;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ServiceManager contains all services which has instance. You can retrieve them in this way or via annotation processor.
 */
public class ServiceManager {
    private static final List<Object> services = new LinkedList<>();

    @ApiStatus.Internal
    public static void putService(Object service) {
        if (!services.contains(service)) {
            services.add(service);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> serviceType) {
        return (T) services.stream().filter(o -> serviceType.isAssignableFrom(o.getClass())).findFirst().orElseThrow();
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> getOptional(Class<T> serviceType) {
        return (Optional<T>) services.stream().filter(o -> serviceType.isAssignableFrom(o.getClass())).findFirst();
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getAll(Class<T> serviceType) {
        return (List<T>) services.stream().filter(o -> serviceType.isAssignableFrom(o.getClass())).collect(Collectors.toList());
    }
}
