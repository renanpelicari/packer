# Package Challenge

This project is an API for choose better products to pack, based on their costs and weight.

It was used a combination algorithm to test all possibilities of combinations, then the better products are select and returned.

Basically, the sum of product weight cannot exceed the package limit weight, and the combination of products has to be the one with more cost.

---

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

---

## Usage

1. Clone the Package Packer repository to your local machine.
2. Navigate to the project directory.
3. If you have Maven installed, you can build the application by running the following command: `mvn clean package`. This will generate a JAR file named `package-packer-<version>.jar` in the `target` directory.
4. Alternatively, you can directly run the application using the following command: `java -jar package-packer-<version>.jar <file_path>`, where `<file_path>` is the path to the input file containing the packages and products data.
5. The application will process the input file and print the results to the console.

---

## Design

The Package Packer application is designed using object-oriented principles and follows a strategy pattern. The main components of the application are:

* `PackStrategy`: This class contains the algorithm to determine the best combination of products to pack in a package. It uses a combination set to generate all possible combinations of products and compares them to find the best one.
* `Pack`: This class represents a pack with its weight limit, products, total weight, and total cost. It also includes methods to compare packs and determine if a pack is better than another.
* `Product`: This class represents a product with its index, weight, and cost.
* `FileContentDto`: This class is a data transfer object that holds the parsed contents of a line from the input file.
* `PackConverter` and `ProductConverter`: These classes are responsible for converting input data from strings to corresponding objects.
* `PackComparator`: This class contains methods for comparing packs based on weight and cost constraints.

---

## Author

Renan Peliçari Rodrigues

[renanpelicari@gmail.com](mailto:renanpelicari@gmail.com)

https://www.linkedin.com/in/renanpelicari/

https://github.com/renanpelicari