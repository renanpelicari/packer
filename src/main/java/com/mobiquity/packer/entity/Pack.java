package com.mobiquity.packer.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Set;

/**
 * The Pack entity.
 */
public class Pack {

    private final Double weightLimit;

    private final Set<Product> products;

    private final Double totalWeight;

    private final Double totalCost;

    /**
     * Default class constructor
     * @param weightLimit that can be found before comma in input file
     *                    (e.g.: "8 : (1,15.3,€34)", in this case 8 is the weightLimit)
     * @param products a {@link Set<Product>} inside pack
     * @param totalWeight the sum of {@link Product#getWeight()}
     * @param totalCost the sum of {@link Product#getCost()}
     */
    public Pack(final Double weightLimit, final Set<Product> products, final Double totalWeight,
                final Double totalCost) {
        this.products = products;
        this.weightLimit = setDoubleValuePrecision(weightLimit);
        this.totalWeight = setDoubleValuePrecision(totalWeight);
        this.totalCost = setDoubleValuePrecision(totalCost);
    }

    /**
     * This methods compare two different packs (the current and the new one),
     * and based on cost and weight return if the new pack is better.
     *
     * If newPack is null, return false;
     * If newPack is heavier than limit, return false;
     * If newPack cost more, return true;
     * If both pack cost the same, but new one is lighter than current, return true;
     *
     * @param newPack the new pack to compare
     * @return a boolean with result of comparision
     */
    public boolean isNewPackBetter(final Pack newPack) {
        // if newPack is null, return false
        if (newPack == null) {
            return false;
        }

        // if newPack not respect weight limit, return false
        if (newPack.getTotalWeight() > newPack.getWeightLimit()) {
            return false;
        }

        // if newPack cost more, return true
        if (newPack.getTotalCost() > this.getTotalCost()) {
            return true;
        }

        // if newPack cost same, but weight is lower, return true
        return newPack.getTotalCost().equals(this.getTotalCost()) && newPack.getTotalWeight() < this.getTotalWeight();
    }

    private Double setDoubleValuePrecision(final Double value) {
        return BigDecimal.valueOf(value).setScale(5, RoundingMode.HALF_UP).doubleValue();
    }

    public Double getWeightLimit() {
        return weightLimit;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public Double getTotalCost() {
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
