package org.screamingsandals.commands.core.command;

import com.google.common.collect.Multimap;
import org.screamingsandals.commands.api.command.CommandCallback;
import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.command.SubCommandNode;
import org.screamingsandals.commands.api.tab.TabCallback;

import java.util.*;

public class SimpleCommandNode extends AbstractCommandBase implements CommandNode {
    private final Map<String, SubCommandNode> subNodes = new HashMap<>();

    protected SimpleCommandNode(String name) {
        super(name);
    }

    protected SimpleCommandNode(String name, CommandNode node) {
        super(name, node);
    }

    public static SimpleCommandNode empty(String name) {
        return new SimpleCommandNode(name);
    }

    public static SimpleCommandNode copy(String newName, CommandNode node) {
        return new SimpleCommandNode(newName, node);
    }

    public static SimpleCommandNode build(String name, String permission,
                                          String description, String usage,
                                          Multimap<CommandCallback.Priority, CommandCallback> callbacks,
                                          TabCallback tabCallback) {
        final var node = new SimpleCommandNode(name);
        node.setPermission(permission);
        node.setDescription(description);
        node.setUsage(usage);
        node.setCallbacks(callbacks);
        node.setTabCallback(tabCallback);
        return node;
    }


    @Override
    public void addSubNode(SubCommandNode node) {
        subNodes.putIfAbsent(node.getName(), node);
    }

    @Override
    public Optional<SubCommandNode> getSubNode(String name) {
        return Optional.ofNullable(subNodes.get(name));
    }

    @Override
    public Map<String, SubCommandNode> getSubNodes() {
        return null;
    }

    @Override
    public void addCallback(CommandCallback callback) {
        addCallback(CommandCallback.Priority.NORMAL, callback);
    }

    @Override
    public void addCallback(CommandCallback.Priority priority, CommandCallback callback) {
        callbacks.put(priority, callback);
    }

    @Override
    public List<CommandCallback> getCallbacks() {
        return new LinkedList<>(callbacks.values());
    }
}
