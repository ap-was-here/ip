package mary.ui;

import java.util.Scanner;

/** Handles MARY's interaction with the user. */
public class Ui {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final String BANNER = "███╗   ███╗ █████╗ ██████╗ ██╗   ██╗\n"
            + "████╗ ████║██╔══██╗██╔══██╗╚██╗ ██╔╝\n"
            + "██╔████╔██║███████║██████╔╝ ╚████╔╝\n"
            + "██║╚██╔╝██║██╔══██║██╔══██╗  ╚██╔╝\n"
            + "██║ ╚═╝ ██║██║  ██║██║  ██║   ██║\n"
            + "╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝";
    private final Scanner scanner = new Scanner(System.in);

    /** Displays the startup greeting. */
    public void showWelcome() {
        showLine();
        System.out.println(BANNER);
        System.out.println("Hi! I'm MARY.");
        System.out.println("What have you got for me today?");
        showLine();
    }

    /** Reads one command, or returns null when input ends. */
    public String readCommand() {
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }

    /** Displays the standard divider. */
    public void showLine() {
        System.out.println(SEPARATOR);
    }

    /** Displays an error message. */
    public void showError(String message) {
        System.out.println(" Error: " + message);
    }

    /** Displays a startup/loading error. */
    public void showLoadingError(String message) {
        showError(message);
        showLine();
    }

    /** Displays the exit message. */
    public void showGoodbye() {
        System.out.println("See you later. Complete your tasks on time!");
        showLine();
    }
}
