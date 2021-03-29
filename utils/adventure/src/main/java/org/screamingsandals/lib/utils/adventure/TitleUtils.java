package org.screamingsandals.lib.utils.adventure;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.title.Title;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.time.Duration;

@UtilityClass
public class TitleUtils {
    public final Class<?> NATIVE_TITLE_CLASS
            = Reflect.getClassSafe("net.kyori.adventure.title.Title");
    public final Class<?> NATIVE_TIMES_CLASS
            = Reflect.getClassSafe("net.kyori.adventure.title.Title$Times");

    public Object timesToPlatform(Title.Times times) {
        if (NATIVE_TIMES_CLASS.isInstance(times)) {
            return times;
        }
        return timesToPlatform(times, NATIVE_TIMES_CLASS);
    }

    public Object timesToPlatform(Title.Times times, Class<?> timesClass) {
        return Reflect
                .getMethod(timesClass, "of", Duration.class, Duration.class, Duration.class)
                .invokeStatic(times.fadeIn(),  times.stay(), times.fadeOut());
    }

    public Title.Times timesFromPlatform(Object platformObject) {
        if (platformObject instanceof Title.Times) {
            return (Title.Times) platformObject;
        }
        return Title.Times.of(
                Reflect.fastInvokeResulted(platformObject, "fadeIn").as(Duration.class),
                Reflect.fastInvokeResulted(platformObject, "stay").as(Duration.class),
                Reflect.fastInvokeResulted(platformObject, "fadeOut").as(Duration.class)
        );
    }

    public Object titleToPlatform(Title title) {
        if (NATIVE_TITLE_CLASS.isInstance(title)) {
            return title;
        }
        return titleToPlatform(title, NATIVE_TITLE_CLASS, ComponentUtils.NATIVE_COMPONENT_CLASS, ComponentUtils.NATIVE_GSON_COMPONENT_SERIALIZER_GETTER.invokeStatic(), NATIVE_TIMES_CLASS);
    }

    public Object titleToPlatform(Title title, Class<?> titleClass, Class<?> componentClass, Object componentSerializer, Class<?> timesClass) {
        return Reflect
                .getMethod(titleClass, "title", componentClass, componentClass, timesClass)
                .invokeStatic(
                        ComponentUtils.componentToPlatform(title.title(), componentSerializer),
                        ComponentUtils.componentToPlatform(title.subtitle(), componentSerializer),
                        title.times() != null ? timesToPlatform(title.times(), timesClass) : null
                );
    }

    public Title titleFromPlatform(Object platformObject) {
        if (platformObject instanceof Title) {
            return (Title) platformObject;
        }
        return Title.title(
                ComponentUtils.componentFromPlatform(Reflect.fastInvoke(platformObject, "title")),
                ComponentUtils.componentFromPlatform(Reflect.fastInvoke(platformObject, "subtitle")),
                timesFromPlatform(Reflect.fastInvoke(platformObject, "times"))
        );
    }

}
