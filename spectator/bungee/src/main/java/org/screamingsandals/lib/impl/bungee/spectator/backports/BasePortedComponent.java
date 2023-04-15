/*
 * Copyright 2023 ScreamingSandals
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

package org.screamingsandals.lib.impl.bungee.spectator.backports;

import com.google.gson.JsonObject;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.reflect.Reflect;

@ApiStatus.Internal
public abstract class BasePortedComponent extends BaseComponent {
    private static final boolean HAS_COPY_FORMATTING = Reflect.hasMethod(BaseComponent.class, "copyFormatting", BaseComponent.class);
    private static final boolean HAS_INSERTION = Reflect.hasMethod(BaseComponent.class, "setInsertion", String.class);

    public BasePortedComponent() {
    }

    public BasePortedComponent(@NotNull BasePortedComponent old) {
        copyFrom(old);
    }

    public void copyFrom(@NotNull BaseComponent old) {
        if (HAS_COPY_FORMATTING) {
            copyFormatting(old);
        } else {
            // every supported style before copyFormatting has been added:
            setColor(old.getColorRaw());
            setBold(old.isBoldRaw());
            setItalic(old.isItalicRaw());
            setUnderlined(old.isUnderlinedRaw());
            setStrikethrough(old.isStrikethroughRaw());
            setObfuscated(old.isObfuscatedRaw());
            if (HAS_INSERTION) {
                setInsertion(old.getInsertion());
            }
            setClickEvent(old.getClickEvent());
            setHoverEvent(old.getHoverEvent());
        }

        // check if the copyFormatting method copied extras or not on this version (I hate you md_5)
        if (old.getExtra() != null && (this.getExtra() == null || this.getExtra().size() != old.getExtra().size())) {
            for (var extra : old.getExtra()) {
                addExtra(extra.duplicate());
            }
        }
    }

    @Override
    public abstract @NotNull BasePortedComponent duplicate();

    public abstract void write(@NotNull JsonObject out);
}
