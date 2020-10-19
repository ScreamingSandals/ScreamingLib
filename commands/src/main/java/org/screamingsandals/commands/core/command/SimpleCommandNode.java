package org.screamingsandals.commands.core.command;

import lombok.Data;
import org.screamingsandals.commands.api.command.CommandHandler;
import org.screamingsandals.commands.api.command.CommandNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
public class SimpleCommandNode implements CommandNode {
    private final Map<String, CommandNode> subNodes = new HashMap<>();
    private final String name;

    private String permissions;
    private String description;
    private String usage;
    private CommandNode parent;
    private CommandNode owner;
    private CommandHandler handler;

    public SimpleCommandNode(String name) {
        this.name = name;
    }

    public SimpleCommandNode(String name, CommandNode node) {
        this.name = name;
        this.permissions = node.getPermissions();
        this.description = node.getPermissions();
        this.usage = node.getUsage();
        this.parent = node.getParent();
        this.owner = node.getOwner().orElse(null);
        this.handler = node.getHandler();
    }


    @Override
    public void addSubNode(CommandNode node) {
        subNodes.putIfAbsent(node.getName(), node);
    }

    @Override
    public Optional<CommandNode> getSubNode(String name) {
        return Optional.ofNullable(subNodes.get(name));
    }
}
