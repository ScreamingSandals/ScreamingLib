package org.screamingsandals.commands.core.command;

import lombok.Data;
import net.md_5.bungee.api.chat.TextComponent;
import org.screamingsandals.commands.api.builder.SCBuilder;
import org.screamingsandals.commands.api.command.CommandCallback;
import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.lib.core.wrapper.PlayerWrapper;

import java.util.*;

@Data
public class SimpleCommandNode implements CommandNode {
    private final Map<String, CommandNode> subNodes = new HashMap<>();
    private final String name;

    private String permissions;
    private String description;
    private String usage;

    private CommandNode parent;
    private CommandNode owner;
    private List<CommandCallback> callbacks = new LinkedList<>();

    private SimpleCommandNode(String name) {
        this.name = name;
    }

    private SimpleCommandNode(String name, CommandNode node) {
        this.name = name;
        this.permissions = node.getPermissions();
        this.description = node.getPermissions();
        this.usage = node.getUsage();
        this.parent = node.getParent();
        this.owner = node.getOwner().orElse(null);
    }

    public static SimpleCommandNode empty(String name) {
        return new SimpleCommandNode(name);
    }

    public static SimpleCommandNode copy(String newName, CommandNode node) {
        return new SimpleCommandNode(newName, node);
    }


    @Override
    public void addSubNode(CommandNode node) {
        subNodes.putIfAbsent(node.getName(), node);
    }

    @Override
    public Optional<CommandNode> getSubNode(String name) {
        return Optional.ofNullable(subNodes.get(name));
    }

    @Override
    public void addCallback(CommandCallback callback) {
        callbacks.add(callback);
    }

    public void test() {
        final var builder = SCBuilder.command("test");
        builder.callback(context -> {
            final var sender = context.getSender();
            final var args = context.getArguments();

            if (sender.isConsole()) {
                //do console stuff here
                return;
            }

            final var player = (PlayerWrapper<?>) sender;
            player.kick(TextComponent.fromLegacyText("YOU FUCKING ASSHOLE!"));

            if (args.size() == 1) {
                //WHOOOSH
            }
        }, CommandCallback.Priority.HIGH);
    }
}
