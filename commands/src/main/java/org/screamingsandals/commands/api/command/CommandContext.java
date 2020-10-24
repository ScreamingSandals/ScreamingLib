package org.screamingsandals.commands.api.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.screamingsandals.lib.core.wrapper.sender.SenderWrapper;

import java.util.List;

@Data
@AllArgsConstructor
public class CommandContext {
    private SenderWrapper<?> sender;
    private CommandNode node;
    private List<String> arguments;
}
