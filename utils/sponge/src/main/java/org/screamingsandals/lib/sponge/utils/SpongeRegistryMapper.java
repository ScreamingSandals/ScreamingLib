package org.screamingsandals.lib.sponge.utils;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.registry.Registry;
import org.spongepowered.api.registry.RegistryEntry;
import org.spongepowered.api.registry.RegistryType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface SpongeRegistryMapper<T> {

    @NotNull
    RegistryType<T> getRegistryType();

    default List<ResourceKey> getAllKeys() {
        return getRegistry()
                .streamEntries()
                .map(RegistryEntry::key)
                .collect(Collectors.toList());
    }

    default Registry<T> getRegistry() {
        return Sponge.getGame().registries().registry(getRegistryType());
    }

    default Optional<RegistryEntry<T>> getEntryOptional(ResourceKey key) {
        return getRegistry().findEntry(key);
    }

    default Optional<RegistryEntry<T>> getEntryOptional(String key) {
        return getEntryOptional(ResourceKey.resolve(key));
    }

    default RegistryEntry<T> getEntry(ResourceKey key) {
        return getEntryOptional(key).orElseThrow();
    }

    default RegistryEntry<T> getEntry(String key) {
        return getEntryOptional(key).orElseThrow();
    }

    default Optional<ResourceKey> getKeyByValueOptional(T value) {
        return getRegistry().findValueKey(value);
    }

    default ResourceKey getKeyByValue(T value) {
        return getKeyByValueOptional(value).orElseThrow();
    }

}
