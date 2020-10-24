package org.screamingsandals.commands.core.command;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Data;
import net.kyori.adventure.text.Component;
import org.screamingsandals.commands.api.builder.SCBuilder;
import org.screamingsandals.commands.api.command.CommandCallback;
import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.tab.TabCallback;

import java.util.*;

@Data
public class SimpleCommandNode implements CommandNode {
    private final Map<String, CommandNode> subNodes = new HashMap<>();
    private final String name;

    private String permission;
    private String description;
    private String usage;
    private List<String> aliases = new LinkedList<>();

    private CommandNode parent;
    private CommandNode owner;
    private Multimap<CommandCallback.Priority, CommandCallback> callbacks = ArrayListMultimap.create();
    private TabCallback tabCallback;

    private SimpleCommandNode(String name) {
        this.name = name;
    }

    private SimpleCommandNode(String name, CommandNode node) {
        this.name = name;
        this.permission = node.getPermission();
        this.description = node.getPermission();
        this.usage = node.getUsage();
        this.parent = node.getParent().orElse(null);
        this.owner = node.getOwner().orElse(null);
    }

    public static SimpleCommandNode empty(String name) {
        return new SimpleCommandNode(name);
    }

    public static SimpleCommandNode copy(String newName, CommandNode node) {
        return new SimpleCommandNode(newName, node);
    }

    public static SimpleCommandNode buildNode(String name, String permission,
                                              String description, String usage,
                                              Multimap<CommandCallback.Priority, CommandCallback> callbacks,
                                              TabCallback tabCallback) {
        return buildNode(name, permission, description, usage, callbacks, null, null, tabCallback);
    }

    public static SimpleCommandNode buildNode(String name, String permission,
                                              String description, String usage,
                                              Multimap<CommandCallback.Priority, CommandCallback> callbacks,
                                              CommandNode parent, CommandNode owner,
                                              TabCallback tabCallback) {
        final var node = new SimpleCommandNode(name);
        node.setPermission(permission);
        node.setDescription(description);
        node.setUsage(usage);
        node.setCallbacks(callbacks);
        node.setParent(parent);
        node.setOwner(owner);
        node.setTabCallback(tabCallback);
        return node;
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

    @Override
    public Optional<CommandNode> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public Optional<CommandNode> getOwner() {
        return Optional.ofNullable(owner);
    }

    public void test() {
        final var parentNode = SCBuilder.command("test")
                .callback(context -> {
                    final var sender = context.getSender();
                    final var args = context.getArguments();

                    if (sender.isSender()) {
                        //do console stuff here
                        return;
                    }

                    final var player = sender.getPlayer();
                    player.kick(Component.text("What a dumbass.."));

                    if (args.size() == 1) {
                        //WHOOOSH
                    }
                })
                .tabCallback(context -> {

                })
                .description("description")
                .usage("YoU dUMbAsS!")
                .permission("use.my.ass")
                .build();

        parentNode.addSubNode(SCBuilder.subCommand("test", parentNode)
                .permission("kokot")
                .callback(callback -> {

                })
                .build());
    }
}
