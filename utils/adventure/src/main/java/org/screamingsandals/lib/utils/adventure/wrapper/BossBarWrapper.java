package org.screamingsandals.lib.utils.adventure.wrapper;

import lombok.Data;
import net.kyori.adventure.bossbar.BossBar;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.adventure.BossBarUtils;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.Arrays;

@Data
public final class BossBarWrapper implements Wrapper {
    private final BossBar bossBar;

    @NotNull
    public BossBar asBossBar() {
        return bossBar;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (type.isInstance(bossBar)) {
            return (T) bossBar;
        }

        var generalPackageSplit = Arrays.asList(type.getPackageName().split("\\."));
        generalPackageSplit.remove(generalPackageSplit.size() - 1);
        var generalPackage = String.join(".", generalPackageSplit);

        var component = Reflect.getClassSafe(generalPackage + ".text.Component");
        var gsonSerializer = Reflect.getClassSafe(generalPackage + ".text.serializer.gson.GsonComponentSerializer");
        var color = Reflect.getClassSafe(type.getName() + "$Color");
        var overlay = Reflect.getClassSafe(type.getName() + "$Overlay");
        var flag = Reflect.getClassSafe(type.getName() + "$Flag");
        if (component == null || gsonSerializer == null || color == null || overlay == null || flag == null) {
            throw new UnsupportedOperationException("Not supported! The target adventure needs to have gson serializer!");
        }

        return (T) BossBarUtils.bossBarToPlatform(bossBar, type, component, Reflect.fastInvoke(gsonSerializer, "gson"), flag, overlay, color);
    }
}
