package core;

import com.google.common.base.Preconditions;

/**
 * Utils methods for precondition validation.
 */
public class PreconditionsValidation {

    /**
     * Checks that a string is not null nor empty (length is 0 and is not all whitespaces).
     * @param string The string to validate
     */
    public static void checkStringNotEmpty(String string) {
        Preconditions.checkArgument( (string != null) && (string.trim().length() > 0));
    }
}
