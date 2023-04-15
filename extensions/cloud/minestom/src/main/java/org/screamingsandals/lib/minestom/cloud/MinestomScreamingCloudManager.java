package org.screamingsandals.lib.minestom.cloud;

import cloud.commandframework.CommandTree;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import io.github.openminigameserver.cloudminestom.MinestomCommandManager;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.SenderWrapper;
import org.screamingsandals.lib.sender.CommandSenderWrapper;

import java.util.function.Function;

public class MinestomScreamingCloudManager extends MinestomCommandManager<CommandSenderWrapper> {
    /**
     * Create a new command manager instance
     *
     * @param commandCoordinator Coordinator provider
     */
    protected MinestomScreamingCloudManager(Function<CommandTree<CommandSenderWrapper>, CommandExecutionCoordinator<CommandSenderWrapper>> commandCoordinator) {
        super(commandCoordinator,
                sender -> {
                    if (sender instanceof Player) {
                        return PlayerMapper.wrapPlayer(sender);
                    }
                    return PlayerMapper.wrapSender(sender);
                }, sender -> {
                    if (sender.getType() == SenderWrapper.Type.PLAYER) {
                        return sender.as(Player.class);
                    }
                    return sender.as(CommandSender.class);
                });
    }
}
