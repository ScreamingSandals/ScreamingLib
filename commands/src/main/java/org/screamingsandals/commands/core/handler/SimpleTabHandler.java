package org.screamingsandals.commands.core.handler;

import org.screamingsandals.commands.api.command.CommandContext;
import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.handler.TabHandler;
import org.screamingsandals.lib.core.wrapper.sender.SenderWrapper;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class SimpleTabHandler implements TabHandler {

    @Override
    public List<String> handle(CommandContext context) {
        final var args = context.getArguments();
        final var sender = context.getSender();
        final var node = context.getNode();

        LoggerFactory.getLogger("args").info(args.toString());
        //Sub Command
        if (args.isEmpty()) {
            final var toReturn = new LinkedList<String>();
            final var tabCallback = node.getTabCallback();

            if (tabCallback != null) {
                toReturn.addAll(tabCallback.handle(context));
            }

            toReturn.addAll(getAvailableSubNodes(node, sender));

            return toReturn;
        }

        //sub command callback
        final var subNodeName = args.get(0);
        final var maybeSubNode = node.getSubNode(subNodeName);

        if (maybeSubNode.isEmpty()) {
            return getAvailableSubNodes(node, sender);
        }

        final var subNode = maybeSubNode.get();
        final var tabCallback = subNode.getTabCallback();

        if (tabCallback == null) {
            return List.of();
        }

        return subNode.getTabCallback().handle(context);
    }

    private List<String> getAvailableSubNodes(CommandNode node, SenderWrapper<?> sender) {
        final var toReturn = new LinkedList<String>();
        final var nodes = node.getSubNodes();

        if (nodes.isEmpty()) {
            return List.of("");
        }

        nodes.values().forEach(subNode -> {
            if (sender.hasPermission(subNode.getPermission())) {
                toReturn.add(subNode.getName());
            }
        });

        return toReturn;
    }
}
