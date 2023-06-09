package com.mobiquity.packer.strategy;

import com.mobiquity.algorithm.CombinationSet;
import com.mobiquity.packer.comparator.PackComparator;
import com.mobiquity.packer.converter.PackConverter;
import com.mobiquity.packer.converter.ProductConverter;
import com.mobiquity.packer.dto.FileContentDto;
import com.mobiquity.packer.model.Pack;
import com.mobiquity.packer.model.Product;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Strategy class for algorithm to decide better option for pack.
 */
public class PackStrategy {

    private static final Logger LOG = Logger.getLogger("PackStrategy");
    private static final BigDecimal MAX_WEIGHT_LIMIT = BigDecimal.valueOf(100);
    private static final BigDecimal MAX_COST_LIMIT = BigDecimal.valueOf(100);
    private static final int MAX_PRODUCTS_LIMIT = 15;

    /**
     * Based on input line (e.g.: "81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3)"),
     * this method will check all the possible combination of products,
     * then will sum the cost and weight between the products and create a newPack,
     * this new pack will be compared with better pack so far, and decide if will change or not.
     *
     * @param line the string containing weight limit of box, and product data like index, cost and weight.
     * @return a {@link Pack} with better option of cost and weight.
     */
    public static Pack getBetterPackFromInputLine(final String line) {
        LOG.info(String.format("BEGIN getBetterPackFromInputLine, line={%s}", line));

        final FileContentDto fileContentDto = PackConverter.getContentFromLine(line);

        if (fileContentDto == null || !validateFileContentConstraints(fileContentDto)) {
            return null;
        }

        final BigDecimal weightLimit = fileContentDto.getWeightLimit();

        final Set<Product> products = new HashSet<>();
        BigDecimal totalWeight = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;

        // create a map to be a base of combinations
        final HashMap<Integer, Product> productMap = new HashMap<>();

        for (final String productString : fileContentDto.getProductsAsString()) {

            final Product product = ProductConverter.getProductFromString(productString);

            // filter only products that respect the weight limit and cost
            if (validateMaxCostAndWeightOfProduct(product, weightLimit)) {

                products.add(product);
                totalWeight = totalWeight.add(product.getWeight());
                totalCost = totalCost.add(product.getCost());

                productMap.put(product.getIndex(), product);
            }
        }

        // if totalWeight less or equal the limit, it's not necessary to test other combinations
        if (totalWeight.compareTo(weightLimit) <= 0) {
            return new Pack(weightLimit, products, totalWeight, totalCost);
        }

        final Pack pack = getBestProductsOption(weightLimit, productMap);
        LOG.info(String.format("END getBetterPackFromInputLine, pack={%s}", pack));

        return pack;
    }

    private static Pack getBestProductsOption(final BigDecimal weightLimit, final HashMap<Integer, Product> productMap) {
        LOG.info(String.format("BEGIN getBestProductsOption, weightLimit={%s}, productMap={%s}", weightLimit, productMap));

        // get all possible combinations between indexes
        final Set<Set<Integer>> allIndexesCombinations =
                CombinationSet.getCombinations(new ArrayList<>(productMap.keySet()));

        Pack betterPack = null;

        for (Set<Integer> combinationIndexes : allIndexesCombinations) {
            final Set<Product> products = new HashSet<>();
            BigDecimal totalWeight = BigDecimal.ZERO;
            BigDecimal totalCost = BigDecimal.ZERO;


            // for each possible combination, check all products based on index
            for (Integer productIndex : combinationIndexes) {
                final Product product = productMap.get(productIndex);

                // sum the cost and weight for comparison
                totalWeight = totalWeight.add(product.getWeight());
                totalCost = totalCost.add(product.getCost());

                products.add(product);
            }

            // create new pack, to compare with better one
            Pack newPack = new Pack(weightLimit, products, totalWeight, totalCost);

            // compare the packs and update if the new one is better
            if (PackComparator.isNewPackBetter(betterPack, newPack)) {
                betterPack = newPack;
            }
        }

        LOG.info(String.format("END getBestProductsOption, betterPack={%s}", betterPack));

        return betterPack;
    }

    private static boolean validateFileContentConstraints(final FileContentDto fileContentDto) {
        boolean isOk = true;

        if (fileContentDto == null) {
            LOG.log(Level.WARNING, "The file is empty or null");
            return false;
        }

        if (fileContentDto.getWeightLimit().compareTo(MAX_WEIGHT_LIMIT) > 0) {
            final String errorMsg = String.format("Weight of package (%f) exceeded 100", fileContentDto.getWeightLimit());
            LOG.log(Level.WARNING, errorMsg);
            isOk = false;
        }

        int totalProducts = fileContentDto.getProductsAsString().length;
        if (totalProducts > MAX_PRODUCTS_LIMIT) {
            final String errorMsg = String.format("Limit of products (%d) exceeded 15", totalProducts);
            LOG.log(Level.WARNING, errorMsg);
            isOk = false;
        }

        return isOk;
    }

    private static boolean validateMaxCostAndWeightOfProduct(final Product product, final BigDecimal weightLimit) {
        boolean isOk = true;

        if (product.getWeight().compareTo(weightLimit) > 0) {
            final String errorMsg = String.format("Weight of product (%f) is heavier than package limit (%f)",
                    product.getWeight(), weightLimit);
            LOG.log(Level.WARNING, errorMsg);
            isOk = false;
        }

        if (product.getWeight().compareTo(MAX_WEIGHT_LIMIT) > 0) {
            final String errorMsg = String.format("Weight of product (%f) exceeded 100", product.getWeight());
            LOG.log(Level.WARNING, errorMsg);
            isOk = false;
        }

        if (product.getCost().compareTo(MAX_COST_LIMIT) > 0) {
            final String errorMsg = String.format("Cost of product (%f) exceeded 100", product.getCost());
            LOG.log(Level.WARNING, errorMsg);
            isOk = false;
        }

        return isOk;
    }

}
