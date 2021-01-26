package org.screamingsandals.lib.bukkit.command;

import cloud.commandframework.CommandTree;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.SenderWrapper;

import java.util.function.Function;

public class BukkitScreamingCloudManager extends BukkitCommandManager<SenderWrapper> {

    public BukkitScreamingCloudManager(org.bukkit.plugin.@NonNull Plugin owningPlugin,
                                       @NonNull Function<@NonNull CommandTree<SenderWrapper>,
                                               @NonNull CommandExecutionCoordinator<SenderWrapper>> commandExecutionCoordinator) throws Exception {
        super(owningPlugin, commandExecutionCoordinator,
                PlayerMapper::wrapSender, sender -> sender.as(CommandSender.class));
    }
}
