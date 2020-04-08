package org.screamingsandals.lib.commands.common;

import org.screamingsandals.lib.commands.api.interfaces.CompleteTab;
import org.screamingsandals.lib.commands.api.interfaces.Execute;

import java.util.LinkedList;
import java.util.List;

public abstract class CommandFrame {

    public static class PlayerCommand<T> implements Execute.Player<T>, CompleteTab.Player<T> {
        @Override
        public void execute(T player, List<String> args) {

        }

        @Override
        public List<String> complete(T player, List<String> args) {
            return new LinkedList<>();
        }
    }

    public static class ConsoleCommand<E> implements Execute.Console<E>, CompleteTab.Console<E> {
        @Override
        public void execute(E console, List<String> args) {

        }

        @Override
        public List<String> complete(E console, List<String> args) {
            return new LinkedList<>();
        }
    }
}
