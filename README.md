## Package Challenge

This project is an API for choose better products to pack, based on their costs and weight.

It was used a combination algorithm to test all possibilities of combinations, then the better products are select and returned.

Basically, the sum of product weight cannot exceed the package limit weight, and the combination of products has to be the one with more cost.

----

### Example

#### input:
```$xslt
81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)
8 : (1,15.3,€34)
75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)
56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)
```
* When the number before colon is the weight limit of box;
* Each product is inside of parenthesis with respective information (index, weight and cost).

#### output:
```$xslt
4
-
2,7
8,9
```

* For each line of input, one line will be provided for output.
* For each package, will appear the indexes' numbers that represents the better selection.

----
