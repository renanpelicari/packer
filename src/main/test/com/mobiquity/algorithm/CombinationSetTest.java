package com.mobiquity.algorithm;

import com.mobiquity.algorithm.CombinationSet;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for class {@link CombinationSet}
 */
public class CombinationSetTest {

    @Test
    public void givenAListWithTwoRepeated_whenCallFindNonRepeatedCombinations_shouldReturnOnlyOne() {
        final Set<Set<Integer>> result = CombinationSet.getCombinations(Arrays.asList(2, 2));
        assertEquals(1, result.size());

        Set<Integer> combination = result.iterator().next();
        assertEquals(Collections.singleton(2), combination);
    }

    @Test
    public void givenAListWithTwoDifferentItems_whenCallFindNonRepeatedCombinations_shouldReturnThreeCombinations() {
        final Set<Set<Integer>> result = CombinationSet.getCombinations(Arrays.asList(2, 3));
        assertEquals(3, result.size());

        // set containing: [[2], [3], [2,3]]
        Set<Set<Integer>> expectedValues = new HashSet<>(Arrays.asList(
                Collections.singleton(2),
                Collections.singleton(3),
                new HashSet<>(Arrays.asList(2, 3)
        )));
        assertEquals(expectedValues, result);
    }

    @Test
    public void givenAListWithFourDifferentItems_whenCallFindNonRepeatedCombinations_shouldReturnThreeCombinations() {
        final Set<Set<Integer>> result = CombinationSet.getCombinations(Arrays.asList(1, 2, 3, 4));
        assertEquals(15, result.size());

        // set containing: [[1], [2], [3], [4], [1,2], [1,3], [1,4], [2,3], [2,4],
        //                  [3,4], [1,2,3], [1,2,4], [1,3,4]. [2,3,4], [1,2,3,4]]
        Set<Set<Integer>> expectedValues = new HashSet<>(Arrays.asList(
                Collections.singleton(1),
                Collections.singleton(2),
                Collections.singleton(3),
                Collections.singleton(4),
                new HashSet<>(Arrays.asList(1, 2)),
                new HashSet<>(Arrays.asList(1, 3)),
                new HashSet<>(Arrays.asList(1, 4)),
                new HashSet<>(Arrays.asList(2, 3)),
                new HashSet<>(Arrays.asList(2, 4)),
                new HashSet<>(Arrays.asList(3, 4)),
                new HashSet<>(Arrays.asList(1, 2, 3)),
                new HashSet<>(Arrays.asList(1, 2, 4)),
                new HashSet<>(Arrays.asList(1, 3, 4)),
                new HashSet<>(Arrays.asList(2, 3, 4)),
                new HashSet<>(Arrays.asList(1, 2, 3, 4))
        ));
        assertEquals(expectedValues, result);
    }

}
