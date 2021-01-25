package org.screamingsandals.lib.world;

import lombok.Data;
import org.screamingsandals.lib.material.MaterialHolder;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.Map;

@Data
public class BlockDataHolder implements Wrapper {
    private final MaterialHolder type;
    private final Map<String, Object> data;
    private final BlockHolder parent;

    public BlockDataHolder(MaterialHolder type, Map<String, Object> data, BlockHolder parent) {
        this.type = type;
        this.data = data;
        this.parent = parent;
    }

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
        return BlockDataMapping.convert(this, type);
    }
}
