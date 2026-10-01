package mary;

import java.nio.file.Path;
import java.util.Arrays;

import javafx.application.Application;
import mary.ui.MaryApplication;

/**
 * Launches the graphical app without extending Application, allowing a runnable fat JAR.
 */
public class Launcher {
    /**
     * Opens JavaFX by default, or the console when --cli is supplied.
     *
     * @param args optional --cli switch.
     */
    public static void main(String[] args) {
        if (Arrays.asList(args).contains("--cli")) {
            Mary.main(args);
        } else {
            // Keep JavaFX's extracted native libraries out of the user's home directory.
            System.setProperty("javafx.cachedir", Path.of(".mary", "javafx-cache").toAbsolutePath().toString());
            Application.launch(MaryApplication.class, args);
        }
    }
}
