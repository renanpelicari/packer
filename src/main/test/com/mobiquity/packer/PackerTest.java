package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for class {@link Packer}
 */
public class PackerTest {

    private final static String VALID_INPUT_FILEPATH = "src/main/test/resources/example_input";
    private final static String INVALID_FILEPATH = "unknown_file";

    @Test(expected = APIException.class)
    public void givenNotValidFilePath_whenCallPack_shouldReturnProperlyResults() {
        try {
            Packer.pack(INVALID_FILEPATH);

        } catch (final APIException ex) {
            final String errorMsg = String.format("Error to handle the file [%s]", INVALID_FILEPATH);
            assertEquals(errorMsg, ex.getMessage());
            throw ex;
        }
        fail("Should throw APIException due the cast error!");
    }

     @Test
    public void givenValidFilePath_whenCallPack_shouldReturnProperlyResults() {
        final String result = Packer.pack(VALID_INPUT_FILEPATH);
        System.out.println(result);

        final String expectedResult = "4\n" +
                "-\n" +
                "2,7\n" +
                "8,9";

        assertEquals(expectedResult, result);
    }
}
