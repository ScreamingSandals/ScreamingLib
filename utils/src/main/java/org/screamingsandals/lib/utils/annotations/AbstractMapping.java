package org.screamingsandals.lib.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AbstractMapping {
    /**
     * Contains pattern which will be used to resolve PlatformMapping
     * This pattern consists of three named groups. These groups will be used in replace rule.
     * Default naming:
     * - basePackage
     * - subPackage
     * - className
     *
     * @return regex pattern
     */
    String pattern() default "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+)\\.(?<className>.+)$";

    /**
     * Contains replace rule which will be used to resolve PlatformMapping
     * Default naming:
     * - basePackage
     * - subPackage
     * - className
     * - platform - lowered platform name (eg. bukkit)
     * - Platform - platform name with first letter capital (eg. Bukkit)
     *
     * @return replace rule
     */
    String replaceRule() default "{basePackage}.{platform}.{subPackage}.{Platform}{className}";
}
