package com.mobiquity.algorithm;

import java.util.*;
import java.util.logging.Logger;


/**
 * This class implements a combinator algorithm.
 */
public class CombinationSet {

    private static final Logger LOG = Logger.getLogger("CombinationStrategy");

    /**
     * Based on elements of a list, this method will return all possible non repeat combinations between them.
     * p.s.: The non repetition is guaranteed by {@link Set}.
     * e.g.
     * input: 1, 2, 3
     * output: [[1], [2], [3], [1,2], [1,3], [2,3], [1,2,3]]
     *
     * @param elements the product index.
     * @return a {@link Set} containing all possible combination.
     */
    public static Set<Set<Integer>> getCombinations(final List<Integer> elements) {

        LOG.info(String.format("BEGIN getCombinations, elements={%s}", elements));

        Set<Set<Integer>> allCombinations = new HashSet<>();

        // first add (one element) combination
        elements.forEach(element ->
                allCombinations.add(new TreeSet<>(Collections.singletonList(element)))
        );

        // then add combination with more than 1 element
        elements.forEach(element -> {

            // generate a copy from allCombinations, to not have problems with current loop
            Set<Set<Integer>> temporaryCombinations = new HashSet<>(allCombinations);

            temporaryCombinations.forEach(combination -> {

                // get exists combination
                Set<Integer> newCombination = new HashSet<>(combination);

                // add new element to combination
                newCombination.add(element);

                // foreach element I compare with exists combination, and generate new ones
                allCombinations.add(newCombination);
            });
        });

        LOG.info(String.format("END getCombinations, allCombinations={%s}", allCombinations));
        return allCombinations;
    }
}
