package org.screamingsandals.commands.core.command;

import lombok.Getter;
import org.screamingsandals.commands.api.command.CommandCallback;
import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.command.SubCommandNode;
import org.screamingsandals.commands.api.tab.TabCallback;

import java.util.List;

@Getter
public class SimpleSubCommandNode extends AbstractCommandBase implements SubCommandNode {
    private final CommandNode parent;

    protected SimpleSubCommandNode(String name, CommandNode parent) {
        super(name);
        this.parent = parent;
    }

    protected SimpleSubCommandNode(String name, SubCommandNode base, CommandNode parent) {
        super(name, base);
        this.parent = parent;
    }

    public static SimpleSubCommandNode empty(String name, CommandNode parent) {
        return new SimpleSubCommandNode(name, parent);
    }

    public static SimpleSubCommandNode copy(String newName, SubCommandNode node, CommandNode parent) {
        return new SimpleSubCommandNode(newName, node, parent);
    }

    public static SimpleSubCommandNode build(String name, CommandNode parent,
                                             String permission, String description,
                                             String usage,
                                             List<CommandCallback> callbacks,
                                             TabCallback tabCallback) {
        final var node = new SimpleSubCommandNode(name, parent);
        node.setPermission(permission);
        node.setDescription(description);
        node.setUsage(usage);
        node.setCallbacks(callbacks);
        node.setTabCallback(tabCallback);
        return node;
    }
}
