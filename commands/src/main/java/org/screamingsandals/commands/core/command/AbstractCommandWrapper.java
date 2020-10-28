package org.screamingsandals.commands.core.command;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.commands.api.command.CommandBase;
import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.registry.HandlerRegistry;
import org.screamingsandals.commands.api.wrapper.WrappedCommand;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class AbstractCommandWrapper<T> {
    private final Map<CommandBase, WrappedCommand<T>> cachedWraps = new HashMap<>();
    protected final HandlerRegistry handlerRegistry;

    public Map<CommandBase, WrappedCommand<T>> getAll() {
        return ImmutableMap.copyOf(cachedWraps);
    }

    public WrappedCommand<T> get(CommandNode node) {
        if (cachedWraps.containsKey(node)) {
            return cachedWraps.get(node);
        }

        return internalWrap(node);
    }

    protected abstract WrappedCommand<T> wrap(CommandNode node);

    private WrappedCommand<T> internalWrap(CommandNode node) {
        final var wrapped = wrap(node);
        cachedWraps.put(node, wrapped);
        return wrapped;
    }
}
