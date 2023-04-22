package com.mobiquity.packer.adapter;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.entity.FileContent;
import com.mobiquity.packer.entity.Product;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Util class to convert String from file into object
 */
public class PackAdapter {

    private static final Logger LOG = Logger.getLogger("PackAdapter");

    private static final String CONTENT_SEPARATOR = " ";
    private static final String PRODUCT_SEPARATOR = ",";
    private static final String REGEX_REPLACE_PRODUCT_STRING = "[()€]";

    /**
     * Based on line from string, convert into {@link FileContent} object.
     * @param line the content from input file (e.g.: "81 : (1,53.38,€45) (2,88.62,€98)")
     * @return the {@link FileContent} object
     */
    public static FileContent getContentFromLine(final String line) {
        LOG.info(String.format("BEGIN getContentFromLine, line={%s}", line));

        if (line == null || line.isBlank()) {
            return null;
        }

        try {
            final String[] lineContent = line.split(CONTENT_SEPARATOR);
            final BigDecimal weightLimit = new BigDecimal(lineContent[0]);

            // this copyOfRange get only the products (index 0 = weightLimit; index 1 = colon)
            final String[] productsAsString = Arrays.copyOfRange(lineContent, 2, lineContent.length);

            final FileContent fileContent = new FileContent(weightLimit, productsAsString);
            LOG.info(String.format("END getContentFromLine, fileContent={%s}", fileContent));

            return fileContent;

        } catch (NumberFormatException ex) {

            final String errorMsg = String.format("Error to convert string to number, content=\"%s\"", line);
            LOG.log(Level.SEVERE, errorMsg);

            throw new APIException(errorMsg, ex);
        }
    }

    /**
     * Read product attributes from string and convert into {@link FileContent} object.
     * @param productString the string with product content (e.g.: "(1,53.38,€45)")
     * @return the {@link Product} object
     */
    public static Product getProductFromString(final String productString) {

        LOG.info(String.format("BEGIN getProductFromString, productString={%s}", productString));

        try {

            final String[] content = productString.replaceAll(REGEX_REPLACE_PRODUCT_STRING, "")
                    .split(PRODUCT_SEPARATOR);

            final int productsQuantity = content.length;

            if (productsQuantity != 3) {
                final String errorMsg = String.format("Expected 3 args but got %d, content=\"%s\"",
                        productsQuantity, productString);
                throw new APIException(errorMsg);
            }

            final Integer index = Integer.valueOf(content[0]);
            final BigDecimal weight = new BigDecimal(content[1]);
            final BigDecimal cost = new BigDecimal(content[2]);

            final Product product = new Product(index, weight, cost);
            LOG.info(String.format("END getProductFromString, product={%s}", product));

            return product;

        } catch (NumberFormatException ex) {

            final String errorMsg = String.format("Error to convert string to number, content=\"%s\"", productString);
            LOG.log(Level.SEVERE, errorMsg);

            throw new APIException(errorMsg, ex);
        }
    }
}
