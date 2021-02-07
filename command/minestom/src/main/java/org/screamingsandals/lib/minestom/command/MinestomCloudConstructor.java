package org.screamingsandals.lib.minestom.command;

import cloud.commandframework.CommandManager;
import cloud.commandframework.CommandTree;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import net.minestom.server.extensions.Extension;
import org.screamingsandals.lib.command.CloudConstructor;
import org.screamingsandals.lib.minestom.player.MinestomPlayerMapper;
import org.screamingsandals.lib.sender.CommandSenderWrapper;
import org.screamingsandals.lib.utils.InitUtils;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.function.Function;

@Service(dependsOn = {
        MinestomPlayerMapper.class
})
public class MinestomCloudConstructor extends CloudConstructor {
    public static void init(Extension extension) {
        CloudConstructor.init(() -> new MinestomCloudConstructor(extension));
    }

    public MinestomCloudConstructor(Extension extension) {
        InitUtils.doIfNot(MinestomPlayerMapper::isInitialized, () -> MinestomPlayerMapper.init(extension));
    }

    @Override
    public CommandManager<CommandSenderWrapper> construct0(Function<CommandTree<CommandSenderWrapper>, CommandExecutionCoordinator<CommandSenderWrapper>> commandCoordinator) {
        return new MinestomScreamingCloudManager(commandCoordinator);
    }
}
