package org.screamingsandals.commands.core.handler;

import org.screamingsandals.commands.api.SCLangKeys;
import org.screamingsandals.commands.api.SCPlaceholders;
import org.screamingsandals.commands.api.command.CommandContext;
import org.screamingsandals.commands.api.command.CommandNode;
import org.screamingsandals.commands.api.handler.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.screamingsandals.lib.core.lang.SLang.mpr;

public class SimpleCommandHandler implements CommandHandler {
    private final Map<CommandNode, Map<String, String>> cachedPlaceholders = new HashMap<>();
    private final Logger log = LoggerFactory.getLogger("CommandHandler");

    @Override
    public void handle(CommandContext context) {
        final var args = context.getArguments();
        final var sender = context.getSender();
        final var node = context.getNode();

        log.debug("Handling command [{}] from [{}] with args[{}]", node.getName(), sender.toString(), args.toString());

        if (args.isEmpty()) {
            if (!sender.hasPermission(node.getPermission())) {
                mpr(SCLangKeys.FAIL_NO_PERMISSIONS, context.getSender())
                        .withPlaceholders(getPlaceholders(node))
                        .send();
                return;
            }

            try {
                node.getCallbacks().forEach(handler -> handler.handle(context));
            } catch (Throwable t) {
                log.warn("Error occurred while calling Node callbacks! {}", t.getMessage(), t);
                mpr(SCLangKeys.FAIL_INTERNAL_ERROR, context.getSender())
                        .withPlaceholders(getPlaceholders(node))
                        .replace(SCPlaceholders.ERROR_MESSAGE, t.getMessage())
                        .send();
            }
            return;
        }

        if (args.size() == 1) {
            final var subNodeName = args.get(0);
            final var maybeSubNode = node.getSubNode(subNodeName);

            if (maybeSubNode.isEmpty()) {
                mpr(SCLangKeys.FAIL_COMMAND_NOT_FOUND, context.getSender())
                        .withPlaceholders(getPlaceholders(node))
                        .send();
                return;
            }

            final var subNode = maybeSubNode.get();
            if (!sender.hasPermission(subNode.getPermission())) {
                mpr(SCLangKeys.FAIL_NO_PERMISSIONS, context.getSender())
                        .withPlaceholders(getPlaceholders(node))
                        .send();
                return;
            }

            if (subNode.getCallbacks().isEmpty()) {
                mpr(SCLangKeys.FAIL_NO_CALLBACKS, context.getSender())
                        .withPlaceholders(getPlaceholders(node))
                        .send();
                return;
            }

            try {
                subNode.getCallbacks().forEach(handler -> handler.handle(context));
            } catch (Throwable t) {
                log.warn("Error occurred while calling Sub-Node callbacks! {}", t.getMessage(), t);
                mpr(SCLangKeys.FAIL_INTERNAL_ERROR, context.getSender())
                        .withPlaceholders(getPlaceholders(node))
                        .replace(SCPlaceholders.ERROR_MESSAGE, t.getMessage())
                        .send();
            }
        }
    }

    private Map<String, String> getPlaceholders(CommandNode node) {
        if (cachedPlaceholders.containsKey(node)) {
            return cachedPlaceholders.get(node);
        }

        final var placeholders = fillPlaceholders(node);
        cachedPlaceholders.put(node, placeholders);
        return placeholders;
    }

    private Map<String, String> fillPlaceholders(CommandNode node) {
        return Map.of(SCPlaceholders.DESCRIPTION, node.getDescription(),
                SCPlaceholders.NODE_NAME, node.getName(),
                SCPlaceholders.PERMISSION, node.getPermission(),
                SCPlaceholders.USAGE, node.getUsage());
    }
}
