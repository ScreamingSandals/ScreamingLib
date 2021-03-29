package org.screamingsandals.lib.utils.adventure;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.bossbar.BossBar;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class BossBarUtils {
    public final Class<?> NATIVE_BOSSBAR_CLASS
            = Reflect.getClassSafe("net.kyori.adventure.bossbar.BossBar");
    public final Class<?> NATIVE_BOSSBAR_OVERLAY_CLASS
            = Reflect.getClassSafe("net.kyori.adventure.bossbar.BossBar$Overlay");
    public final Class<?> NATIVE_BOSSBAR_FLAG_CLASS
            = Reflect.getClassSafe("net.kyori.adventure.bossbar.BossBar$Flag");
    public final Class<?> NATIVE_BOSSBAR_COLOR_CLASS
            = Reflect.getClassSafe("net.kyori.adventure.bossbar.BossBar$Color");

    public Object bossBarToPlatform(BossBar bossBar) {
        if (NATIVE_BOSSBAR_CLASS.isInstance(bossBar)) {
            return bossBar;
        }
        return bossBarToPlatform(
                bossBar,
                NATIVE_BOSSBAR_CLASS,
                ComponentUtils.NATIVE_COMPONENT_CLASS,
                ComponentUtils.NATIVE_GSON_COMPONENT_SERIALIZER_GETTER.invokeStatic(),
                NATIVE_BOSSBAR_FLAG_CLASS,
                NATIVE_BOSSBAR_OVERLAY_CLASS,
                NATIVE_BOSSBAR_COLOR_CLASS
        );
    }

    public Object bossBarToPlatform(BossBar bossBar, Class<?> bossBarClass, Class<?> componentClass, Object componentSerializer, Class<?> flagsClass, Class<?> overlayClass, Class<?> colorClass) {
        var platformBossBar = Reflect
                .getMethod(bossBarClass, "name", componentClass)
                .invokeStaticResulted(ComponentUtils.componentToPlatform(bossBar.name(), componentSerializer));

        // TODO: Cache boss bars if converting to native

        platformBossBar
                .getMethod("progress", float.class)
                .invoke(bossBar.progress());

        platformBossBar
                .getMethod("color", colorClass)
                .invoke(colorToPlatform(bossBar.color(), colorClass));

        platformBossBar
                .getMethod("overlay", overlayClass)
                .invoke(overlayToPlatform(bossBar.overlay(), overlayClass));

        platformBossBar
                .getMethod("flags", Set.class)
                .invoke(bossBar
                        .flags()
                        .stream()
                        .map(flag -> flagToPlatform(flag, flagsClass))
                        .collect(Collectors.toSet())
                );

        // TODO: Listeners

        return platformBossBar.raw();
    }

    @SuppressWarnings("unchecked")
    public BossBar bossBarFromPlatform(Object platformObject) {
        if (platformObject instanceof BossBar) {
            return (BossBar) platformObject;
        }
        return null; // TODO
    }

    public Object colorToPlatform(BossBar.Color color) {
        if (NATIVE_BOSSBAR_COLOR_CLASS.isInstance(color)) {
            return color;
        }
        return colorToPlatform(color, NATIVE_BOSSBAR_COLOR_CLASS);
    }

    public Object colorToPlatform(BossBar.Color color, Class<?> colorClass) {
        return Reflect.findEnumConstant(colorClass, color.name());
    }

    public BossBar.Color colorFromPlatform(Object platformObject) {
        if (platformObject instanceof BossBar.Color) {
            return (BossBar.Color) platformObject;
        }
        return BossBar.Color.valueOf(Reflect.fastInvoke(platformObject, "name").toString());
    }

    public Object overlayToPlatform(BossBar.Overlay overlay) {
        if (NATIVE_BOSSBAR_OVERLAY_CLASS.isInstance(overlay)) {
            return overlay;
        }
        return overlayToPlatform(overlay, NATIVE_BOSSBAR_OVERLAY_CLASS);
    }

    public Object overlayToPlatform(BossBar.Overlay overlay, Class<?> overlayClass) {
        return Reflect.findEnumConstant(overlayClass, overlay.name());
    }

    public BossBar.Overlay overlayFromPlatform(Object platformObject) {
        if (platformObject instanceof BossBar.Overlay) {
            return (BossBar.Overlay) platformObject;
        }
        return BossBar.Overlay.valueOf(Reflect.fastInvoke(platformObject, "name").toString());
    }

    public Object flagToPlatform(BossBar.Flag flag) {
        if (NATIVE_BOSSBAR_FLAG_CLASS.isInstance(flag)) {
            return flag;
        }
        return flagToPlatform(flag, NATIVE_BOSSBAR_FLAG_CLASS);
    }

    public Object flagToPlatform(BossBar.Flag flag, Class<?> flagClass) {
        return Reflect.findEnumConstant(flagClass, flag.name());
    }

    public BossBar.Flag flagFromPlatform(Object platformObject) {
        if (platformObject instanceof BossBar.Flag) {
            return (BossBar.Flag) platformObject;
        }
        return BossBar.Flag.valueOf(Reflect.fastInvoke(platformObject, "name").toString());
    }
}
