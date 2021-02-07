package org.screamingsandals.lib.velocity.command;

import cloud.commandframework.CommandManager;
import cloud.commandframework.CommandTree;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import org.screamingsandals.lib.command.CloudConstructor;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.utils.InitUtils;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.velocity.proxiedplayer.VelocityProxiedPlayerMapper;

import java.util.function.Function;

@Service(dependsOn = {
        VelocityProxiedPlayerMapper.class
})
public class VelocityCloudConstructor extends CloudConstructor {
    private final PluginContainer plugin;
    private final ProxyServer proxyServer;

    public static void init(PluginContainer plugin, ProxyServer proxyServer) {
        CloudConstructor.init(() -> new VelocityCloudConstructor(plugin, proxyServer));
    }

    public VelocityCloudConstructor(PluginContainer plugin, ProxyServer proxyServer) {
        this.plugin = plugin;
        this.proxyServer = proxyServer;

        InitUtils.doIfNot(VelocityCloudConstructor::isInitialized, () -> VelocityProxiedPlayerMapper.init(plugin.getInstance().orElseThrow(), proxyServer));
    }

    @Override
    public CommandManager<CommandSenderWrapper> construct0(Function<CommandTree<CommandSenderWrapper>, CommandExecutionCoordinator<CommandSenderWrapper>> commandCoordinator) {
        return new VelocityScreamingCloudManager(plugin, proxyServer, commandCoordinator);
    }
}
