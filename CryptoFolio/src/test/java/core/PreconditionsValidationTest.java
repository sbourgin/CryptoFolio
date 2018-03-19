package core;

import org.junit.Test;

/**
 * Tests the PreconditionsValidation class.
 */
public class PreconditionsValidationTest {

    /**
     * Tests the checkStringNotEmpty method with an actual string.
     * Test is expected to pass as the string is not empty.
     */
    @Test
    public void checkStringNotEmptyWithAString() {

        // Arrange
        String input = "abcd";

        // Act && Assert
        PreconditionsValidation.checkStringNotEmpty(input);
    }

    /**
     * Tests the checkStringNotEmpty method with a null string.
     * Test is expecting to fail since a null string is an empty string.
     */
    @Test (expected = IllegalArgumentException.class)
    public void checkStringNotEmptyWithANullString() {

        // Arrange
        String input = null;

        // Act
        PreconditionsValidation.checkStringNotEmpty(input);
    }

    /**
     * Tests the checkStringNotEmpty method with an empty string.
     * Test is expecting to fail.
     */
    @Test (expected = IllegalArgumentException.class)
    public void checkStringNotEmptyWithEmptyString() {

        // Arrange
        String input = "";

        // Act
        PreconditionsValidation.checkStringNotEmpty(input);
    }

    /**
     * Tests the checkStringNotEmpty method with an all white spaced string.
     * Test is expecting to fail since an all white spaced string is an empty string.
     */
    @Test (expected = IllegalArgumentException.class)
    public void checkStringNotEmptyWithAWhiteSpacedString() {

        // Arrange
        String input = "  ";

        // Act
        PreconditionsValidation.checkStringNotEmpty(input);
    }
}