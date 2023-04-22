package com.mobiquity.packer.strategy;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.entity.Pack;
import com.mobiquity.packer.entity.Product;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Unit tests for class {@link PackStrategy}
 */
public class PackStrategyTest {

    private static final String STRING_FORMAT_WRONG_CONVERT_ERROR = "Error to convert string to number, content=\"%s\"";

    @Test
    public void givenEmptyString_whenCallLineToPack_thenReturnNull() {
        assertNull(PackStrategy.getBetterPackFromInputLine(""));
    }

    @Test
    public void givenNullAsLine_whenCallLineToPack_thenReturnNull() {
        assertNull(PackStrategy.getBetterPackFromInputLine(null));
    }

    @Test
    public void givenSpaceAsLine_whenCallLineToPack_thenReturnNull() {
        assertNull(PackStrategy.getBetterPackFromInputLine(" "));
    }

    @Test
    public void givenAPackageOverWeightLimit_whenCallLineToPack_thenReturnNull() {
        assertNull(PackStrategy.getBetterPackFromInputLine("101.01 : (1,15.3,€34,1)"));
    }

    @Test
    public void givenAPackageWithMoreProductsThanExpected_whenCallLineToPack_thenReturnNull() {
        String content = "100 : (1,15.3,€34,1) (2,15.3,€34,1) (3,15.3,€34,1) (4,15.3,€34,1) (5,15.3,€34,1) " +
                "(6,15.3,€34,1) (7,15.3,€34,1) (8,15.3,€34,1) (9,15.3,€34,1) (10,15.3,€34,1) (11,15.3,€34,1) " +
                "(12,15.3,€34,1) (13,15.3,€34,1) (14,15.3,€34,1) (15,15.3,€34,1) (10,15.3,€34,1) " +
                "(16,15.3,€34,1)";

        assertNull(PackStrategy.getBetterPackFromInputLine(content));
    }

    @Test(expected = APIException.class)
    public void givenNotValidContent_whenCallLineToPack_thenThrowsException() {
        try {
            PackStrategy.getBetterPackFromInputLine("RPR : ");

        } catch (final APIException ex) {
            final String errorMsg = String.format(STRING_FORMAT_WRONG_CONVERT_ERROR, "RPR : ");
            assertEquals(errorMsg, ex.getMessage());
            throw ex;
        }
        fail("Should throw APIException due the cast error!");
    }

    @Test
    public void givenLineWithOnlyWeightLimit_whenCallLineToPack_thenReturnPackObjectOnlyWithWeight() {
        final Pack pack = PackStrategy.getBetterPackFromInputLine("87.65 : ");
        assertEquals(Double.valueOf(87.65), pack.getWeightLimit());
        assertEquals(Collections.emptySet(), pack.getProducts());
    }

    @Test(expected = APIException.class)
    public void givenProductWith2Parameters_whenCallLineToPack_thenThrowsException() {
        final String product = "(1,15.3)";
        final String line = "8 : " + product;

        try {
            PackStrategy.getBetterPackFromInputLine(line);

        } catch (final APIException ex) {
            final String errorMsg = String.format("Expected 3 args but got 2, content=\"%s\"", product);
            assertEquals(errorMsg, ex.getMessage());
            throw ex;
        }
        fail("Should throw APIException due the cast error!");
    }

    @Test(expected = APIException.class)
    public void givenProductWith4Parameters_whenCallLineToPack_thenThrowsException() {
        final String product = "(1,15.3,€34,1)";
        final String line = "8 : " + product;

        try {
            PackStrategy.getBetterPackFromInputLine(line);

        } catch (final APIException ex) {
            final String errorMsg = String.format("Expected 3 args but got 4, content=\"%s\"", product);
            assertEquals(errorMsg, ex.getMessage());
            throw ex;
        }
        fail("Should throw APIException due the cast error!");
    }

    @Test(expected = APIException.class)
    public void givenProductWithWrongIndex_whenCallLineToPack_thenThrowsException() {
        final String line = "8 : (A,15.3,€34)";

        try {
            PackStrategy.getBetterPackFromInputLine(line);

        } catch (final APIException ex) {
            final String errorMsg = String.format(STRING_FORMAT_WRONG_CONVERT_ERROR, "(A,15.3,€34)");
            assertEquals(errorMsg, ex.getMessage());
            throw ex;
        }
        fail("Should throw APIException due the cast error!");
    }

    @Test(expected = APIException.class)
    public void givenProductWithWrongWeight_whenCallLineToPack_thenThrowsException() {
        final String line = "8 : (1,A,€34)";

        try {
            PackStrategy.getBetterPackFromInputLine(line);

        } catch (final APIException ex) {
            final String errorMsg = String.format(STRING_FORMAT_WRONG_CONVERT_ERROR, "(1,A,€34)");
            assertEquals(errorMsg, ex.getMessage());
            throw ex;
        }
        fail("Should throw APIException due the cast error!");
    }

    @Test(expected = APIException.class)
    public void givenProductWithWrongCost_whenCallLineToPack_thenThrowsException() {
        final String line = "8 : (1,15.3,A)";

        try {
            PackStrategy.getBetterPackFromInputLine(line);

        } catch (final APIException ex) {
            final String errorMsg = String.format(STRING_FORMAT_WRONG_CONVERT_ERROR, "(1,15.3,A)");
            assertEquals(errorMsg, ex.getMessage());
            throw ex;
        }
        fail("Should throw APIException due the cast error!");
    }
    
    @Test
    public void givenLineWithOneElementOverWeight_whenCallLineToPack_thenReturnPackObjectOnlyWithWeight() {
        final Pack pack = PackStrategy.getBetterPackFromInputLine("15.3 : (1,15.31,€8.02)");
        assertEquals(Double.valueOf(15.3), pack.getWeightLimit());
        assertEquals(Collections.emptySet(), pack.getProducts());
    }

    @Test
    public void givenLineWithOneProductWithSameWeightAsLimit_whenCallLineToPack_thenReturnPackWithProduct() {
        final Pack pack = PackStrategy.getBetterPackFromInputLine("15.311 : (1,15.311,€8.01)");
        assertEquals(Double.valueOf(15.311), pack.getWeightLimit());
        assertEquals(1, pack.getProducts().size());

        final Product product = pack.getProducts().stream().findFirst().orElse(null);
        if (product == null) {
            fail("Should return 1 product");
        }

        assertEquals(Integer.valueOf(1), product.getIndex());
        assertEquals(Double.valueOf(15.311), product.getWeight());
        assertEquals(Double.valueOf(8.01), product.getCost());
    }

    @Test
    public void givenLineWithTwoProducts_onlyOneIsEligible_whenCallLineToPack_thenReturnPackWithOneProduct() {
        final Pack pack = PackStrategy.getBetterPackFromInputLine("15.311 : (1,15.311,€8.01) (2,15.312,€8.01)");
        assertEquals(Double.valueOf(15.311), pack.getWeightLimit());
        assertEquals(1, pack.getProducts().size());

        final Product product = pack.getProducts().stream().findFirst().orElse(null);
        if (product == null) {
            fail("Should return 1 product");
        }

        assertEquals(Integer.valueOf(1), product.getIndex());
        assertEquals(Double.valueOf(15.311), product.getWeight());
        assertEquals(Double.valueOf(8.01), product.getCost());
    }

    @Test
    public void given2ProductsWithTotalWeightIsOverThanLimit_whenCallLineToPack_thenReturnPackWithOneProductWithHigherCost() {
        final Pack pack = PackStrategy.getBetterPackFromInputLine("10 : (1,5,€8.01) (2,5.01,€8.02)");
        assertEquals(Double.valueOf(10), pack.getWeightLimit());
        assertEquals(1, pack.getProducts().size());
        assertEquals(Double.valueOf(5.01), pack.getTotalWeight());
        assertEquals(Double.valueOf(8.02), pack.getTotalCost());

        final Product product = pack.getProducts().stream().findFirst().orElse(null);
        if (product == null) {
            fail("Should return 1 product");
        }

        assertEquals(Integer.valueOf(2), product.getIndex());
        assertEquals(Double.valueOf(5.01), product.getWeight());
        assertEquals(Double.valueOf(8.02), product.getCost());
    }

    @Test
    public void givenInputExampleScenario1_whenCallLineToPack_thenReturnIndex4AsBetterOption() {
        final Pack pack = PackStrategy.getBetterPackFromInputLine("81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)");
        assertEquals(Double.valueOf(81), pack.getWeightLimit());
        assertEquals(1, pack.getProducts().size());
        assertEquals(Double.valueOf(72.3), pack.getTotalWeight());
        assertEquals(Double.valueOf(76), pack.getTotalCost());

        final Product product = pack.getProducts().stream().findFirst().orElse(null);
        if (product == null) {
            fail("Should return 1 product");
        }

        assertEquals(Integer.valueOf(4), product.getIndex());
        assertEquals(Double.valueOf(72.3), product.getWeight());
        assertEquals(Double.valueOf(76), product.getCost());
    }

    @Test
    public void givenInputExampleScenario2_whenCallLineToPack_thenReturnNoneAsBetterOption() {
        final Pack pack = PackStrategy.getBetterPackFromInputLine("8 : (1,15.3,€34)");
        assertEquals(Double.valueOf(8), pack.getWeightLimit());
        assertEquals(0, pack.getProducts().size());
    }

    @Test
    public void givenInputExampleScenario3_whenCallLineToPack_thenReturnIndex2And7AsBetterOption() {
        final Pack pack = PackStrategy.getBetterPackFromInputLine("75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)");
        assertEquals(Double.valueOf(75), pack.getWeightLimit());
        assertEquals(2, pack.getProducts().size());
        assertEquals(Double.valueOf(74.57), pack.getTotalWeight());
        assertEquals(Double.valueOf(148), pack.getTotalCost());

        final Set<Product> expectedProducts = new HashSet<>(Arrays.asList(
                new Product(2, 14.55, 74d),
                new Product(7, 60.02, 74d)
        ));

        assertEquals(expectedProducts, pack.getProducts());
    }

    @Test
    public void givenInputExampleScenario4_whenCallLineToPack_thenReturnIndex8And9AsBetterOption() {
        final Pack pack = PackStrategy.getBetterPackFromInputLine("56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)");
        assertEquals(Double.valueOf(56), pack.getWeightLimit());
        assertEquals(2, pack.getProducts().size());
        assertEquals(Double.valueOf(26.12), pack.getTotalWeight());
        assertEquals(Double.valueOf(143), pack.getTotalCost());

        final Set<Product> expectedProducts = new HashSet<>(Arrays.asList(
                new Product(8, 19.36, 79d),
                new Product(9, 6.76, 64d)
        ));

        assertEquals(expectedProducts, pack.getProducts());
    }
}
