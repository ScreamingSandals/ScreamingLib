package org.screamingsandals.commands.core.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.handler.CommandHandler;

import com.google.common.collect.ImmutableMap;

import lombok.RequiredArgsConstructor;

/**
 * @author Frantisek Novosad (fnovosad@monetplus.cz)
 */
@RequiredArgsConstructor
public abstract class AbstractCommandWrapper<T> {
    private final Map<CommandNode, WrappedCommand<T>> wrappedCommands = new HashMap<>();
    protected final CommandHandler handler;

    public WrappedCommand<T> internalWrap(CommandNode node) {
        final var wrapped = wrap(node);
        wrappedCommands.put(node, wrapped);
        return wrapped;
    }

    public abstract WrappedCommand<T> wrap(CommandNode node);

    public Map<CommandNode, WrappedCommand<T>> getAll() {
        return ImmutableMap.copyOf(wrappedCommands);
    }

    public Optional<WrappedCommand<T>> get(CommandNode node) {
        return Optional.of(wrappedCommands.get(node));
    }

    public interface WrappedCommand<T> {
        T get();
    }
}
