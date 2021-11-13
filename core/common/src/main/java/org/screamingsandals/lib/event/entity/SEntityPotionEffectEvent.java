package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.item.meta.PotionEffectHolder;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityPotionEffectEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;
    private final ImmutableObjectLink<PotionEffectHolder> oldEffect;
    private final ImmutableObjectLink<PotionEffectHolder> newEffect;
    private final ImmutableObjectLink<Cause> cause;
    private final ImmutableObjectLink<Action> action;
    private final ObjectLink<Boolean> override;

    public EntityBasic getEntity() {
        return entity.get();
    }

    public PotionEffectHolder getOldEffect() {
        return oldEffect.get();
    }

    public PotionEffectHolder getNewEffect() {
        return newEffect.get();
    }

    public Cause getCause() {
        return cause.get();
    }

    public Action getAction() {
        return action.get();
    }

    public boolean isOverride() {
        return override.get();
    }

    public void setOverride(boolean override) {
        this.override.set(override);
    }

    /**
     * An enum to specify the action to be performed.
     */
    // TODO: holder?
    public enum Action {

        /**
         * When the potion effect is added because the entity didn't have its
         * type.
         */
        ADDED,
        /**
         * When the entity already had the potion effect type, but the effect is
         * changed.
         */
        CHANGED,
        /**
         * When the effect is removed due to all effects being removed.
         */
        CLEARED,
        /**
         * When the potion effect type is completely removed.
         */
        REMOVED
    }

    /**
     * An enum to specify the cause why an effect was changed.
     */
    // TODO: holder
    public enum Cause {

        /**
         * When the entity stands inside an area effect cloud.
         */
        AREA_EFFECT_CLOUD,
        /**
         * When the entity is hit by an spectral or tipped arrow.
         */
        ARROW,
        /**
         * When the entity is inflicted with a potion effect due to an entity
         * attack (e.g. a cave spider or a shulker bullet).
         */
        ATTACK,
        /**
         * When beacon effects get applied due to the entity being nearby.
         */
        BEACON,
        /**
         * When a potion effect is changed due to the /effect command.
         */
        COMMAND,
        /**
         * When the entity gets the effect from a conduit.
         */
        CONDUIT,
        /**
         * When a conversion from a villager zombie to a villager is started or
         * finished.
         */
        CONVERSION,
        /**
         * When all effects are removed due to death (Note: This is called on
         * respawn, so it's player only!)
         */
        DEATH,
        /**
         * When the entity gets the effect from a dolphin.
         */
        DOLPHIN,
        /**
         * When the effect was removed due to expiration.
         */
        EXPIRATION,
        /**
         * When an effect is inflicted due to food (e.g. when a player eats or a
         * cookie is given to a parrot).
         */
        FOOD,
        /**
         * When an illusion illager makes himself disappear.
         */
        ILLUSION,
        /**
         * When all effects are removed due to a bucket of milk.
         */
        MILK,
        /**
         * When a player gets bad omen after killing a patrol captain.
         */
        PATROL_CAPTAIN,
        /**
         * When a potion effect is modified through the plugin methods.
         */
        PLUGIN,
        /**
         * When the entity drinks a potion.
         */
        POTION_DRINK,
        /**
         * When the entity is inflicted with an effect due to a splash potion.
         */
        POTION_SPLASH,
        /**
         * When a spider gets effects when spawning on hard difficulty.
         */
        SPIDER_SPAWN,
        /**
         * When the entity gets effects from a totem item saving its life.
         */
        TOTEM,
        /**
         * When the entity gets water breathing by wearing a turtle helmet.
         */
        TURTLE_HELMET,
        /**
         * When the Cause is missing.
         */
        UNKNOWN,
        /**
         * When a villager gets regeneration after a trade.
         */
        VILLAGER_TRADE,
        /**
         * When an entity comes in contact with a wither rose.
         */
        WITHER_ROSE
    }
}
