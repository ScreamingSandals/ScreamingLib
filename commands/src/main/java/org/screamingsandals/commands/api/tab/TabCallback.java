package org.screamingsandals.commands.api.tab;

import org.screamingsandals.commands.api.command.CommandContext;

import java.util.List;

@FunctionalInterface
public interface TabCallback {

    List<String> handle(CommandContext context);
}
