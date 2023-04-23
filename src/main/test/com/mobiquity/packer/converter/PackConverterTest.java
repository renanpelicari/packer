package com.mobiquity.packer.converter;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.dto.FileContentDto;
import com.mobiquity.packer.strategy.PackStrategy;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Unit tests for class {@link PackConverter}
 */
public class PackConverterTest {

    private static final String STRING_FORMAT_WRONG_CONVERT_ERROR = "Error to convert string to number, content=\"%s\"";

    @Test
    public void givenEmptyString_whenCallLineToPack_thenReturnNull() {
        assertNull(PackConverter.getContentFromLine(""));
    }

    @Test
    public void givenNullAsLine_whenCallLineToPack_thenReturnNull() {
        assertNull(PackConverter.getContentFromLine(null));
    }

    @Test
    public void givenSpaceAsLine_whenCallLineToPack_thenReturnNull() {
        assertNull(PackConverter.getContentFromLine(" "));
    }

    @Test(expected = APIException.class)
    public void givenNotValidContent_whenCallLineToPack_thenThrowsException() {
        try {
            PackConverter.getContentFromLine("RPR : ");

        } catch (final APIException ex) {
            final String errorMsg = String.format(STRING_FORMAT_WRONG_CONVERT_ERROR, "RPR : ");
            assertEquals(errorMsg, ex.getMessage());
            throw ex;
        }
        fail("Should throw APIException due the cast error!");
    }

    @Test
    public void givenLineWithOnlyWeightLimit_whenCallLineToPack_thenReturnPackObjectOnlyWithWeight() {
        final FileContentDto fileContentDto = PackConverter.getContentFromLine("87.65 : ");
        assertNotNull(fileContentDto);
        assertEquals(new BigDecimal("87.65"), fileContentDto.getWeightLimit());
        assertEquals(0, fileContentDto.getProductsAsString().length);
    }

    @Test(expected = APIException.class)
    public void givenProductWithWrongWeight_whenCallLineToPack_thenThrowsException() {
        final String line = "(1,A,€34)";

        try {
            PackStrategy.getBetterPackFromInputLine(line);

        } catch (final APIException ex) {
            final String errorMsg = String.format(STRING_FORMAT_WRONG_CONVERT_ERROR, line);
            assertEquals(errorMsg, ex.getMessage());
            throw ex;
        }
        fail("Should throw APIException due the cast error!");
    }

    @Test(expected = APIException.class)
    public void givenProductWithWrongCost_whenCallLineToPack_thenThrowsException() {
        final String line = "(1,15.3,A)";

        try {
            PackStrategy.getBetterPackFromInputLine(line);

        } catch (final APIException ex) {
            final String errorMsg = String.format(STRING_FORMAT_WRONG_CONVERT_ERROR, line);
            assertEquals(errorMsg, ex.getMessage());
            throw ex;
        }
        fail("Should throw APIException due the cast error!");
    }

}
