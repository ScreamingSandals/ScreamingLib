package org.screamingsandals.lib.utils.adventure.wrapper;

import lombok.Data;
import net.kyori.adventure.sound.Sound;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.adventure.SoundUtils;

@Data
public final class SoundSourceWrapper implements Wrapper {
    private final Sound.Source source;

    public Sound.Source asSoundSource() {
        return source;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (String.class.isAssignableFrom(type)) {
            return (T) source.name();
        } else if (type.isInstance(source)) {
            return (T) source;
        }

        return (T) SoundUtils.sourceToPlatform(source, type);
    }
}
