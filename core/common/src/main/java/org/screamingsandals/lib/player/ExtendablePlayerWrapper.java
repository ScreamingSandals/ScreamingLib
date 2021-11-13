package org.screamingsandals.lib.player;

import io.netty.channel.Channel;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.attribute.AttributeHolder;
import org.screamingsandals.lib.attribute.AttributeTypeHolder;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockTypeHolder;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.Openable;
import org.screamingsandals.lib.container.PlayerContainer;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.entity.EntityProjectile;
import org.screamingsandals.lib.entity.type.EntityTypeHolder;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.particle.ParticleHolder;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;
import org.screamingsandals.lib.utils.BasicWrapper;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.weather.WeatherHolder;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

// TODO: This class should be generated
public class ExtendablePlayerWrapper extends BasicWrapper<PlayerWrapper> implements PlayerWrapper {
    protected ExtendablePlayerWrapper(PlayerWrapper wrappedObject) {
        super(wrappedObject);
        if (wrappedObject instanceof ExtendablePlayerWrapper) {
            throw new UnsupportedOperationException("ExtendablePlayerWrapper can't wrap another ExtendablePlayerWrapper!");
        }
    }

    @Override
    public EntityTypeHolder getEntityType() {
        return wrappedObject.getEntityType();
    }

    @Override
    public LocationHolder getLocation() {
        return wrappedObject.getLocation();
    }

    @Override
    public Vector3D getVelocity() {
        return wrappedObject.getVelocity();
    }

    @Override
    public void setVelocity(Vector3D velocity) {
        wrappedObject.setVelocity(velocity);
    }

    @Override
    public double getHeight() {
        return wrappedObject.getHeight();
    }

    @Override
    public double getWidth() {
        return wrappedObject.getWidth();
    }

    @Override
    public boolean isOnGround() {
        return wrappedObject.isOnGround();
    }

    @Override
    public boolean isInWater() {
        return wrappedObject.isInWater();
    }

    @Override
    public CompletableFuture<Boolean> teleport(LocationHolder location) {
        return wrappedObject.teleport(location);
    }

    @Override
    public CompletableFuture<Void> teleport(LocationHolder location, Runnable callback, boolean forceCallback) {
        return wrappedObject.teleport(location, callback, forceCallback);
    }

    @Override
    public boolean teleportSync(LocationHolder location) {
        return wrappedObject.teleportSync(location);
    }

    @Override
    public int getEntityId() {
        return wrappedObject.getEntityId();
    }

    @Override
    public int getFireTicks() {
        return wrappedObject.getFireTicks();
    }

    @Override
    public int getMaxFireTicks() {
        return wrappedObject.getMaxFireTicks();
    }

    @Override
    public void setFireTicks(int fireTicks) {
        wrappedObject.setFireTicks(fireTicks);
    }

    @Override
    public void remove() {
        wrappedObject.remove();
    }

    @Override
    public boolean isDead() {
        return wrappedObject.isDead();
    }

    @Override
    public boolean isPersistent() {
        return wrappedObject.isPersistent();
    }

    @Override
    public void setPersistent(boolean persistent) {
        wrappedObject.setPersistent(persistent);
    }

    @Override
    public List<EntityBasic> getPassengers() {
        return wrappedObject.getPassengers();
    }

    @Override
    public boolean addPassenger(EntityBasic passenger) {
        return wrappedObject.addPassenger(passenger);
    }

    @Override
    public boolean removePassenger(EntityBasic passenger) {
        return wrappedObject.removePassenger(passenger);
    }

    @Override
    public boolean hasPassengers() {
        return wrappedObject.hasPassengers();
    }

    @Override
    public boolean ejectPassengers() {
        return wrappedObject.ejectPassengers();
    }

    @Override
    public float getFallDistance() {
        return wrappedObject.getFallDistance();
    }

    @Override
    public void setFallDistance(float distance) {
        wrappedObject.setFallDistance(distance);
    }

    @Override
    public UUID getUniqueId() {
        return wrappedObject.getUniqueId();
    }

    @Override
    public int getTicksLived() {
        return wrappedObject.getTicksLived();
    }

    @Override
    public void setTicksLived(int value) {
        wrappedObject.setTicksLived(value);
    }

    @Override
    public boolean isInsideVehicle() {
        return wrappedObject.isInsideVehicle();
    }

    @Override
    public boolean leaveVehicle() {
        return wrappedObject.leaveVehicle();
    }

    @Override
    public EntityBasic getVehicle() {
        return wrappedObject.getVehicle();
    }

    @Override
    public void setCustomName(String name) {
        wrappedObject.setCustomName(name);
    }

    @Override
    public void setCustomName(Component name) {
        wrappedObject.setCustomName(name);
    }

    @Override
    public @Nullable Component getCustomName() {
        return wrappedObject.getCustomName();
    }

    @Override
    public void setCustomNameVisible(boolean flag) {
        wrappedObject.setCustomNameVisible(flag);
    }

    @Override
    public boolean isCustomNameVisible() {
        return wrappedObject.isCustomNameVisible();
    }

    @Override
    public void setGlowing(boolean flag) {
        wrappedObject.setGlowing(flag);
    }

    @Override
    public boolean isGlowing() {
        return wrappedObject.isGlowing();
    }

    @Override
    public void setInvulnerable(boolean flag) {
        wrappedObject.setInvulnerable(flag);
    }

    @Override
    public boolean isInvulnerable() {
        return wrappedObject.isInvulnerable();
    }

    @Override
    public boolean isSilent() {
        return wrappedObject.isSilent();
    }

    @Override
    public void setSilent(boolean flag) {
        wrappedObject.setSilent(flag);
    }

    @Override
    public boolean hasGravity() {
        return wrappedObject.hasGravity();
    }

    @Override
    public void setGravity(boolean gravity) {
        wrappedObject.setGravity(gravity);
    }

    @Override
    public int getPortalCooldown() {
        return wrappedObject.getPortalCooldown();
    }

    @Override
    public void setPortalCooldown(int cooldown) {
        wrappedObject.setPortalCooldown(cooldown);
    }

    @Override
    public boolean hasMetadata(String metadata) {
        return wrappedObject.hasMetadata(metadata);
    }

    @Override
    public Object getMetadata(String metadata) {
        return wrappedObject.getMetadata(metadata);
    }

    @Override
    public int getIntMetadata(String metadata) {
        return wrappedObject.getIntMetadata(metadata);
    }

    @Override
    public boolean getBooleanMetadata(String metadata) {
        return wrappedObject.getBooleanMetadata(metadata);
    }

    @Override
    public byte getByteMetadata(String metadata) {
        return wrappedObject.getByteMetadata(metadata);
    }

    @Override
    public long getLongMetadata(String metadata) {
        return wrappedObject.getLongMetadata(metadata);
    }

    @Override
    public String getStringMetadata(String metadata) {
        return wrappedObject.getStringMetadata(metadata);
    }

    @Override
    public Component getComponentMetadata(String metadata) {
        return wrappedObject.getComponentMetadata(metadata);
    }

    @Override
    public LocationHolder getLocationMetadata(String metadata) {
        return wrappedObject.getLocationMetadata(metadata);
    }

    @Override
    public RGBLike getColorMetadata(String metadata) {
        return wrappedObject.getColorMetadata(metadata);
    }

    @Override
    public Vector3D getVectorMetadata(String metadata) {
        return wrappedObject.getVectorMetadata(metadata);
    }

    @Override
    public Vector3Df getFloatVectorMetadata(String metadata) {
        return wrappedObject.getFloatVectorMetadata(metadata);
    }

    @Override
    public void setMetadata(String metadata, Object value) {
        wrappedObject.setMetadata(metadata, value);
    }

    @Override
    public int getExpToLevel() {
        return wrappedObject.getExpToLevel();
    }

    @Override
    public float getSaturation() {
        return wrappedObject.getSaturation();
    }

    @Override
    public void setSaturation(float saturation) {
        wrappedObject.setSaturation(saturation);
    }

    @Override
    public float getExhaustion() {
        return wrappedObject.getExhaustion();
    }

    @Override
    public void setExhaustion(float exhaustion) {
        wrappedObject.setExhaustion(exhaustion);
    }

    @Override
    public int getFoodLevel() {
        return wrappedObject.getFoodLevel();
    }

    @Override
    public void setFoodLevel(int foodLevel) {
        wrappedObject.setFoodLevel(foodLevel);
    }

    @Override
    public Optional<AttributeHolder> getAttribute(AttributeTypeHolder attributeType) {
        return wrappedObject.getAttribute(attributeType);
    }

    @Override
    public double getEyeHeight() {
        return wrappedObject.getEyeHeight();
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        return wrappedObject.getEyeHeight(ignorePose);
    }

    @Override
    public LocationHolder getEyeLocation() {
        return wrappedObject.getEyeLocation();
    }

    @Override
    public BlockHolder getTargetBlock(Collection<BlockTypeHolder> transparent, int maxDistance) {
        return wrappedObject.getTargetBlock(transparent, maxDistance);
    }

    @Override
    public Optional<BlockHolder> getTargetBlock(int maxDistance) {
        return wrappedObject.getTargetBlock(maxDistance);
    }

    @Override
    public int getRemainingAir() {
        return wrappedObject.getRemainingAir();
    }

    @Override
    public void setRemainingAir(int ticks) {
        wrappedObject.setRemainingAir(ticks);
    }

    @Override
    public int getMaximumAir() {
        return wrappedObject.getMaximumAir();
    }

    @Override
    public void setMaximumAir(int ticks) {
        wrappedObject.setMaximumAir(ticks);
    }

    @Override
    public int getArrowCooldown() {
        return wrappedObject.getArrowCooldown();
    }

    @Override
    public void setArrowCooldown(int ticks) {
        wrappedObject.setArrowCooldown(ticks);
    }

    @Override
    public int getArrowsInBody() {
        return wrappedObject.getArrowsInBody();
    }

    @Override
    public void setArrowsInBody(int count) {
        wrappedObject.setArrowsInBody(count);
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return wrappedObject.getMaximumNoDamageTicks();
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {
        wrappedObject.setMaximumNoDamageTicks(ticks);
    }

    @Override
    public double getLastDamage() {
        return wrappedObject.getLastDamage();
    }

    @Override
    public void setLastDamage(double damage) {
        wrappedObject.setLastDamage(damage);
    }

    @Override
    public int getNoDamageTicks() {
        return wrappedObject.getNoDamageTicks();
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        wrappedObject.setNoDamageTicks(ticks);
    }

    @Override
    public Optional<EntityLiving> getHumanKiller() {
        return wrappedObject.getHumanKiller();
    }

    @Override
    public boolean addPotionEffect(PotionEffectHolder effect) {
        return wrappedObject.addPotionEffect(effect);
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffectHolder> effects) {
        return wrappedObject.addPotionEffects(effects);
    }

    @Override
    public boolean hasPotionEffect(PotionEffectHolder type) {
        return wrappedObject.hasPotionEffect(type);
    }

    @Override
    public void removePotionEffect(PotionEffectHolder type) {
        wrappedObject.removePotionEffect(type);
    }

    @Override
    public List<PotionEffectHolder> getActivePotionEffects() {
        return wrappedObject.getActivePotionEffects();
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return wrappedObject.getRemoveWhenFarAway();
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {
        wrappedObject.setRemoveWhenFarAway(remove);
    }

    @Override
    public void setCanPickupItems(boolean pickup) {
        wrappedObject.setCanPickupItems(pickup);
    }

    @Override
    public boolean getCanPickupItems() {
        return wrappedObject.getCanPickupItems();
    }

    @Override
    public boolean isLeashed() {
        return wrappedObject.isLeashed();
    }

    @Override
    public Optional<EntityBasic> getLeashHolder() {
        return wrappedObject.getLeashHolder();
    }

    @Override
    public boolean setLeashHolder(EntityBasic holder) {
        return wrappedObject.setLeashHolder(holder);
    }

    @Override
    public boolean removeLeashHolder() {
        return wrappedObject.removeLeashHolder();
    }

    @Override
    public boolean isGliding() {
        return wrappedObject.isGliding();
    }

    @Override
    public void setGliding(boolean gliding) {
        wrappedObject.setGliding(gliding);
    }

    @Override
    public boolean isSwimming() {
        return wrappedObject.isSwimming();
    }

    @Override
    public void setSwimming(boolean swimming) {
        wrappedObject.setSwimming(swimming);
    }

    @Override
    public boolean isRiptiding() {
        return wrappedObject.isRiptiding();
    }

    @Override
    public boolean isSleeping() {
        return wrappedObject.isSleeping();
    }

    @Override
    public void setAI(boolean ai) {
        wrappedObject.setAI(ai);
    }

    @Override
    public boolean hasAI() {
        return wrappedObject.hasAI();
    }

    @Override
    public void attack(EntityBasic target) {
        wrappedObject.attack(target);
    }

    @Override
    public void swingMainHand() {
        wrappedObject.swingMainHand();
    }

    @Override
    public void swingOffHand() {
        wrappedObject.swingOffHand();
    }

    @Override
    public void setCollidable(boolean collidable) {
        wrappedObject.setCollidable(collidable);
    }

    @Override
    public boolean isCollidable() {
        return wrappedObject.isCollidable();
    }

    @Override
    public void setInvisible(boolean invisible) {
        wrappedObject.setInvisible(invisible);
    }

    @Override
    public boolean isInvisible() {
        return wrappedObject.isInvisible();
    }

    @Override
    public void damage(double amount) {
        wrappedObject.damage(amount);
    }

    @Override
    public void damage(double amount, EntityBasic damageSource) {
        wrappedObject.damage(amount, damageSource);
    }

    @Override
    public double getAbsorptionAmount() {
        return wrappedObject.getAbsorptionAmount();
    }

    @Override
    public double getHealth() {
        return wrappedObject.getHealth();
    }

    @Override
    public void setAbsorptionAmount(double amount) {
        wrappedObject.setAbsorptionAmount(amount);
    }

    @Override
    public void setHealth(double health) {
        wrappedObject.setHealth(health);
    }

    @Override
    public @Nullable Item getHelmet() {
        return wrappedObject.getHelmet();
    }

    @Override
    public @Nullable Item getChestplate() {
        return wrappedObject.getChestplate();
    }

    @Override
    public @Nullable Item getLeggings() {
        return wrappedObject.getLeggings();
    }

    @Override
    public @Nullable Item getBoots() {
        return wrappedObject.getBoots();
    }

    @Override
    public void setHelmet(@Nullable Item helmet) {
        wrappedObject.setHelmet(helmet);
    }

    @Override
    public void setChestplate(@Nullable Item chestplate) {
        wrappedObject.setChestplate(chestplate);
    }

    @Override
    public void setLeggings(@Nullable Item leggings) {
        wrappedObject.setLeggings(leggings);
    }

    @Override
    public void setBoots(@Nullable Item boots) {
        wrappedObject.setBoots(boots);
    }

    @Override
    public @Nullable Item getItemInMainHand() {
        return wrappedObject.getItemInMainHand();
    }

    @Override
    public void setItemInMainHand(@Nullable Item item) {
        wrappedObject.setItemInMainHand(item);
    }

    @Override
    public @Nullable Item getItemInOffHand() {
        return wrappedObject.getItemInOffHand();
    }

    @Override
    public void setItemInOffHand(@Nullable Item item) {
        wrappedObject.setItemInOffHand(item);
    }

    @Override
    public Optional<EntityProjectile> launchProjectile(EntityTypeHolder projectileType) {
        return wrappedObject.launchProjectile(projectileType);
    }

    @Override
    public Optional<EntityProjectile> launchProjectile(EntityTypeHolder projectileType, Vector3D velocity) {
        return wrappedObject.launchProjectile(projectileType, velocity);
    }

    @Override
    public boolean isSprinting() {
        return wrappedObject.isSprinting();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        wrappedObject.setSprinting(sprinting);
    }

    @Override
    public boolean isFlying() {
        return wrappedObject.isFlying();
    }

    @Override
    public void setFlying(boolean flying) {
        wrappedObject.setFlying(flying);
    }

    @Override
    public boolean isAllowFlight() {
        return wrappedObject.isAllowFlight();
    }

    @Override
    public void setAllowFlight(boolean flying) {
        wrappedObject.setAllowFlight(flying);
    }

    @Override
    public boolean isSneaking() {
        return wrappedObject.isSneaking();
    }

    @Override
    public void setSneaking(boolean sneaking) {
        wrappedObject.setSneaking(sneaking);
    }

    @Override
    public int getPing() {
        return wrappedObject.getPing();
    }

    @Override
    public @Nullable Component getPlayerListName() {
        return wrappedObject.getPlayerListName();
    }

    @Override
    public void setPlayerListName(@Nullable Component component) {
        wrappedObject.setPlayerListName(component);
    }

    @Override
    public void setPlayerListName(@Nullable ComponentLike component) {
        wrappedObject.setPlayerListName(component);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return wrappedObject.getDisplayName();
    }

    @Override
    public void setDisplayName(@Nullable Component component) {
        wrappedObject.setDisplayName(component);
    }

    @Override
    public void setDisplayName(@Nullable ComponentLike component) {
        wrappedObject.setDisplayName(component);
    }

    @Override
    public Container getEnderChest() {
        return wrappedObject.getEnderChest();
    }

    @Override
    public PlayerContainer getPlayerInventory() {
        return wrappedObject.getPlayerInventory();
    }

    @Override
    public Optional<Container> getOpenedInventory() {
        return wrappedObject.getOpenedInventory();
    }

    @Override
    public void openInventory(Openable container) {
        wrappedObject.openInventory(container);
    }

    @Override
    public void closeInventory() {
        wrappedObject.closeInventory();
    }

    @Override
    public void kick(Component message) {
        wrappedObject.kick(message);
    }

    @Override
    public void kick(ComponentLike message) {
        wrappedObject.kick(message);
    }

    @Override
    public GameModeHolder getGameMode() {
        return wrappedObject.getGameMode();
    }

    @Override
    public void setGameMode(@NotNull GameModeHolder gameMode) {
        wrappedObject.setGameMode(gameMode);
    }

    @Override
    public int getLevel() {
        return wrappedObject.getLevel();
    }

    @Override
    public float getExp() {
        return wrappedObject.getExp();
    }

    @Override
    public void setLevel(int level) {
        wrappedObject.setLevel(level);
    }

    @Override
    public void setExp(float exp) {
        wrappedObject.setExp(exp);
    }

    @Override
    public void forceUpdateInventory() {
        wrappedObject.forceUpdateInventory();
    }

    @Override
    public Optional<WeatherHolder> getPlayerWeather() {
        return wrappedObject.getPlayerWeather();
    }

    @Override
    public void setPlayerWeather(@Nullable WeatherHolder weather) {
        wrappedObject.setPlayerWeather(weather);
    }

    @Override
    public long getPlayerTime() {
        return wrappedObject.getPlayerTime();
    }

    @Override
    public void setPlayerTime(long time, boolean relative) {
        wrappedObject.setPlayerTime(time, relative);
    }

    @Override
    public void resetPlayerTime() {
        wrappedObject.resetPlayerTime();
    }

    @Override
    public void sendParticle(ParticleHolder particle, LocationHolder location) {
        wrappedObject.sendParticle(particle, location);
    }

    @Override
    public void setCompassTarget(LocationHolder location) {
        wrappedObject.setCompassTarget(location);
    }

    @Override
    public void restoreDefaultScoreboard() {
        wrappedObject.restoreDefaultScoreboard();
    }

    @Override
    public Optional<EntityBasic> getSpectatorTarget() {
        return wrappedObject.getSpectatorTarget();
    }

    @Override
    public void setSpectatorTarget(@Nullable EntityBasic entity) {
        wrappedObject.setSpectatorTarget(entity);
    }

    @Override
    public LocationHolder getCompassTarget() {
        return wrappedObject.getCompassTarget();
    }

    @Override
    public Channel getChannel() {
        return wrappedObject.getChannel();
    }

    @Override
    public void sendMessage(String message) {
        wrappedObject.sendMessage(message);
    }

    @Override
    public String getName() {
        return wrappedObject.getName();
    }

    @Override
    public @NotNull Audience audience() {
        return wrappedObject.audience();
    }

    @Override
    public UUID getUuid() {
        return wrappedObject.getUuid();
    }

    @Override
    public Optional<String> getLastName() {
        return wrappedObject.getLastName();
    }

    @Override
    public <T> T as(Class<T> type) {
        try {
            return super.as(type);
        } catch (UnsupportedOperationException ignored) {
            return wrappedObject.as(type);
        }
    }

    @Override
    public Object raw() {
        return wrappedObject.raw();
    }

    @Override
    public boolean holdsInventory() {
        return wrappedObject.holdsInventory();
    }

    @Override
    public Optional<Container> getInventory() {
        return wrappedObject.getInventory();
    }

    @Override
    public void tryToDispatchCommand(String command) {
        wrappedObject.tryToDispatchCommand(command);
    }
}
