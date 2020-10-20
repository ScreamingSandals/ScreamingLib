package org.screamingsandals.commands.api.command;

import lombok.Data;
import org.screamingsandals.lib.core.wrapper.SenderWrapper;

import java.util.List;

@Data
public class CommandContext {
    private SenderWrapper<?> sender;
    private CommandNode node;
    private List<String> arguments;
}
