package com.mobiquity.packer.dto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

/**
 * A data structure to manipulate info from input file.
 */
public class FileContentDto {

    private final BigDecimal weightLimit;

    private final String[] productsAsString;

    /**
     * Default class constructor.
     * @param weightLimit that can be found before comma in input file
     *                   (e.g.: "8 : (1,15.3,€34)", in this case 8 is the weightLimit)
     * @param productsAsString that can be found before comma in input file
     *                         (e.g.: "75 : (1,85.31,€29) (2,14.55,€74) ", in this case productAsString is an Array
     *                         with 2 elements: [[1,85.31,€29], [2,14.55,€74]])
     */
    public FileContentDto(final BigDecimal weightLimit, final String[] productsAsString) {
        this.weightLimit = weightLimit;
        this.productsAsString = productsAsString;
    }

    public BigDecimal getWeightLimit() {
        return weightLimit;
    }

    public String[] getProductsAsString() {
        return productsAsString;
    }

    @Override
    public String toString() {
        return "FileContent{" +
                "weightLimit=" + weightLimit +
                ", productsAsString=" + Arrays.toString(productsAsString) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileContentDto that = (FileContentDto) o;
        return Objects.equals(weightLimit, that.weightLimit) && Arrays.equals(productsAsString, that.productsAsString);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(weightLimit);
        result = 31 * result + Arrays.hashCode(productsAsString);
        return result;
    }
}
