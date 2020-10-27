package org.screamingsandals.commands.core.handler;

import org.screamingsandals.commands.api.SCLangKeys;
import org.screamingsandals.commands.api.SCPlaceholders;
import org.screamingsandals.commands.api.command.CommandContext;
import org.screamingsandals.commands.api.handler.CommandHandler;

import static org.screamingsandals.lib.core.lang.SLang.mpr;

public class SimpleCommandHandler implements CommandHandler {

    @Override
    public void handle(CommandContext context) {
        final var args = context.getArguments();
        final var sender = context.getSender();
        final var node = context.getNode();

        if (args.isEmpty()) {
            if (!sender.hasPermission(node.getPermission())) {
                mpr(SCLangKeys.FAIL_NO_PERMISSIONS, context.getSender())
                        .replace(SCPlaceholders.NODE_NAME, node.getName())
                        .send();
                return;
            }

            node.getCallbacks().forEach(handler -> handler.handle(context));
            return;
        }

        if (args.size() == 1) {
            final var subNodeName = args.get(0);
            final var maybeSubNode = node.getSubNode(subNodeName);

            if (maybeSubNode.isEmpty()) {
                mpr(SCLangKeys.FAIL_INTERNAL_ERROR, context.getSender())
                        .replace(SCPlaceholders.SUB_NODE_NAME, subNodeName)
                        .send();
                return;
            }

            final var subNode = maybeSubNode.get();
            if (!sender.hasPermission(subNode.getPermission())) {
                mpr(SCLangKeys.FAIL_NO_PERMISSIONS, context.getSender())
                        .replace(SCPlaceholders.NODE_NAME, node.getName())
                        .replace(SCPlaceholders.SUB_NODE_NAME, subNode.getName())
                        .send();
                return;
            }

            subNode.getCallbacks().forEach(handler -> handler.handle(context));
        }
    }
}
