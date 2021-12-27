package org.screamingsandals.lib.utils.adventure.wrapper;

import lombok.Data;
import net.kyori.adventure.bossbar.BossBar;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.adventure.BossBarUtils;

@Data
public final class BossBarFlagWrapper implements Wrapper {
    private final BossBar.Flag flag;

    public BossBar.Flag asBossBarFlag() {
        return flag;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (String.class.isAssignableFrom(type)) {
            return (T) flag.name();
        } else if (type.isInstance(flag)) {
            return (T) flag;
        }

        return (T) BossBarUtils.flagToPlatform(flag, type);
    }
}
