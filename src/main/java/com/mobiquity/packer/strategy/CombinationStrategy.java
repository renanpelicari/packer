package com.mobiquity.packer.strategy;

import java.util.*;
import java.util.logging.Logger;

/**
 * Strategy class for combination algorithm.
 */
public class CombinationStrategy {

    private static final Logger LOG = Logger.getLogger("CombinationStrategy");

    /**
     * Given all product index, this method will return all possible non repeat combinations between them.
     * p.s.: The non repetition is guaranteed by {@link Set}.
     * e.g.
     * input: 1, 2, 3
     * output: [[1], [2], [3], [1,2], [1,3], [2,3], [1,2,3]]
     *
     * @param elements the product index.
     * @return a {@link Set} containing all possible combination.
     */
    public static Set<Set<Integer>> findNonRepeatedCombinations(final List<Integer> elements) {

        LOG.info(String.format("BEGIN findNonRepeatedCombinations, elements={%s}", elements));

        Set<Set<Integer>> allCombinations = new HashSet<>();

        // first add (one element) combination
        elements.forEach(element -> allCombinations.add(new TreeSet<>(Collections.singletonList(element))));

        for (Integer element : elements) {

            // generate a copy from allCombinations, to not have problems with current loop
            Set<Set<Integer>> temporaryCombinations = new HashSet<>(allCombinations);

            for (Set<Integer> combination : temporaryCombinations) {

                // get exists combination
                Set<Integer> newCombination = new HashSet<>(combination);

                // add new element to combination
                newCombination.add(element);

                // foreach element I compare with exists combination, and generate new ones
                allCombinations.add(newCombination);
            }
        }

        LOG.info(String.format("END findNonRepeatedCombinations, allCombinations={%s}", allCombinations));
        return allCombinations;
    }
}
