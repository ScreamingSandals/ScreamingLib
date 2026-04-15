/*
 * Copyright 2026 ScreamingSandals
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

package org.screamingsandals.lib.impl.adventure.spectator.compat.v4;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.api.Wrapper;
import org.screamingsandals.lib.nbt.SNBTSerializer;
import org.screamingsandals.lib.spectator.event.click.Payload;

@UtilityClass
public class PayloadConverterCompat {
    public static @NotNull ClickEvent withPayload(ClickEvent.@NotNull Action action, @NotNull Payload payload, @NotNull SNBTSerializer snbtSerializer) {
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
                if (payload instanceof Payload.ShowDialog) {
                    return ClickEvent.showDialog(((Payload.ShowDialog) payload).dialog().as(DialogLike.class));
                }
                break;
            case CUSTOM:
                if (payload instanceof Payload.Custom) {
                    if (payload instanceof Wrapper) {
                        var advPayload = ((Wrapper) payload).asNullable(ClickEvent.Payload.Custom.class);
                        if (advPayload != null) {
                            return ClickEvent.custom(advPayload.key(), advPayload.nbt());
                        }
                    }

                    var location = ((Payload.Custom) payload).location();
                    var tag = ((Payload.Custom) payload).tag();
                    return ClickEvent.custom(
                            Key.key(location.namespace(), location.path()),
                            BinaryTagHolder.binaryTagHolder(snbtSerializer.serialize(tag))
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
