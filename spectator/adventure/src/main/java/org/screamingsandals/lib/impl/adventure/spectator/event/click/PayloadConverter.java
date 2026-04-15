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

package org.screamingsandals.lib.impl.adventure.spectator.event.click;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureBackend;
import org.screamingsandals.lib.impl.adventure.spectator.AdventureFeature;
import org.screamingsandals.lib.impl.adventure.spectator.compat.v4.PayloadConverterCompat;
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
        if (AdventureFeature.CLICK_EVENT_ACTION_NOT_ENUM.isSupported()) {
            return PayloadConverter5.withPayload(action, payload);
        } else {
            return PayloadConverterCompat.withPayload(action, payload, AdventureBackend.getSnbtSerializer());
        }
    }

    public static @NotNull ClickEvent withAction(ClickEvent.@NotNull Action action, @NotNull ClickEvent clickEvent) {
        if (AdventureFeature.CLICK_EVENT_ACTION_NOT_ENUM.isSupported()) {
            return PayloadConverter5.withAction(action, clickEvent);
        } else {
            return PayloadConverterCompat.withAction(action, clickEvent);
        }
    }
}
