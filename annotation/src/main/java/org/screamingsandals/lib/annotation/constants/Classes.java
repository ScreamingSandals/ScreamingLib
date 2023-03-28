package org.screamingsandals.lib.annotation.constants;

import com.squareup.javapoet.ClassName;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class Classes {
    public static final @NotNull ClassName SLIB_PLUGINS = ClassName.get("org.screamingsandals.lib.plugin", "Plugins");
    public static final @NotNull ClassName SLIB_PLUGIN = ClassName.get("org.screamingsandals.lib.plugin", "Plugin");
    public static final @NotNull ClassName SLIB_PLUGIN_CONTAINER = ClassName.get("org.screamingsandals.lib.plugin", "PluginContainer");

    public static final @NotNull ClassName SLIB_CONTROLLABLE = ClassName.get("org.screamingsandals.lib.utils", "Controllable");
    public static final @NotNull ClassName SLIB_CONTROLLABLE_IMPL = ClassName.get("org.screamingsandals.lib.utils", "ControllableImpl");

    public static final @NotNull ClassName SLIB_CORE = ClassName.get("org.screamingsandals.lib", "Core");
    public static final @NotNull ClassName SLIB_PROXY_CORE = ClassName.get("org.screamingsandals.lib.proxy", "ProxyCore");

    public static final @NotNull ClassName SLIB_LOGGER_WRAPPER = ClassName.get("org.screamingsandals.lib.utils.logger", "LoggerWrapper");
    public static final @NotNull ClassName SLIB_JUL_LOGGER_WRAPPER = ClassName.get("org.screamingsandals.lib.utils.logger", "JULLoggerWrapper");
    public static final @NotNull ClassName SLIB_SLF4J_LOGGER_WRAPPER = ClassName.get("org.screamingsandals.lib.utils.logger", "Slf4jLoggerWrapper");
    public static final @NotNull ClassName SLIB_DUAL_LOGGER_WRAPPER = ClassName.get("org.screamingsandals.lib.utils.logger", "DualLoggerWrapper");

    public static final @NotNull ClassName SLIB_REFLECT = ClassName.get("org.screamingsandals.lib.utils.reflect", "Reflect");

    // TODO: add more classes
}
