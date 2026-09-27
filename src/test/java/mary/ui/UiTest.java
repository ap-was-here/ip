package mary.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

/**
 * Verifies the ASCII welcome banner independently of task commands.
 */
class UiTest {
    /**
     * Checks the cat artwork, greeting, and separators without non-ASCII characters.
     */
    @Test
    void showWelcome_catBanner_printsExactAsciiGreeting() {
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (PrintStream capturedOutput = new PrintStream(output, true, StandardCharsets.UTF_8)) {
            System.setOut(capturedOutput);
            new Ui().showWelcome();
        } finally {
            System.setOut(originalOutput);
        }

        String actual = output.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
        String separator = "____________________________________________________________\n";
        String expected = separator
                + " /\\_/\\\n"
                + "( o.o )   M A R Y\n"
                + " > ^ <    Your purr-sonal task assistant.\n"
                + "\n"
                + "What's on your list today?\n"
                + separator;
        assertEquals(expected, actual);
        assertTrue(actual.chars().allMatch(character -> character < 128));
    }
}
