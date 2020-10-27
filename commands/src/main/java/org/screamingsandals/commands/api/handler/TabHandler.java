package org.screamingsandals.commands.api.handler;

import org.screamingsandals.commands.api.command.CommandContext;

import java.util.List;

@FunctionalInterface
public interface TabHandler {

    List<String> handle(CommandContext context);
}
