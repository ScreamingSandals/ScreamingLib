package org.screamingsandals.lib.world;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.screamingsandals.lib.utils.Wrapper;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class BlockDataHolder implements Wrapper {
    private final Map<String, Object> data = new HashMap<>();
    private BlockHolder parent;

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
