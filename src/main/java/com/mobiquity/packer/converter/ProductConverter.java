package com.mobiquity.packer.converter;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.model.Product;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Util class to convert String from file into object
 */
public class ProductConverter {

    private static final Logger LOG = Logger.getLogger("ProductConverter");

    private static final String PRODUCT_SEPARATOR = ",";
    private static final String REGEX_REPLACE_PRODUCT_STRING = "[()€]";

    /**
     * Read product attributes from string and convert into {@link Product} object.
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
