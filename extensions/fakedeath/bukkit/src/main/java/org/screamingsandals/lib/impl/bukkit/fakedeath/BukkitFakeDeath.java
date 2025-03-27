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

package org.screamingsandals.lib.impl.bukkit.fakedeath;

import org.bukkit.*;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.event.entity.EntityDamageEvent;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.impl.nms.accessors.network.chat.ComponentAccessor;
import org.screamingsandals.lib.impl.nms.accessors.server.level.ServerPlayerAccessor;
import org.screamingsandals.lib.impl.nms.accessors.world.damagesource.CombatTrackerAccessor;
import org.screamingsandals.lib.impl.nms.accessors.world.entity.ExperienceOrbAccessor;
import org.screamingsandals.lib.impl.nms.accessors.world.entity.LivingEntityAccessor;
import org.screamingsandals.lib.impl.nms.accessors.world.entity.player.PlayerAccessor;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.fakedeath.FakeDeath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

@Service
public class BukkitFakeDeath extends FakeDeath {
    @Override
    protected void die0(@NotNull org.screamingsandals.lib.player.Player slibPlayer, @Nullable EntityDamageEvent damageEvent, @NotNull FakeDeath.PlayerInventoryLifeResetFunction function) {
        var player = slibPlayer.as(Player.class);
        if (player.isDead()) {
            return;
        }

        var loot = new ArrayList<ItemStack>();
        Collections.addAll(loot, player.getInventory().getContents());
        loot.removeIf(Objects::isNull); // remove nulls;

        var deathWorld = player.getWorld();
        var deathLoc = player.getLocation();

        String message = null;
        try {
            var combatTracker = Reflect.fastInvoke(ClassStorage.getHandle(player), LivingEntityAccessor.METHOD_GET_COMBAT_TRACKER.get());
            var component = Reflect.fastInvoke(combatTracker, CombatTrackerAccessor.METHOD_GET_DEATH_MESSAGE.get());
            message = (String) Reflect.fastInvoke(component, ComponentAccessor.METHOD_GET_COLORED_STRING.get()); // TODO: fix death message obtaining
        } catch (Throwable ignored) {}

        PlayerDeathEvent event;
        try {
            // Spigot 1.20.6: DamageSource is now required in constructor
            DamageSource damageSource;
            if (damageEvent != null) {
                damageSource = damageEvent.as(org.bukkit.event.entity.EntityDamageEvent.class).getDamageSource();
            } else {
                damageSource = DamageSource.builder(DamageType.GENERIC_KILL).build();
            }

            event = new PlayerDeathEvent(player, damageSource, loot, player.getTotalExperience(), 0, message);
        } catch (Throwable ignored) {
            // 1.8.8-1.20.4
            event = Compat1_8.construct(player, loot, message);
        }
        Bukkit.getServer().getPluginManager().callEvent(event);

        for (var stack : event.getDrops()) {
            if (stack == null || stack.getType() == Material.AIR) continue;

            deathWorld.dropItem(deathLoc, stack);
        }

        player.closeInventory();

        var deathMessage = event.getDeathMessage();
        if (deathMessage != null && !deathMessage.trim().equals("") && Boolean.parseBoolean(deathWorld.getGameRuleValue("showDeathMessages"))) {
            Bukkit.broadcastMessage(deathMessage);
        }

        // TODO: find better way how to broadcast this effect and don't break the game

        /*
        try {
            Reflect.getMethod(ClassStorage.getHandle(deathWorld), "broadcastEntityEffect,func_72960_a", ClassStorage.NMS.Entity, byte.class)
                    .invoke(ClassStorage.getHandle(player), (byte) 3);
        } catch (Throwable t) {
        }
         */

        // ignoring PacketPlayOutCombatEvent, client probably didn't know that he died

        try {
            Reflect.fastInvoke(ClassStorage.getHandle(player), PlayerAccessor.METHOD_REMOVE_ENTITIES_ON_SHOULDER.get());
        } catch (Throwable ignored) {}

        if (Server.isVersion(1, 16)) {
            try {
                Boolean b = deathWorld.getGameRuleValue(GameRule.FORGIVE_DEAD_PLAYERS);
                if (b != null && b) {
                    Reflect.fastInvoke(ClassStorage.getHandle(player), ServerPlayerAccessor.METHOD_TELL_NEUTRAL_MOBS_THAT_IDIED.get());
                }
            } catch (Throwable ignored) {}
        }

        int i = event.getDroppedExp();
        while (i > 0) {
            int j = (int) Reflect.fastInvoke(ExperienceOrbAccessor.METHOD_GET_EXPERIENCE_VALUE.get(), i);
            i -= j;
            ((ExperienceOrb) deathWorld.spawnEntity(deathLoc, EntityType.EXPERIENCE_ORB)).setExperience(j);
        }

        function.reset(slibPlayer, event.getKeepInventory());
        try {
            player.setKiller(null);
            Reflect.getMethod(player, "setLastDamageCause", EntityDamageEvent.class).invoke((Object) null); // Not exposed to the API anymore
        } catch (Throwable ignored) {}

        if (event.getKeepLevel() || event.getKeepInventory()) {
            player.setTotalExperience(event.getNewTotalExp());
            player.setLevel(event.getNewLevel());
            player.setExp(event.getNewExp());
        }

        try {
            Reflect.fastInvoke(ClassStorage.getHandle(player), ServerPlayerAccessor.METHOD_SET_CAMERA.get(), ClassStorage.getHandle(player));
        } catch (Throwable ignored) {}

        try {
            Object combatTracker = Reflect.fastInvoke(ClassStorage.getHandle(player), LivingEntityAccessor.METHOD_GET_COMBAT_TRACKER.get());
            Reflect.fastInvoke(combatTracker, CombatTrackerAccessor.METHOD_RECHECK_STATUS.get());
        } catch (Throwable ignored) {}

        // respawn location will be changed by PlayerListener
        var respawnEvent = new PlayerRespawnEvent(player, player.getLocation(), false);
        Bukkit.getServer().getPluginManager().callEvent(respawnEvent);

        slibPlayer.teleport(Location.fromPlatform(respawnEvent.getRespawnLocation()));
    }
}
