package com.mobiquity.packer.adapter;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.entity.FileContent;
import com.mobiquity.packer.strategy.PackStrategy;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for class {@link PackAdapter}
 */
public class PackAdapterTest {

    private static final String STRING_FORMAT_WRONG_CONVERT_ERROR = "Error to convert string to number, content=\"%s\"";

    @Test
    public void givenEmptyString_whenCallLineToPack_thenReturnNull() {
        assertNull(PackAdapter.getContentFromLine(""));
    }

    @Test
    public void givenNullAsLine_whenCallLineToPack_thenReturnNull() {
        assertNull(PackAdapter.getContentFromLine(null));
    }

    @Test
    public void givenSpaceAsLine_whenCallLineToPack_thenReturnNull() {
        assertNull(PackAdapter.getContentFromLine(" "));
    }

    @Test(expected = APIException.class)
    public void givenNotValidContent_whenCallLineToPack_thenThrowsException() {
        try {
            PackAdapter.getContentFromLine("RPR : ");

        } catch (final APIException ex) {
            final String errorMsg = String.format(STRING_FORMAT_WRONG_CONVERT_ERROR, "RPR : ");
            assertEquals(errorMsg, ex.getMessage());
            throw ex;
        }
        fail("Should throw APIException due the cast error!");
    }

    @Test
    public void givenLineWithOnlyWeightLimit_whenCallLineToPack_thenReturnPackObjectOnlyWithWeight() {
        final FileContent fileContent = PackAdapter.getContentFromLine("87.65 : ");
        assertEquals(Double.valueOf(87.65), fileContent.getWeightLimit());
        assertEquals(0, fileContent.getProductsAsString().length);
    }

    @Test(expected = APIException.class)
    public void givenProductWith2Parameters_whenCallLineToPack_thenThrowsException() {
        final String product = "(1,15.3)";

        try {
            PackAdapter.getProductFromString(product);

        } catch (final APIException ex) {
            final String errorMsg = String.format("Expected 3 args but got 2, content=\"%s\"", product);
            assertEquals(errorMsg, ex.getMessage());
            throw ex;
        }
        fail("Should throw APIException due the cast error!");
    }

    @Test(expected = APIException.class)
    public void givenProductWith4Parameters_whenCallLineToPack_thenThrowsException() {
        final String product = "(1,15.3,€34,1)";

        try {
            PackAdapter.getProductFromString(product);

        } catch (final APIException ex) {
            final String errorMsg = String.format("Expected 3 args but got 4, content=\"%s\"", product);
            assertEquals(errorMsg, ex.getMessage());
            throw ex;
        }
        fail("Should throw APIException due the cast error!");
    }

    @Test(expected = APIException.class)
    public void givenProductWithWrongIndex_whenCallLineToPack_thenThrowsException() {
        final String line = "(A,15.3,€34)";

        try {
            PackAdapter.getProductFromString(line);

        } catch (final APIException ex) {
            final String errorMsg = String.format(STRING_FORMAT_WRONG_CONVERT_ERROR, line);
            assertEquals(errorMsg, ex.getMessage());
            throw ex;
        }
        fail("Should throw APIException due the cast error!");
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
