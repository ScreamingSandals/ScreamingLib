package org.screamingsandals.lib.utils.adventure.wrapper;

import lombok.Data;
import net.kyori.adventure.bossbar.BossBar;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.adventure.BossBarUtils;

@Data
public final class BossBarOverlayWrapper implements Wrapper {
    private final BossBar.Overlay overlay;

    public BossBar.Overlay asBossBarOverlay() {
        return overlay;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (String.class.isAssignableFrom(type)) {
            return (T) overlay.name();
        } else if (type.isInstance(overlay)) {
            return (T) overlay;
        }

        return (T) BossBarUtils.overlayToPlatform(overlay, type);
    }
}
