package org.screamingsandals.commands.api.command;

import lombok.Data;
import org.screamingsandals.lib.core.player.PlayerWrapper;

import java.util.List;

@Data
public class CommandContext {
    private PlayerWrapper<?> player;
    private CommandNode node;
    private List<String> arguments;
}
