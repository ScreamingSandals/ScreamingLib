package org.screamingsandals.lib.world;

import lombok.Data;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.Map;

@Data
public class BlockDataHolder implements Wrapper {
    private final MaterialHolder type;
    private final Map<String, Object> data;
    @Nullable
    private final BlockHolder parent;

    public void addData(Map<String, Object> data) {
        this.data.putAll(data);
    }

    public void addData(String key, Object value) {
        data.put(key, value);
    }

    public boolean remove(String key) {
        return data.remove(key) != null;
    }

    public void clearData() {
        data.clear();
    }

    @Override
    public <T> T as(Class<T> type) {
        return BlockDataMapper.convert(this, type);
    }
}
