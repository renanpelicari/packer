package com.mobiquity.packer.strategy;

import com.mobiquity.packer.adapter.PackAdapter;
import com.mobiquity.packer.entity.FileContent;
import com.mobiquity.packer.entity.Pack;
import com.mobiquity.packer.entity.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Strategy class for algorithm to decide better option for pack.
 */
public class PackStrategy {

    private static final Logger LOG = Logger.getLogger("PackStrategy");

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

        final FileContent fileContent = PackAdapter.getContentFromLine(line);

        if (!validateFileContentConstraints(fileContent)) {
            return null;
        }

        final BigDecimal weightLimit = fileContent.getWeightLimit();

        final Set<Product> products = new HashSet<>();
        BigDecimal totalWeight = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;

        // create a map to be a base of combinations
        final HashMap<Integer, Product> productMap = new HashMap<>();

        for (final String productString : fileContent.getProductsAsString()) {

            final Product product = PackAdapter.getProductFromString(productString);

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
                CombinationStrategy.findNonRepeatedCombinations(new ArrayList<>(productMap.keySet()));

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

            // if new pack is null or better, betterPack receive newPack
            if (betterPack == null || betterPack.isNewPackBetter(newPack)) {
                betterPack = newPack;
            }
        }

        LOG.info(String.format("END getBestProductsOption, betterPack={%s}", betterPack));

        return betterPack;
    }

    private static boolean validateFileContentConstraints(final FileContent fileContent) {
        boolean isOk = true;

        if (fileContent == null) {
            LOG.log(Level.WARNING, "The file is empty or null");
            return false;
        }

        if (fileContent.getWeightLimit().compareTo(BigDecimal.valueOf(100)) > 0) {
            final String errorMsg = String.format("Weight of package (%f) exceeded 100", fileContent.getWeightLimit());
            LOG.log(Level.WARNING, errorMsg);
            isOk = false;
        }

        int totalProducts = fileContent.getProductsAsString().length;
        if (totalProducts > 15) {
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

        if (product.getWeight().compareTo(BigDecimal.valueOf(100)) > 0) {
            final String errorMsg = String.format("Weight of product (%f) exceeded 100", product.getWeight());
            LOG.log(Level.WARNING, errorMsg);
            isOk = false;
        }

        if (product.getCost().compareTo(BigDecimal.valueOf(100)) > 0) {
            final String errorMsg = String.format("Cost of product (%f) exceeded 100", product.getCost());
            LOG.log(Level.WARNING, errorMsg);
            isOk = false;
        }

        return isOk;
    }

}
