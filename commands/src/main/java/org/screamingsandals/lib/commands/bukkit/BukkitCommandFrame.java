package org.screamingsandals.lib.commands.bukkit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.commands.api.core.CommandsBase;
import org.screamingsandals.lib.commands.common.CommandFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class BukkitCommandFrame extends CommandFrame {
    private final BukkitBuilder bukkitBuilder;
    private Command bukkitCommand;

    public BukkitCommandFrame(BukkitBuilder bukkitBuilder) {
        this.bukkitBuilder = bukkitBuilder;
        bukkitCommand = createCommandInstance();
    }

    private Command createCommandInstance() {
        final String commandName = bukkitBuilder.getName();
        final List<String> aliases = bukkitBuilder.getAliases();

        final Command command = new Command(commandName, bukkitBuilder.getDescription(), bukkitBuilder.getUsage(), aliases) {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
                try {
                    if (commandSender instanceof Player) {
                        getSubCommand(bukkitBuilder, commandName, args).getExecuteByPlayer().execute((Player) commandSender, Arrays.asList(args));
                    } else {
                        getSubCommand(bukkitBuilder, commandName, args).getExecuteByConsole().execute((ConsoleCommandSender) commandSender, Arrays.asList(args));
                    }
                    return true;
                } catch (Throwable tr) {
                    tr.printStackTrace();
                    return false;
                }
            }

            @Override
            @NotNull
            public List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String alias1, @NotNull String[] args) {
                try {
                    if (commandSender instanceof Player) {
                        return bukkitBuilder.getCompleteByPlayer().complete((Player) commandSender, Arrays.asList(args));
                    } else {
                        return bukkitBuilder.getCompleteByConsole().complete((ConsoleCommandSender) commandSender, Arrays.asList(args));
                    }
                } catch (Throwable tr) {
                    tr.printStackTrace();
                    return new ArrayList<>();
                }
            }
        };

        command.setPermission(bukkitBuilder.getPermission());
        command.setPermissionMessage("No permissions!"); //TODO
        command.setAliases(aliases);
        command.setDescription(bukkitBuilder.getDescription());
        command.setUsage(bukkitBuilder.getUsage());

        return command;
    }

    private BukkitBuilder getSubCommand(BukkitBuilder original, String commandName, String[] args) {
        String subCommandName = "";
        if (args.length >= 1) {
            subCommandName = args[0];
        }

        final var commandFrames = ((BukkitManager) CommandsBase.getInstance().getCommandManager())
                        .getSubCommands().get(subCommandName);

        if (commandFrames == null) {
            return original;
        }

        for (var commandFrame : commandFrames) {
            final BukkitBuilder bukkitBuilder = commandFrame.getBukkitBuilder();
            if (bukkitBuilder.getName().equalsIgnoreCase(commandName)
                    && bukkitBuilder.getSubName().equalsIgnoreCase(subCommandName)) {
                return bukkitBuilder;
            }
        }

        return original;
    }
}
