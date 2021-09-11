package org.screamingsandals.lib.utils.adventure.wrapper;

import lombok.Data;
import net.kyori.adventure.title.TitlePart;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.adventure.TitleUtils;

@Data
public final class TitlePartWrapper implements Wrapper {
    private final TitlePart<?> part;

    @SuppressWarnings("unchecked")
    public <T> TitlePart<T> asTitlePart() {
        return (TitlePart<T>) part;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (type.isInstance(part)) {
            return (T) part;
        }

        return (T) TitleUtils.titlePartToPlatform(part, type);
    }
}
