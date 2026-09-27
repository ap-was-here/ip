package mary.command;

import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import static org.junit.jupiter.api.Assertions.*;

/** Tests additions, persistence, validation, and storage failure feedback. */
class AddCommandTest extends CommandTestSupport {
    @Test
    void execute_validTodo_addsAndSavesTask() throws Exception {
        assertEquals(" Got it. I've added this task:\n   [T][ ] read book\n Now you have 1 tasks in the list.\n",
                execute(new AddCommand("todo read book")));
        assertEquals(1, tasks.size());
        assertEquals("[T][ ] read book", storage.load().get(0).toString());
    }

    @Test
    void execute_deadlineAndEvent_persistsBothSubtypes() throws Exception {
        execute(new AddCommand("deadline read /by 2/12/2019 1800"));
        execute(new AddCommand("event camp /from 2/12/2019 1400 /to 4/12/2019 1600"));
        assertEquals(tasks.getTasks().stream().map(Object::toString).toList(),
                storage.load().stream().map(Object::toString).toList());
        assertEquals(2, tasks.size());
    }

    @Test
    void execute_invalidDescription_reportsErrorWithoutAddingOrSaving() {
        assertTrue(execute(new AddCommand("todo ")).startsWith(" Error:"));
        assertTrue(tasks.isEmpty());
        assertFalse(Files.exists(directory.resolve("tasks.txt")));
    }

    @Test
    void execute_saveFailure_reportsErrorWithoutSuccessMessage() {
        storage = new mary.storage.Storage(directory.toString());
        String output = execute(new AddCommand("todo read"));
        assertTrue(output.contains("Error: could not save tasks"));
        assertFalse(output.contains("Got it."));
    }
}
