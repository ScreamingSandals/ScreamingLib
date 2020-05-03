package org.screamingsandals.lib.commands.common.commands;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SubCommand {
    private String name;
    private String permission;
    private List<String> aliases;
}
