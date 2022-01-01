package org.screamingsandals.lib.spectator;

import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

@LimitedVersionSupport(">= 1.14")
public interface NBTComponent extends SeparableComponent {
    String nbtPath();

    boolean interpret();

    interface Builder<B extends Builder<B, C>, C extends NBTComponent> extends SeparableComponent.Builder<B, C> {
        B nbtPath(String nbtPath);

        default B interpret() {
            return interpret(true);
        }

        B interpret(boolean interpret);
    }
}
