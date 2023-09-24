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

package org.screamingsandals.lib.impl.bukkit;

import io.netty.channel.ChannelFuture;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.impl.bukkit.entity.BukkitPlayer;
import org.screamingsandals.lib.impl.bukkit.player.GenericCommandSender;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.impl.bukkit.utils.Version;
import org.screamingsandals.lib.impl.nms.accessors.*;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.player.Sender;
import org.screamingsandals.lib.utils.ProxyType;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BukkitServer extends Server {
    private static final @NotNull Map<@NotNull String, String> UNSAFE_SOUND_CACHE = new HashMap<>();

    {
        try {
            UNSAFE_SOUND_CACHE.clear();

            if (BukkitFeature.SOUND_KEYED.isSupported()) {
                for (var v : Sound.values()) {
                    if ("minecraft".equals(v.getKey().getNamespace())) {
                        UNSAFE_SOUND_CACHE.put(v.name(), v.getKey().getKey());
                    }
                }
            } else {
                boolean is1_9 = Version.isVersion(1, 9);
                for (var v : Sound.values()) {
                    var craftSound = Reflect.getMethod(ClassStorage.CB.CraftSound, "getSound", Sound.class).invokeStatic(v);
                    UNSAFE_SOUND_CACHE.put(v.name(), craftSound.toString());
                    if (!is1_9) {
                        // 1.8.8: Bukkit sound keys are different from vanilla keys, let's map them as well (not to mention that some sounds are completly missing, TODO: we need to manually declare some sounds, which were new in 1.8)
                        UNSAFE_SOUND_CACHE.put(craftSound.toString().replace('.', '_').toUpperCase(Locale.ROOT), craftSound.toString());
                    }
                }
            }

            if (!Version.isVersion(1, 13)) {
                map1_8_to_1_9();
                map1_12_to_1_13();
            }

            // 1.14 <> 1.15
            fallbackSound("entity.parrot.imitate.enderman", "entity.enderman.ambient");
            fallbackSound("entity.parrot.imitate.panda", "entity.panda.ambient");
            fallbackSound("entity.parrot.imitate.polar_bear", "entity.polar_bear.ambient");
            fallbackSound("entity.parrot.imitate.wolf", "entity.wolf.ambient");
            fallbackSound("entity.parrot.imitate.zombie_pigman", "entity.zombie_pigman.ambient");

            // 1.15 <> 1.16 Keys
            mapSound("entity.zombie_pigman.ambient", "entity.zombified_piglin.ambient");
            mapSound("entity.zombie_pigman.angry", "entity.zombified_piglin.angry");
            mapSound("entity.zombie_pigman.death", "entity.zombified_piglin.death");
            mapSound("entity.zombie_pigman.hurt", "entity.zombified_piglin.hurt");
            mapSound("music.nether", "music.nether.nether_wastes");

            // 1.16 <> 1.17 Keys
            mapSound("item.sweet_berries.pick_from_bush", "block.sweet_berry_bush.pick_berries");

            // 1.19 <> 1.20 Keys
            mapSound("music.overworld.jungle_and_forest", "music.overworld.jungle");

            if (Version.isVersion(1, 13)) {
                map1_12_to_1_13();
                map1_8_to_1_9();
            }
        } catch (Throwable ignored) {
        }
    }

    private void map1_8_to_1_9() {

        // 1.8 <> 1.9 Keys
        mapSound("mob.horse.zombie.idle", "entity.zombie_horse.ambient");
        mapSound("note.snare", "block.note.snare");
        mapSound("random.wood_click", "block.wood_button.click_on");
        mapSound("dig.gravel", "block.gravel.place");
        mapSound("random.bowhit", "block.tripwire.detach");
        mapSound("dig.glass", "block.glass.break");
        mapSound("mob.zombie.say", "entity.zombie.ambient");
        mapSound("mob.pig.death", "entity.pig.death");
        mapSound("mob.horse.donkey.hit", "entity.donkey.hurt");
        mapSound("game.neutral.swim", "entity.player.swim");
        mapSound("game.player.swim", "entity.player.swim");
        mapSound("mob.endermen.idle", "entity.endermen.ambient");
        mapSound("portal.portal", "block.portal.ambient");
        mapSound("random.fizz", "entity.generic.extinguish_fire");
        mapSound("note.harp", "block.note.harp");
        mapSound("step.snow", "block.snow.step");
        mapSound("random.successful_hit", "entity.arrow.hit_player");
        mapSound("mob.zombiepig.zpighurt", "entity.zombie_pig.hurt");
        mapSound("mob.wolf.howl", "entity.wolf.howl");
        mapSound("fireworks.launch", "entity.firework.launch");
        mapSound("mob.cow.hurt", "entity.cow.death");
        mapSound("fireworks.largeBlast", "entity.firework.large_blast");
        mapSound("mob.blaze.hit", "entity.blaze.hurt");
        mapSound("mob.villager.death", "entity.villager.death");
        mapSound("mob.blaze.death", "entity.blaze.death");
        mapSound("mob.horse.zombie.death", "entity.zombie_horse.death");
        mapSound("mob.silverfish.kill", "entity.endermite.death");
        mapSound("mob.wolf.panting", "entity.wolf.pant");
        mapSound("note.bass", "block.note.bass");
        mapSound("dig.stone", "block.glass.place");
        mapSound("mob.endermen.stare", "entity.endermen.stare");
        mapSound("game.player.swim.splash", "entity.generic.splash");
        mapSound("mob.slime.small", "block.slime.hit");
        mapSound("mob.ghast.death", "entity.ghast.death");
        mapSound("mob.guardian.attack", "entity.guardian.attack");
        mapSound("random.click", "block.wood_pressureplate.click_on");
        mapSound("mob.zombiepig.zpig", "entity.zombie_pig.ambient");
        mapSound("game.player.die", "entity.player.death");
        mapSound("fireworks.twinkle_far", "entity.firework.twinkle_far");
        mapSound("mob.guardian.land.idle", "entity.guardian.ambient_land");
        mapSound("dig.grass", "block.grass.place");
        mapSound("mob.skeleton.step", "entity.skeleton.step");
        mapSound("mob.wither.death", "entity.wither.death");
        mapSound("mob.wolf.hurt", "entity.wolf.hurt");
        mapSound("mob.horse.leather", "entity.horse.saddle");
        mapSound("mob.bat.loop", "entity.bat.loop");
        mapSound("mob.ghast.scream", "entity.ghast.hurt");
        mapSound("game.player.hurt", "entity.player.death");
        mapSound("game.neutral.die", "entity.player.death");
        mapSound("mob.creeper.death", "entity.creeper.death");
        mapSound("mob.horse.gallop", "entity.horse.gallop");
        mapSound("mob.wither.spawn", "entity.wither.spawn");
        mapSound("mob.endermen.hit", "entity.endermen.hurt");
        mapSound("mob.creeper.say", "entity.creeper.hurt");
        mapSound("mob.horse.wood", "entity.horse.step_wood");
        mapSound("mob.zombie.unfect", "entity.zombie_villager.converted");
        mapSound("random.anvil_use", "block.anvil.use");
        mapSound("random.chestclosed", "block.chest.close");
        mapSound("mob.sheep.shear", "entity.sheep.shear");
        mapSound("random.pop", "entity.item.pickup");
        mapSound("mob.bat.death", "entity.bat.death");
        mapSound("dig.wood", "block.ladder.break");
        mapSound("mob.horse.donkey.death", "entity.donkey.death");
        mapSound("fireworks.blast", "entity.firework.blast");
        mapSound("mob.zombiepig.zpigangry", "entity.zombie_pig.angry");
        mapSound("game.hostile.swim", "entity.player.swim");
        mapSound("mob.guardian.flop", "entity.guardian.flop");
        mapSound("mob.villager.yes", "entity.villager.yes");
        mapSound("mob.ghast.charge", "entity.ghast.warn");
        mapSound("creeper.primed", "entity.creeper.primed");
        mapSound("dig.sand", "block.sand.break");
        mapSound("mob.chicken.say", "entity.chicken.ambient");
        mapSound("random.door_close", "block.wooden_door.close");
        mapSound("mob.guardian.elder.death", "entity.elder_guardian.death");
        mapSound("fireworks.twinkle", "entity.firework.twinkle");
        mapSound("mob.horse.skeleton.death", "entity.skeleton_horse.death");
        mapSound("ambient.weather.rain", "weather.rain.above");
        mapSound("portal.trigger", "block.portal.trigger");
        mapSound("random.chestopen", "block.chest.open");
        mapSound("mob.horse.land", "entity.horse.land");
        mapSound("mob.silverfish.step", "entity.silverfish.step");
        mapSound("mob.bat.takeoff", "entity.bat.takeoff");
        mapSound("mob.villager.no", "entity.villager.no");
        mapSound("game.hostile.hurt.fall.big", "entity.hostile.big_fall");
        mapSound("mob.irongolem.walk", "entity.irongolem.step");
        mapSound("note.hat", "block.note.hat");
        mapSound("mob.zombie.metal", "entity.zombie.attack_iron_door");
        mapSound("mob.villager.haggle", "entity.villager.trading");
        mapSound("mob.ghast.fireball", "entity.blaze.shoot");
        mapSound("mob.irongolem.death", "entity.irongolem.death");
        mapSound("random.break", "item.shield.break");
        mapSound("mob.zombie.remedy", "entity.zombie_villager.cure");
        mapSound("random.bow", "entity.splash_potion.throw");
        mapSound("mob.villager.idle", "entity.villager.ambient");
        mapSound("step.cloth", "block.cloth.fall");
        mapSound("mob.silverfish.hit", "entity.endermite.hurt");
        mapSound("liquid.lava", "block.lava.ambient");
        mapSound("game.neutral.hurt.fall.big", "entity.hostile.big_fall");
        mapSound("fire.fire", "block.fire.ambient");
        mapSound("mob.zombie.wood", "entity.zombie.attack_door_wood");
        mapSound("mob.chicken.step", "entity.chicken.step");
        mapSound("mob.guardian.land.hit", "entity.guardian.hurt_land");
        mapSound("mob.chicken.plop", "entity.donkey.chest");
        mapSound("mob.enderdragon.wings", "entity.enderdragon.flap");
        mapSound("step.grass", "block.grass.hit");
        mapSound("mob.horse.breathe", "entity.horse.breathe");
        mapSound("game.player.hurt.fall.big", "entity.hostile.big_fall");
        mapSound("mob.horse.donkey.idle", "entity.donkey.ambient");
        mapSound("mob.spider.step", "entity.spider.step");
        mapSound("game.neutral.hurt", "entity.player.death");
        mapSound("mob.cow.say", "entity.cow.ambient");
        mapSound("mob.horse.jump", "entity.horse.jump");
        mapSound("mob.horse.soft", "entity.horse.step");
        mapSound("game.neutral.swim.splash", "entity.generic.splash");
        mapSound("mob.guardian.hit", "entity.guardian.hurt");
        mapSound("mob.enderdragon.end", "entity.enderdragon.death");
        mapSound("mob.zombie.step", "entity.zombie.step");
        mapSound("mob.enderdragon.growl", "entity.enderdragon.growl");
        mapSound("mob.wolf.shake", "entity.wolf.shake");
        mapSound("mob.endermen.death", "entity.endermen.death");
        mapSound("random.anvil_land", "block.anvil.land");
        mapSound("game.hostile.hurt", "entity.player.death");
        mapSound("minecart.inside", "entity.minecart.inside");
        mapSound("mob.slime.big", "entity.slime.death");
        mapSound("liquid.water", "block.water.ambient");
        mapSound("mob.pig.say", "entity.pig.ambient");
        mapSound("mob.wither.shoot", "entity.wither.shoot");
        mapSound("item.fireCharge.use", "entity.blaze.shoot");
        mapSound("step.sand", "block.sand.fall");
        mapSound("mob.irongolem.hit", "entity.irongolem.hurt");
        mapSound("mob.horse.death", "entity.horse.death");
        mapSound("mob.bat.hurt", "entity.bat.hurt");
        mapSound("mob.ghast.affectionate_scream", "entity.ghast.scream");
        mapSound("mob.guardian.elder.idle", "entity.elder_guardian.ambient");
        mapSound("mob.zombiepig.zpigdeath", "entity.zombie_pig.death");
        mapSound("ambient.weather.thunder", "entity.lightning.thunder");
        mapSound("minecart.base", "entity.minecart.riding");
        mapSound("step.ladder", "block.ladder.hit");
        mapSound("mob.horse.donkey.angry", "entity.donkey.angry");
        mapSound("ambient.cave.cave", "ambient.cave");
        mapSound("fireworks.blast_far", "entity.firework.blast_far");
        mapSound("game.neutral.hurt.fall.small", "entity.generic.small_fall");
        mapSound("game.hostile.swim.splash", "entity.generic.splash");
        mapSound("random.drink", "entity.generic.drink");
        mapSound("game.hostile.die", "entity.player.death");
        mapSound("mob.cat.hiss", "entity.cat.hiss");
        mapSound("note.bd", "block.note.basedrum");
        mapSound("mob.spider.say", "entity.spider.hurt");
        mapSound("step.stone", "block.anvil.hit");
        mapSound("random.levelup", "entity.player.levelup");
        mapSound("liquid.lavapop", "block.lava.pop");
        mapSound("mob.sheep.say", "entity.sheep.ambient");
        mapSound("mob.skeleton.say", "entity.skeleton.ambient");
        mapSound("mob.blaze.breathe", "entity.blaze.ambient");
        mapSound("mob.bat.idle", "entity.bat.ambient");
        mapSound("mob.magmacube.big", "entity.magmacube.squish");
        mapSound("mob.horse.idle", "entity.horse.ambient");
        mapSound("game.hostile.hurt.fall.small", "entity.generic.small_fall");
        mapSound("mob.horse.zombie.hit", "entity.zombie_horse.hurt");
        mapSound("mob.irongolem.throw", "entity.irongolem.attack");
        mapSound("dig.cloth", "block.cloth.place");
        mapSound("step.gravel", "block.gravel.hit");
        mapSound("mob.silverfish.say", "entity.silverfish.ambient");
        mapSound("mob.cat.purr", "entity.cat.purr");
        mapSound("mob.zombie.infect", "entity.zombie.infect");
        mapSound("random.eat", "entity.generic.eat");
        mapSound("mob.wolf.bark", "entity.wolf.ambient");
        mapSound("game.tnt.primed", "entity.creeper.primed");
        mapSound("mob.sheep.step", "entity.sheep.step");
        mapSound("mob.zombie.death", "entity.zombie.death");
        mapSound("random.door_open", "block.wooden_door.open");
        mapSound("mob.endermen.portal", "entity.endermen.teleport");
        mapSound("mob.horse.angry", "entity.horse.angry");
        mapSound("mob.wolf.growl", "entity.wolf.growl");
        mapSound("dig.snow", "block.snow.place");
        mapSound("tile.piston.out", "block.piston.extend");
        mapSound("random.burp", "entity.player.burp");
        mapSound("mob.cow.step", "entity.cow.step");
        mapSound("mob.wither.hurt", "entity.wither.hurt");
        mapSound("mob.guardian.land.death", "entity.elder_guardian.death_land");
        mapSound("mob.chicken.hurt", "entity.chicken.death");
        mapSound("mob.wolf.step", "entity.wolf.step");
        mapSound("mob.wolf.death", "entity.wolf.death");
        mapSound("mob.wolf.whine", "entity.wolf.whine");
        mapSound("note.pling", "block.note.pling");
        mapSound("game.player.hurt.fall.small", "entity.generic.small_fall");
        mapSound("mob.cat.purreow", "entity.cat.purreow");
        mapSound("fireworks.largeBlast_far", "entity.firework.large_blast_far");
        mapSound("mob.skeleton.hurt", "entity.skeleton.hurt");
        mapSound("mob.spider.death", "entity.spider.death");
        mapSound("random.anvil_break", "block.anvil.destroy");
        mapSound("mob.wither.idle", "entity.wither.ambient");
        mapSound("mob.guardian.elder.hit", "entity.elder_guardian.hurt");
        mapSound("mob.endermen.scream", "entity.endermen.scream");
        mapSound("mob.cat.hitt", "entity.cat.hurt");
        mapSound("mob.magmacube.small", "entity.small_magmacube.squish");
        mapSound("fire.ignite", "item.flintandsteel.use");
        mapSound("mob.enderdragon.hit", "entity.enderdragon.hurt");
        mapSound("mob.zombie.hurt", "entity.zombie.hurt");
        mapSound("random.explode", "block.end_gateway.spawn");
        mapSound("mob.slime.attack", "entity.slime.attack");
        mapSound("mob.magmacube.jump", "entity.magmacube.jump");
        mapSound("random.splash", "entity.bobber.splash");
        mapSound("mob.horse.skeleton.hit", "entity.skeleton_horse.hurt");
        mapSound("mob.ghast.moan", "entity.ghast.ambient");
        mapSound("mob.guardian.curse", "entity.elder_guardian.curse");
        mapSound("game.potion.smash", "block.glass.break");
        mapSound("note.bassattack", "block.note.bass");
        mapSound("gui.button.press", "block.wood_pressureplate.click_on");
        mapSound("random.orb", "entity.experience_orb.pickup");
        mapSound("mob.zombie.woodbreak", "entity.zombie.break_door_wood");
        mapSound("mob.horse.armor", "entity.horse.armor");
        mapSound("tile.piston.in", "block.piston.contract");
        mapSound("mob.cat.meow", "entity.cat.ambient");
        mapSound("mob.pig.step", "entity.pig.step");
        mapSound("step.wood", "block.wood.step");
        mapSound("portal.travel", "block.portal.travel");
        mapSound("mob.guardian.death", "entity.guardian.death");
        mapSound("mob.skeleton.death", "entity.skeleton.death");
        mapSound("mob.horse.hit", "entity.horse.hurt");
        mapSound("mob.villager.hit", "entity.villager.hurt");
        mapSound("mob.horse.skeleton.idle", "entity.skeleton_horse.ambient");
        mapSound("records.chirp", "record.chirp");
        mapSound("mob.rabbit.hurt", "entity.rabbit.hurt");
        mapSound("records.stal", "record.stal");
        mapSound("music.game.nether", "music.nether");
        mapSound("records.mellohi", "record.mellohi");
        mapSound("records.cat", "record.cat");
        mapSound("records.far", "record.far");
        mapSound("music.game.end.dragon", "music.dragon");
        mapSound("mob.rabbit.death", "entity.rabbit.death");
        mapSound("mob.rabbit.idle", "entity.rabbit.ambient");
        mapSound("music.game.end", "music.end");
        mapSound("mob.guardian.idle", "entity.elder_guardian.ambient");
        mapSound("records.ward", "record.ward");
        mapSound("records.13", "record.13");
        mapSound("mob.rabbit.hop", "entity.rabbit.jump");
        mapSound("records.strad", "record.strad");
        mapSound("records.11", "record.11");
        mapSound("records.mall", "record.mall");
        mapSound("records.blocks", "record.blocks");
        mapSound("records.wait", "record.wait");
        mapSound("music.game.end.credits", "music.credits");
        mapSound("music.game.creative", "music.creative");
    }

    private void map1_12_to_1_13() {
        // 1.12 <> 1.13 keys
        mapSound("block.cloth.break", "block.wool.break");
        mapSound("block.cloth.fall", "block.wool.fall");
        mapSound("block.cloth.hit", "block.wool.hit");
        mapSound("block.cloth.place", "block.wool.place");
        mapSound("block.cloth.step", "block.wool.step");
        mapSound("block.enderchest.close", "block.ender_chest.close");
        mapSound("block.enderchest.open", "block.ender_chest.open");
        mapSound("block.metal_pressureplate.click_off", "block.metal_pressure_plate.click_off");
        mapSound("block.metal_pressureplate.click_on", "block.metal_pressure_plate.click_on");
        mapSound("block.note.basedrum", "block.note_block.basedrum");
        mapSound("block.note.bass", "block.note_block.bass");
        mapSound("block.note.bell", "block.note_block.bell");
        mapSound("block.note.chime", "block.note_block.chime");
        mapSound("block.note.flute", "block.note_block.flute");
        mapSound("block.note.guitar", "block.note_block.guitar");
        mapSound("block.note.harp", "block.note_block.harp");
        mapSound("block.note.hat", "block.note_block.hat");
        mapSound("block.note.pling", "block.note_block.pling");
        mapSound("block.note.snare", "block.note_block.snare");
        mapSound("block.note.xylophone", "block.note_block.xylophone");
        mapSound("block.slime.break", "block.slime_block.break");
        mapSound("block.slime.fall", "block.slime_block.fall");
        mapSound("block.slime.hit", "block.slime_block.hit");
        mapSound("block.slime.place", "block.slime_block.place");
        mapSound("block.slime.step", "block.slime_block.step");
        mapSound("block.stone_pressureplate.click_off", "block.stone_pressure_plate.click_off");
        mapSound("block.stone_pressureplate.click_on", "block.stone_pressure_plate.click_on");
        mapSound("block.waterlily.place", "block.lily_pad.place");
        mapSound("block.wood_pressureplate.click_off", "block.wooden_pressure_plate.click_off");
        mapSound("block.wood_button.click_on", "block.wooden_button.click_on");
        mapSound("block.wood_button.click_off", "block.wooden_button.click_off");
        mapSound("block.wood_pressureplate.click_on", "block.wooden_pressure_plate.click_on");
        mapSound("entity.armorstand.break", "entity.armor_stand.break");
        mapSound("entity.armorstand.fall", "entity.armor_stand.fall");
        mapSound("entity.armorstand.hit", "entity.armor_stand.hit");
        mapSound("entity.armorstand.place", "entity.armor_stand.place");
        mapSound("entity.bobber.retrieve", "entity.fishing_bobber.retrieve");
        mapSound("entity.bobber.splash", "entity.fishing_bobber.splash");
        mapSound("entity.bobber.throw", "entity.fishing_bobber.throw");
        mapSound("entity.enderdragon.ambient", "entity.ender_dragon.ambient");
        mapSound("entity.enderdragon.death", "entity.ender_dragon.death");
        mapSound("entity.enderdragon.flap", "entity.ender_dragon.flap");
        mapSound("entity.enderdragon.growl", "entity.ender_dragon.growl");
        mapSound("entity.enderdragon.hurt", "entity.ender_dragon.hurt");
        mapSound("entity.enderdragon.shoot", "entity.ender_dragon.shoot");
        mapSound("entity.enderdragon_fireball.explode", "entity.dragon_fireball.explode");
        mapSound("entity.endereye.death", "entity.ender_eye.death");
        mapSound("entity.endereye.launch", "entity.ender_eye.launch");
        mapSound("entity.endermen.ambient", "entity.enderman.ambient");
        mapSound("entity.endermen.death", "entity.enderman.death");
        mapSound("entity.endermen.hurt", "entity.enderman.hurt");
        mapSound("entity.endermen.scream", "entity.enderman.scream");
        mapSound("entity.endermen.stare", "entity.enderman.stare");
        mapSound("entity.endermen.teleport", "entity.enderman.teleport");
        mapSound("entity.enderpearl.throw", "entity.ender_pearl.throw");
        mapSound("entity.evocation_fangs.attack", "entity.evoker_fangs.attack");
        mapSound("entity.evocation_illager.ambient", "entity.evoker.ambient");
        mapSound("entity.evocation_illager.cast_spell", "entity.evoker.cast_spell");
        mapSound("entity.evocation_illager.death", "entity.evoker.death");
        mapSound("entity.evocation_illager.hurt", "entity.evoker.hurt");
        mapSound("entity.evocation_illager.prepare_attack", "entity.evoker.prepare_attack");
        mapSound("entity.evocation_illager.prepare_summon", "entity.evoker.prepare_summon");
        mapSound("entity.evocation_illager.prepare_wololo", "entity.evoker.prepare_wololo");
        mapSound("entity.firework.blast", "entity.firework_rocket.blast");
        mapSound("entity.firework.blast_far", "entity.firework_rocket.blast_far");
        mapSound("entity.firework.large_blast", "entity.firework_rocket.large_blast");
        mapSound("entity.firework.large_blast_far", "entity.firework_rocket.large_blast_far");
        mapSound("entity.firework.launch", "entity.firework_rocket.launch");
        mapSound("entity.firework.shoot", "entity.firework_rocket.shoot");
        mapSound("entity.firework.twinkle", "entity.firework_rocket.twinkle");
        mapSound("entity.firework.twinkle_far", "entity.firework_rocket.twinkle_far");
        mapSound("entity.illusion_illager.ambient", "entity.illusioner.ambient");
        mapSound("entity.illusion_illager.cast_spell", "entity.illusioner.cast_spell");
        mapSound("entity.illusion_illager.death", "entity.illusioner.death");
        mapSound("entity.illusion_illager.hurt", "entity.illusioner.hurt");
        mapSound("entity.illusion_illager.mirror_move", "entity.illusioner.mirror_move");
        mapSound("entity.illusion_illager.prepare_blindness", "entity.illusioner.prepare_blindness");
        mapSound("entity.illusion_illager.prepare_mirror", "entity.illusioner.prepare_mirror");
        mapSound("entity.irongolem.attack", "entity.iron_golem.attack");
        mapSound("entity.irongolem.death", "entity.iron_golem.death");
        mapSound("entity.irongolem.hurt", "entity.iron_golem.hurt");
        mapSound("entity.irongolem.step", "entity.iron_golem.step");
        mapSound("entity.itemframe.add_item", "entity.item_frame.add_item");
        mapSound("entity.itemframe.break", "entity.item_frame.break");
        mapSound("entity.itemframe.place", "entity.item_frame.place");
        mapSound("entity.itemframe.remove_item", "entity.item_frame.remove_item");
        mapSound("entity.itemframe.rotate_item", "entity.item_frame.rotate_item");
        mapSound("entity.leashknot.break", "entity.leash_knot.break");
        mapSound("entity.leashknot.place", "entity.leash_knot.place");
        mapSound("entity.lightning.impact", "entity.lightning_bolt.impact");
        mapSound("entity.lightning.thunder", "entity.lightning_bolt.thunder");
        mapSound("entity.lingeringpotion.throw", "entity.lingering_potion.throw");
        mapSound("entity.magmacube.death", "entity.magma_cube.death");
        mapSound("entity.magmacube.hurt", "entity.magma_cube.hurt");
        mapSound("entity.magmacube.jump", "entity.magma_cube.jump");
        mapSound("entity.magmacube.squish", "entity.magma_cube.squish");
        mapSound("entity.parrot.imitate.enderdragon", "entity.parrot.imitate.ender_dragon");
        mapSound("entity.parrot.imitate.evocation_illager", "entity.parrot.imitate.evoker");
        mapSound("entity.parrot.imitate.illusion_illager", "entity.parrot.imitate.illusioner");
        mapSound("entity.parrot.imitate.magmacube", "entity.parrot.imitate.magma_cube");
        mapSound("entity.parrot.imitate.vindication_illager", "entity.parrot.imitate.vindicator");
        mapSound("entity.player.splash.highspeed", "entity.player.splash.high_speed");
        mapSound("entity.polar_bear.baby_ambient", "entity.polar_bear.ambient_baby");
        mapSound("entity.small_magmacube.death", "entity.magma_cube.death_small");
        mapSound("entity.small_magmacube.hurt", "entity.magma_cube.hurt_small");
        mapSound("entity.small_magmacube.squish", "entity.magma_cube.squish_small");
        mapSound("entity.small_slime.death", "entity.slime.death_small");
        mapSound("entity.small_slime.hurt", "entity.slime.hurt_small");
        mapSound("entity.small_slime.jump", "entity.slime.jump_small");
        mapSound("entity.small_slime.squish", "entity.slime.squish_small");
        mapSound("entity.snowman.ambient", "entity.snow_golem.ambient");
        mapSound("entity.snowman.death", "entity.snow_golem.death");
        mapSound("entity.snowman.hurt", "entity.snow_golem.hurt");
        mapSound("entity.snowman.shoot", "entity.snow_golem.shoot");
        mapSound("entity.vindication_illager.ambient", "entity.vindicator.ambient");
        mapSound("entity.vindication_illager.death", "entity.vindicator.death");
        mapSound("entity.vindication_illager.hurt", "entity.vindicator.hurt");
        mapSound("entity.zombie.attack_door_wood", "entity.zombie.attack_wooden_door");
        mapSound("entity.zombie.break_door_wood", "entity.zombie.break_wooden_door");
        mapSound("entity.zombie_pig.ambient", "entity.zombie_pigman.ambient");
        mapSound("entity.zombie_pig.angry", "entity.zombie_pigman.angry");
        mapSound("entity.zombie_pig.death", "entity.zombie_pigman.death");
        mapSound("entity.zombie_pig.hurt", "entity.zombie_pigman.hurt");
        mapSound("record.11", "music_disc.11");
        mapSound("record.13", "music_disc.13");
        mapSound("record.blocks", "music_disc.blocks");
        mapSound("record.cat", "music_disc.cat");
        mapSound("record.chirp", "music_disc.chirp");
        mapSound("record.far", "music_disc.far");
        mapSound("record.mall", "music_disc.mall");
        mapSound("record.mellohi", "music_disc.mellohi");
        mapSound("record.stal", "music_disc.stal");
        mapSound("record.strad", "music_disc.strad");
        mapSound("record.wait", "music_disc.wait");
        mapSound("record.ward", "music_disc.ward");
    }

    private void mapSound(@NotNull String keyOne, @NotNull String keyTwo) {
        // Bukkit-ify the sound keys
        var bukkitKeyOne = keyOne.replace('.', '_').toUpperCase(Locale.ROOT);
        var bukkitKeyTwo = keyTwo.replace('.', '_').toUpperCase(Locale.ROOT);

        if (UNSAFE_SOUND_CACHE.containsKey(bukkitKeyOne) && !UNSAFE_SOUND_CACHE.containsKey(bukkitKeyTwo)) {
            UNSAFE_SOUND_CACHE.put(bukkitKeyTwo, UNSAFE_SOUND_CACHE.get(bukkitKeyOne));
        } else if (UNSAFE_SOUND_CACHE.containsKey(bukkitKeyTwo) && !UNSAFE_SOUND_CACHE.containsKey(bukkitKeyTwo)) {
            UNSAFE_SOUND_CACHE.put(bukkitKeyTwo, UNSAFE_SOUND_CACHE.get(bukkitKeyTwo));
        }
    }

    private void fallbackSound(@NotNull String keyOne, @NotNull String fallbackKey) {
        // Bukkit-ify the sound keys
        var bukkitKeyOne = keyOne.replace('.', '_').toUpperCase(Locale.ROOT);
        var bukkitFallbackKey = fallbackKey.replace('.', '_').toUpperCase(Locale.ROOT);

        if (UNSAFE_SOUND_CACHE.containsKey(bukkitFallbackKey) && !UNSAFE_SOUND_CACHE.containsKey(bukkitKeyOne)) {
            UNSAFE_SOUND_CACHE.put(bukkitKeyOne, UNSAFE_SOUND_CACHE.get(bukkitFallbackKey));
        }
    }

    @Override
    public @NotNull String getVersion0() {
        if (Version.PATCH_VERSION == 0) {
            return Version.MAJOR_VERSION + "." + Version.MINOR_VERSION;
        }
        return Version.MAJOR_VERSION + "." + Version.MINOR_VERSION + "." + Version.PATCH_VERSION;
    }

    @Override
    public @NotNull String getServerSoftwareVersion0() {
        return Bukkit.getVersion();
    }

    @Override
    public boolean isVersion0(int major, int minor) {
        return Version.isVersion(major, minor);
    }

    @Override
    public boolean isVersion0(int major, int minor, int patch) {
        return Version.isVersion(major, minor, patch);
    }

    @Override
    public boolean isServerThread0() {
        return Bukkit.getServer().isPrimaryThread();
    }

    @Override
    public @NotNull List<@NotNull Player> getConnectedPlayers0() {
        return Bukkit.getOnlinePlayers()
                .stream()
                .map(BukkitPlayer::new)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull List<@NotNull Player> getConnectedPlayersFromWorld0(@NotNull World holder) {
        return holder.as(org.bukkit.World.class).getPlayers()
                .stream()
                .map(BukkitPlayer::new)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<@NotNull ChannelFuture> getConnections0() {
        return (List<ChannelFuture>) Reflect.fastInvokeResulted(Bukkit.getServer(), "getServer")
                .getFieldResulted(MinecraftServerAccessor.getFieldConnection())
                .getFieldResulted(ServerConnectionListenerAccessor.getFieldChannels())
                .raw();
    }

    @Override
    public @NotNull Sender getConsoleSender0() {
        return new GenericCommandSender(Bukkit.getConsoleSender());
    }

    @Override
    public void shutdown0() {
        Bukkit.shutdown();
    }

    @Override
    public @NotNull ProxyType getProxyType0() {
        // Bukkit#spigot() exists in 1.9.4, verified with https://helpch.at/docs/1.9.4/org/bukkit/Bukkit.html#spigot()
        // Server.Spigot#getPaperConfig() exists in 1.9.4, verified with https://github.com/PaperMC/Paper/blob/ver/1.9.4/Spigot-Server-Patches/0005-Timings-v2.patch#L995
        // Server.Spigot#getConfig() exists in 1.9.4 and resolves to org.spigotmc.SpigotConfig#config in 1.9.4 and latest
        // verified with https://github.com/PaperMC/Paper/blob/ver/1.9.4/Spigot-Server-Patches/0005-Timings-v2.patch#L991
        // and https://github.com/PaperMC/Paper/blob/master/patches/server/0010-Timings-v2.patch#L1727
        if (BukkitFeature.HAS_PAPER_CONFIG.isSupported()) {
            if (Bukkit.spigot().getPaperConfig().getBoolean("settings.velocity-support.enabled", false)) {
                return ProxyType.VELOCITY;
            }
        }
        return Bukkit.spigot().getConfig().getBoolean("settings.bungeecord",false) ? ProxyType.BUNGEE : ProxyType.NONE;
    }

    @Override
    public @NotNull Integer getProtocolVersion0() {
        if (BukkitFeature.UNSAFE_VALUES_PROTOCOL_VERSION.isSupported()) {
            return Bukkit.getUnsafe().getProtocolVersion();
        }

        if (SharedConstantsAccessor.getMethodGetProtocolVersion1() != null) {
            return Reflect.fastInvokeResulted(SharedConstantsAccessor.getMethodGetProtocolVersion1()).as(Integer.class);
        }

        return Reflect.getFieldResulted(Reflect.fastInvoke(Bukkit.getServer(), "getServer"), MinecraftServerAccessor.getFieldStatus())
                .fastInvokeResulted(ServerStatusAccessor.getMethodGetVersion1())
                .fastInvokeResulted(ServerStatus_i_VersionAccessor.getMethodProtocol1())
                .as(Integer.class);
    }

    public static @NotNull String UNSAFE_normalizeSoundKey0(@NotNull String s) {
        return UNSAFE_SOUND_CACHE.getOrDefault(s.replace('.', '_').toUpperCase(Locale.ROOT), s).toLowerCase(Locale.ROOT);
    }
}
