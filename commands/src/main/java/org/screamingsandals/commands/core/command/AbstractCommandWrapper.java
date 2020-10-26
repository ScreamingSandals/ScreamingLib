package org.screamingsandals.commands.core.command;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.screamingsandals.commands.api.command.CommandBase;
import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.handler.CommandHandler;
import org.screamingsandals.commands.api.handler.TabHandler;
import org.screamingsandals.commands.api.wrapper.WrappedCommand;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class AbstractCommandWrapper<T> {
    private final Map<CommandBase, WrappedCommand<T>> wrappedCommands = new HashMap<>();
    protected final CommandHandler commandHandler;
    protected final TabHandler tabHandler;

    private WrappedCommand<T> internalWrap(CommandNode node) {
        final var wrapped = wrap(node);
        wrappedCommands.put(node, wrapped);
        return wrapped;
    }

    public abstract WrappedCommand<T> wrap(CommandNode node);

    public Map<CommandBase, WrappedCommand<T>> getAll() {
        return ImmutableMap.copyOf(wrappedCommands);
    }

    public WrappedCommand<T> get(CommandNode node) {
        if (wrappedCommands.containsKey(node)) {
            return wrappedCommands.get(node);
        }

        return internalWrap(node);
    }
}
