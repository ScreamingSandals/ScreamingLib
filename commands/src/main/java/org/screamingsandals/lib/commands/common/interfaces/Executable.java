package org.screamingsandals.lib.commands.common.interfaces;

import java.util.List;

public interface Executable<T> {
    void execute(T sender, List<String> args);
}
