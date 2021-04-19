package org.screamingsandals.lib.material.firework;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.util.RGBLike;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.List;

@Data
@RequiredArgsConstructor
public class FireworkEffectHolder implements Wrapper {
    private final String platformName;
    private final List<RGBLike> colors;
    private final List<RGBLike> fadeColors;
    private final boolean flicker;
    private final boolean trail;

    public FireworkEffectHolder(String name) {
        this(name, List.of(), List.of(), true, true);
    }

    public FireworkEffectHolder colors(List<RGBLike> colors) {
        return new FireworkEffectHolder(this.platformName, colors, this.fadeColors, this.flicker, this.trail);
    }

    public FireworkEffectHolder fadeColors(List<RGBLike> fadeColors) {
        return new FireworkEffectHolder(this.platformName, this.colors, fadeColors, this.flicker, this.trail);
    }

    public FireworkEffectHolder flicker(boolean flicker) {
        return new FireworkEffectHolder(this.platformName, this.colors, this.fadeColors, flicker, this.trail);
    }

    public FireworkEffectHolder trail(boolean trail) {
        return new FireworkEffectHolder(this.platformName, this.colors, this.fadeColors, this.flicker, trail);
    }


    @Override
    public <T> T as(Class<T> type) {
        return FireworkEffectMapping.convertFireworkEffectHolder(this, type);
    }
}
