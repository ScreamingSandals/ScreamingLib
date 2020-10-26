package org.screamingsandals.commands.core.handler;

import org.screamingsandals.commands.api.command.CommandContext;
import org.screamingsandals.commands.api.handler.CommandHandler;

import static org.screamingsandals.lib.core.lang.SLang.mpr;

public class CommandHandlerImpl implements CommandHandler {

    @Override
    public void handle(CommandContext context) {
        final var args = context.getArguments();
        final var sender = context.getSender();
        final var node = context.getNode();

        if (args.size() == 1) {
            if (!sender.hasPermission(node.getPermission())) {
                mpr("commands.fail.no_permission", context.getSender())
                        .replace("<nodeName>", node.getName())
                        .send();
            }

            node.getCallbacks().forEach(handler -> handler.handle(context));
        }

        if (args.size() == 2) {
            final var maybeSubNode = node.getSubNode(args.get(1));
            if (maybeSubNode.isEmpty()) {
                mpr("commands.fail.internal_error", context.getSender())
                        .replace("<message>", "SubNode not found!")
                        .send();
                return;
            }

            final var subNode = maybeSubNode.get();
            if (!sender.hasPermission(subNode.getPermission())) {
                mpr("commands.fail.no_permission_for_sub", context.getSender())
                        .replace("<nodeName>", node.getName())
                        .replace("<subNodeName>", subNode.getName())
                        .send();
            }
        }

    }
}
