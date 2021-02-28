package org.screamingsandals.lib.bukkit.world.state;

import net.kyori.adventure.text.Component;
import org.bukkit.block.Sign;
import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.utils.AdventureHelper;
import org.screamingsandals.lib.world.state.SignHolder;

import java.util.Arrays;

public class SignBlockStateHolder extends TileBlockStateHolder implements SignHolder {
    protected SignBlockStateHolder(Sign wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public Component[] lines() {
        return Arrays.stream(((Sign) wrappedObject).getLines()).map(AdventureHelper::toComponent).toArray(Component[]::new);
    }

    @Override
    public Component line(@Range(from = 0, to = 3) int index) {
        return lines()[index];
    }

    @Override
    public void line(@Range(from = 0, to = 3) int index, Component component) {
        ((Sign) wrappedObject).setLine(index, AdventureHelper.toLegacy(component));
    }
}
