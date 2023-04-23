package com.mobiquity.packer.comparator;

import com.mobiquity.packer.model.Pack;

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
        // if newPack is null, return false
        if (newPack == null) {
            return false;
        }

        if (currentPack == null) {
            return true;
        }

        // if newPack does not respect weight limit, return false
        if (newPack.getTotalWeight().compareTo(newPack.getWeightLimit()) > 0) {
            return false;
        }

        // if newPack costs more, return true
        if (newPack.getTotalCost().compareTo(currentPack.getTotalCost()) > 0) {
            return true;
        }

        // if newPack costs the same, but weight is lower, return true
        return newPack.getTotalCost().compareTo(currentPack.getTotalCost()) == 0
                && newPack.getTotalWeight().compareTo(currentPack.getTotalWeight()) < 0;
    }
}
