/*
 * Copyright 2025 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.impl.adventure.spectator.event.click;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.spectator.event.click.Payload;

@UtilityClass
public class PayloadConverter {
    public static @NotNull Payload convertPayload(@NotNull ClickEvent clickEvent) {
        var payload = clickEvent.payload();
        if (payload instanceof ClickEvent.Payload.Text) {
            return Payload.text(((ClickEvent.Payload.Text) payload).value());
        } else if (payload instanceof ClickEvent.Payload.Int) {
            return Payload.integer(((ClickEvent.Payload.Int) payload).integer());
        } else if (payload instanceof ClickEvent.Payload.Custom) {
            return new AdventurePayloadCustom((ClickEvent.Payload.Custom) payload);
        } else if (payload instanceof ClickEvent.Payload.Dialog) {
            return new AdventurePayloadShowDialog((ClickEvent.Payload.Dialog) payload);
        } else {
            throw new UnsupportedOperationException("Unknown payload of class " + payload.getClass());
        }
    }

    public static @NotNull ClickEvent withPayload(ClickEvent.@NotNull Action action, @NotNull Payload payload) {
        switch (action) {
            case OPEN_URL:
                if (payload instanceof Payload.Text) {
                    return ClickEvent.openUrl(((Payload.Text) payload).text());
                }
                break;
            case OPEN_FILE:
                if (payload instanceof Payload.Text) {
                    return ClickEvent.openFile(((Payload.Text) payload).text());
                }
                break;
            case RUN_COMMAND:
                if (payload instanceof Payload.Text) {
                    return ClickEvent.runCommand(((Payload.Text) payload).text());
                }
                break;
            case SUGGEST_COMMAND:
                if (payload instanceof Payload.Text) {
                    return ClickEvent.suggestCommand(((Payload.Text) payload).text());
                }
                break;
            case CHANGE_PAGE:
                if (payload instanceof Payload.Text) {
                    return ClickEvent.changePage(Integer.parseInt(((Payload.Text) payload).text()));
                } else if (payload instanceof Payload.Int) {
                    return ClickEvent.changePage(((Payload.Int) payload).number());
                }
                break;
            case COPY_TO_CLIPBOARD:
                if (payload instanceof Payload.Text) {
                    return ClickEvent.copyToClipboard(((Payload.Text) payload).text());
                }
                break;
            case SHOW_DIALOG:
                if (payload instanceof AdventurePayloadShowDialog) {
                    return ClickEvent.showDialog(((AdventurePayloadShowDialog) payload).as(ClickEvent.Payload.Dialog.class).dialog());
                } else if (payload instanceof Payload.ShowDialog) {
                    return ClickEvent.showDialog(((Payload.ShowDialog) payload).dialog().as(DialogLike.class));
                }
                break;
            case CUSTOM:
                if (payload instanceof AdventurePayloadCustom) {
                    var advPayload = ((AdventurePayloadCustom) payload).as(ClickEvent.Payload.Custom.class);
                    return ClickEvent.custom(advPayload.key(), advPayload.nbt());
                } else if (payload instanceof Payload.Custom) {
                    var location = ((Payload.Custom) payload).location();
                    var tag = ((Payload.Custom) payload).tag();
                    return ClickEvent.custom(
                            Key.key(location.namespace(), location.path()),
                            BinaryTagHolder.binaryTagHolder(AdventureBackend.getSnbtSerializer().serialize(tag))
                    );
                }
                break;
        }
        throw new IllegalArgumentException("Cannot use payload type " + payload.getClass() + " for action " + action.name());
    }

    public static @NotNull ClickEvent withAction(ClickEvent.@NotNull Action action, @NotNull ClickEvent clickEvent) {
        var payload = clickEvent.payload();
        switch (action) {
            case OPEN_URL:
                if (payload instanceof ClickEvent.Payload.Text) {
                    return ClickEvent.openUrl(((ClickEvent.Payload.Text) payload).value());
                }
                break;
            case OPEN_FILE:
                if (payload instanceof ClickEvent.Payload.Text) {
                    return ClickEvent.openFile(((ClickEvent.Payload.Text) payload).value());
                }
                break;
            case RUN_COMMAND:
                if (payload instanceof ClickEvent.Payload.Text) {
                    return ClickEvent.runCommand(((ClickEvent.Payload.Text) payload).value());
                }
                break;
            case SUGGEST_COMMAND:
                if (payload instanceof ClickEvent.Payload.Text) {
                    return ClickEvent.suggestCommand(((ClickEvent.Payload.Text) payload).value());
                }
                break;
            case CHANGE_PAGE:
                if (payload instanceof ClickEvent.Payload.Text) {
                    return ClickEvent.changePage(Integer.parseInt(((ClickEvent.Payload.Text) payload).value()));
                } else if (payload instanceof ClickEvent.Payload.Int) {
                    return ClickEvent.changePage(((ClickEvent.Payload.Int) payload).integer());
                }
                break;
            case COPY_TO_CLIPBOARD:
                if (payload instanceof ClickEvent.Payload.Text) {
                    return ClickEvent.copyToClipboard(((ClickEvent.Payload.Text) payload).value());
                }
                break;
            case SHOW_DIALOG:
                if (payload instanceof ClickEvent.Payload.Dialog) {
                    return ClickEvent.showDialog(((ClickEvent.Payload.Dialog) payload).dialog());
                }
                break;
            case CUSTOM:
                if (payload instanceof ClickEvent.Payload.Custom) {
                    return ClickEvent.custom(((ClickEvent.Payload.Custom) payload).key(), ((ClickEvent.Payload.Custom) payload).nbt());
                }
                break;
        }
        throw new IllegalArgumentException("Cannot use action " + action.name() + " with payload type " + payload.getClass());
    }
}
