package org.screamingsandals.lib.utils;

import com.google.gson.Gson;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GsonUtils {
    private final static Gson instance = new Gson();

    public Gson gson() {
        return instance;
    }
}
