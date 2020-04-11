package org.screamingsandals.lib.commands.common.commands;

import lombok.Data;

import java.util.List;

@Data
public class SubCommand {
    private String name;
    private String permission;
    private List<String> aliases;
}
