package org.screamingsandals.commands.core.registry;

import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.registry.CommandRegistry;
import org.screamingsandals.commands.api.registry.ServerRegistry;
import org.screamingsandals.commands.core.command.AbstractCommandWrapper;
import org.screamingsandals.lib.core.util.result.Result;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCommandRegistry<T> implements CommandRegistry {
    private final Map<String, CommandNode> registeredNodes = new HashMap<>();
    private final AbstractCommandWrapper<T> wrapper;
    private final ServerRegistry<T> serverRegistry;

    public AbstractCommandRegistry(AbstractCommandWrapper<T> wrapper, ServerRegistry<T> serverRegistry) {
        this.wrapper = wrapper;
        this.serverRegistry = serverRegistry;
    }

    @Override
    public Result registerNode(CommandNode node) {
        if (registeredNodes.containsKey(node.getName())) {
            return Result.fail("Node already registered!");
        }

        final var registerResult = serverRegistry.register(wrapper.get(node).getCommand());

        if (registerResult.isOk()) {
            registeredNodes.put(node.getName(), node);
            return Result.ok();
        }

        return registerResult;
    }

    @Override
    public CommandNode getNode(String name) {
        return registeredNodes.getOrDefault(name, null);
    }

    @Override
    public Result removeNode(CommandNode node) {
        if (!registeredNodes.containsKey(node.getName())) {
            return Result.fail("Node is not registered");
        }

        registeredNodes.remove(node.getName());
        return Result.ok();
    }

    @Override
    public Map<String, CommandNode> getNodes() {
        return new HashMap<>(registeredNodes);
    }
}
