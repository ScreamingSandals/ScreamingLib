package org.screamingsandals.lib.bukkit.command;

import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.SenderWrapper;

public class PaperScreamingCloudManager extends PaperCommandManager<SenderWrapper> {

    public PaperScreamingCloudManager(@NonNull Plugin owningPlugin) throws Exception {
        super(owningPlugin, AsynchronousCommandExecutionCoordinator.<SenderWrapper>newBuilder().build(),
                PlayerMapper::wrapSender, sender -> sender.as(CommandSender.class));
    }
}
