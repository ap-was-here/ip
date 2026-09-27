package mary.command;

import mary.storage.Storage;
import mary.task.TaskList;
import mary.ui.Ui;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/** Captures command output and isolates persistence for each test. */
abstract class CommandTestSupport {
    @TempDir
    Path directory;
    TaskList tasks;
    Storage storage;
    Ui ui;
    private PrintStream originalOutput;
    private PrintStream capturedOutput;
    private ByteArrayOutputStream output;

    @BeforeEach
    void prepareCommandEnvironment() {
        tasks = new TaskList();
        storage = new Storage(directory.resolve("tasks.txt").toString());
        ui = new Ui();
        originalOutput = System.out;
        output = new ByteArrayOutputStream();
        capturedOutput = new PrintStream(output, true, StandardCharsets.UTF_8);
        System.setOut(capturedOutput);
    }

    @AfterEach
    void restoreConsole() {
        System.setOut(originalOutput);
        capturedOutput.close();
    }

    /** Runs one command and returns only its output, normalizing line endings. */
    String execute(Command command) {
        output.reset();
        command.execute(tasks, ui, storage);
        return output.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
    }
}
