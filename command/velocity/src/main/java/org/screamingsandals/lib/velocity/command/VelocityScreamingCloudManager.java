package org.screamingsandals.lib.velocity.command;

import cloud.commandframework.CommandTree;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.velocity.VelocityCommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.proxiedplayer.ProxiedSenderWrapper;

import java.util.function.Function;

public class VelocityScreamingCloudManager extends VelocityCommandManager<ProxiedSenderWrapper> {
    public VelocityScreamingCloudManager(@Nullable PluginContainer plugin,
                                         @NonNull ProxyServer proxyServer,
                                         @NonNull Function<@NonNull CommandTree<ProxiedSenderWrapper>,
                                                 @NonNull CommandExecutionCoordinator<ProxiedSenderWrapper>> commandExecutionCoordinator,
                                         @NonNull Function<@NonNull CommandSource, @NonNull ProxiedSenderWrapper> commandSenderMapper,
                                         @NonNull Function<@NonNull ProxiedSenderWrapper, @NonNull CommandSource> backwardsCommandSenderMapper) {
        super(plugin, proxyServer, commandExecutionCoordinator, commandSenderMapper, backwardsCommandSenderMapper);
    }
}
