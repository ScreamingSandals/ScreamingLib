package org.screamingsandals.lib.utils.annotations;

import org.jetbrains.annotations.ApiStatus;
import org.screamingsandals.lib.utils.PlatformType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target({
        ElementType.TYPE,
        ElementType.PACKAGE
})
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

    /**
     * Contains all packages which should be initialized. These packages are annotated using {@link Init} annotation which contains another packages or services.
     *
     * @return All packages which should be initialized
     */
    String[] packages() default {};

    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.TYPE)
    @ApiStatus.Internal
    @interface List {
        Init[] value();
    }
}
