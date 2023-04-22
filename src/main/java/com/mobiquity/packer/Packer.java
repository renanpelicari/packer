package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.entity.Pack;
import com.mobiquity.packer.entity.Product;
import com.mobiquity.packer.strategy.PackStrategy;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The entry point for Packer API.
 * Based on file with information concern about product and weight limit,
 * this class will return the better selection of products.
 */
public class Packer {

    private static final Logger LOG = Logger.getLogger("Packer");

    private Packer() {
    }

    /**
     * This method will convert the information in the input file (e.g.: "81 : (1,53.38,€45) (2,88.62,€98)")
     * into object. compare all the possible combination of products and return the better one.
     * @param filePath the path and filename, e.g.: "src/main/test/resources/example_input"
     * @return the string containing the better option of indexes for each line of input file
     */
     public static String pack(String filePath) {
         LOG.info(String.format("BEGIN pack, filePath={%s}", filePath));

          try {
              final BufferedReader reader = new BufferedReader
                      (new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));

              final Stream<String> lines = reader.lines();

              final String result = lines.map(fileLine -> {
                  final Pack pack = PackStrategy.getBetterPackFromInputLine(fileLine);

                  if (pack == null) {
                      return "-";
                  }

                  return pack.getProducts().stream()
                          .map(Product::getIndex)
                          .sorted()
                          .map(Object::toString)
                          .collect(Collectors.joining(","));

              }).map(line -> (line.isBlank()) ? "-" : line)
                      .collect(Collectors.joining("\n"));

              reader.close();

              LOG.info(String.format("END pack, result={%s}", result));
              return result;
          } catch (IOException e) {

              final String errorMsg = String.format("Error to handle the file [%s]", filePath);
              LOG.log(Level.SEVERE, errorMsg);

              throw new APIException(errorMsg, e);
          }
    }
}
