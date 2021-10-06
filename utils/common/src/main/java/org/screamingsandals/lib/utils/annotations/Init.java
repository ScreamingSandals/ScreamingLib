package org.screamingsandals.lib.utils.annotations;

import org.screamingsandals.lib.utils.PlatformType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Repeatable(Init.List.class)
public @interface Init {
    /**
     * Contains all platforms which need these services. If empty, it means all platforms
     *
     * @return All platforms which need these services
     */
    PlatformType[] platforms() default {};

    /**
     * Contains all services which should be initialized
     *
     * @return All services which should be initialized
     */
    Class<?>[] services();

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    @interface List {
        Init[] value();
    }
}
