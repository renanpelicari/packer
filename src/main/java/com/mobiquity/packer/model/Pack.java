package com.mobiquity.packer.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

/**
 * The Pack entity.
 */
public class Pack {

    private final BigDecimal weightLimit;

    private final Set<Product> products;

    private final BigDecimal totalWeight;

    private final BigDecimal totalCost;

    /**
     * Default class constructor
     * @param weightLimit that can be found before comma in input file
     *                    (e.g.: "8 : (1,15.3,€34)", in this case 8 is the weightLimit)
     * @param products a {@link Set<Product>} inside pack
     * @param totalWeight the sum of {@link Product#getWeight()}
     * @param totalCost the sum of {@link Product#getCost()}
     */
    public Pack(final BigDecimal weightLimit, final Set<Product> products, final BigDecimal totalWeight,
                final BigDecimal totalCost) {
        this.products = products;
        this.weightLimit = weightLimit;
        this.totalWeight = totalWeight;
        this.totalCost = totalCost;
    }

    /**
     * This method compare two different packs (the current and the new one),
     * and based on cost and weight return if the new pack is better.
     * If newPack is null, return false;
     * If newPack is heavier than limit, return false;
     * If newPack cost more, return true;
     * If both pack cost the same, but new one is lighter than current, return true;
     *
     * @param newPack the new pack to compare
     * @return a boolean with result of comparision
     */
    // TODO: externalize this to a static class
    public boolean isNewPackBetter(final Pack newPack) {
        // if newPack is null, return false
        if (newPack == null) {
            return false;
        }

        // if newPack not respect weight limit, return false
        if (newPack.getTotalWeight().compareTo(newPack.getWeightLimit()) > 0) {
            return false;
        }

        // if newPack cost more, return true
        if (newPack.getTotalCost().compareTo(this.getTotalCost()) > 0) {
                return true;
        }

        // if newPack cost same, but weight is lower, return true
        return newPack.getTotalCost().compareTo(this.getTotalCost()) == 0 &&
                newPack.getTotalWeight().compareTo(this.getTotalWeight()) < 0;
    }

    public BigDecimal getWeightLimit() {
        return weightLimit;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    @Override
    public String toString() {
        return "Pack{" +
                "weightLimit=" + weightLimit +
                ", products=" + products +
                ", totalWeight=" + totalWeight +
                ", totalCost=" + totalCost +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pack pack = (Pack) o;

        if (!Objects.equals(weightLimit, pack.weightLimit)) return false;
        if (!Objects.equals(products, pack.products)) return false;
        if (!Objects.equals(totalWeight, pack.totalWeight)) return false;
        return Objects.equals(totalCost, pack.totalCost);
    }

    @Override
    public int hashCode() {
        int result = weightLimit != null ? weightLimit.hashCode() : 0;
        result = 31 * result + (products != null ? products.hashCode() : 0);
        result = 31 * result + (totalWeight != null ? totalWeight.hashCode() : 0);
        result = 31 * result + (totalCost != null ? totalCost.hashCode() : 0);
        return result;
    }
}
