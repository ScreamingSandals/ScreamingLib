package org.screamingsandals.lib.utils.adventure.wrapper;

import lombok.Data;
import net.kyori.adventure.bossbar.BossBar;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.adventure.BossBarUtils;

@Data
public final class BossBarColorWrapper implements Wrapper {
    private final BossBar.Color color;

    public BossBar.Color asBossBarColor() {
        return color;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (String.class.isAssignableFrom(type)) {
            return (T) color.name();
        } else if (type.isInstance(color)) {
            return (T) color;
        }

        return (T) BossBarUtils.colorToPlatform(color, type);
    }
}
