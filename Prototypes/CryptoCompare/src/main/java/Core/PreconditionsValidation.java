package Core;

import com.google.common.base.Preconditions;

/**
 * Utils methods for precondition validation.
 */
public class PreconditionsValidation {

    /**
     * Checks that a string is not null nor empty (length is 0).
     * @param string The string to validate
     */
    public static void checkStringNotEmpty(String string) {
        Preconditions.checkNotNull(string);
        Preconditions.checkArgument(string.length() > 0);
    }
}
