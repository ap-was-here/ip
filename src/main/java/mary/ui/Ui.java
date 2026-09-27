package mary.ui;

import java.util.Scanner;

/**
 * Handles MARY's interaction with the user.
 */
public class Ui {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final String BANNER = "███╗   ███╗ █████╗ ██████╗ ██╗   ██╗\n"
            + "████╗ ████║██╔══██╗██╔══██╗╚██╗ ██╔╝\n"
            + "██╔████╔██║███████║██████╔╝ ╚████╔╝\n"
            + "██║╚██╔╝██║██╔══██║██╔══██╗  ╚██╔╝\n"
            + "██║ ╚═╝ ██║██║  ██║██║  ██║   ██║\n"
            + "╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝";
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Displays the startup greeting.
     */
    public void showWelcome() {
        showLine();
        System.out.println(BANNER);
        System.out.println("Hi! I'm MARY.");
        System.out.println("What have you got for me today?");
        showLine();
    }

    /**
     * Reads a line without trimming whitespace.
     *
     * @return the input line, or null at end-of-input.
     */
    public String readCommand() {
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }

    /**
     * Displays the standard divider.
     */
    public void showLine() {
        System.out.println(SEPARATOR);
    }

    /**
     * Displays an error message with the standard error prefix.
     *
     * @param message explanation and any corrective guidance.
     */
    public void showError(String message) {
        System.out.println(" Error: " + message);
    }

    /**
     * Displays a startup/loading error followed by a divider.
     *
     * @param message explanation of the loading problem.
     */
    public void showLoadingError(String message) {
        showError(message);
        showLine();
    }

    /**
     * Displays the exit message.
     */
    public void showGoodbye() {
        System.out.println("See you later. Complete your tasks on time!");
        showLine();
    }
}
