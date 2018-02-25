import model.ManageEmployee;

/**
 * Created by sylvain on 2/22/18.
 */
public class Hello {

    /**
     * Application entry point method.
     * @param args Arguments
     */
    public static void main(String[] args) {
        // Prints "Hello, World" to the terminal window.
        System.out.println("Hello, World");

        new ManageEmployee().plouf(null);

        System.out.println("End, World");
    }
}
