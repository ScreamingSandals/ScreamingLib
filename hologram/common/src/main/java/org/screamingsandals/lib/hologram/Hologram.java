package org.screamingsandals.lib.hologram;

import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.utils.visual.TextEntry;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.*;

/**
 * Hologram that shows some text.
 */
public interface Hologram {
    /**
     * This is default view distance SQUARED!
     * <p>
     * That means that at this distance, you will see the hologram from 64 blocks away.
     */
    int DEFAULT_VIEW_DISTANCE = 4096;

    /**
     * Default rate that the item is rotated.
     */
    float DEFAULT_ROTATION_INCREMENT = 10f;

    /**
     * Creates new hologram.
     *
     * @param location location where to create the hologram
     * @return created hologram
     */
    static Hologram of(LocationHolder location) {
        return HologramManager.hologram(location);
    }

    /**
     * Creates new touchable hologram.
     *
     * @param location location where to create the hologram
     * @return created hologram
     */
    static Hologram touchableOf(LocationHolder location) {
        return HologramManager.hologram(location, true);
    }

    /**
     * @return UUID of this Hologram
     */
    UUID getUuid();

    /**
     * @return list of active viewers
     */
    List<PlayerWrapper> getViewers();

    /**
     * Adds new viewer into this Hologram
     *
     * @param player new viewer
     * @return this hologram
     */
    Hologram addViewer(PlayerWrapper player);

    /**
     * Removes given viewer from this Hologram
     *
     * @param player      viewer to remove
     * @param sendPackets if we should send destroy packets
     * @return this hologram
     */
    Hologram removeViewer(PlayerWrapper player, boolean sendPackets);

    /**
     * Removes given viewer from this Hologram
     *
     * @param player viewer to remove
     * @return this hologram
     */
    default Hologram removeViewer(PlayerWrapper player) {
        return removeViewer(player, true);
    }

    /**
     * Checks if this Hologram has any viewers.
     *
     * @return true if Hologram has any viewers
     */
    boolean hasViewers();

    /**
     * Location of this hologram
     *
     * @return {@link Optional} of Hologram location
     */
    Optional<LocationHolder> getLocation();

    /**
     * Sets new location for this Hologram
     *
     * @param location new location
     * @return this hologram
     */
    Hologram setLocation(LocationHolder location);

    /**
     * @return current view distance SQUARED
     */
    int getViewDistance();

    /**
     * Sets new view distance for this hologram.
     * !!!! NEEDS TO BE SQUARED !!!!
     *
     * @param viewDistance new view distance
     * @return this hologram
     */
    Hologram setViewDistance(int viewDistance);

    /**
     * Checks if you can interact with this Hologram
     *
     * @return true if this hologram is touchable
     */
    boolean isTouchable();

    /**
     * Changes interact state for this hologram
     *
     * @param touchable touchable state
     * @return this hologram
     */
    Hologram setTouchable(boolean touchable);

    /**
     * Checks if this hologram is shown to viewers.
     *
     * @return true if is shown to viewers
     */
    boolean isShown();

    /**
     * Shows this Hologram to viewers.
     *
     * @return this hologram
     */
    Hologram show();

    /**
     * Hides this Hologram from viewers;
     *
     * @return this hologram
     */
    Hologram hide();

    /**
     * Destroy this hologram.
     */
    void destroy();

    /**
     * @return data stored in this hologram
     */
    Data getData();

    /**
     * Replaces current data with new one
     *
     * @param data new data
     */
    void newData(Data data);

    /**
     * This is copy of the original data.
     * Changing this will have no effects on the actual hologram.
     *
     * @return a new {@link TreeMap} with lines.
     */
    Map<Integer, TextEntry> getLines();

    /**
     * Adds new line to the hologram and moves everything else 1 line down.
     *
     * @param text text to add
     * @return this hologram
     */
    Hologram firstLine(TextEntry text);

    /**
     * Adds given collection of new lines  at the bottom of this hologram.
     *
     * @param text text to add
     * @return this hologram
     */
    Hologram newLine(List<TextEntry> text);

    /**
     * Adds new line at the bottom of this hologram.
     *
     * @param text
     * @return this hologram
     */
    Hologram newLine(TextEntry text);

    /**
     * Sets new line. If the given line already exists, it's replaced.
     *
     * @param line where to add
     * @param text text to add
     * @return this hologram
     */
    Hologram setLine(int line, TextEntry text);

    /**
     * Tries to set the line by {@link TextEntry}.
     * If the {@link TextEntry#getIdentifier()} is empty, creates new line.
     * If not, it tries to replace given text to the line that has this identifier.
     *
     * @param entry TextEntry
     * @return this hologram
     */
    Hologram setLine(TextEntry entry);

    /**
     * Adds new line to the position and moves everything below.
     *
     * @param line where to add
     * @param text text to add
     * @return this hologram
     */
    Hologram addLine(int line, TextEntry text);

    /**
     * Removes given line from the hologram.
     *
     * @param line line to remove
     * @return this hologram
     */
    Hologram removeLine(int line);

    /**
     * Replaces all lines with new ones.
     *
     * @param lines new lines
     * @return this hologram
     */
    Hologram replaceLines(Map<Integer, TextEntry> lines);

    /**
     * Replaces lines with given collection.
     * The list is converted to TreeMap by it's given order
     *
     * @param lines new lines
     * @return this hologram
     */
    Hologram replaceLines(List<TextEntry> lines);

    /**
     * @return current rotation time
     */
    Pair<Integer, TaskerTime> getRotationTime();

    /**
     * The rotation time settings.
     *
     * @param rotatingTime {@link Pair} of time and unit.
     * @return this hologram
     */
    Hologram rotationTime(Pair<Integer, TaskerTime> rotatingTime);

    /**
     * @return current rotation mode
     */
    RotationMode getRotationMode();

    /**
     * The mode that the Hologram should be rotating
     *
     * @param mode rotation mode
     * @return this hologram
     */
    Hologram rotationMode(RotationMode mode);

    /**
     * Stands for how much should this hologram rotate per one cycle
     *
     * @param toIncrement
     * @return
     */
    Hologram rotationIncrement(float toIncrement);

    /**
     * Changes the item to show
     *
     * @param item item to show
     * @return this hologram
     */
    Hologram item(Item item);

    /**
     * Position of the item shown as hologram
     *
     * @param position position of the item
     * @return this hologram
     */
    Hologram itemPosition(ItemPosition position);

    /**
     * @return current item position
     */
    ItemPosition getItemPosition();

    /**
     * Represents rotation mode
     */
    enum RotationMode {
        X,
        Y,
        Z,
        ALL,
        XY,
        NONE
    }

    /**
     * Represents Item position
     */
    enum ItemPosition {
        ABOVE,
        BELOW
    }

    /**
     * Data storage for given hologram
     */
    interface Data {
        /**
         * Immutable copy of the data in this hologram.
         *
         * @return copy of data that this hologram has.
         */
        Map<String, Object> getAll();

        <T> T get(String key);

        boolean contains(String key);

        void set(String key, Object data);

        void add(String key, Object data);
    }
}
