package org.screamingsandals.lib.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AbstractService {
    /**
     * Contains pattern which will be used to resolve PlatformMapping
     * This pattern consists of three named groups. These groups will be used in replace rule.
     * <p>
     * Default naming:
     * <ul>
     *   <li>basePackage</li>
     *   <li>subPackage</li>
     *   <li>className</li>
     * </ul>
     *
     * @return regex pattern
     */
    String pattern() default "^(?<basePackage>.+)\\.(?<subPackage>[^\\.]+)\\.(?<className>.+)$";

    /**
     * Contains replace rule which will be used to resolve PlatformMapping
     * <p>
     * Default naming:
     * <ul>
     *   <li>basePackage</li>
     *   <li>subPackage</li>
     *   <li>className</li>
     *   <li>platform - lowered platform name (eg. bukkit)</li>
     *   <li>Platform - platform name with first letter capital (eg. Bukkit)</li>
     * </ul>
     *
     * @return replace rule
     */
    String replaceRule() default "{basePackage}.{platform}.{subPackage}.{Platform}{className}";
}
