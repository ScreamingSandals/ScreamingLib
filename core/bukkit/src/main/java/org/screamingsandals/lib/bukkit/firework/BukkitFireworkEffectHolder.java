package org.screamingsandals.lib.bukkit.firework;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.screamingsandals.lib.firework.FireworkEffectHolder;
import org.screamingsandals.lib.utils.BasicWrapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BukkitFireworkEffectHolder extends BasicWrapper<FireworkEffect> implements FireworkEffectHolder {
    public BukkitFireworkEffectHolder(FireworkEffect.Type type) {
        this(FireworkEffect.builder().with(type).build());
    }

    public BukkitFireworkEffectHolder(FireworkEffect wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public String platformName() {
        return wrappedObject.getType().name();
    }

    @Override
    public List<RGBLike> colors() {
        return wrappedObject.getColors()
                .stream()
                .map(color -> TextColor.color(color.getRed(), color.getGreen(), color.getBlue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<RGBLike> fadeColors() {
        return wrappedObject.getFadeColors()
                .stream()
                .map(color -> TextColor.color(color.getRed(), color.getGreen(), color.getBlue()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean flicker() {
        return wrappedObject.hasFlicker();
    }

    @Override
    public boolean trail() {
        return wrappedObject.hasTrail();
    }

    @Override
    public FireworkEffectHolder withColors(List<RGBLike> colors) {
        /* Dear Bukkit API, I hate your inconsistency so much */
        return new BukkitFireworkEffectHolder(
                FireworkEffect.builder()
                        .with(wrappedObject.getType())
                        .withColor(colors.stream().map(rgbLike -> Color.fromRGB(rgbLike.red(), rgbLike.green(), rgbLike.blue())).collect(Collectors.toList()))
                        .withFade(wrappedObject.getFadeColors())
                        .flicker(wrappedObject.hasFlicker())
                        .trail(wrappedObject.hasTrail())
                        .build()
        );
    }

    @Override
    public FireworkEffectHolder withFadeColors(List<RGBLike> fadeColors) {
        /* Dear Bukkit API, I hate your inconsistency so much */
        return new BukkitFireworkEffectHolder(
                FireworkEffect.builder()
                        .with(wrappedObject.getType())
                        .withColor(wrappedObject.getColors())
                        .withFade(fadeColors.stream().map(rgbLike -> Color.fromRGB(rgbLike.red(), rgbLike.green(), rgbLike.blue())).collect(Collectors.toList()))
                        .flicker(wrappedObject.hasFlicker())
                        .trail(wrappedObject.hasTrail())
                        .build()
        );
    }

    @Override
    public FireworkEffectHolder withFlicker(boolean flicker) {
        /* Dear Bukkit API, I hate your inconsistency so much */
        return new BukkitFireworkEffectHolder(
                FireworkEffect.builder()
                        .with(wrappedObject.getType())
                        .withColor(wrappedObject.getColors())
                        .withFade(wrappedObject.getFadeColors())
                        .flicker(flicker)
                        .trail(wrappedObject.hasTrail())
                        .build()
        );
    }

    @Override
    public FireworkEffectHolder withTrail(boolean trail) {
        /* Dear Bukkit API, I hate your inconsistency so much */
        return new BukkitFireworkEffectHolder(
                FireworkEffect.builder()
                        .with(wrappedObject.getType())
                        .withColor(wrappedObject.getColors())
                        .withFade(wrappedObject.getFadeColors())
                        .flicker(wrappedObject.hasFlicker())
                        .trail(trail)
                        .build()
        );
    }

    @Override
    public boolean is(Object object) {
        if (object instanceof FireworkEffect.Type) {
            return wrappedObject.getType().equals(object);
        }
        if (object instanceof FireworkEffect || object instanceof FireworkEffectHolder) {
            return equals(object);
        }
        return equals(FireworkEffectHolder.ofOptional(object).orElse(null));
    }

    @Override
    public boolean is(Object... objects) {
        return Arrays.stream(objects).anyMatch(this::is);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (type == FireworkEffect.Type.class) {
            return (T) wrappedObject.getType();
        }
        return super.as(type);
    }
}