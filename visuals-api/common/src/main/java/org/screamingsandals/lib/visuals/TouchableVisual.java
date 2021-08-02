package org.screamingsandals.lib.visuals;

public interface TouchableVisual<T> extends LocatableVisual<T> {
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
}
