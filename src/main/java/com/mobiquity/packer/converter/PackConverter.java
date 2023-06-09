package com.mobiquity.packer.converter;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.dto.FileContentDto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Util class to convert String from file into object
 */
public class PackConverter {

    private static final Logger LOG = Logger.getLogger("PackConverter");

    private static final String CONTENT_SEPARATOR = " ";

    /**
     * Based on a string that contains a line from an input pack file, converts it into a {@link FileContentDto} object.
     *
     * @param line the content from input file (e.g.: "81 : (1,53.38,€45) (2,88.62,€98)")
     * @return the {@link FileContentDto} object
     * @throws APIException if there is an error in converting the string to number
     */
    public static FileContentDto getContentFromLine(final String line) {
        LOG.info(String.format("BEGIN getContentFromLine, line={%s}", line));

        if (line == null || line.isBlank()) {
            return null;
        }

        try {
            final String[] lineContent = line.split(CONTENT_SEPARATOR);
            final BigDecimal weightLimit = new BigDecimal(lineContent[0]);

            // Skip the weightLimit and colon to get only the products
            final String[] productsAsString = Arrays.copyOfRange(lineContent, 2, lineContent.length);

            final FileContentDto fileContentDto = new FileContentDto(weightLimit, productsAsString);
            LOG.info(String.format("END getContentFromLine, fileContent={%s}", fileContentDto));

            return fileContentDto;

        } catch (NumberFormatException ex) {
            final String errorMsg = String.format("Error to convert string to number, content=\"%s\"", line);
            LOG.log(Level.SEVERE, errorMsg);
            throw new APIException(errorMsg, ex);
        }
    }
}
