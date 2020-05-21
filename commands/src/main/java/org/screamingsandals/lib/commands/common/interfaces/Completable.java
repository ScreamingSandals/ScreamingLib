package org.screamingsandals.lib.commands.common.interfaces;

import java.util.List;

public interface Completable<T> {
    List<String> complete(T player, List<String> args);
}
