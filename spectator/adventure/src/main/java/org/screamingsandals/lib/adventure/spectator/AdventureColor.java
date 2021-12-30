package org.screamingsandals.lib.adventure.spectator;

import net.kyori.adventure.text.format.TextColor;
import org.screamingsandals.lib.spectator.Color;
import org.screamingsandals.lib.utils.BasicWrapper;

public class AdventureColor extends BasicWrapper<TextColor> implements Color {
    public AdventureColor(TextColor wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public int red() {
        return wrappedObject.red();
    }

    @Override
    public int green() {
        return wrappedObject.green();
    }

    @Override
    public int blue() {
        return wrappedObject.blue();
    }
}
