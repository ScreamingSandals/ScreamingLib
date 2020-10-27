package org.screamingsandals.commands.core.command;

import org.screamingsandals.commands.api.command.CommandCallback;
import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.command.SubCommandNode;
import org.screamingsandals.commands.api.tab.TabCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
                                          List<CommandCallback> callbacks,
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
    public void removeSubNode(String name) {
        subNodes.remove(name);
    }

    @Override
    public Map<String, SubCommandNode> getSubNodes() {
        return new HashMap<>(subNodes);
    }
}
