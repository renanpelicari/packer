package com.mobiquity.packer.entity;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for {@link Pack}
 */
public class PackTest {

    private final static Product DEFAULT_PRODUCT = new Product(1, 80d, 70d);
    private final static Set<Product> DEFAULT_PRODUCTS = Collections.singleton(DEFAULT_PRODUCT);
    private final static Pack DEFAULT_PACK = new Pack(80d, DEFAULT_PRODUCTS, 80d, 70d);

    @Test
    public void givenNullAsNewPack_whenCallIsNewPackBetter_shouldReturnFalse() {
        assertFalse(DEFAULT_PACK.isNewPackBetter(null));
    }

    @Test
    public void givenANewPackWithTotalWeightHeavierThanLimit_whenCallIsNewPackBetter_shouldReturnFalse() {
        final Pack pack =  new Pack(80d, DEFAULT_PRODUCTS, 80.01d, 70d);
        assertFalse(DEFAULT_PACK.isNewPackBetter(pack));
    }

    @Test
    public void givenANewPackThatCostMore_whenCallIsNewPackBetter_shouldReturnTrue() {
        final Pack pack =  new Pack(80d, DEFAULT_PRODUCTS, 80d, 70.01d);
        assertTrue(DEFAULT_PACK.isNewPackBetter(pack));
    }

    @Test
    public void givenANewPackWithSameCost_butWeightIsLighterThanCurrent_whenCallIsNewPackBetter_shouldReturnTrue() {
        final Pack pack =  new Pack(80d, DEFAULT_PRODUCTS, 79.99d, 70d);
        assertTrue(DEFAULT_PACK.isNewPackBetter(pack));
    }
}
