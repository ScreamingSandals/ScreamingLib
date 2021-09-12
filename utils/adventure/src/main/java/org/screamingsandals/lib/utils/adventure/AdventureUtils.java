package org.screamingsandals.lib.utils.adventure;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.screamingsandals.lib.utils.reflect.InstanceMethod;
import org.screamingsandals.lib.utils.reflect.Reflect;

@UtilityClass
public class AdventureUtils {
    public final Class<?> NATIVE_MESSAGE_TYPE_CLASS
            = Reflect.getClassSafe(String.join(".", "net", "kyori", "adventure", "audience", "MessageType"));

    public InstanceMethod get(Object instance, String method, Class<?>... types) {
        if (ComponentUtils.NATIVE_COMPONENT_CLASS == null) {
            // okay, no adventure, fall to your fallback method please
            return new InstanceMethod(null, null);
        }

        if (ComponentUtils.NATIVE_COMPONENT_CLASS.isAssignableFrom(Component.class)) {
            // Ok, we are not using shaded adventure
            return Reflect.getMethod(instance, method, types);
        }

        // TODO: Pointers
        var classes = new Class<?>[types.length];
        for (var i = 0; i < classes.length; i++) {
            if (Component.class.isAssignableFrom(types[i])) {
                types[i] = ComponentUtils.NATIVE_COMPONENT_CLASS;
            } else if (Title.Times.class.isAssignableFrom(types[i])) {
                types[i] = TitleUtils.NATIVE_TIMES_CLASS;
            } else if (Title.class.isAssignableFrom(types[i])) {
                types[i] = TitleUtils.NATIVE_TITLE_CLASS;
            } else if (TitlePart.class.isAssignableFrom(types[i])) {
                types[i] = TitleUtils.NATIVE_TITLE_PART_CLASS;
            } else if (Book.class.isAssignableFrom(types[i])) {
                types[i] = BookUtils.NATIVE_BOOK_CLASS;
            } else if (Identity.class.isAssignableFrom(types[i])) {
                types[i] = IdentityUtils.NATIVE_IDENTITY_CLASS;
            } else if (MessageType.class.isAssignableFrom(types[i])) {
                types[i] = NATIVE_MESSAGE_TYPE_CLASS;
            } else if (Key.class.isAssignableFrom(types[i])) {
                types[i] = KeyUtils.NATIVE_KEY_CLASS;
            } else if (Sound.class.isAssignableFrom(types[i])) {
                types[i] = SoundUtils.NATIVE_SOUND_CLASS;
            } else if (Sound.Source.class.isAssignableFrom(types[i])) {
                types[i] = SoundUtils.NATIVE_SOURCE_CLASS;
            } else if (SoundStop.class.isAssignableFrom(types[i])) {
                types[i] = SoundUtils.NATIVE_SOUND_STOP_CLASS;
            } else if (BossBar.class.isAssignableFrom(types[i])) {
                types[i] = BossBarUtils.NATIVE_BOSSBAR_CLASS;
            } else if (BossBar.Color.class.isAssignableFrom(types[i])) {
                types[i] = BossBarUtils.NATIVE_BOSSBAR_COLOR_CLASS;
            } else if (BossBar.Flag.class.isAssignableFrom(types[i])) {
                types[i] = BossBarUtils.NATIVE_BOSSBAR_FLAG_CLASS;
            } else if (BossBar.Overlay.class.isAssignableFrom(types[i])) {
                types[i] = BossBarUtils.NATIVE_BOSSBAR_OVERLAY_CLASS;
            }
            classes[i] = types[i];
        }

        return Reflect
                .getMethod(instance, method, classes)
                .withTransformers((parameterTypes, parameters) -> {
                    var result = new Object[parameters.length];
                    for (var i = 0; i < result.length; i++) {
                        if (parameters[i] instanceof Component) {
                            result[i] = ComponentUtils.componentToPlatform((Component) parameters[i]);
                        } else if (TitleUtils.NATIVE_TIMES_CLASS != null && parameters[i] instanceof Title.Times) {
                            result[i] = TitleUtils.timesToPlatform((Title.Times) parameters[i]);
                        } else if (TitleUtils.NATIVE_TITLE_CLASS != null && parameters[i] instanceof Title) {
                            result[i] = TitleUtils.titleToPlatform((Title) parameters[i]);
                        } else if (TitleUtils.NATIVE_TITLE_PART_CLASS != null && parameters[i] instanceof TitlePart) {
                            result[i] = TitleUtils.titlePartToPlatform((TitlePart<?>) parameters[i]);
                        } else if (BookUtils.NATIVE_BOOK_CLASS != null && parameters[i] instanceof Book) {
                            result[i] = BookUtils.bookToPlatform((Book) parameters[i]);
                        } else if (IdentityUtils.NATIVE_IDENTITY_CLASS != null && parameters[i] instanceof Identity) {
                            result[i] = IdentityUtils.identityToPlatform((Identity) parameters[i]);
                        } else if (NATIVE_MESSAGE_TYPE_CLASS != null && parameters[i] instanceof MessageType) {
                            result[i] = Reflect.findEnumConstant(NATIVE_MESSAGE_TYPE_CLASS, ((MessageType) parameters[i]).name());
                        } else if (KeyUtils.NATIVE_KEY_CLASS != null && parameters[i] instanceof Key) {
                            result[i] = KeyUtils.keyToPlatform((Key) parameters[i]);
                        } else if (SoundUtils.NATIVE_SOUND_CLASS != null && parameters[i] instanceof Sound) {
                            result[i] = SoundUtils.soundToPlatform((Sound) parameters[i]);
                        } else if (SoundUtils.NATIVE_SOURCE_CLASS != null && parameters[i] instanceof Sound.Source) {
                            result[i] = SoundUtils.sourceToPlatform((Sound.Source) parameters[i]);
                        } else if (SoundUtils.NATIVE_SOUND_STOP_CLASS != null && parameters[i] instanceof SoundStop) {
                            result[i] = SoundUtils.stopSoundToPlatform((SoundStop) parameters[i]);
                        } else if (BossBarUtils.NATIVE_BOSSBAR_CLASS != null && parameters[i] instanceof BossBar) {
                            result[i] = BossBarUtils.bossBarToPlatform((BossBar) parameters[i]);
                        } else if (BossBarUtils.NATIVE_BOSSBAR_COLOR_CLASS != null && parameters[i] instanceof BossBar.Color) {
                            result[i] = BossBarUtils.colorToPlatform((BossBar.Color) parameters[i]);
                        } else if (BossBarUtils.NATIVE_BOSSBAR_FLAG_CLASS != null && parameters[i] instanceof BossBar.Flag) {
                            result[i] = BossBarUtils.flagToPlatform((BossBar.Flag) parameters[i]);
                        } else if (BossBarUtils.NATIVE_BOSSBAR_OVERLAY_CLASS != null && parameters[i] instanceof BossBar.Overlay) {
                            result[i] = BossBarUtils.overlayToPlatform((BossBar.Overlay) parameters[i]);
                        } else {
                            result[i] = parameters[i];
                        }
                    }
                    return result;
                }, o -> {
                    if (ComponentUtils.NATIVE_COMPONENT_CLASS.isInstance(o)) {
                        return ComponentUtils.componentFromPlatform(o);
                    } else if (TitleUtils.NATIVE_TIMES_CLASS != null && TitleUtils.NATIVE_TIMES_CLASS.isInstance(o)) {
                        return TitleUtils.timesFromPlatform(o);
                    } else if (TitleUtils.NATIVE_TITLE_CLASS != null && TitleUtils.NATIVE_TITLE_CLASS.isInstance(o)) {
                        return TitleUtils.titleFromPlatform(o);
                    } else if (TitleUtils.NATIVE_TITLE_PART_CLASS != null && TitleUtils.NATIVE_TITLE_PART_CLASS.isInstance(o)) {
                        return TitleUtils.titlePartFromPlatform(o);
                    } else if (BookUtils.NATIVE_BOOK_CLASS != null && BookUtils.NATIVE_BOOK_CLASS.isInstance(o)) {
                        return BookUtils.bookFromPlatform(o);
                    } else if (IdentityUtils.NATIVE_IDENTITY_CLASS != null && IdentityUtils.NATIVE_IDENTITY_CLASS.isInstance(o)) {
                        return IdentityUtils.identityFromPlatform(o);
                    } else if (NATIVE_MESSAGE_TYPE_CLASS != null && NATIVE_MESSAGE_TYPE_CLASS.isInstance(o)) {
                        return MessageType.valueOf(Reflect.fastInvokeResulted(o, "name").as(String.class));
                    } else if (KeyUtils.NATIVE_KEY_CLASS != null && KeyUtils.NATIVE_KEY_CLASS.isInstance(o)) {
                        return KeyUtils.keyFromPlatform(o);
                    } else if (SoundUtils.NATIVE_SOUND_CLASS != null && SoundUtils.NATIVE_SOUND_CLASS.isInstance(o)) {
                        return SoundUtils.soundFromPlatform(o);
                    } else if (SoundUtils.NATIVE_SOURCE_CLASS != null && SoundUtils.NATIVE_SOURCE_CLASS.isInstance(o)) {
                        return SoundUtils.sourceFromPlatform(o);
                    } else if (SoundUtils.NATIVE_SOUND_STOP_CLASS != null && SoundUtils.NATIVE_SOUND_STOP_CLASS.isInstance(o)) {
                        return SoundUtils.stopSoundFromPlatform(o);
                    } else if (BossBarUtils.NATIVE_BOSSBAR_CLASS != null && BossBarUtils.NATIVE_BOSSBAR_CLASS.isInstance(o)) {
                        return BossBarUtils.bossBarFromPlatform(o);
                    } else if (BossBarUtils.NATIVE_BOSSBAR_COLOR_CLASS != null && BossBarUtils.NATIVE_BOSSBAR_COLOR_CLASS.isInstance(o)) {
                        return BossBarUtils.colorFromPlatform(o);
                    } else if (BossBarUtils.NATIVE_BOSSBAR_FLAG_CLASS != null && BossBarUtils.NATIVE_BOSSBAR_FLAG_CLASS.isInstance(o)) {
                        return BossBarUtils.flagFromPlatform(o);
                    } else if (BossBarUtils.NATIVE_BOSSBAR_OVERLAY_CLASS != null && BossBarUtils.NATIVE_BOSSBAR_OVERLAY_CLASS.isInstance(o)) {
                        return BossBarUtils.overlayFromPlatform(o);
                    }
                    return o;
                });
    }
}
