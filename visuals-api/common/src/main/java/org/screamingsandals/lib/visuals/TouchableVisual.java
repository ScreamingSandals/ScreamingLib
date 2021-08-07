package org.screamingsandals.lib.visuals;

public interface TouchableVisual<T> extends LocatableVisual<T> {


    /**
     * This is the default click cool down in milliseconds
     */
    long DEFAULT_CLICK_COOL_DOWN = 20L;

    /**
     * Checks if you can interact with this Visual.
     *
     * @return true if this Visual is touchable
     */
    boolean isTouchable();

    /**
     * Changes interact state for this Visual.
     *
     * @param touchable touchable state
     * @return this Visual
     */
    T setTouchable(boolean touchable);

    /**
     * Returns if the entity id provided belongs to this Visual. Used for detecting Player interaction.
     *
     * @param entityId the id of the entity
     * @return true if the visual contains the entity id, false otherwise
     */
    boolean hasId(int entityId);

    /**
     *
     * @param delay the amount of time (in milliseconds) the last clicked user has to wait before interacting with this Visual again
     * @return this NPC
     */
    T setClickCoolDown(long delay);

    /**
     *
     * @return the amount of time (in milliseconds) the last clicked user has to wait before interacting with this Visual again
     */
    long getClickCoolDown();
}
