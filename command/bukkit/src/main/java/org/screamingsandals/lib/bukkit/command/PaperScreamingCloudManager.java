package org.screamingsandals.lib.bukkit.command;

import cloud.commandframework.CommandTree;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.sender.CommandSenderWrapper;

import java.util.function.Function;

public class PaperScreamingCloudManager extends PaperCommandManager<CommandSenderWrapper> {
    /**
     * Construct a new Paper command manager
     *
     * @param owningPlugin       Plugin that is constructing the manager
     * @param commandCoordinator Coordinator provider
     */
    public PaperScreamingCloudManager(@NotNull Plugin owningPlugin,
                                      Function<CommandTree<CommandSenderWrapper>, CommandExecutionCoordinator<CommandSenderWrapper>> commandCoordinator) throws Exception {
        super(owningPlugin, commandCoordinator,
                sender -> {
                    if (sender instanceof Player) {
                        return PlayerMapper.wrapPlayer(sender);
                    }
                    return PlayerMapper.wrapSender(sender);
                }, sender -> {
                    if (sender.getType() == CommandSenderWrapper.Type.PLAYER) {
                        return sender.as(Player.class);
                    }
                    return sender.as(CommandSender.class);
                });

        if (queryCapability(CloudBukkitCapabilities.BRIGADIER)) {
            registerBrigadier();
            brigadierManager().setNativeNumberSuggestions(false);
        }

        if (queryCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            registerAsynchronousCompletions();
        }
    }
}
