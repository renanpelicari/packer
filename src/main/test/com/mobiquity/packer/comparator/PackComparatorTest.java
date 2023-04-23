package com.mobiquity.packer.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for {@link Pack}
 */
public class PackTest {

    private final static BigDecimal BIG_DECIMAL_70 = new BigDecimal(70);
    private final static BigDecimal BIG_DECIMAL_80 = new BigDecimal(80);
    private final static BigDecimal BIG_DECIMAL_00_01 = new BigDecimal("0.01");

    private final static Product DEFAULT_PRODUCT = new Product(1, BIG_DECIMAL_80, BIG_DECIMAL_70);
    private final static Set<Product> DEFAULT_PRODUCTS = Collections.singleton(DEFAULT_PRODUCT);
    private final static Pack DEFAULT_PACK = new Pack(BIG_DECIMAL_80, DEFAULT_PRODUCTS, BIG_DECIMAL_80, BIG_DECIMAL_70);

    @Test
    public void givenNullAsNewPack_whenCallIsNewPackBetter_shouldReturnFalse() {
        assertFalse(DEFAULT_PACK.isNewPackBetter(null));
    }

    @Test
    public void givenANewPackWithTotalWeightHeavierThanLimit_whenCallIsNewPackBetter_shouldReturnFalse() {
        final Pack pack =  new Pack(BIG_DECIMAL_80, DEFAULT_PRODUCTS, BIG_DECIMAL_80.add(BIG_DECIMAL_00_01),
                BIG_DECIMAL_70);
        assertFalse(DEFAULT_PACK.isNewPackBetter(pack));
    }

    @Test
    public void givenANewPackThatCostMore_whenCallIsNewPackBetter_shouldReturnTrue() {
        final Pack pack =  new Pack(BIG_DECIMAL_80, DEFAULT_PRODUCTS, BIG_DECIMAL_80,
                BIG_DECIMAL_70.add(BIG_DECIMAL_00_01));
        assertTrue(DEFAULT_PACK.isNewPackBetter(pack));
    }

    @Test
    public void givenANewPackWithSameCost_butWeightIsLighterThanCurrent_whenCallIsNewPackBetter_shouldReturnTrue() {
        final Pack pack =  new Pack(BIG_DECIMAL_80, DEFAULT_PRODUCTS,
                BIG_DECIMAL_80.subtract(BIG_DECIMAL_00_01), BIG_DECIMAL_80);
        assertTrue(DEFAULT_PACK.isNewPackBetter(pack));
    }
}
