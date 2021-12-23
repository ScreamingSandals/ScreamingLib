package org.screamingsandals.lib.container;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.container.type.InventoryTypeHolder;
import org.screamingsandals.lib.utils.annotations.AbstractService;

import java.util.Optional;

@AbstractService
public abstract class ContainerFactory {

    private static ContainerFactory factory;

    protected ContainerFactory() {
        if (factory != null) {
            throw new UnsupportedOperationException("ContainerFactory is already initialized.");
        }

        factory = this;
    }

    public static <C extends Container> Optional<C> wrapContainer(Object container) {
        if (factory == null) {
            throw new UnsupportedOperationException("ContainerFactory is not initialized yet.");
        }
        return factory.wrapContainer0(container);
    }

    public abstract <C extends Container> Optional<C> wrapContainer0(Object container);

    public static <C extends Container> Optional<C> createContainer(InventoryTypeHolder type) {
        if (factory == null) {
            throw new UnsupportedOperationException("ContainerFactory is not initialized yet.");
        }
        return factory.createContainer0(type);
    }

    public static <C extends Container> Optional<C> createContainer(InventoryTypeHolder type, Component name) {
        if (factory == null) {
            throw new UnsupportedOperationException("ContainerFactory is not initialized yet.");
        }
        return factory.createContainer0(type, name);
    }

    public static <C extends Container> Optional<C> createContainer(int size) {
        if (factory == null) {
            throw new UnsupportedOperationException("ContainerFactory is not initialized yet.");
        }
        return factory.createContainer0(size);
    }

    public static <C extends Container> Optional<C> createContainer(int size, Component name) {
        if (factory == null) {
            throw new UnsupportedOperationException("ContainerFactory is not initialized yet.");
        }
        return factory.createContainer0(size, name);
    }

    public abstract <C extends Container> Optional<C> createContainer0(InventoryTypeHolder type);

    public abstract <C extends Container> Optional<C> createContainer0(InventoryTypeHolder type, Component name);

    public abstract <C extends Container> Optional<C> createContainer0(int size);

    public abstract <C extends Container> Optional<C> createContainer0(int size, Component name);
}
