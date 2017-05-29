/**
 * Indicates that this is a living thing and can be eaten.
 */
public interface Living {
    /**
     * Getter function for the energy that this thing has.
     * @return The current energy capacity of this thing.
     */
    int getEnergy();

    /**
     * Function to remove self from world when eaten or dead.
     */
    void removeSelfFromWorld();
}
