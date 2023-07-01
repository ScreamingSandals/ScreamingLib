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

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Sign;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.bukkit.utils.Version;
import org.screamingsandals.lib.impl.utils.feature.PlatformFeature;
import org.screamingsandals.lib.sender.CommandSender;
import org.screamingsandals.lib.utils.reflect.Reflect;

@UtilityClass
public class BukkitFeature {
    // Event classes
    // TODO: Check versions of these events, check whether the vanilla feature existed in older versions, check whether we can somehow backport the event, and properly annotate the api interfaces
    public static final @NotNull PlatformFeature AREA_CLOUD_APPLY_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.AreaCloudApplyEvent"));
    public static final @NotNull PlatformFeature ARROW_BODY_COUNT_CHANGE_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.ArrowBodyCountChangeEvent"));
    public static final @NotNull PlatformFeature BAT_TOGGLE_SLEEP_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.BatToggleSleepEvent"));
    public static final @NotNull PlatformFeature ENDER_DRAGON_CHANGE_PHASE_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.EnderDragonChangePhaseEvent"));
    public static final @NotNull PlatformFeature ENTITY_AIR_CHANGE_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.EntityAirChangeEvent"));
    public static final @NotNull PlatformFeature ENTITY_BREED_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.EntityBreedEvent"));
    public static final @NotNull PlatformFeature ENTITY_DROP_ITEM_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.EntityDropItemEvent"));
    public static final @NotNull PlatformFeature ENTITY_ENTER_BLOCK_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.EntityEnterBlockEvent"));
    public static final @NotNull PlatformFeature ENTITY_ENTER_LOVE_MODE_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.EntityEnterLoveModeEvent"));
    public static final @NotNull PlatformFeature ENTITY_EXHAUSTION_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.EntityExhaustionEvent"));
    public static final @NotNull PlatformFeature ENTITY_PICKUP_ITEM_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.EntityPickupItemEvent"));
    public static final @NotNull PlatformFeature ENTITY_PLACE_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.EntityPlaceEvent"));
    public static final @NotNull PlatformFeature ENTITY_POSE_CHANGE_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.EntityPoseChangeEvent"));
    public static final @NotNull PlatformFeature ENTITY_POTION_EFFECT_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.EntityPotionEffectEvent"));
    public static final @NotNull PlatformFeature ENTITY_RESURRECT_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.EntityResurrectEvent"));
    public static final @NotNull PlatformFeature EXP_BOTTLE_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.ExpBottleEvent"));
    public static final @NotNull PlatformFeature STRIDER_TEMPERATURE_CHANGE_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.StriderTemperatureChangeEvent"));
    public static final @NotNull PlatformFeature VILLAGER_ACQUIRE_TRADE_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.VillagerAcquireTradeEvent"));
    public static final @NotNull PlatformFeature VILLAGER_CAREER_CHANGE_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.VillagerCareerChangeEvent"));
    public static final @NotNull PlatformFeature VILLAGER_REPLENISH_TRADE_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.VillagerReplenishTradeEvent"));
    public static final @NotNull PlatformFeature ENTITY_TOGGLE_GLIDE_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.EntityToggleGlideEvent"));
    public static final @NotNull PlatformFeature ENTITY_TOGGLE_SWIM_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.entity.EntityToggleSwimEvent"));
    public static final @NotNull PlatformFeature PLAYER_ARMOR_STAND_MANIPULATE_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.player.PlayerArmorStandManipulateEvent"));
    public static final @NotNull PlatformFeature PLAYER_COMMAND_SEND_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.player.PlayerCommandSendEvent"));
    public static final @NotNull PlatformFeature PLAYER_HARVEST_BLOCK_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.player.PlayerHarvestBlockEvent"));
    public static final @NotNull PlatformFeature PLAYER_ITEM_MEND_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.player.PlayerItemMendEvent"));
    public static final @NotNull PlatformFeature PLAYER_LOCALE_CHANGE_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.player.PlayerLocaleChangeEvent"));
    public static final @NotNull PlatformFeature PLAYER_SWAP_HAND_ITEMS_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.player.PlayerSwapHandItemsEvent"));
    public static final @NotNull PlatformFeature BLOCK_COOK_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.block.BlockCookEvent"));
    public static final @NotNull PlatformFeature BLOCK_DROP_ITEM_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.block.BlockDropItemEvent"));
    public static final @NotNull PlatformFeature BLOCK_FERTILIZE_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.block.BlockFertilizeEvent"));
    public static final @NotNull PlatformFeature BLOCK_SHEAR_ENTITY_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.block.BlockShearEntityEvent"));
    public static final @NotNull PlatformFeature CAULDRON_LEVEL_CHANGE_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.block.CauldronLevelChangeEvent"));
    public static final @NotNull PlatformFeature FLUID_LEVEL_CHANGE_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.block.FluidLevelChangeEvent"));
    public static final @NotNull PlatformFeature MOISTURE_CHANGE_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.block.MoistureChangeEvent"));
    public static final @NotNull PlatformFeature SPONGE_ABSORB_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.block.SpongeAbsorbEvent"));
    public static final @NotNull PlatformFeature BLOCK_RECEIVE_GAME_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.block.BlockReceiveGameEvent"));
    public static final @NotNull PlatformFeature TIME_SKIP_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.event.world.TimeSkipEvent"));

    // Attribute API
    public static final @NotNull PlatformFeature ATTRIBUTES_API = PlatformFeature.of(() -> Version.isVersion(1, 9));
    public static final @NotNull PlatformFeature ATTRIBUTE_DEFAULT_VALUE = ATTRIBUTES_API.and(() -> Reflect.hasMethod(AttributeInstance.class, "getDefaultValue"));
    public static final @NotNull PlatformFeature ATTRIBUTE_TYPE_KEYED = PlatformFeature.of(() -> Version.isVersion(1, 16));
    public static final @NotNull PlatformFeature ATTRIBUTE_ARMOR_TOUGHNESS_VANILLA = PlatformFeature.of(() -> Version.isVersion(1, 9, 1));
    public static final @NotNull PlatformFeature ATTRIBUTE_ARMOR_TOUGHNESS = ATTRIBUTES_API.and(() -> Reflect.getField(Attribute.class, "GENERIC_ARMOR_TOUGHNESS") != null);

    // Block API
    public static final @NotNull PlatformFeature COMMAND_BLOCK_VERBOSE_BLOCK_STATE = PlatformFeature.of(() -> Version.isVersion(1, 9));
    public static final @NotNull PlatformFeature TILE_STATE = PlatformFeature.of(() -> Reflect.has("org.bukkit.block.TileState"));
    public static final @NotNull PlatformFeature SIGN_IS_WAXED = PlatformFeature.of(() -> Reflect.hasMethod(Sign.class, "isWaxed"));
    public static final @NotNull PlatformFeature SIGN_IS_GLOWING_TEXT = PlatformFeature.of(() -> Reflect.hasMethod(Sign.class, "isGlowingText"));
    public static final @NotNull PlatformFeature SIGN_BILATERAL = PlatformFeature.of(() -> Reflect.has("org.bukkit.block.sign.SignSide") && Version.isVersion(1, 20)); // md_5 decided to introduce the API before updating to a version which supports this

    // Material API (Block & Item)
    public static final @NotNull PlatformFeature FLATTENING = PlatformFeature.of(() -> Version.isVersion(1, 13));

    // Item API
    public static final @NotNull PlatformFeature ITEM_ATTRIBUTE_MODIFIERS_API = PlatformFeature.of(() -> Reflect.hasMethod(ItemMeta.class, "hasAttributeModifiers"));
    public static final @NotNull PlatformFeature ITEM_META_PDC = PlatformFeature.of(() -> Reflect.hasMethod(ItemMeta.class, "getPersistentDataContainer"));
    public static final @NotNull PlatformFeature ITEM_META_CUSTOM_TAG = PlatformFeature.of(() -> Reflect.hasMethod(ItemMeta.class, "getCustomTagContainer"));
    public static final @NotNull PlatformFeature ITEM_META_CUSTOM_MODEL_DATA = PlatformFeature.of(() -> Reflect.hasMethod(ItemMeta.class, "hasCustomModelData"));
    public static final @NotNull PlatformFeature ITEM_META_IS_UNBREAKABLE = PlatformFeature.of(() -> Reflect.hasMethod(ItemMeta.class, "isUnbreakable"));
    public static final @NotNull PlatformFeature KNOWLEDGE_BOOK_META = PlatformFeature.of(() -> Reflect.has("org.bukkit.inventory.meta.KnowledgeBookMeta"));
    public static final @NotNull PlatformFeature POTION_META_COLOR = PlatformFeature.of(() -> Reflect.hasMethod(PotionMeta.class, "setColor", Color.class));

    // Registry API
    public static final @NotNull PlatformFeature REGISTRY = PlatformFeature.of(() -> Version.isVersion(1, 14));
    public static final @NotNull PlatformFeature TAGS = PlatformFeature.of(() -> Version.isVersion(1, 13));

    // Entity API
    public static final @NotNull PlatformFeature REGISTRY_ENTITY_TYPES = TAGS.and(() -> Reflect.getField(Tag.class, "REGISTRY_ENTITY_TYPES") != null);
    public static final @NotNull PlatformFeature REGISTRY_ENTITIES = TAGS.and(() -> Reflect.getField(Tag.class, "REGISTRY_ENTITIES") != null);
    public static final @NotNull PlatformFeature ENTITY_KEYED = PlatformFeature.of(() -> Version.isVersion(1, 14));
    public static final @NotNull PlatformFeature NORMAL_ENTITY_RESOURCE_LOCATIONS = PlatformFeature.of(() -> Version.isVersion(1, 11));
    public static final @NotNull PlatformFeature ENTITIES_IN_REGISTRY = PlatformFeature.of(() -> Version.isVersion(1, 14));
    public static final @NotNull PlatformFeature HUSK = PlatformFeature.of(() -> Version.isVersion(1, 10));
    public static final @NotNull PlatformFeature STRAY = PlatformFeature.of(() -> Version.isVersion(1, 10));
    public static final @NotNull PlatformFeature MOB_INTERFACE = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Mob"));
    public static final @NotNull PlatformFeature ENDER_DRAGON_EXTEND_MOB_INTERFACE_IN_API = MOB_INTERFACE.and(() -> org.bukkit.entity.Mob.class.isAssignableFrom(org.bukkit.entity.EnderDragon.class));
    public static final @NotNull PlatformFeature ENTITY_ADD_PASSENGER = PlatformFeature.of(() -> Reflect.hasMethod(Entity.class, "addPassenger", Entity.class));
    public static final @NotNull PlatformFeature ENTITY_REMOVE_PASSENGER = PlatformFeature.of(() -> Reflect.hasMethod(Entity.class, "removePassenger", Entity.class));
    public static final @NotNull PlatformFeature ENTITY_IS_GLOWING = PlatformFeature.of(() -> Reflect.hasMethod(Entity.class, "isGlowing"));
    public static final @NotNull PlatformFeature ENTITY_IS_INVULNERABLE = PlatformFeature.of(() -> Reflect.hasMethod(Entity.class, "isInvulnerable"));
    public static final @NotNull PlatformFeature ENTITY_IS_SILENT = PlatformFeature.of(() -> Reflect.hasMethod(Entity.class, "isSilent"));
    public static final @NotNull PlatformFeature ENTITY_HAS_GRAVITY = PlatformFeature.of(() -> Reflect.hasMethod(Entity.class, "hasGravity"));
    public static final @NotNull PlatformFeature ENTITY_PORTAL_COOLDOWN = PlatformFeature.of(() -> Reflect.hasMethod(Entity.class, "getPortalCooldown"));
    public static final @NotNull PlatformFeature HUMAN_SATURATION = PlatformFeature.of(() -> Reflect.hasMethod(HumanEntity.class, "getSaturation"));
    public static final @NotNull PlatformFeature HUMAN_EXHAUSTION = PlatformFeature.of(() -> Reflect.hasMethod(HumanEntity.class, "getExhaustion"));
    public static final @NotNull PlatformFeature FOOD_LEVEL = PlatformFeature.of(() -> Reflect.hasMethod(HumanEntity.class, "getFoodLevel"));
    public static final @NotNull PlatformFeature ITEM_CAN_PLAYER_PICKUP = PlatformFeature.of(() -> Reflect.hasMethod(Item.class, "canPlayerPickup"));
    public static final @NotNull PlatformFeature ARROW_COOLDOWN = PlatformFeature.of(() -> Reflect.hasMethod(LivingEntity.class, "getArrowCooldown"));
    public static final @NotNull PlatformFeature ARROWS_IN_BODY = PlatformFeature.of(() -> Reflect.hasMethod(LivingEntity.class, "getArrowsInBody"));
    public static final @NotNull PlatformFeature GLIDING = PlatformFeature.of(() -> Reflect.hasMethod(LivingEntity.class, "isGliding"));
    public static final @NotNull PlatformFeature SWIMMING = PlatformFeature.of(() -> Reflect.hasMethod(LivingEntity.class, "isSwimming"));
    public static final @NotNull PlatformFeature RIPTIDING = PlatformFeature.of(() -> Reflect.hasMethod(LivingEntity.class, "isRiptiding"));
    public static final @NotNull PlatformFeature SLEEPING = PlatformFeature.of(() -> Reflect.hasMethod(LivingEntity.class, "isSleeping"));
    public static final @NotNull PlatformFeature SET_AI = PlatformFeature.of(() -> Reflect.hasMethod(LivingEntity.class, "setAI", boolean.class));
    public static final @NotNull PlatformFeature ATTACK = PlatformFeature.of(() -> Reflect.hasMethod(LivingEntity.class, "attack", Entity.class));
    public static final @NotNull PlatformFeature SWING_HAND = PlatformFeature.of(() -> Reflect.hasMethod(LivingEntity.class, "swingMainHand"));
    public static final @NotNull PlatformFeature COLLIDABLE = PlatformFeature.of(() -> Reflect.hasMethod(LivingEntity.class, "isCollidable"));
    public static final @NotNull PlatformFeature INVISIBLE = PlatformFeature.of(() -> Reflect.hasMethod(LivingEntity.class, "isInvisible"));
    public static final @NotNull PlatformFeature ABSORPTION_AMOUNT = PlatformFeature.of(() -> Reflect.hasMethod(Damageable.class, "getAbsorptionAmount"));
    public static final @NotNull PlatformFeature SLIME_TARGET = PlatformFeature.of(() -> Reflect.hasMethod(Slime.class, "getTarget"));
    public static final @NotNull PlatformFeature PLAYER_GET_LOCALE = PlatformFeature.of(() -> Reflect.hasMethod(Player.class, "getLocale"));
    public static final @NotNull PlatformFeature PLAYER_HIDE_API_PLUGIN_TICKET = PlatformFeature.of(() -> Reflect.hasMethod(Player.class, "hidePlayer", Plugin.class, Player.class));
    public static final @NotNull PlatformFeature ENTITY_PRE_SPAWN_FUNCTION = PlatformFeature.of(() -> Reflect.has("org.bukkit.util.Consumer")); // org.bukkit.util.Consumer added in the same commit as the overloaded method
    public static final @NotNull PlatformFeature ITEM_ENTITY_PRE_SPAWN_FUNCTION = ENTITY_PRE_SPAWN_FUNCTION.and(() -> Reflect.hasMethod(World.class, "dropItem", Location.class, ItemStack.class, Consumer.class));
    public static final @NotNull PlatformFeature NEW_VILLAGERS = PlatformFeature.of(() -> Reflect.hasMethod(Villager.Profession.class, "getKey"));
    public static final @NotNull PlatformFeature ENTITY_AREA_EFFECT_CLOUD = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.AreaEffectCloud"));
    public static final @NotNull PlatformFeature ENTITY_INTERACTION = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Interaction"));
    public static final @NotNull PlatformFeature ENTITY_DISPLAY = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Display"));
    public static final @NotNull PlatformFeature ENTITY_MARKER = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Marker"));
    public static final @NotNull PlatformFeature ENTITY_EVOKER_FANGS = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.EvokerFangs"));
    public static final @NotNull PlatformFeature ENTITY_CHEST_BOAT = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.ChestBoat"));
    public static final @NotNull PlatformFeature ENTITY_LLAMA_SPIT = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.LlamaSpit"));
    public static final @NotNull PlatformFeature ENTITY_SHULKER_BULLET = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.ShulkerBullet"));
    public static final @NotNull PlatformFeature ENTITY_ABSTRACT_ARROW = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.AbstractArrow"));
    public static final @NotNull PlatformFeature ENTITY_SPECTRAL_ARROW = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.SpectralArrow"));
    public static final @NotNull PlatformFeature ENTITY_TRIDENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Trident"));
    public static final @NotNull PlatformFeature ENTITY_TIPPED_ARROW = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.TippedArrow"));
    public static final @NotNull PlatformFeature ENTITY_SIZED_FIREBALL = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.SizedFireball"));
    public static final @NotNull PlatformFeature ENTITY_DRAGON_FIREBALL = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.DragonFireball"));
    public static final @NotNull PlatformFeature ENTITY_PHANTOM = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Phantom"));
    public static final @NotNull PlatformFeature ENTITY_FISH = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Fish"));
    public static final @NotNull PlatformFeature ENTITY_GLOW_SQUID = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.GlowSquid"));
    public static final @NotNull PlatformFeature ENTITY_TADPOLE = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Tadpole"));
    public static final @NotNull PlatformFeature ENTITY_CAT = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Cat"));
    public static final @NotNull PlatformFeature ENTITY_PARROT = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Parrot"));
    public static final @NotNull PlatformFeature ENTITY_POLAR_BEAR = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.PolarBear"));
    public static final @NotNull PlatformFeature ENTITY_TURTLE = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Turtle"));
    public static final @NotNull PlatformFeature ENTITY_PANDA = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Panda"));
    public static final @NotNull PlatformFeature ENTITY_FOX = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Fox"));
    public static final @NotNull PlatformFeature ENTITY_BEE = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Bee"));
    public static final @NotNull PlatformFeature ENTITY_HOGLIN = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Hoglin"));
    public static final @NotNull PlatformFeature ENTITY_STRIDER = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Strider"));
    public static final @NotNull PlatformFeature ENTITY_AXOLOTL = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Axolotl"));
    public static final @NotNull PlatformFeature ENTITY_GOAT = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Goat"));
    public static final @NotNull PlatformFeature ENTITY_FROG = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Frog"));
    public static final @NotNull PlatformFeature ENTITY_SNIFFER = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Sniffer"));
    public static final @NotNull PlatformFeature ENTITY_ABSTRACT_HORSE = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.AbstractHorse"));
    public static final @NotNull PlatformFeature ENTITY_TRADER_LLAMA = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.TraderLlama"));
    public static final @NotNull PlatformFeature ENTITY_CAMEL = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Camel"));
    public static final @NotNull PlatformFeature ENTITY_ALLAY = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Allay"));
    public static final @NotNull PlatformFeature ENTITY_WANDERING_TRADER = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.WanderingTrader"));
    public static final @NotNull PlatformFeature ENTITY_SHULKER = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Shulker"));
    public static final @NotNull PlatformFeature ENTITY_DROWNED = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Drowned"));
    public static final @NotNull PlatformFeature ENTITY_VEX = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Vex"));
    public static final @NotNull PlatformFeature ILLAGERS_1_11 = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Evoker"));
    public static final @NotNull PlatformFeature ILLAGERS_1_12 = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Illager"));
    public static final @NotNull PlatformFeature ENTITY_PILLAGER = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Pillager"));
    public static final @NotNull PlatformFeature ENTITY_RAVAGER = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Ravager"));
    public static final @NotNull PlatformFeature ENTITY_PIGLIN = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Piglin"));
    public static final @NotNull PlatformFeature ENTITY_PIGLIN_BRUTE = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.PiglinBrute"));
    public static final @NotNull PlatformFeature ENTITY_ZOGLIN = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Zoglin"));
    public static final @NotNull PlatformFeature ENTITY_WARDEN = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Warden"));

    // WORLD
    public static final @NotNull PlatformFeature WORLD_MIN_HEIGHT = PlatformFeature.of(() -> Reflect.hasMethod(World.class, "getMinHeight"));
    public static final @NotNull PlatformFeature GAME_RULE_API = PlatformFeature.of(() -> Reflect.has("org.bukkit.GameRule"));
    public static final @NotNull PlatformFeature PARTICLES_API = PlatformFeature.of(() -> Version.isVersion(1, 9));
    public static final @NotNull PlatformFeature EXTENDED_PARTICLE_METHOD = PARTICLES_API.and(() -> Reflect.hasMethod(World.class, "spawnParticle", Particle.class, Location.class, int.class, double.class, double.class, double.class, double.class, Object.class, boolean.class));

    // POTION & POTION EFFECT API
    public static final @NotNull PlatformFeature POTION_API = PlatformFeature.of(() -> Version.isVersion(1, 9));

    public static final @NotNull PlatformFeature POTION_EFFECT_TYPE_REGISTRY = PlatformFeature.of(() -> Version.isVersion(1, 18, 2) && Reflect.getField("org.bukkit.Registry", "POTION_EFFECT_TYPE") != null); // exclusive to paper
    public static final @NotNull PlatformFeature POTION_EFFECT_KEYED = PlatformFeature.of(() -> Version.isVersion(1, 18));
    public static final @NotNull PlatformFeature POTION_EFFECT_CONSTRUCTOR_WITH_ICON = PlatformFeature.of(() -> Reflect.constructor(PotionEffect.class, PotionEffectType.class, int.class, int.class, boolean.class, boolean.class, boolean.class).isPresent());

    // CHAT MESSAGE API
    public static final @NotNull PlatformFeature COMMAND_SENDER_SKELETON_API = PlatformFeature.of(() -> Reflect.hasMethod(CommandSender.class, "spigot"));
    public static final @NotNull PlatformFeature ADVENTURE = PlatformFeature.of(() -> Reflect.has("net.kyori.adventure.Adventure") && Reflect.has("io.papermc.paper.text.PaperComponents"));
    public static final @NotNull PlatformFeature NBT_LONG_ARRAYS = PlatformFeature.of(() -> Version.isVersion(1, 12));
    public static final @NotNull PlatformFeature MODERN_BOSSBARS = PlatformFeature.of(() -> Version.isVersion(1, 9));
    public static final @NotNull PlatformFeature SOUND_CATEGORY = PlatformFeature.of(() -> Reflect.has("org.bukkit.SoundCategory"));
    public static final @NotNull PlatformFeature PLAYER_SET_PLAYER_LIST_HEADER_FOOTER_COMPONENT = PlatformFeature.of(() -> Reflect.hasMethod(Player.class, "setPlayerListHeaderFooter", BaseComponent.class, BaseComponent.class));
    public static final @NotNull PlatformFeature PLAYER_SET_PLAYER_LIST_HEADER_FOOTER_TEXT = PlatformFeature.of(() -> Reflect.hasMethod(Player.class, "setPlayerListHeaderFooter", String.class, String.class));
    public static final @NotNull PlatformFeature EMPTY_COMPONENT_1_15 = PlatformFeature.of(() -> Version.isVersion(1, 15));
    public static final @NotNull PlatformFeature DESTROYSTOKYO_TITLE = PlatformFeature.of(() -> Reflect.has("com.destroystokyo.paper.Title"));
    public static final @NotNull PlatformFeature VERBOSE_TITLE_METHOD = PlatformFeature.of(() -> Reflect.hasMethod(Player.class, "sendTitle", String.class, String.class, int.class, int.class, int.class));
    public static final @NotNull PlatformFeature STOP_ALL_SOUNDS = PlatformFeature.of(() -> Reflect.hasMethod(Player.class, "stopAllSounds"));
    public static final @NotNull PlatformFeature STOP_SOUND = PlatformFeature.of(() -> Reflect.hasMethod(Player.class, "stopSound", String.class));
    public static final @NotNull PlatformFeature MODERN_OPEN_BOOK_PACKET = PlatformFeature.of(() -> Version.isVersion(1, 13, 1));
    public static final @NotNull PlatformFeature MODERN_OPEN_BOOK_PLUGIN_MESSAGE = PlatformFeature.of(() -> Version.isVersion(1, 13) && !Version.isVersion(1, 13, 1));

    // MISC
    public static final @NotNull PlatformFeature SOUND_KEYED = PlatformFeature.of(() -> Reflect.hasMethod(Sound.class, "getKey"));
    public static final @NotNull PlatformFeature HAS_PAPER_CONFIG = PlatformFeature.of(() -> Reflect.hasMethod(org.bukkit.Server.Spigot.class, "getPaperConfig"));
    public static final @NotNull PlatformFeature COLORED_BEDS = PlatformFeature.of(() -> Version.isVersion(1, 12));
    public static final @NotNull PlatformFeature OFF_HAND = PlatformFeature.of(() -> Version.isVersion(1, 9));
    public static final @NotNull PlatformFeature ENTITY_POSE = PlatformFeature.of(() -> Reflect.has("org.bukkit.entity.Pose"));
    public static final @NotNull PlatformFeature GAME_EVENT = PlatformFeature.of(() -> Reflect.has("org.bukkit.GameEvent"));
    public static final @NotNull PlatformFeature ENTITY_SHOOT_BOW_EVENT_CONSUMABLE = PlatformFeature.of(() -> Reflect.hasMethod(EntityShootBowEvent.class, "getConsumable"));
    public static final @NotNull PlatformFeature ENTITY_SHOOT_BOW_EVENT_HAND = PlatformFeature.of(() -> Reflect.hasMethod(EntityShootBowEvent.class, "getHand"));
    public static final @NotNull PlatformFeature PLAYER_SHEAR_ENTITY_EVENT_ITEM_HAND = PlatformFeature.of(() -> Reflect.hasMethod(PlayerShearEntityEvent.class, "getItem"));
    public static final @NotNull PlatformFeature FOLIA_TASKER = PlatformFeature.of(() -> Reflect.has("io.papermc.paper.threadedregions.scheduler.ScheduledTask"));
}
