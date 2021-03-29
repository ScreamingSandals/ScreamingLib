package org.screamingsandals.lib.utils.adventure.wrapper;

import lombok.Data;
import net.kyori.adventure.title.Title;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.adventure.TitleUtils;

@Data
public final class TitleTimesWrapper implements Wrapper {
    private final Title.Times times;

    public Title.Times asTitleTimes() {
        return times;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (type.isInstance(times)) {
            return (T) times;
        }

        return (T) TitleUtils.timesToPlatform(times, type);
    }
}
