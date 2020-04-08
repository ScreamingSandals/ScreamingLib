package org.screamingsandals.lib.commands.bungee;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.entity.Player;
import org.screamingsandals.lib.commands.common.CommandFrame;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class BungeeCommandFrame extends CommandFrame {
    private final BungeeBuilder bungeeBuilder;
    private BungeeCommandWrapper bungeeCommandWrapper;

    public BungeeCommandFrame(BungeeBuilder bungeeBuilder) {
        this.bungeeBuilder = bungeeBuilder;
        this.bungeeCommandWrapper = createCommandInstance();
    }

    private BungeeCommandWrapper createCommandInstance() {
        final String commandName = bungeeBuilder.getName();
        final String permission = bungeeBuilder.getPermission();
        final List<String> aliases = bungeeBuilder.getAliases();

        return new BungeeCommandWrapper(commandName, permission, aliases) {
            @Override
            public void execute(net.md_5.bungee.api.CommandSender commandSender, String[] args) {
                if (commandSender instanceof Player) {
                    bungeeBuilder.getExecuteByPlayer().execute((ProxiedPlayer) commandSender, Arrays.asList(args));
                } else {
                    bungeeBuilder.getExecuteByConsole().execute(commandSender, Arrays.asList(args));
                }
            }

            @Override
            public Iterable<String> onTabComplete(net.md_5.bungee.api.CommandSender commandSender, String[] strings) {
                return super.onTabComplete(commandSender, strings);
            }
        };
    }
}
