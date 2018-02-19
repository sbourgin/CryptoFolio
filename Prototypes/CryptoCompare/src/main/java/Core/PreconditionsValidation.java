package Core;

import com.google.common.base.Preconditions;

/**
 * Created by sylvain on 2/18/18.
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
