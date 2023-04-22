package com.mobiquity.packer.entity;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * The product entity.
 */
public class Product {

    private final Integer index;

    private final BigDecimal weight;

    private final BigDecimal cost;

    /**
     * Default class constructor.
     * @param index the index of product
     * @param weight the weight of product
     * @param cost the cost of product
     */
    public Product(final Integer index, final BigDecimal weight, final BigDecimal cost) {
        this.index = index;
        this.weight = weight;
        this.cost = cost;
    }

    public Integer getIndex() {
        return index;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public BigDecimal getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "Product{" +
                "index=" + index +
                ", weight=" + weight +
                ", cost=" + cost +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (!Objects.equals(index, product.index)) return false;
        if (!Objects.equals(weight, product.weight)) return false;
        return Objects.equals(cost, product.cost);
    }

    @Override
    public int hashCode() {
        int result = index != null ? index.hashCode() : 0;
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        return result;
    }

}
