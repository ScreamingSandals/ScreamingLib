/*
 * Copyright 2022 ScreamingSandals
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

package org.screamingsandals.lib.bukkit.event.player;

import lombok.*;
import net.kyori.adventure.text.Component;
import org.bukkit.event.block.SignChangeEvent;
import org.jetbrains.annotations.Range;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.entity.BukkitEntityPlayer;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.player.SPlayerUpdateSignEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.AdventureHelper;

import java.util.Arrays;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitPlayerUpdateSignEvent implements SPlayerUpdateSignEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final SignChangeEvent event;

    // Internal cache
    private PlayerWrapper player;
    private BlockHolder block;

    @Override
    public PlayerWrapper getPlayer() {
        if (player == null) {
            player = new BukkitEntityPlayer(event.getPlayer());
        }
        return player;
    }

    @Override
    public BlockHolder getBlock() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    // TODO: IMPLEMENT PLATFORM ADVENTURE
    @Override
    public Component[] lines() {
        return Arrays.stream(event.getLines()).map(AdventureHelper::toComponent).toArray(Component[]::new);
    }

    @Override
    public Component line(@Range(from = 0, to = 3) int index) {
        return lines()[index];
    }

    @Override
    public void line(@Range(from = 0, to = 3) int index, Component component) {
        event.setLine(index, AdventureHelper.toLegacy(component));
    }
}
