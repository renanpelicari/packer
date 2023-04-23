package com.mobiquity.packer.comparator;

import com.mobiquity.packer.model.Pack;

/**
 * This is util class to compare two different packs.
 */
public class PackComparator {

    /**
     * Compares two different packs (the current and the new one) and based on cost and weight
     * returns if the new pack is better.
     *
     * @param currentPack the current pack
     * @param newPack     the new pack to compare
     * @return true if the new pack is better, false otherwise
     */
    public static boolean isNewPackBetter(final Pack currentPack, final Pack newPack) {

        // Return false if newPack is null or does not respect weight limit
        if (newPack == null || newPack.getTotalWeight().compareTo(newPack.getWeightLimit()) > 0) {
            return false;
        }

        // Return true if currentPack is null
        if (currentPack == null) {
            return true;
        }

        // Compare total cost and total weight
        int costComparison = newPack.getTotalCost().compareTo(currentPack.getTotalCost());
        int weightComparison = newPack.getTotalWeight().compareTo(currentPack.getTotalWeight());

        // Return true if newPack costs more or costs the same but has lower weight
        return costComparison > 0 || (costComparison == 0 && weightComparison < 0);
    }
}
